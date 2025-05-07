import Grid from "@mui/material/Grid2";
import { useEffect, useState } from "react";
import Comment from "../../Comment";
import { comments_data } from "./data";
import { Button, Typography } from "@mui/material";
import CommentField from "./ComentField";
import { getComments } from "../../../services/commentService";
import useIsAuthenticated from "../../../utils/hooks/useIsAuthenticated";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);

  const fetchComments = async () => {
    try {
      const response = await getComments(postId);
      setComments((previous) => [...previous, ...response.data]);
    } catch (e) {
      console.log("Error: ", e);
    }
  };

  useEffect(() => {
    fetchComments();
  }, []);

  const showMore = () => {
    alert("Loading more comments...");
  };

  return (
    <>
      <Grid
        container
        direction="row"
        justifyContent="space-between"
        sx={{ width: "100%" }}
      >
        <Typography fontWeight={500} marginLeft={2}>
          <strong>{comments.length}</strong> Comment{comments.length > 1 && "s"}
        </Typography>
        {comments.length > 2 && (
          <Button onClick={() => showMore()}>Show more comment</Button>
        )}
      </Grid>
      <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="flex-start"
        spacing={2}
      >
        {comments.map((comment, index) => {
          return <Comment key={comment.id} comment={comment} />;
        })}
      </Grid>
      <CommentField
        postId={postId}
        afterSent={() => {
          fetchComments();
        }}
      />
    </>
  );
};

export default CommentSection;
