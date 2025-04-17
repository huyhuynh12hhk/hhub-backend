package services_follow

import "hhub/connection-service/internal/dtos"

type IFollowService interface {
	CreateFollow(request *dtos.FollowRequest) (data *dtos.FollowResponse, code int, err error)
	UpdateFollowStatus(subscriberId string, request *dtos.UpdateFollowStatusRequest) (code int, err error)
	RemoveFollow(subscriberId, targetId string) (code int, err error)
	GetFollowingUsers(ownerId string) (data []dtos.FollowResponse, code int, err error)
	GetFollowers(ownerId string) (data []dtos.FollowResponse, code int, err error)
}
