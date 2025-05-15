using AutoMapper;
using blog_api.Controllers;
using blog_api.DTOs;
using blog_api.DTOs.Request;
using blog_api.DTOs.Response;
using blog_api.Models.Entities;
using blog_api.Services;
using FakeItEasy;
using Microsoft.AspNetCore.Mvc;

namespace blog_api_test.Controllers
{
    public class PostsControllerTests
    {
        private readonly IPostService _service;
        private readonly IMapper _mapper;
        private readonly PostsController _controller;

        public PostsControllerTests()
        {
            _service = A.Fake<IPostService>();
            _mapper = A.Fake<IMapper>();
            _controller = new(_service, _mapper);
        }

        [Fact]
        public void GetDetail_NormalFlow_ReturnItem()
        {

            var id = "ab2bd817-98cd-4cf3-a80a-53ea0cd9c200";
            var post = A.Fake<Post>();
            var postResponse = new PostResponse
            {
                Id = id,
                AuthorId = "AuthorId",
                AuthorName = "Test",
                Content = "Test",


            };
            A.CallTo(() => _service.GetPostById(id)).Returns(post);
            A.CallTo(() => _mapper.Map<PostResponse>(post)).Returns(postResponse);

            var result = _controller.Get(id).Result as OkObjectResult;


            Assert.IsType<OkObjectResult>(result);
            Assert.Equal(id, ((BaseRepsonse<PostResponse>)result.Value!).Data!.Id);
        }

        [Fact]
        public void Get_NormalFlow_ReturnListItem()
        {

            var posts = A.Fake<ICollection<Post>>();
            var postsResponse = A.Fake<List<PostResponse>>();
            A.CallTo(() => _mapper.Map<List<PostResponse>>(posts)).Returns(postsResponse);

            var result = _controller.Get().Result;

            Assert.IsType<OkObjectResult>(result);
        }

        [Fact]
        public void Create_NormalFlow_CreateSuccess()
        {
            var request = A.Fake<CreatePostRequest>();
            var post = A.Fake<Post>();
            var postResponse = A.Fake<PostResponse>();
            A.CallTo(() => _service.CreateAPost(request)).Returns(post);
            A.CallTo(() => _mapper.Map<PostResponse>(post)).Returns(postResponse);

            var result = _controller.Post(request).Result;

            Assert.IsType<OkObjectResult>(result);
        }
    }
}
