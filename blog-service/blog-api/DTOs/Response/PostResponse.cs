using blog_api.Entities;

namespace blog_api.DTOs.Response
{
    public class PostResponse
    {
        public string Id { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;
        public List<UserDetail> Reactions { get; set; } = new();
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}