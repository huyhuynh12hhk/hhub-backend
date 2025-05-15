namespace blog_api.Configuration
{
    public class RedisSettings
    {
        public string ConnectionString { get; set; } = null!;
        public string FriendKey { get; set; } = null!;
        public string FollowKey { get; set; } = null!;
        public string UserKey { get; set; } = null!;
    }
}
