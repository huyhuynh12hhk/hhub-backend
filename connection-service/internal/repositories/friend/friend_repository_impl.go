package repositories_friend

import (
	"hhub/connection-service/internal/models"
)


type _FriendRepository struct{}

// CreateFriendRequest implements IFriendRepository.
func (f *_FriendRepository) CreateFriendRequest(friendRequest models.FriendRequest) (models.FriendRequest, error) {
	panic("unimplemented")
}

// DeleteFriendRequest implements IFriendRepository.
func (f *_FriendRepository) DeleteFriendRequest(requestId string) (models.FriendRequest, error) {
	panic("unimplemented")
}

// GetFriendRequestByReceiverId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestByReceiverId(receiverId string) ([]models.FriendRequest, error) {
	panic("unimplemented")
}

// GetFriendRequestBySenderId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestBySenderId(senderId string) ([]models.FriendRequest, error) {
	panic("unimplemented")
}

// GetFriendRequestBySenderIdAndReceiverId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestBySenderIdAndReceiverId(senderId string, receiverId string) (models.FriendRequest, error) {
	panic("unimplemented")
}

// UpdateStatusFriendRequest implements IFriendRepository.
func (f *_FriendRepository) UpdateStatusFriendRequest(requestId string, status string) (models.FriendRequest, error) {
	panic("unimplemented")
}

func NewFriendRepository() IFriendRepository {
	return &_FriendRepository{}
}
