using blog_api.DTOs.Request;
using blog_api.Models.Entities;

namespace blog_api.Services
{
    public interface IPostService
    {
        Task<Post> CreateAPost(CreatePostRequest request);
        Task<Post> UpdatePost(string id, UpdatePostRequest request);
        Task ToggleReaction(string id, MakeReactionRequest request);
        Task DeletePost(string id);
        Task<Post?> GetPostById(string id);
        Task<List<Post>> GetPosts();
    }
}