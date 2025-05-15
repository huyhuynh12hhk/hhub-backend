namespace blog_api.Pagination
{
    public class CursorPaginatedList<T> : List<T>
    {
        public string NextCursor { get; private set; }
        public int TotalPages { get; private set; }
        public int PageSize { get; private set; }
        public int TotalCount { get; private set; }
        public bool HasMore { get; private set; }
        public CursorPaginatedList(List<T> items, string nextCursor, int pageSize, bool hasMore)
        {
            NextCursor = nextCursor;
            PageSize = pageSize;
            HasMore = hasMore;
            AddRange(items);
        }

    }
}
