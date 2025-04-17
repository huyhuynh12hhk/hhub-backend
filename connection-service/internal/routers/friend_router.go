package routers

import (
	"hhub/connection-service/internal/controllers"

	"github.com/gin-gonic/gin"
)

type FriendRouter struct{}

func (fr *FriendRouter) InitFriendRouter(
	router *gin.RouterGroup,
	controller *controllers.FriendController,
	// TODO: Implement a dependency injection container will affect performance?
	authentication gin.HandlerFunc,
) {
	if controller == nil {
		panic("Follow controller has not been initialized.")
	}

	// Public end point

	// Private end point
	friendPrivateRoute := router.Group("/friends")
	if authentication != nil {
		friendPrivateRoute.Use(authentication)
	}
	{
		friendPrivateRoute.GET("/:ownerId", controller.GetFriendList)
		friendPrivateRoute.GET("/:ownerId/invitations", controller.GetFriendRequestList)
		friendPrivateRoute.POST("", controller.AddFriend)
		friendPrivateRoute.PATCH("/:receiverId/accept/:senderId", controller.AcceptFriendRequest)
		friendPrivateRoute.PATCH("/:receiverId/decline/:senderId", controller.DeclineFriendRequest)
		friendPrivateRoute.DELETE("/:senderId/remove/:receiverId", controller.RemoveFriend)
	}

}
