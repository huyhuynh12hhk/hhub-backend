using blog_api.DTOs;
using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public class UserHttpRepository : IUserRepository
    {

        private readonly HttpClient _httpClient;

        public UserHttpRepository(HttpClient httpClient, IConfiguration configuration)
        {
            _httpClient = httpClient;
            _httpClient.BaseAddress = new Uri(configuration["Services:Auth"] ?? "");
        }

        public async Task<BaseRepsonse<UserResponse>?> GetByUIDAsync(string uid)
        {
            throw new NotImplementedException();
        }

        public async Task<BaseRepsonse<UserResponse>?> GetByUsernameAsync(string username)
        {
            return await _httpClient.GetFromJsonAsync<BaseRepsonse<UserResponse>>(username);
        }
    }
}
