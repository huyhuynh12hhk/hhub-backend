using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.Models.Entities;
using blog_api.Models.Events;
using blog_api.Repositories;
using blog_api.Repositories.Http;

using Confluent.Kafka;

using Newtonsoft.Json;

namespace blog_api.Consumers
{
    public class PostSavedConsumer : BackgroundService
    {
        private readonly IConsumer<Ignore, string> _consumer;
        private readonly ILogger<PostSavedConsumer> _logger;
        private readonly IFollowRepository _followRepository;
        private readonly IMongoDBRepository<UserFeedEntry> _feedRepository;
        private readonly IMapper _mapper;
        private readonly ISearchRepository _searchRepository;
        private readonly ITokenRepository _tokenRepository;

        public PostSavedConsumer(
            ConsumerConfig consumerConfig,
            IConfiguration configuration,
            ILogger<PostSavedConsumer> logger,
            IServiceProvider services,
            IMapper mapper)
        {
            _logger = logger;
            var scope = services.CreateScope();
            _followRepository = scope
                .ServiceProvider
                .GetRequiredService<IFollowRepository>();
            _feedRepository = scope
                .ServiceProvider
                .GetRequiredService<IMongoDBRepository<UserFeedEntry>>();
            _searchRepository = scope
                .ServiceProvider
                .GetRequiredService<ISearchRepository>();
            _tokenRepository = scope
                .ServiceProvider
                .GetRequiredService<ITokenRepository>();

            this._consumer = new ConsumerBuilder<Ignore, string>(consumerConfig)
                .SetErrorHandler((_, e) => logger.LogError(e.Reason))
                .Build();

            _consumer.Subscribe(configuration["Kafka:PostSavedTopic"]);
            _mapper = mapper;
        }


        protected override async Task ExecuteAsync(CancellationToken cancellation)
        {
            while (!cancellation.IsCancellationRequested)
            {

                await ProcessKafkaMessage(cancellation);
                await Task.Delay(TimeSpan.FromMinutes(1), cancellation);
            }

            _consumer.Close();
        }

        public async Task ProcessKafkaMessage(CancellationToken cancellation)
        {
            try
            {
                var consumeResult = _consumer.Consume(cancellation);
                var token = await _tokenRepository.getAccessToken();
                var evt = JsonConvert.DeserializeObject<PostSavedEvent>(consumeResult.Message.Value)!;

                // TODO: split to standalone search service?
                await SavePostToELK(evt);

                var followers = _followRepository.GetFollowersByUIDAsync(token.AccessToken, evt.AuthorId).Result!.Data!;

                foreach (var user in followers)
                {
                    await _feedRepository.CreateAsync(new UserFeedEntry
                    {
                        PostId = evt.PostId,
                        UserId = user.Id,
                        AuthorId = evt.AuthorId,
                    });
                }
                _logger.LogInformation($"Feeds of post {evt.PostId} have propagated success.");
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error processing Kafka message: {ex.Message}");
            }
        }

        private async Task SavePostToELK(PostSavedEvent postEvent)
        {
            var post = _mapper.Map<SavePostToElasticRequest>(postEvent);
            if (post == null)
            {
                _logger.LogError($"Cannot add post {postEvent.PostId} to elastic, post not exist!");
                return;
            }
            await _searchRepository.AddOrUpdateDocument(post);
            _logger.LogInformation($"Saved document success.");
        }
    }
}
