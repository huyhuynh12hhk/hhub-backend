using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace blog_api.Entities
{
    public class Comment : BaseEntity
    {

        public string PostId { get; set; } = null!;
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;

    }
}