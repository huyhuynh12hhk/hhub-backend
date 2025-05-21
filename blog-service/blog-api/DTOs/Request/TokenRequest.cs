using Newtonsoft.Json;

namespace blog_api.DTOs.Request
{
    public class TokenRequest
    {
        [JsonProperty("grant_type")]
        public string GrantType { get; set; } = null!;
        [JsonProperty("client_id")]
        public string ClientId { get; set; } = null!;

        [JsonProperty("client_secret")]
        public string ClientSecret { get; set; } = null!;
        [JsonProperty("scope")]
        public string Scope { get; set; } = null!;

        public Dictionary<string, string> ToDictionary()
        {
            var json = JsonConvert.SerializeObject(this);
            return JsonConvert.DeserializeObject<Dictionary<string, string>>(json)!;
        }
    }
}
