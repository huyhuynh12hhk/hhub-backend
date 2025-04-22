package services_follow

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/pkg/response"
)

type _MockFollowService struct{}

// CreateFollow implements IFollowService.
func (mfs *_MockFollowService) CreateFollow(request *dtos.FollowRequest) (data *dtos.FollowResponse, code int, err error) {
	return &dtos.FollowResponse{}, response.CreatedSuccess, nil
}

// GetFollowers implements IFollowService.
func (mfs *_MockFollowService) GetFollowers(ownerId string) (data []dtos.FollowResponse, code int, err error) {
	return []dtos.FollowResponse{}, response.Success, nil
}

// GetFollowingUsers implements IFollowService.
func (mfs *_MockFollowService) GetFollowingUsers(ownerId string) (data []dtos.FollowResponse, code int, err error) {
	return []dtos.FollowResponse{}, response.Success, nil
}

// RemoveFollow implements IFollowService.
func (mfs *_MockFollowService) RemoveFollow(subscriberId string, targetId string) (code int, err error) {
	return response.Accepted, nil
}

// UpdateFollowStatus implements IFollowService.
func (mfs *_MockFollowService) UpdateFollowStatus(subscriberId string, request *dtos.UpdateFollowStatusRequest) (code int, err error) {
	return response.Accepted, nil
}

func NewMockFollowService() IFollowService {
	return &_MockFollowService{}
}
