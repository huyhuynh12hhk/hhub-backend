package response

import "net/http"

// TODO: import code and message from json files, for that we can apply cross platform and localization

const (
	Success        = 20000
	CreatedSuccess = 20001
	Accepted       = 20002
	ParamInvalid   = 40003
	CommonError    = 40000
	Unauthorized   = 40101
	NotFound       = 40004
)

var messageContent = map[int]string{
	Success:        "Success",
	CreatedSuccess: "Created Success",
	Accepted:       "Request accepted",
	
	ParamInvalid:   "Invalid parameter",
	Unauthorized:   "You must login to see this resource",
	NotFound:       "This resource not exist",
	CommonError:    "Something when wrong try again later!",
}

var codeToStatus = map[int]int{
	Success:        http.StatusOK,
	CreatedSuccess: http.StatusCreated,
	Accepted:       http.StatusAccepted,
	ParamInvalid:   http.StatusBadRequest,
	Unauthorized:   http.StatusUnauthorized,
	NotFound:       http.StatusNotFound,
	CommonError:    http.StatusBadRequest,
}
