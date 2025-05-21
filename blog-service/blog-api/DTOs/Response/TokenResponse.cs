using Newtonsoft.Json;

namespace blog_api.DTOs.Response
{
    public class TokenResponse
    {
        [JsonProperty("access_token")]
        public string AccessToken { get; set; } = null!;
        [JsonProperty("token_type")]
        public string TokenType { get; set; } = null!;

        [JsonProperty("expires_in")]
        public long ExpiresIn { get; set; }
        [JsonProperty("scope")]
        public string Scope { get; set; } = null!;
    }
}
