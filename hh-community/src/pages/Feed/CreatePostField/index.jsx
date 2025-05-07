import Grid from "@mui/material/Grid2";
import { Delta } from "quill";
import { useRef, useState } from "react";
import Editor from "../../../components/Editor";
import { Button } from "@mui/material";
import { createPost } from "../../../services/postService";
import { useSelector } from "react-redux";
import { getSelf } from "../../../selectors/state";
import { notifyErrorMessage } from "../../../utils/toastHelper";

const CreatePostField = ({ afterCreated }) => {
  const quillRef = useRef();
  const [content, setContent] = useState("");
  const [range, setRange] = useState();
  const [readOnly, setReadOnly] = useState(false);
  const self = useSelector(getSelf);

  const handleSubmit = async () => {
    if (content === "") {
      notifyErrorMessage("Content cannot be blank")
      return
    };
    console.log("Send content: ", content);

    try {
      const response = await createPost(content, self);
      // console.log(response);
    } catch (e) {
      console.log("Post error: ", e);
    }
    setContent("");
    quillRef.current.setContents("");
    await setTimeout(function () {
       afterCreated();
    }, 300);
  };

  return (
    <Grid
      sx={{
        width: "100%",
      }}
      flex
      direction="column"
      justifyContent="center"
      alignItems="center"
      paddingBottom="10px"
    >
      <Editor
        ref={quillRef}
        readOnly={readOnly}
        placeholder={`Hello, ${self.username} share something with us here`}
        onSelectionChange={setRange}
        onTextChange={(e) => {
          const value = quillRef.current.root.innerHTML;
          // https://stackoverflow.com/questions/42541353/how-do-i-retrieve-the-contents-of-a-quill-text-editor
          // console.log("Content:", value);
          setContent(value);
        }}
      />
      <Grid
        width="100%"
        display="flex"
        direction="row-reverse"
        marginTop="20px"
        alignItems="center"
        justifyContent="flex-end"
        paddingRight="20px"
      >
        <Button onClick={() => handleSubmit()} variant="contained">
          Post
        </Button>
      </Grid>
    </Grid>
  );
};

export default CreatePostField;
