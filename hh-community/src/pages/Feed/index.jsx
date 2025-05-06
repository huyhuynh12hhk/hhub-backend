import { useCallback, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import useIsAuthenticated from "../../utils/hooks/useIsAuthenticated";
import { Card, CircularProgress, Typography } from "@mui/material";
import Post from "../../components/Post";
import { Box } from "@mui/system";
import CreatePostField from "./CreatePostField";
import Grid from "@mui/material/Grid2";
import { fetchPosts } from "../../services/postService";
import { authenticate } from "../../services/authenticationService";
import ScrollDataView from "../../components/ScrollDataView";
import InfiniteScroll from "react-infinite-scroll-component";

const Feed = () => {
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const isAuthenticated = useIsAuthenticated();
  const navigate = useNavigate();

  // initial & page-based load
  const loadPosts = async () => {
    setTimeout(async () => {
      try {
        console.log(`Load on page ${page}`);
        const { data } = await fetchPosts(page);
        console.log(`Response`, data);
        if (data.length === 0) {
          console.log(`End`);
          setHasMore(false);
        } else {
          setPosts((prev) => [...prev, ...data]);
        }
      } catch (err) {
        console.error("Error loading posts:", err);
      }
      setPage((prev) => prev + 1);
    }, 1500);
  };

  useEffect(() => {
    loadPosts();
  }, []);
  // );

  // useEffect(() => {
  //   loadPosts();
  // }, [navigate]);

  // When a new post is created, reset feed
  const handleAfterCreate = () => {
    setPosts([]);
    setPage(1);
    setHasMore(true);
  };

  const renderPostCreationSection = () => {
    // if (!isAuthenticated) return null;
    return (
      <Card
        sx={{
          width: "100%",
          boxShadow: 3,
          padding: "20px",
          marginBlock: 2,
        }}
      >
        <CreatePostField afterCreated={handleAfterCreate} />
      </Card>
    );
  };

  return (
    <Grid
      container
      direction="column"
      alignItems="center"
      sx={{ px: 2 }}
      // flex
      // width="100%"
      // minWidth="500px"
      maxWidth="800px"
      // direction="column"
      // paddingLeft="80px"
    >
      {renderPostCreationSection()}
      <Card sx={{ width: "100%", boxShadow: 3, p: 2 }}>
        <Typography
          sx={{
            fontSize: 18,
            mb: "10px",
          }}
        >
          New contents here,
        </Typography>

        <InfiniteScroll
          scrollableTarget="scrollableDiv"
          dataLength={posts.length}
          next={loadPosts}
          hasMore={hasMore}
          loader={
            <Box sx={{ display: "flex", justifyContent: "center", py: 2 }}>
              <CircularProgress size={24} />
            </Box>
          }
          endMessage={
            <Box
              sx={{
                marginBottom: "10vh",
              }}
            >
              <Typography align="center" sx={{ py: 4 }}>
                No posts to display.
              </Typography>
            </Box>
          }
          // Optional: wrap the scroll area
          // You can target a scrollable div if you don't want window scroll
          // scrollableTarget="scrollableDiv"
        >
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
            {posts.map((post) => (
              <Post key={post.id} post={post} />
            ))}
          </Box>
        </InfiniteScroll>
      </Card>
    </Grid>
  );
};

export default Feed;

// {!posts.length && <div ref={lastElementRef} />}
// {posts.map((post, index) => {
//   if (index === posts.length - 1) {
//     return <Post ref={lastElementRef} key={post.id} post={post} />;
//   }
//   return <Post key={post.id} post={post} />;
// })}
// {loading && (
//   <Box sx={{ display: "flex", justifyContent: "center", py: 2 }}>
//     <CircularProgress size={24} />
//   </Box>
// )}

{
  /* <Box
  sx={{
    display: "flex",
    flexDirection: "column",
    // alignItems: "flex-start",
    // width: "100%",
    gap: 2,
  }}
>
  <ScrollDataView fetchData={loadPosts} items={posts} loading={loading} />
</Box>; */
}

// const loadPosts = (page) => {
//   console.log(`loading posts for page ${page}`);
//   setLoading(true);
//   fetchPosts(page)
//     .then((response) => {
//       console.log(`Feed data`, response);

//       if (response.data.length === 0) {
//         console.log("There no new now!");
//         setHasMore(false);
//         return;
//       }
//       setPosts((prevPosts) => [...prevPosts, ...response.data]);
//       setHasMore(true);
//       // console.log("loaded posts:", response.data);
//     })
//     .catch((error) => {})
//     .finally(() => {
//       setLoading(false);
//     });
//   // setPosts(posts_data);
//   setLoading(false);
// };
