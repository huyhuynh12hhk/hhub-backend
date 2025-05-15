using blog_api.DTOs;
using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public class FollowHttpRepository : IFollowRepository
    {
        private readonly HttpClient _httpClient;

        public FollowHttpRepository(HttpClient httpClient, IConfiguration configuration)
        {
            _httpClient = httpClient;
            _httpClient.BaseAddress = new Uri(configuration["Services:Conn"] ?? "");
        }

        public async Task<BaseRepsonse<List<FollowResponse>>?> GetFollingsByUIDAsync(string uid)
        {
            return await _httpClient.GetFromJsonAsync<BaseRepsonse<List<FollowResponse>>>($"follows/{uid}/followings");

        }

        public async Task<BaseRepsonse<List<FollowResponse>>?> GetFollowersByUIDAsync(string uid)
        {
            return await _httpClient.GetFromJsonAsync<BaseRepsonse<List<FollowResponse>>>($"follows/{uid}/followers");
        }
    }
}
