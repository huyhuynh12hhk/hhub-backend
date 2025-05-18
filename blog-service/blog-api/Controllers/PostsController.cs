using System.ComponentModel.DataAnnotations;

using AutoMapper;

using blog_api.DTOs;
using blog_api.DTOs.Request;
using blog_api.DTOs.Response;
using blog_api.Services;

using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace blog_api.Controllers
{
    [Authorize]
    [Route("posts")]
    [ApiController]
    public class PostsController : ControllerBase
    {
        private readonly IPostService _postService;
        private readonly IMapper _mapper;

        public PostsController(IPostService postService, IMapper mapper)
        {
            this._postService = postService;
            this._mapper = mapper;
        }

        [AllowAnonymous]
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var result = await _postService.GetPosts();

            return Ok(BaseRepsonse<IEnumerable<PostResponse>>
                .Success(
                    result.Select(p => _mapper.Map<PostResponse>(p))
                ));
        }

        [AllowAnonymous]
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(string id)
        {
            var result = await _postService.GetPostById(id);

            return Ok(BaseRepsonse<PostResponse>
                .Success(_mapper.Map<PostResponse>(result)));
        }


        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreatePostRequest request)
        {
            var post = await _postService.CreateAPost(request);

            return Ok(BaseRepsonse<PostResponse>
                .Success(_mapper.Map<PostResponse>(post)));
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(string id, [FromBody] UpdatePostRequest request)
        {
            var result = await _postService.UpdatePost(id, request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    _mapper.Map<CommentResponse>(result)
                ));
        }

        [HttpPost("{id}/reactions")]
        public async Task<IActionResult> ToggleReaction([Required] string id, [FromBody] MakeReactionRequest request)
        {

            await _postService.ToggleReaction(id, request);

            return Accepted();
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([Required] string id)
        {
            await _postService.DeletePost(id);

            return NoContent();
        }
    }
}