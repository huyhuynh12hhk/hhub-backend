import toast from "react-hot-toast";

export const notifyAuthorizeRequire = () =>
  toast.error(
    "You need login to do that!", 
    {
      position: "top-right",
    }
);

export const notifyErrorMessage = (message) => {
  toast.error(`${message}`, {
    position: "top-right",
  });
};


export const notifySuccessMessage = (message) => {
  toast.success(`${message}`, {
    position: "top-right",
  });
};


export const notifyNotImplementFeatureMessage = (message) => {
  toast.error(`Feature under development!`, {
    position: "top-right",
  });
};