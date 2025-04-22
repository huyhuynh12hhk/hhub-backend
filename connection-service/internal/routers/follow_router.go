package routers

import (
	"hhub/connection-service/internal/controllers"

	"github.com/gin-gonic/gin"
)

type FollowRouter struct{}

func (fr *FollowRouter) InitFollowRouter(
	router *gin.RouterGroup,
	controller *controllers.FollowController,
	// TODO: Implement a dependency injection container will affect performance?
	authentication gin.HandlerFunc,
	// authorization gin.HandlerFunc,
) {
	if controller == nil {
		panic("Follow controller has not been initialized.")
	}
	// if authentication==nil{
	// 	panic("Follow controller has not been initialized.")
	// }

	// Public end point

	// Private end point
	followPrivateRoute := router.Group("/follows")
	if authentication != nil {
		followPrivateRoute.Use(authentication)
	}
	{
		followPrivateRoute.GET("/:ownerId/followers", controller.GetFollower)
		followPrivateRoute.GET("/:ownerId/followings", controller.GetFollowings)
		followPrivateRoute.POST("", controller.CreateFollow)
		followPrivateRoute.PATCH("/:subscriberId", controller.UpdateFollowStatus)
		followPrivateRoute.DELETE("/:subscriberId/remove/:producerId", controller.RemoveFollow)
	}

}
