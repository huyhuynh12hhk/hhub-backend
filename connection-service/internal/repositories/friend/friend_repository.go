package repositories_friend

import (
	"hhub/connection-service/internal/models"
)

type IFriendRepository interface {
	GetFriendRequestBySenderIdAndReceiverId(senderId, receiverId string) (models.FriendRequest, error)
	GetFriendRequestByReceiverId(receiverId string) ([]models.FriendRequest, error)
	GetFriendRequestBySenderId(senderId string) ([]models.FriendRequest, error)
	CreateFriendRequest(friendRequest models.FriendRequest) (models.FriendRequest, error)
	UpdateStatusFriendRequest(requestId, status string) (models.FriendRequest, error)
	DeleteFriendRequest(requestId string) (models.FriendRequest, error)
}
