package services_friend

import "hhub/connection-service/internal/dtos"

type IFriendService interface {
	CreateFriendRequest(request *dtos.AddFriendRequest) error
	AcceptFriendRequest(senderId, receiverId string) error
	DeclineFriendRequest(senderId, receiverId string) error
	RemoveFriend(senderId, receiverId string) error
	GetFriendList(ownerId string) ([]string, error)
	GetFriendRequestList(ownerId string) ([]string, error)
}
