using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.Entities;
using blog_api.Repositories;

namespace blog_api.Services
{
    internal class PostService : IPostService
    {
        private readonly IMongoDBRepository<Post> postRepo;
        private readonly IMapper mapper;

        public PostService(IMongoDBRepository<Post> postRepo, IMapper mapper)
        {
            this.postRepo = postRepo;
            this.mapper = mapper;
        }

        public async Task<List<Post>> GetPosts()
        {
            var posts = await postRepo.GetAllAsync();

            return posts.OrderByDescending(e => e.CreatedAt).ToList();
        }

        public async Task<Post> GetPostById(string id)
        {
            var post = await postRepo.GetAsync(id);

            return post;
        }

        public async Task<Post> CreateAPost(CreatePostRequest request)
        {
            var post = mapper.Map<Post>(request);

            await postRepo.CreateAsync(post);

            return post;
        }

        public async Task<Post> UpdatePost(string id, UpdatePostRequest request)
        {
            var post = await postRepo.GetAsync(id);
            mapper.Map(request, post);

            await postRepo.UpdateAsync(post);

            return post;
        }

        public async Task ToggleReaction(string id, MakeReactionRequest request)
        {
            var post = await postRepo.GetAsync(id);

            if (post.Reactions == null)
                post.Reactions = new();

            var user = post.Reactions.Find(u => u.Id == request.Id);
            if (user == null)
            {
                post.Reactions.Add(mapper.Map<UserDetail>(request));
            }
            else
            {
                post.Reactions.Remove(user);
            }

            await postRepo.UpdateAsync(post);

        }

        public async Task DeletePost(string id)
        {

            await postRepo.RemoveAsync(id);
        }


    }
}