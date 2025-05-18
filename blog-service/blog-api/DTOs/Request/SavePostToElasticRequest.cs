namespace blog_api.DTOs.Request
{
    public class SavePostToElasticRequest
    {
        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string Content { get; set; } = null!;
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
