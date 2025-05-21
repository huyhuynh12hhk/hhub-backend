using AutoMapper;

using blog_api.Configuration;
using blog_api.DTOs.Request;
using blog_api.Models.Entities;
using blog_api.Repositories;
using blog_api.Services.Interface;

using Microsoft.Extensions.Caching.Distributed;

using Microsoft.Extensions.Options;

namespace blog_api.Services
{
    internal class PostService : IPostService
    {
        private readonly IMongoDBRepository<Post> _postRepository;
        private readonly IFeedService _feedService;
        private readonly ILogger<PostService> _logger;
        private readonly IMapper _mapper;
        private readonly IDistributedCache _cache;
        private readonly RedisSettings _redisConf;

        public PostService(
            IDistributedCache cache,
            IOptions<RedisSettings> redisConf,
            IMongoDBRepository<Post> _postRepository,
            IMapper _mapper, ILogger<PostService> logger,
            IFeedService feedService)
        {
            this._postRepository = _postRepository;
            this._mapper = _mapper;
            this._logger = logger;
            this._feedService = feedService;
            this._cache = cache;
            this._redisConf = redisConf.Value;
        }

        public async Task<List<Post>> GetPosts()
        {
            var posts = await _postRepository.GetAllAsync();

            return posts.OrderByDescending(e => e.CreatedAt).ToList();
        }

        public async Task<Post?> GetPostById(string id)
        {
            var cacheKey = $"{_redisConf.FeedKey}:{id}";
            var cacheOptions = new DistributedCacheEntryOptions()
                .SetAbsoluteExpiration(TimeSpan.FromMinutes(10));
            var post = await _cache.GetOrSetAsync(
                cacheKey,
                async () =>
                {
                    _logger.LogInformation("No cache found for key: {0}, fetch from http service.", cacheKey);
                    return await _postRepository.GetAsync(id);
                },
                cacheOptions

            );

            return post;
        }

        public async Task<Post> CreateAPost(CreatePostRequest request)
        {
            var post = _mapper.Map<Post>(request);

            await _postRepository.CreateAsync(post);
            _logger.LogInformation($"Post {post.Id} has created success.");

            await _feedService.AddFeed(post);
            return post;
        }

        public async Task<Post> UpdatePost(string id, UpdatePostRequest request)
        {
            var post = await _postRepository.GetAsync(id);
            _mapper.Map(request, post);

            await _postRepository.UpdateAsync(post);

            return post;
        }

        public async Task ToggleReaction(string id, MakeReactionRequest request)
        {
            var post = await _postRepository.GetAsync(id);

            if (post.Reactions == null)
                post.Reactions = new();

            var user = post.Reactions.Find(u => u.Id == request.Id);
            if (user == null)
            {
                post.Reactions.Add(_mapper.Map<UserDetail>(request));
            }
            else
            {
                post.Reactions.Remove(user);
            }

            await _postRepository.UpdateAsync(post);

        }

        public async Task DeletePost(string id)
        {

            await _postRepository.RemoveAsync(id);
        }


    }
}