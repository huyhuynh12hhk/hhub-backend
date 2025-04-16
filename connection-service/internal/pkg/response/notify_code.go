package response

// TODO: import code and message from json files, for that we can apply cross platform and localization

const (
	Success        = 20000
	CreatedSuccess = 20001
	Accepted         = 20002
	ParamInvalid   = 40003
	CommonError    = 40000
	Unauthorized   = 40101
	NotFound       = 40004
)

var messageContent = map[int]string{
	CreatedSuccess: "Success",
	ParamInvalid:   "Invalid parameter",
	Unauthorized:   "Please login",
	NotFound:       "This resource not exist",
	CommonError:    "Something when wrong try again later!",
	Accepted:         "Request accepted",
}
