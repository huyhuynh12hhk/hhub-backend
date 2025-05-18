//go:build wireinject

package di

import (
	"hhub/connection-service/internal/controllers"
	repositories "hhub/connection-service/internal/repositories/follow"
	services "hhub/connection-service/internal/services/follow"
	cache "hhub/connection-service/third_party/cache/redis"
	"hhub/connection-service/third_party/database/mysql"

	"github.com/google/wire"
)


func InitFollowController() (*controllers.FollowController, error) {
	wire.Build(
		mysql.GetInstance,
		repositories.NewFollowRepository,
		cache.NewRedis,
		services.NewFollowService,
		controllers.NewFollowController,
	)
	return new(controllers.FollowController), nil
}