using blog_api.DTOs.Response;

namespace blog_api.Repositories.Http
{
    public interface ITokenRepository
    {
        Task<TokenResponse> getAccessToken();
    }
}
