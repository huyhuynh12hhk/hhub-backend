using blog_api.DTOs;
using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public class FriendHttpRepository : IFriendRepository
    {
        public async Task<BaseRepsonse<FollowResponse>?> GetByUIDAsync(string uid)
        {
            throw new NotImplementedException();
        }
    }
}
