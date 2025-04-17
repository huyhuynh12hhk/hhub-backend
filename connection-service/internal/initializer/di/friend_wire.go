//go:build wireinject

package di

import (
	"hhub/connection-service/internal/controllers"
	repositories_follow "hhub/connection-service/internal/repositories/follow"
	repositories "hhub/connection-service/internal/repositories/friend"
	services_follow "hhub/connection-service/internal/services/follow"
	services "hhub/connection-service/internal/services/friend"

	"github.com/google/wire"
)


func InitFriendController() (*controllers.FriendController, error) {
	wire.Build(
		repositories_follow.NewFollowRepository,
		repositories.NewFriendRepository,
		services_follow.NewFollowService,
		services.NewFriendService,
		controllers.NewFriendController,
	)
	return new(controllers.FriendController), nil
}