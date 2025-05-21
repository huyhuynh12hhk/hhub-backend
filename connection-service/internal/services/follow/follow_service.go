package services_follow

import "hhub/connection-service/internal/dtos"

type IFollowService interface {
	CreateFollow(request *dtos.FollowRequest) (*dtos.FollowResponse, int, error)
	UpdateFollowStatus(subscriberId string, request *dtos.UpdateFollowStatusRequest) (int, error)
	RemoveFollow(subscriberId, targetId string) (int, error)
	GetFollowingUsers(ownerId string) ([]dtos.FollowResponse, int, error)
	GetFollowers(ownerId string) ([]dtos.FollowResponse, int, error)
}
