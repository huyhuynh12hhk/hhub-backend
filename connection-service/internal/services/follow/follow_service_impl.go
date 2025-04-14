package services_follow

import (
	"hhub/connection-service/internal/dtos"
	repositories "hhub/connection-service/internal/repositories/follow"
)

type _FollowService struct {
	followRepository repositories.IFollowRepository
}

// CreateFollow implements IFollowService.
func (s *_FollowService) CreateFollow(request *dtos.FollowRequest) error {
	panic("unimplemented")
}

// GetFollowers implements IFollowService.
func (s *_FollowService) GetFollowers(ownerId string) ([]string, error) {
	panic("unimplemented")
}

// GetFollowingUsers implements IFollowService.
func (s *_FollowService) GetFollowingUsers(ownerId string) ([]string, error) {
	panic("unimplemented")
}

// RemoveFollow implements IFollowService.
func (s *_FollowService) RemoveFollow(subscriberId string, targetId string) error {
	panic("unimplemented")
}

// UpdateFollowStatus implements IFollowService.
func (s *_FollowService) UpdateFollowStatus(request *dtos.UpdateFollowStatusRequest) error {
	panic("unimplemented")
}

func NewFollowService(
	followRepository repositories.IFollowRepository,
) IFollowService {
	return &_FollowService{
		followRepository: followRepository,
	}
}
