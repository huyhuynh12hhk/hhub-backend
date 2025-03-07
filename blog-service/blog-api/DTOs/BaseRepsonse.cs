using System.Net;

namespace blog_api.DTOs
{
    public class BaseRepsonse<T>
    {
        public int Code { get; set; }
        public string? Message { get; set; }
        public T? Data { get; set; }

        public static BaseRepsonse<T> Success(T value)
        {
            return new BaseRepsonse<T>
            {
                Code = 200,
                Data = value
            };
        }

        public static BaseRepsonse<T> Error(string message, int code = 400)
        {
            return new BaseRepsonse<T>
            {
                Code = code,
                Message = message
            };
        }

    }
}