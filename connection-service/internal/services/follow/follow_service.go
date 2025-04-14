package services_follow

import "hhub/connection-service/internal/dtos"

type IFollowService interface {
	CreateFollow(request *dtos.FollowRequest) error
	UpdateFollowStatus(request *dtos.UpdateFollowStatusRequest) error
	RemoveFollow(subscriberId, targetId string) error
	GetFollowingUsers(ownerId string) ([]string, error)
	GetFollowers(ownerId string) ([]string, error)
}
