import { Box, Avatar, Typography, Card } from "@mui/material";
import React from "react";
import Grid from "@mui/material/Grid2";
import CommentSection from "./CommentSection";
import ReactionSection from "./ReactionSection";
import CommentField from "./CommentSection/CommentField";
import parse from "html-react-parser";

const Post = (props, ref) => {
  const { id, coverImage, authorName, createdAt, content, reactions } =
    props.post;
  return (
    <Card
      id={id}
      ref={ref}
      sx={{
        width: "100%",
        minWidth: "400px",
        maxWidth: "800px",
        boxShadow: 3,
        borderRadius: 3,
        padding: 4,
      }}
    >
      <Grid
        container
        spacing={1}
        direction="column"
        alignItems="start"
        justifyContent="normal"
      >
        <Grid
          // rowSpacing={1}
          container
          alignItems="center"
          justifyContent="flex-start"
        >
          <Grid>
            <Avatar
              src={coverImage}
              sx={{ width: "50px", height: "50px", marginRight: "5px" }}
            />
          </Grid>
          <Grid>
            <Typography
              sx={{
                fontSize: 14,
                fontWeight: 600,
              }}
            >
              {authorName}
            </Typography>
            <Typography
              sx={{
                fontSize: 14,
                fontWeight: 400,
              }}
            >
              {new Date(createdAt).toLocaleString()}
            </Typography>
          </Grid>
        </Grid>
        <Box
          minHeight="50px"
          sx={{
            fontSize: 14,
            paddingInline: 2,
            lineHeight: 1.2,
          }}
        >
          <div>{parse(content)}</div>
        </Box>
        <ReactionSection postId={id} hasLiked={false} items={reactions} />
        <CommentSection postId={id} />
      </Grid>
    </Card>
  );
};

export default Post;
