using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.Models.Entities;
using blog_api.Repositories;
using blog_api.Services.Interface;

namespace blog_api.Services
{
    internal class CommentService : ICommentService
    {
        private readonly IMongoDBRepository<Comment> _commentRepository;
        private readonly IMapper mapper;
        private readonly ILogger<CommentService> _logger;

        public CommentService(
            IMongoDBRepository<Comment> commentRepo,
            IMapper mapper, ILogger<CommentService> logger)
        {
            this._commentRepository = commentRepo;
            this.mapper = mapper;
            _logger = logger;
        }

        public async Task<Comment> CreateComment(CreateCommentRequest request)
        {
            var comment = mapper.Map<Comment>(request);

            await _commentRepository.CreateAsync(comment);

            return comment;
        }

        public async Task DeleteComment(string id)
        {
            await _commentRepository.RemoveAsync(id);
        }

        public async Task<Comment> GetCommentById(string id)
        {
            var comment = await _commentRepository.GetAsync(id);

            return comment;
        }

        public async Task<List<Comment>> GetComments(string postId, int page = 1, int pageSize = 10)
        {
            var comments = await _commentRepository.GetAllAsync(c => c.PostId == postId);

            return comments;
        }

        public async Task<Comment> UpdateComment(string id, UpdateCommentRequest request)
        {
            var comment = await _commentRepository.GetAsync(id);

            mapper.Map(request, comment);

            await _commentRepository.UpdateAsync(comment);

            return comment;
        }
    }
}