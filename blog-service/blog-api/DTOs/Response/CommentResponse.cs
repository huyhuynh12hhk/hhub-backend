namespace blog_api.DTOs.Response
{
    public class CommentResponse
    {
        public string Id { get; set; } = null!;
        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}