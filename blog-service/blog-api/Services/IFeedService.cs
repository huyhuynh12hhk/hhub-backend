using blog_api.DTOs.Response;
using blog_api.Models.Entities;
using blog_api.Pagination;

namespace blog_api.Services.Interface
{
    public interface IFeedService
    {
        Task<CursorPaginatedList<PostResponse>> GetFeeds(string userId, string? cursor, int size);
        Task AddFeed(Post post);
    }
}
