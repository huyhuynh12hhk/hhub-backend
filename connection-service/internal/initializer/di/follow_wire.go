//go:build wireinject

package di

import (
	"hhub/connection-service/internal/controllers"
	repositories "hhub/connection-service/internal/repositories/follow"
	services "hhub/connection-service/internal/services/follow"

	"github.com/google/wire"
)


func InitFollowController() (*controllers.FollowController, error) {
	wire.Build(
		repositories.NewFollowRepository,
		services.NewFollowService,
		controllers.NewFollowController,
	)
	return new(controllers.FollowController), nil
}