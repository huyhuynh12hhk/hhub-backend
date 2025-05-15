using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace blog_api.Models.Entities
{
    public class Post : BaseEntity
    {
        public string AuthorId { get; set; } = null!;
        public string Content { get; set; } = null!;

        [BsonElement("Reactions")]
        public List<UserDetail> Reactions { get; set; } = new();

    }
}