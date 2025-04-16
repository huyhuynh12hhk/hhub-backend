package repositories_follow

import (
	"hhub/connection-service/internal/models"
)

type _FollowRepository struct{}

// CreateFollow implements IFollowRepository.
func (f *_FollowRepository) CreateFollow(friendRequest models.Follow) (models.Follow, error) {
	panic("unimplemented")
}

// DeleteFollow implements IFollowRepository.
func (f *_FollowRepository) DeleteFollow(requestId string) (models.Follow, error) {
	panic("unimplemented")
}

// GetFollowsByReceiverId implements IFollowRepository.
func (f *_FollowRepository) GetFollowsByReceiverId(receiverId string) ([]models.Follow, error) {
	panic("unimplemented")
}

// GetFollowsBySenderId implements IFollowRepository.
func (f *_FollowRepository) GetFollowsBySenderId(senderId string) ([]models.Follow, error) {
	panic("unimplemented")
}

// GetFollowsBySenderIdAndReceiverId implements IFollowRepository.
func (f *_FollowRepository) GetFollowsBySenderIdAndReceiverId(senderId string, receiverId string) (models.Follow, error) {
	panic("unimplemented")
}

// UpdateStatusFollow implements IFollowRepository.
func (f *_FollowRepository) UpdateStatusFollow(requestId string, status string) (models.Follow, error) {
	panic("unimplemented")
}

func NewFollowRepository() IFollowRepository {
	return &_FollowRepository{}
}
