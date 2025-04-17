package utils_auth

import (
	"strings"

	"github.com/gin-gonic/gin"
)

func ExtractToken(c *gin.Context) (string, bool) {
	authHeader := c.GetHeader("Authorization")
	if strings.HasPrefix(authHeader, "Bearer") {
		return strings.TrimPrefix(authHeader, "Bearer "), true
	}

	return "", false

}


