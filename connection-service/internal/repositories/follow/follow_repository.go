package repositories_follow

import (
	"hhub/connection-service/internal/models"
)

type IFollowRepository interface {
	GetFollowsBySubscriberIdAndProducerId(subscriberId, producerId string) *models.Follow
	GetFollowsByProducerId(producerId string) []models.Follow
	GetFollowsBySubscriberId(subscriberId string) []models.Follow
	CreateFollow(model *models.Follow) *models.Follow
	UpdateFollow(model *models.Follow) *models.Follow
	DeleteFollow(requestId string) bool
}
