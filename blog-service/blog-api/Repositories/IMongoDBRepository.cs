using System.Linq.Expressions;

using blog_api.Entities;

using MongoDB.Driver;

namespace blog_api.Repositories
{
    public interface IMongoDBRepository<T> where T : BaseEntity
    {
        Task<List<T>> GetAllAsync();
        Task<List<T>> GetAllAsync(Expression<Func<T, bool>>? filters);
        Task<T> GetAsync(string id);
        Task<T> GetAsync(Expression<Func<T, bool>> filter);
        Task CreateAsync(T entity);
        Task CreateManyAsync(List<T> entity);
        Task UpdateAsync(T entity);
        Task UpdateManyAsync(List<T> entities);
        Task RemoveAsync(string id);
        Task RemoveAsync(Expression<Func<T, bool>> filter);
        Task RemoveManyAsync(List<string> id);
        Task RemoveManyAsync(Expression<Func<T, bool>> filters);
    }
}