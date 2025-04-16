//go:build wireinject

package di

import (
	"hhub/connection-service/internal/controllers"
	repositories "hhub/connection-service/internal/repositories/friend"
	services "hhub/connection-service/internal/services/friend"

	"github.com/google/wire"
)


func InitFriendController() (*controllers.FriendController, error) {
	wire.Build(
		repositories.NewFriendRepository,
		services.NewFriendService,
		controllers.NewFriendController,
	)
	return new(controllers.FriendController), nil
}