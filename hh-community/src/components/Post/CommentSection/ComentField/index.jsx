import { Button, IconButton, Stack, TextField } from "@mui/material";
import Grid from "@mui/material/Grid2";
import SendIcon from "@mui/icons-material/Send";
import AddAPhotoIcon from "@mui/icons-material/AddAPhoto";
import { grey } from "@mui/material/colors";
import { useState } from "react";
import { createComment } from "../../../../services/commentService";
import { useSelector } from "react-redux";
import { getSelf } from "../../../../selectors/state";
import useIsAuthenticated from "../../../../utils/hooks/useIsAuthenticated";
import {
  notifyAuthorizeRequire,
  notifyErrorMessage,
  notifyNotImplementFeatureMessage,
} from "../../../../utils/toastHelper";
import { useAuthorizeRequire } from "../../../../context/authorizeRequireContext";

const CommentField = ({ postId, afterSent }) => {
  const [loading, setLoading] = useState(false);
  const [content, setContent] = useState("");
  const self = useSelector(getSelf);
  const authenticated = useIsAuthenticated();
  const loginRequire = useAuthorizeRequire();

  const sendComment = async () => {
    if (!authenticated) {
      loginRequire?.require(true);
      return;
    }
    if (content === "") {
      notifyErrorMessage("Content cannot be blank");
      return;
    }
    setLoading(true);
    try {
      const response = await createComment(postId, content, self);
    } catch (e) {
      console.log("Error: ", e);
    }
    afterSent?.();
    setContent("");
    setLoading(false);
  };

  return (
    <Grid
      container
      justifyContent="center"
      alignItems="center"
      spacing={1}
      marginTop={2}
      width="100%"
      minWidth="300px"
      gap={1}
    >
      <Grid size={9}>
        <TextField
          multiline
          maxRows={4}
          fullWidth
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Post your comment here..."
        />
      </Grid>
      <Grid size={3} justifyContent="space-between" spacing={2}>
        <Stack direction="row" spacing={1} justifyContent="center">
          <IconButton
            onClick={() => notifyNotImplementFeatureMessage()}
            color="info"
            size="small"
            sx={{
              height: "40px",
              width: "40px",
              overflow: "hidden",
            }}
          >
            <AddAPhotoIcon sx={{ color: "GrayText", fontSize: 25 }} />
          </IconButton>
          <Button
            loading={loading}
            onClick={() => sendComment()}
            variant="outlined"
            color="info"
            size="small"
            sx={{
              height: "40px",
              width: "40px",
              overflow: "hidden",
              backgroundColor: "#42a5f5",
              borderRadius: 4,
              padding: 1,
            }}
          >
            <SendIcon sx={{ color: grey[200], fontSize: 20 }} />
          </Button>
        </Stack>
      </Grid>
    </Grid>
  );
};

export default CommentField;
