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

        public PostSavedConsumer(
            ConsumerConfig consumerConfig,
            IConfiguration configuration,
            ILogger<PostSavedConsumer> logger,
            IServiceScopeFactory serviceScopeFactory,
            IServiceProvider services)
        {
            _logger = logger;
            var scope = services.CreateScope();
            _followRepository = scope
                .ServiceProvider
                .GetRequiredService<IFollowRepository>();
            _feedRepository = scope
                .ServiceProvider
                .GetRequiredService<IMongoDBRepository<UserFeedEntry>>();

            this._consumer = new ConsumerBuilder<Ignore, string>(consumerConfig)
                .SetErrorHandler((_, e) => logger.LogError(e.Reason))
                .Build();

            _consumer.Subscribe(configuration["Kafka:PostSavedTopic"]);
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
                var evt = JsonConvert.DeserializeObject<PostSavedEvent>(consumeResult.Message.Value)!;

                var followers = _followRepository.GetFollowersByUIDAsync(evt.AuthorId).Result!.Data!;

                foreach (var user in followers)
                {
                    //var update = Builders<UserFeedEntry>.Update.Push(f => f.Entries,
                    //    new FeedEntry { PostId = evt.PostId, CreatedAt = evt.CreatedAt });
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
    }
}
