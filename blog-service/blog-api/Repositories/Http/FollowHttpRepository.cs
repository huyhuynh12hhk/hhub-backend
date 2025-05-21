using System.Net.Http.Headers;

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

        public async Task<BaseRepsonse<List<FollowResponse>>?> GetFollingsByUIDAsync(string accessToken, string uid)
        {
            _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
            return await _httpClient
                .GetFromJsonAsync<BaseRepsonse<List<FollowResponse>>>($"follows/{uid}/followings");

        }

        public async Task<BaseRepsonse<List<FollowResponse>>?> GetFollowersByUIDAsync(string accessToken, string uid)
        {
            _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
            return await _httpClient.GetFromJsonAsync<BaseRepsonse<List<FollowResponse>>>($"follows/{uid}/followers");
        }
    }
}
