using blog_api.DTOs.Request;

namespace blog_api.Repositories.Http
{
    public interface ISearchRepository
    {
        Task AddOrUpdateDocument(SavePostToElasticRequest post);
        Task DeleteDocument(string key);
    }
}
