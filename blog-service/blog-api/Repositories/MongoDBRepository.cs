using System.Linq.Expressions;

using blog_api.Entities;

using MongoDB.Driver;

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
        public async Task<List<T>> GetAllAsync()
        {
            return await dbCollection.Find(filterBuilder.Empty).ToListAsync();
        }
        public async Task<List<T>> GetAllAsync(Expression<Func<T, bool>> filters)
        {
            return await dbCollection.Find(filters).ToListAsync();
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
    }
}