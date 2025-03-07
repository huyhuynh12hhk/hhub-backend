using AutoMapper;

using blog_api.DTOs.Request;
using blog_api.DTOs.Response;
using blog_api.Entities;

namespace blog_api.Mapper
{
    public class MapperProfile : Profile
    {
        public MapperProfile()
        {

            CreateMap<CreatePostRequest, Post>().ReverseMap();
            CreateMap<UpdatePostRequest, Post>().ReverseMap();
            CreateMap<Post, PostResponse>().ReverseMap();

            CreateMap<CreateCommentRequest, Comment>().ReverseMap();
            CreateMap<UpdateCommentRequest, Comment>().ReverseMap();
            CreateMap<Comment, CommentResponse>().ReverseMap();

            CreateMap<MakeReactionRequest, UserDetail>().ReverseMap();

        }
    }
}