package response

import "github.com/gin-gonic/gin"

type ResponseData struct {
	Code    int         `json:"code"`
	Message string      `json:"message"`
	Data    interface{} `json:"data"`
}

// success response
func SuccessResponse(c *gin.Context, code int, data interface{}) {
	c.JSON(code, ResponseData{
		Code:    code,
		Message: messageContent[code],
		Data:    data,
	})
}

// fail response
func ErrorResponse(c *gin.Context, code int, statusCode int) {
	c.JSON(statusCode, ResponseData{
		Code:    code,
		Message: messageContent[code],
		Data:    nil,
	})
}
