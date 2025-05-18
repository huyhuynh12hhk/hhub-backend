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
        private readonly ICommentService _commentService;
        private readonly IMapper _mapper;

        public CommentsController(ICommentService commentService, IMapper mapper)
        {
            this._commentService = commentService;
            this._mapper = mapper;
        }

        [AllowAnonymous]
        [HttpGet]
        public async Task<IActionResult> GetAll([Required][FromQuery] string postId)
        {
            var result = await _commentService.GetComments(postId);

            return Ok(BaseRepsonse<List<CommentResponse>>
                .Success(
                    _mapper.Map<List<CommentResponse>>(result)
                ));
        }

        [AllowAnonymous]
        [HttpGet("{id}")]
        public async Task<IActionResult> Get([Required] string id)
        {
            var result = await _commentService.GetCommentById(id);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    _mapper.Map<CommentResponse>(result)
                ));
        }


        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreateCommentRequest request)
        {
            var result = await _commentService.CreateComment(request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    _mapper.Map<CommentResponse>(result)
                ));
        }

        // PUT api/<CommentsController>/5
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(string id, [FromBody] UpdateCommentRequest request)
        {
            var result = await _commentService.UpdateComment(id, request);

            return Ok(BaseRepsonse<CommentResponse>
                .Success(
                    _mapper.Map<CommentResponse>(result)
                ));
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete([Required] string id)
        {
            await _commentService.DeleteComment(id);

            return NoContent();
        }
    }
}