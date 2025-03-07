using blog_api.DTOs.Request;
using blog_api.Entities;

namespace blog_api.Services
{
    public interface ICommentService
    {
        Task<Comment> CreateComment(CreateCommentRequest request);
        Task<Comment> UpdateComment(string id, UpdateCommentRequest request);
        Task DeleteComment(string id);
        Task<Comment> GetCommentById(string id);
        Task<List<Comment>> GetComments(string postId);
    }
}