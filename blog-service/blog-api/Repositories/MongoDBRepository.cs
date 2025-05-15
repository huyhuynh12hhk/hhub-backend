using System.Linq.Expressions;
using System.Text;

using blog_api.Models.Entities;
using blog_api.Pagination;

using MongoDB.Driver;
using MongoDB.Driver.Linq;

using Newtonsoft.Json;

namespace blog_api.Repositories
{
    public class MongoDBRepository<T> : IMongoDBRepository<T> where T : BaseEntity
    {
        private readonly IMongoCollection<T> dbCollection;
        private readonly FilterDefinitionBuilder<T> filterBuilder = Builders<T>.Filter;
        public MongoDBRepository(IMongoDatabase database, string collectionName)
        {
            dbCollection = database.GetCollection<T>(collectionName);
        }
        public async Task<OffSetPaginatedList<T>> GetAllAsync(int page = 1, int pageSize = 10)
        {
            var totalCount = (int)await dbCollection
                            .CountDocumentsAsync(filterBuilder.Empty);

            var items = await dbCollection
                .Find(filterBuilder.Empty)
                .Skip((page - 1) * pageSize)
                .Limit(pageSize)
                .ToListAsync();

            return new OffSetPaginatedList<T>(
                items,
                totalCount,
                page,
                pageSize
            );
        }
        public async Task<OffSetPaginatedList<T>> GetAllAsync(Expression<Func<T, bool>> filters, int page = 1, int pageSize = 10)
        {
            var totalCount = (int)await dbCollection
                            .CountDocumentsAsync(filters);

            var items = await dbCollection
                .Find(filters)
                .Skip((page - 1) * pageSize)
                .Limit(pageSize)
                .ToListAsync();

            return new OffSetPaginatedList<T>(
                items,
                totalCount,
                page,
                pageSize
            );

        }

        public async Task<CursorPaginatedList<T>> GetAllAsync(
            Expression<Func<T, bool>> filters,
            string cursor,
            int limit = 10
        )
        {
            PageCursor? curObj = null;
            if (!string.IsNullOrEmpty(cursor))
            {
                var json = Encoding.UTF8.GetString(Convert.FromBase64String(cursor));
                curObj = JsonConvert.DeserializeObject<PageCursor>(json);
            }

            var baseFilter = filterBuilder.Where(filters);
            FilterDefinition<T> pagingFilter;
            if (curObj == null)
            {
                pagingFilter = filterBuilder.Empty;
            }
            else
            {
                var ltCreated = filterBuilder.Lt(x => x.CreatedAt, curObj.CreatedAt);
                var eqCreated = filterBuilder.Eq(x => x.CreatedAt, curObj.CreatedAt);
                var ltId = filterBuilder.Lt(x => x.Id, curObj.Id);

                pagingFilter = filterBuilder.Or(
                    ltCreated,
                    filterBuilder.And(eqCreated, ltId)
                );
            }

            var combinedFilter = filterBuilder.And(baseFilter, pagingFilter);

            // Check has more
            var page = await dbCollection
                .Find(combinedFilter)
                .SortByDescending(x => x.CreatedAt)
                .ThenByDescending(x => x.Id)
                .Limit(limit + 1)
                .ToListAsync();

            // Next Cursor
            bool hasMore = page.Count > limit;
            if (hasMore) page.RemoveAt(page.Count - 1);

            string? nextCursor = null;
            if (hasMore)
            {
                var last = page.Last();
                var newCurObj = new PageCursor
                {
                    Id = last.Id,
                    CreatedAt = last.CreatedAt
                };
                var jsonOut = JsonConvert.SerializeObject(newCurObj);
                nextCursor = Convert.ToBase64String(
                    Encoding.UTF8.GetBytes(jsonOut)
                );
            }

            // 7. Return paginated list
            return new CursorPaginatedList<T>(
                page,          // items
                nextCursor,    // new cursor or null
                limit,
                hasMore
            );

        }
        public async Task<T> GetAsync(string id)
        {
            FilterDefinition<T> filter = filterBuilder.Eq(entity => entity.Id, id);
            return await dbCollection.Find(filter).FirstOrDefaultAsync();
        }
        public async Task<T> GetAsync(Expression<Func<T, bool>> filter)
        {
            return await dbCollection.Find(filter).FirstOrDefaultAsync();
        }
        public async Task CreateAsync(T entity)
        {
            if (entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }
            await dbCollection.InsertOneAsync(entity);
        }
        public async Task CreateManyAsync(List<T> entity)
        {
            if (entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }
            await dbCollection.InsertManyAsync(entity);
        }
        public async Task UpdateAsync(T entity)
        {
            if (entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }

            FilterDefinition<T> filter = filterBuilder.Eq(existingEntity => existingEntity.Id, entity.Id);
            await dbCollection.ReplaceOneAsync(filter, entity);
        }
        public async Task UpdateManyAsync(List<T> entities)
        {
            if (entities == null || !entities.Any())
            {
                throw new ArgumentNullException(nameof(entities));
            }
            foreach (var entity in entities)
            {
                FilterDefinition<T> filter = filterBuilder.Eq(existingEntity => existingEntity.Id, entity.Id);
                await dbCollection.ReplaceOneAsync(filter, entity);
            }
        }
        public async Task RemoveAsync(string id)
        {
            FilterDefinition<T> filter = filterBuilder.Eq(entity => entity.Id, id);
            await dbCollection.DeleteOneAsync(filter);
        }
        public async Task RemoveAsync(Expression<Func<T, bool>> filter)
        {
            await dbCollection.DeleteOneAsync(filter);
        }
        public async Task RemoveManyAsync(List<string> ids)
        {
            var filters = filterBuilder.In("_id", ids.Select(i => i.ToString()));
            await dbCollection.DeleteManyAsync(filters);
        }
        public async Task RemoveManyAsync(Expression<Func<T, bool>> filters)
        {
            await dbCollection.DeleteManyAsync(filters);
        }

        public async Task<int> CountAsync(Expression<Func<T, bool>> filters)
        {
            return (int)await dbCollection.CountDocumentsAsync<T>(filters);
        }
    }
}