using System.Net.Http.Headers;

using blog_api.DTOs.Response;

using Newtonsoft.Json;

namespace blog_api.Repositories.Http
{
    public class TokenHttpRepository : ITokenRepository
    {
        private readonly string _tokenUrl;
        private readonly string _clientId;
        private readonly string _clientSecret;
        private readonly string _scope;
        private readonly HttpClient _httpClient;
        private readonly ILogger<TokenHttpRepository> _logger;

        public TokenHttpRepository(
            IConfiguration configuration,
            HttpClient httpClient,
            ILogger<TokenHttpRepository> logger)
        {
            _tokenUrl = configuration["Jwt:TokenUrl"]!;
            _clientId = configuration["Jwt:ClientId"]!;
            _clientSecret = configuration["Jwt:ClientSecret"]!;
            _scope = "openid";
            _httpClient = httpClient;
            _httpClient.BaseAddress = new Uri(_tokenUrl);
            _logger = logger;
        }

        public async Task<TokenResponse> getAccessToken()
        {
            var form = new Dictionary<string, string>()
            {
                ["client_id"] = _clientId,
                ["client_secret"] = _clientSecret,
                ["scope"] = _scope,
                ["grant_type"] = "client_credentials"
            };

            _httpClient.DefaultRequestHeaders.Accept.Add(
                new MediaTypeWithQualityHeaderValue("application/json"));

            using var content = new FormUrlEncodedContent(form);
            using var response = await _httpClient
                .PostAsync(_tokenUrl, content);

            var rawBody = await response.Content.ReadAsStringAsync();
            /*_logger.LogInformation("Token endpoint returned HTTP {StatusCode}: {Body}",
                                   response.StatusCode, rawBody);*/

            if (!response.IsSuccessStatusCode)
            {
                throw new HttpRequestException(
                    $"Error retrieving access token (HTTP {(int)response.StatusCode}): {rawBody}");
            }

            try
            {
                var token = JsonConvert.DeserializeObject<TokenResponse>(rawBody);
                if (token is null)
                    throw new JsonException("Deserialized token error");

                return token;
            }
            catch (Exception ex) when (ex is JsonException || ex is NotSupportedException)
            {
                throw new InvalidOperationException(
                    "Failed to parse token response JSON.", ex);
            }
        }
    }
}
