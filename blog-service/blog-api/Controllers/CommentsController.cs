using System.ComponentModel.DataAnnotations;

using AutoMapper;

using blog_api.DTOs;
using blog_api.DTOs.Request;
using blog_api.DTOs.Response;
using blog_api.Services.Interface;

using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace blog_api.Controllers
{
    [Authorize]
    [Route("comments")]
    [ApiController]
    public class CommentsController : ControllerBase
    {
        private readonly ICommentService commentService;
        private readonly IMapper mapper;

        public CommentsController(ICommentService commentService, IMapper mapper)
        {
            this.commentService = commentService;
            this.mapper = mapper;
        }

        [AllowAnonymous]
        [HttpGet]
        public async Task<IActionResult> GetAll([Required][FromQuery] string postId)
        {
            var result = await commentService.GetComments(postId);

            return Ok(BaseRepsonse<List<CommentResponse>>
                .Success(
                    mapper.Map<List<CommentResponse>>(result)
                ));
        }

        [AllowAnonymous]
        [HttpGet("{id}")]
        public async Task<IActionResult> Get([Required] string id)
        {
            var result = await commentService.GetCommentById(id);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    mapper.Map<CommentResponse>(result)
                ));
        }


        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreateCommentRequest request)
        {
            var result = await commentService.CreateComment(request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    mapper.Map<CommentResponse>(result)
                ));
        }

        // PUT api/<CommentsController>/5
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(string id, [FromBody] UpdateCommentRequest request)
        {
            var result = await commentService.UpdateComment(id, request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    mapper.Map<CommentResponse>(result)
                ));
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([Required] string id)
        {
            await commentService.DeleteComment(id);

            return NoContent();
        }
    }
}