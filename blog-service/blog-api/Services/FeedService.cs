using AutoMapper;

using blog_api.DTOs.Response;
using blog_api.Models.Entities;
using blog_api.Models.Events;
using blog_api.Pagination;
using blog_api.Repositories;
using blog_api.Services.Interface;

using Confluent.Kafka;

using Newtonsoft.Json;

namespace blog_api.Services
{
    internal class FeedService : IFeedService
    {
        private readonly IMongoDBRepository<UserFeedEntry> _feedRepository;
        private readonly IMongoDBRepository<Post> _postRepository;
        private readonly IMapper _mapper;
        private readonly string PostSavedTopic = "";
        private readonly IProducer<Null, string> _producer;
        private readonly ILogger<FeedService> _logger;


        public FeedService(
            IMapper mapper,
            IConfiguration configuration,
            IMongoDBRepository<UserFeedEntry> feedRepository,
            IMongoDBRepository<Post> postRepository,
            IProducer<Null, string> producer,
            ILogger<FeedService> logger)
        {
            this._feedRepository = feedRepository;
            this._mapper = mapper;
            this._logger = logger;
            _postRepository = postRepository;
            _producer = producer;
            PostSavedTopic = configuration["Kafka:PostSavedTopic"] ?? throw new Exception("Cannot Read PostSavedTopic");
            _logger = logger;
        }

        public async Task<CursorPaginatedList<PostResponse>> GetFeeds(string userId, string cursor, int size)
        {

            var result = await _feedRepository.GetAllAsync(f => f.UserId == userId, cursor, size);
            var postIds = result.Select(f => f.PostId);
            var items = _postRepository.GetAllAsync(p => postIds.Contains(p.Id))
                        .Result
                        .Select(f => _mapper.Map<PostResponse>(f))
                        .ToList();


            return new CursorPaginatedList<PostResponse>(
                items,
                result.NextCursor,
                result.PageSize,
                result.HasMore
            );
        }

        public async Task AddFeed(Post post)
        {
            var message = JsonConvert.SerializeObject(new PostSavedEvent
            {
                AuthorId = post.AuthorId,
                Content = post.Content,
                PostId = post.Id!,

            });
            await _producer.ProduceAsync(
                topic: PostSavedTopic,
                message: new Message<Null, string> { Value = message }
            );
        }

    }
}
