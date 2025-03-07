namespace blog_api.DTOs.Request
{
    public class CreateCommentRequest
    {
        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;
    }
}