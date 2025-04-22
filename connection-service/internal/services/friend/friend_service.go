package services_friend

import (
	"hhub/connection-service/internal/dtos"
)

type IFriendService interface {
	CreateFriendRequest(request *dtos.AddFriendRequest) (data *dtos.FriendRequestResponse, code int, err error)
	AcceptFriendRequest(senderId, receiverId string) (code int, err error)
	DeclineFriendRequest(senderId, receiverId string) (code int, err error)
	RemoveFriend(senderId, receiverId string) (code int, err error)
	GetFriendList(ownerId string) (data []dtos.FriendRequestResponse, code int, err error)
	GetFriendRequestList(ownerId string) (data []dtos.FriendRequestResponse, code int, err error)
}
