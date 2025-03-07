using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace blog_api.Entities
{
    public class Post : BaseEntity
    {
        public string AuthorId { get; set; } = null!;
        public string AuthorName { get; set; } = null!;
        public string Content { get; set; } = null!;

        [BsonElement("Reactions")]
        public List<UserDetail> Reactions { get; set; } = new();
        [BsonElement("Comments")]
        public List<Comment> Comments { get; set; } = new();

    }
}