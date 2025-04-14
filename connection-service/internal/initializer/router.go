package initializer

import (
	"hhub/connection-service/global"
	"hhub/connection-service/internal/routers"

	"github.com/gin-gonic/gin"
)

func UseRouting() *gin.Engine{
	// Add router initialization logic here
	r := gin.Default()


	// Middleware setup
	// r.Use() 

	// Controller endpoints
	
	prefix:= global.Config.Server.Prefix
	if prefix==""{
		prefix = "/api/v1"
	}

	friendRouter := routers.AppRouter.Friend
	followRouter := routers.AppRouter.Follow

	mainRoute:= r.Group(prefix)
	// {
	// 	mainRoute.GET("health")
	// }
	{
		followRouter.InitFollowRouter(mainRoute, followController)
		friendRouter.InitFriendRouter(mainRoute, friendController)
	}

	return r
}