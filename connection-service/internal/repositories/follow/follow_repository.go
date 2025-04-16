package repositories_follow

import (
	"hhub/connection-service/internal/models"
)

type IFollowRepository interface {
	GetFollowsBySenderIdAndReceiverId(senderId, receiverId string) (models.Follow, error)
	GetFollowsByReceiverId(receiverId string) ([]models.Follow, error)
	GetFollowsBySenderId(senderId string) ([]models.Follow, error)
	CreateFollow(friendRequest models.Follow) (models.Follow, error)
	UpdateStatusFollow(requestId, status string) (models.Follow, error)
	DeleteFollow(requestId string) (models.Follow, error)
}
