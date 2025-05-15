using blog_api.DTOs;
using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public interface IUserRepository
    {
        Task<BaseRepsonse<UserResponse>?> GetByUsernameAsync(string username);
        Task<BaseRepsonse<UserResponse>?> GetByUIDAsync(string uid);
    }
}
