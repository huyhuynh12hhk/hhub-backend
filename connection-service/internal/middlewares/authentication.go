package middlewares

import (
	"fmt"
	"hhub/connection-service/internal/pkg/response"
	utils_auth "hhub/connection-service/internal/pkg/utils/auth"
	auth "hhub/connection-service/third_party/oidc/app"

	"github.com/gin-gonic/gin"
)

func Authentication() gin.HandlerFunc {
	return func(c *gin.Context) {

		// if global.Config.Server.Debug {
		// }
		uri := c.Request.URL.Path
		fmt.Printf("Uri request call: %s\n", uri)
		
		// Check authentication
		jwt, success := utils_auth.ExtractToken(c)
		if !success {
			response.ErrorResponse(c, response.Unauthorized)
			c.Abort()
			return
		}
		// fmt.Printf("Token extracted: %s", jwt)

		token, err := auth.AppTokenVerifier().Verify(c, jwt)
		if err != nil {
			fmt.Printf("Token verified error: %+v\n", err)
			response.ErrorResponse(c, response.Unauthorized)
			c.Abort()
			return
		}
		// fmt.Printf("Token verified: %+v\n", token)

		var claims map[string]interface{}
		if err := token.Claims(&claims); err != nil {
			response.ErrorResponse(c, response.Unauthorized)
			c.Abort()
			return
		}

		fmt.Printf("Claim extract: %+v\n", claims)
		// ctx:= context.WithValue(c.Request.Context(), "jti", claims["jti"])
		// c.Request = c.Request.WithContext(ctx)

		c.Set("claims", claims)
		// TODO: Using with claims after integrate with other services
		// c.Set("userId", )
		c.Next()
	}
}
