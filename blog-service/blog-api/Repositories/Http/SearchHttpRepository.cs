using blog_api.Configuration;
using blog_api.DTOs.Request;

using Elastic.Clients.Elasticsearch;

using Microsoft.Extensions.Options;

namespace blog_api.Repositories.Http
{
    public class SearchHttpRepository : ISearchRepository
    {
        private readonly ElasticsearchClient _elasticClient;
        private readonly ElasticSettings _elasticSettings;

        public SearchHttpRepository(
            ElasticsearchClient elasticClient,
            IOptions<ElasticSettings> elasticSettings)
        {
            _elasticClient = elasticClient;
            _elasticSettings = elasticSettings.Value;
        }

        public async Task AddOrUpdateDocument(SavePostToElasticRequest post)
        {
            var response = await _elasticClient.IndexAsync(post,
                idx => idx.Index(_elasticSettings.DefaultIndex)
                .OpType(OpType.Index)
            );

            if (!response.IsValidResponse) { }
        }


        public async Task DeleteDocument(string key)
        {
            await _elasticClient.DeleteAsync<SavePostToElasticRequest>(key, d => d.Index(_elasticSettings.DefaultIndex));
        }
    }
}
