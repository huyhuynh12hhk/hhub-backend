namespace blog_api.DTOs
{
    public class BaseResponse
    {
        public int Code { get; set; }
        public string? Message { get; set; }

        public static BaseResponse Error(string message, int code = 400)
        {
            return new BaseResponse
            {
                Code = code,
                Message = message
            };
        }

    }

    public class BaseRepsonse<T> : BaseResponse where T : class
    {

        public T? Data { get; set; }

        public static BaseRepsonse<T> Success(T value, string message = "Success")
        {
            return new BaseRepsonse<T>
            {
                Code = 200,
                Data = value
            };
        }

    }
}