using blog_api.DTOs;
using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public interface IFriendRepository
    {
        Task<BaseRepsonse<FollowResponse>?> GetByUIDAsync(string uid);
    }
}
