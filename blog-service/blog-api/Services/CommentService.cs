using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.Entities;
using blog_api.Repositories;

namespace blog_api.Services
{
    internal class CommentService : ICommentService
    {
        private readonly IMongoDBRepository<Comment> commentRepo;
        private readonly IMapper mapper;

        public CommentService(IMongoDBRepository<Comment> commentRepo, IMapper mapper)
        {
            this.commentRepo = commentRepo;
            this.mapper = mapper;
        }

        public async Task<Comment> CreateComment(CreateCommentRequest request)
        {
            var comment = mapper.Map<Comment>(request);

            await commentRepo.CreateAsync(comment);

            return comment;
        }

        public async Task DeleteComment(string id)
        {
            await commentRepo.RemoveAsync(id);
        }

        public async Task<Comment> GetCommentById(string id)
        {
            var comment = await commentRepo.GetAsync(id);

            return comment;
        }

        public async Task<List<Comment>> GetComments(string postId)
        {
            var comments = await commentRepo.GetAllAsync(c => c.PostId == postId);

            return comments;
        }

        public async Task<Comment> UpdateComment(string id, UpdateCommentRequest request)
        {
            var comment = await commentRepo.GetAsync(id);

            mapper.Map(request, comment);

            await commentRepo.UpdateAsync(comment);

            return comment;
        }
    }
}