package services_friend

import (
	"hhub/connection-service/internal/dtos"
	repositories "hhub/connection-service/internal/repositories/friend"
)

type _FriendService struct {
	friendRepository repositories.IFriendRepository
}

// AcceptFriendRequest implements IFriendService.
func (s *_FriendService) AcceptFriendRequest(senderId string, receiverId string) error {
	panic("unimplemented")
}

// CreateFriendRequest implements IFriendService.
func (s *_FriendService) CreateFriendRequest(request *dtos.AddFriendRequest) error {
	panic("unimplemented")
}

// DeclineFriendRequest implements IFriendService.
func (s *_FriendService) DeclineFriendRequest(senderId string, receiverId string) error {
	panic("unimplemented")
}

// GetFriendList implements IFriendService.
func (s *_FriendService) GetFriendList(ownerId string) ([]string, error) {
	panic("unimplemented")
}

// GetFriendRequestList implements IFriendService.
func (s *_FriendService) GetFriendRequestList(ownerId string) ([]string, error) {
	panic("unimplemented")
}

// RemoveFriend implements IFriendService.
func (s *_FriendService) RemoveFriend(senderId string, receiverId string) error {
	panic("unimplemented")
}

func NewFriendService(
	friendRepository repositories.IFriendRepository,
) IFriendService {
	return &_FriendService{
		friendRepository: friendRepository,
	}
}
