import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import useIsAuthenticated from "../../utils/hooks/useIsAuthenticated";
import { Card, CircularProgress, Typography } from "@mui/material";
import Post from "../../components/Post";
import { Box } from "@mui/system";
import CreatePostField from "./CreatePostField";
import Grid from "@mui/material/Grid2";
import { fetchPosts } from "../../services/postService";
import { authenticate } from "../../services/authenticationService";

const Feed = () => {
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);
  const observer = useRef();
  const lastPostElementRef = useRef();
  const isAuthenticated = useIsAuthenticated();
  const navigate = useNavigate();

  //   useEffect(() => {
  //     if (!isAuthenticated) {
  //       navigate("/login");
  //     } else {
  //       loadPosts(page);
  //     }
  //   }, [navigate, page]);

  useEffect(() => {
    loadPosts(page);
  }, []);

  const loadPosts = (page) => {
    console.log(`loading posts for page ${page}`);
    setLoading(true);
    fetchPosts(page)
      .then((response) => {
        // setTotalPages(response.data.result.totalPages);
        setPosts((prevPosts) => [...prevPosts, ...response.data]);
        // setHasMore(response.data.result.data.length > 0);
        console.log("loaded posts:", response.data);
      })
      .catch((error) => {})
      .finally(() => {
        setLoading(false);
      });
    // setPosts(posts_data);
    setLoading(false);
  };

  useEffect(() => {
    if (!hasMore) return;

    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        if (page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
      }
    });
    if (lastPostElementRef.current) {
      observer.current.observe(lastPostElementRef.current);
    }

    setHasMore(false);
  }, [hasMore]);

  const renderPostCreationSection = () => {
    if (!isAuthenticated) return null;
    return (
      <Card
        sx={{
          width: "100%",
          boxShadow: 3,
          padding: "20px",
          marginBlock: 2,
        }}
      >
        <CreatePostField
          afterCreated={() => {
            loadPosts(page)
            console.log("After post")
          }}
        />
      </Card>
    );
  };

  return (
    <Grid
      flex
      width="100%"
      minWidth="500px"
      maxWidth="800px"
      direction="column"
      paddingLeft="80px"
    >
      {renderPostCreationSection()}
      <Card
        sx={{
          width: "100%",
          boxShadow: 3,
          padding: "20px",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "flex-start",
            width: "100%",
            gap: "10px",
          }}
        >
          <Typography
            sx={{
              fontSize: 18,
              mb: "10px",
            }}
          >
            New contents here,
          </Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "flex-start",
              width: "100%",
            }}
          ></Box>
          {posts.map((post, index) => {
            if (posts.length === index + 1) {
              return (
                <Post ref={lastPostElementRef} key={post.id} post={post} />
              );
            } else {
              return <Post key={post.id} post={post} />;
            }
          })}
          {loading && (
            <Box
              sx={{ display: "flex", justifyContent: "center", width: "100%" }}
            >
              <CircularProgress size="24px" />
            </Box>
          )}
        </Box>
      </Card>
    </Grid>
  );
};

export default Feed;
