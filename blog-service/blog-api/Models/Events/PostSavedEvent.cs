using blog_api.Models.Entities;

namespace blog_api.Models.Events
{
    public class PostSavedEvent
    {
        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime CreatedDate { get; set; } = DateTime.UtcNow;
        public List<UserDetail> Reactions { get; set; } = new();
    }
}
