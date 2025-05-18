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
            return await _httpClient.GetFromJsonAsync<BaseRepsonse<UserResponse>>(uid);
        }

        public async Task<BaseRepsonse<UserResponse>?> GetByUsernameAsync(string username)
        {
            throw new NotImplementedException();
            //return await _httpClient.GetFromJsonAsync<BaseRepsonse<UserResponse>>(username);
        }
    }
}
