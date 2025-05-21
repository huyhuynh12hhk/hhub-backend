using System.Linq.Expressions;

using blog_api.Models.Entities;
using blog_api.Pagination;

namespace blog_api.Repositories
{
    public interface IMongoDBRepository<T> where T : BaseEntity
    {
        Task<OffSetPaginatedList<T>> GetAllAsync(int page = 1, int pageSize = 10);
        Task<OffSetPaginatedList<T>> GetAllAsync(Expression<Func<T, bool>>? filters, int page = 1, int pageSize = 10);
        Task<CursorPaginatedList<T>> GetAllAsync(Expression<Func<T, bool>> filters, string cursor, int limit = 10);
        Task<int> CountAsync(Expression<Func<T, bool>> filters);
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