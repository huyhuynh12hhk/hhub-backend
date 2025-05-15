namespace blog_api.Models.Entities
{
    public class UserFeedEntry : BaseEntity
    {
        public string UserId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string PostId { get; set; } = null!;
    }
}
