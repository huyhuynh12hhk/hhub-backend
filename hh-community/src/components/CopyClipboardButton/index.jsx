import ContentCopyIcon from "@mui/icons-material/ContentCopy";
import { Box, Stack } from "@mui/material";
import CopyToClipboard from "react-copy-to-clipboard";
import { notifySuccessMessage } from "../../utils/toastHelper";

const CopyClipboardButton = ({ text }) => {
  const handleCopy = () => {
    // displayToast("Copied to the clipboard", ToastType.success);
    notifySuccessMessage("Copied success!")
  };

  return (
    <Stack width={"100%"}>
      <CopyToClipboard text={text} onCopy={handleCopy}>
        <span>
          <ContentCopyIcon />
        </span>
      </CopyToClipboard>
    </Stack>
  );
};

export default CopyClipboardButton;
