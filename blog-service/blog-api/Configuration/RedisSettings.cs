namespace blog_api.Configuration
{
    public class RedisSettings
    {
        public string ConnectionString { get; set; } = null!;
        public string FriendKey { get; set; } = null!;
        public string FollowKey { get; set; } = null!;
        public string UserKey { get; set; } = null!;
        public string PostKey { get; set; } = null!;
        public string CommentKey { get; set; } = null!;
        public string FeedKey { get; set; } = null!;
    }
}
