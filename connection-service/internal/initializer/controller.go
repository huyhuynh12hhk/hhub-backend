package initializer

import (
	"hhub/connection-service/internal/controllers"
	"hhub/connection-service/internal/initializer/di"
)

var(
	friendController *controllers.FriendController
	followController *controllers.FollowController
)

func AddControllers() {
	// Dependency injection and other setup for controller layer here
	friendController, _ = di.InitFriendController()
	followController, _ = di.InitFollowController()

}
