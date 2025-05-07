import { Button, Divider, Typography } from "@mui/material";
import Grid from "@mui/material/Grid2";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import FavoriteIcon from "@mui/icons-material/Favorite";
import ShareIcon from "@mui/icons-material/Share";
import { use, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { getSelf } from "../../../selectors/state";
import { reactPost } from "../../../services/postService";
import useIsAuthenticated from "../../../utils/hooks/useIsAuthenticated";
import { notifyAuthorizeRequire, notifyNotImplementFeatureMessage } from "../../../utils/toastHelper";
import { useAuthorizeRequire } from "../../../context/authorizeRequireContext";

const ReactionSection = ({ postId, hasLiked, items, afterLiked }) => {
  const [reactions, setReactions] = useState([]);
  const self = useSelector(getSelf);
  const authenticated = useIsAuthenticated();
  const loginRequire = useAuthorizeRequire();

  useEffect(() => {
    setReactions(items);
  }, [items]);

  const toggleReaction = async () => {
    if (!authenticated) {
      loginRequire?.require(true);
      return;
    }
    const user = {
      id: self.id,
      name: self.username,
    };
    try {
      await reactPost(postId, self);
      if (!reactions.find((r) => r.id === user.id)) {
        setReactions((reactions) => [...reactions, ...[user]]);
      } else {
        setReactions((reactions) => reactions.filter((r) => r.id !== user.id));
      }
    } catch (e) {
      console.log("Error: ", e);
    }
    afterLiked?.();
  };

  return (
    <Grid
      container
      justifyContent="center"
      width={"100%"}
      border={"1px solid"}
      borderRadius={3}
      rowSpacing={1}
      columnSpacing={{ xs: 1, sm: 2, md: 3 }}
    >
      <Grid size={5.5} justifyContent="center">
        <Button
          fullWidth
          sx={{
            borderRadius: 2,
          }}
          onClick={() => toggleReaction()}
        >
          {reactions.find((r) => r.id === self.id) ? (
            <FavoriteIcon color="error" />
          ) : (
            <FavoriteBorderIcon color="error" />
          )}
          <Typography
            sx={{
              fontWeight: 600,
            }}
          >
            {reactions.length ?? 0}
          </Typography>
        </Button>
      </Grid>
      <Divider orientation="vertical" variant="middle" flexItem />
      <Grid size={5.5}>
        <Button fullWidth onClick={()=>notifyNotImplementFeatureMessage()}>
          <ShareIcon
            sx={{
              color: "#f9a825",
            }}
          />
        </Button>
      </Grid>
    </Grid>
  );
};

export default ReactionSection;
