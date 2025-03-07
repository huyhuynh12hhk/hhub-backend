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
        private readonly IPostService postService;
        private readonly IMapper mapper;

        public PostsController(IPostService postService, IMapper mapper)
        {
            this.postService = postService;
            this.mapper = mapper;
        }

        [AllowAnonymous]
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var result = await postService.GetPosts();

            return Ok(BaseRepsonse<IEnumerable<PostResponse>>
                .Success(
                    result.Select(p => mapper.Map<PostResponse>(p))
                ));
        }

        [AllowAnonymous]
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(string id)
        {
            var result = await postService.GetPostById(id);

            return Ok(BaseRepsonse<PostResponse>
                .Success(mapper.Map<PostResponse>(result)));
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreatePostRequest request)
        {
            var post = await postService.CreateAPost(request);

            return Ok(BaseRepsonse<PostResponse>
                .Success(mapper.Map<PostResponse>(post)));
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(string id, [FromBody] UpdatePostRequest request)
        {
            var result = await postService.UpdatePost(id, request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    mapper.Map<CommentResponse>(result)
                ));
        }

        [HttpPost("{id}/reactions")]
        public async Task<IActionResult> ToggleReaction([Required] string id, [FromBody] MakeReactionRequest request)
        {

            await postService.ToggleReaction(id, request);

            return Accepted();
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([Required] string id)
        {
            await postService.DeletePost(id);

            return NoContent();
        }
    }
}