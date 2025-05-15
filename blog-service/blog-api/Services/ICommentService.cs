using blog_api.DTOs.Request;
using blog_api.Models.Entities;

namespace blog_api.Services.Interface
{
    public interface ICommentService
    {
        Task<Comment> CreateComment(CreateCommentRequest request);
        Task<Comment> UpdateComment(string id, UpdateCommentRequest request);
        Task DeleteComment(string id);
        Task<Comment> GetCommentById(string id);
        Task<List<Comment>> GetComments(string postId, int page = 1, int pageSize = 10);
    }
}