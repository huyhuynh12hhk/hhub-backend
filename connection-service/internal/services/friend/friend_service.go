package services_friend

import (
	"hhub/connection-service/internal/dtos"
)

type IFriendService interface {
	CreateFriendRequest(request *dtos.AddFriendRequest) (*dtos.FriendRequestResponse, int, error)
	AcceptFriendRequest(senderId, receiverId string) (int, error)
	DeclineFriendRequest(senderId, receiverId string) (int, error)
	RemoveFriend(senderId, receiverId string) (int, error)
	GetFriendList(ownerId string) ([]dtos.FriendRequestResponse, int, error)
	GetFriendRequestList(ownerId string) ([]dtos.FriendRequestResponse, int, error)
}
