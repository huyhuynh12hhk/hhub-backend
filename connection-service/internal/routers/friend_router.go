package routers

import (
	"hhub/connection-service/internal/controllers"

	"github.com/gin-gonic/gin"
)

type FriendRouter struct{}

func (fr *FriendRouter) InitFriendRouter(
	router *gin.RouterGroup,
	controller *controllers.FriendController,
){
	if controller==nil{
		panic("Follow controller has not been initialized.")
	}

	// Public end point
	
	
	// Private end point
	friendPrivateRoute := router.Group("/friends")
	{
		friendPrivateRoute.GET("/:ownerId", controller.GetFriendList)
		friendPrivateRoute.GET("/:ownerId/invitations", controller.DeclineFriendRequest)
		friendPrivateRoute.POST("", controller.AddFriend)
		friendPrivateRoute.PATCH("/accept", controller.AcceptFriendRequest)
		friendPrivateRoute.PATCH("/decline", controller.AcceptFriendRequest)
		friendPrivateRoute.DELETE("/:senderId/:receiverId", controller.RemoveFriend)
	}

}