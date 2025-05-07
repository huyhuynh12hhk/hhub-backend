import { Avatar, Card, CardContent, Typography } from "@mui/material";
import Grid from "@mui/material/Grid2";
import DefaultAvatar from "../../assets/default-avatar.png";

const Comment = ({ comment }) => {
  const { authorName, createdAt, content } = comment;
  return (
    <Grid
      container
      minWidth="200px"
      direction="column"
      justifyContent="space-between"
      alignItems="flex-start"
      sx={{
        marginBottom: 1,
      }}
    >
      <Grid container direction="row" spacing={1} alignItems="center">
        <Avatar src={DefaultAvatar} sx={{ marginRight: 2 }} />
        <Grid container direction="column">
          <Typography
            sx={{
              fontSize: 13,
              fontWeight: 600,
            }}
          >
            {authorName}
          </Typography>
          <Typography
            sx={{
              fontSize: 12,
              fontWeight: 300,
            }}
          >
            {new Date(createdAt).toLocaleString()}
          </Typography>
        </Grid>
      </Grid>
      <Card
        sx={{
          width: "100%",
          height: "100%",
          minHeight: "50px",
          marginTop: "5px",
          borderRadius: 3,
          padding: 0,
        }}
      >
        <CardContent
          sx={{
            padding: "14px !important",
          }}
        >
          <Typography
            sx={{
              fontSize: 14,
            }}
          >
            {content}
          </Typography>
        </CardContent>
      </Card>
    </Grid>
  );
};

export default Comment;
