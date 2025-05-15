using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.Models.Entities;
using blog_api.Repositories;
using blog_api.Services.Interface;

namespace blog_api.Services
{
    internal class PostService : IPostService
    {
        private readonly IMongoDBRepository<Post> _postRepository;
        private readonly IFeedService _feedService;
        private readonly ILogger<PostService> _logger;
        private readonly IMapper _mapper;

        public PostService(
            IMongoDBRepository<Post> _postRepository,
            IMapper _mapper, ILogger<PostService> logger,
            IFeedService feedService)
        {
            this._postRepository = _postRepository;
            this._mapper = _mapper;
            this._logger = logger;
            _feedService = feedService;
        }

        public async Task<List<Post>> GetPosts()
        {
            var posts = await _postRepository.GetAllAsync();

            return posts.OrderByDescending(e => e.CreatedAt).ToList();
        }

        public async Task<Post> GetPostById(string id)
        {
            var post = await _postRepository.GetAsync(id);

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