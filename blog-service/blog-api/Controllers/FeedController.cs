using AutoMapper;

using blog_api.DTOs;
using blog_api.DTOs.Response;
using blog_api.Services.Interface;

using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace blog_api.Controllers
{
    [Authorize]
    [Route("feed")]
    [ApiController]
    public class FeedController : ControllerBase
    {
        private readonly IFeedService _feedService;
        private readonly IMapper _mapper;

        public FeedController(IMapper mapper, IFeedService feedService)
        {
            _mapper = mapper;
            _feedService = feedService;
        }

        [HttpGet]
        public async Task<IActionResult> Get(
            [FromQuery] string user,
            [FromQuery] int size = 10,
            [FromQuery] string? cursor = null
        )
        {
            var result = await _feedService.GetFeeds(user, cursor, size);

            return Ok(BaseRepsonse<IEnumerable<PostResponse>>
            .Success(
                    result.Select(p => _mapper.Map<PostResponse>(p))
                ));
        }
    }
}
