package repositories_friend

import (
	"hhub/connection-service/internal/models"
)

type IFriendRepository interface {
	GetFriendRequestBySenderIdAndReceiverId(senderId, receiverId string) *models.FriendRequest
	GetFriendList(ownerId string) []models.FriendRequest
	GetFriendRequestByReceiverId(receiverId string) []models.FriendRequest
	GetFriendRequestBySenderId(senderId string) []models.FriendRequest
	CreateFriendRequest(model *models.FriendRequest) *models.FriendRequest
	UpdateFriendRequest(model *models.FriendRequest) *models.FriendRequest
	DeleteFriendRequest(requestId string) bool
}
