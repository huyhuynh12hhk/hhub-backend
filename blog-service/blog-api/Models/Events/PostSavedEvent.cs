namespace blog_api.Models.Events
{
    public class PostSavedEvent
    {
        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
