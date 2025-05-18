using blog_api.Models.Entities;

namespace blog_api.Models.Cache
{
    public class PostCache : CacheBaseObject
    {
        public string Id { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;
        public List<UserDetail> Reactions { get; set; } = new();
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
