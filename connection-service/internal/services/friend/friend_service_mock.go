package services_friend

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/mappers"
	"hhub/connection-service/internal/pkg/response"
)

type _MockFriendService struct{}

// AcceptFriendRequest implements IFriendService.
func (mfs *_MockFriendService) AcceptFriendRequest(senderId string, receiverId string) (code int, err error) {
	return response.Accepted, nil
}

// CreateFriendRequest implements IFriendService.
func (mfs *_MockFriendService) CreateFriendRequest(request *dtos.AddFriendRequest) (data *dtos.FriendRequestResponse, code int, err error) {
	rs := mappers.AddFriendRequestToModel(request)
	resp := rs.ToResponse()
	return &resp, response.CreatedSuccess, nil
}

// DeclineFriendRequest implements IFriendService.
func (mfs *_MockFriendService) DeclineFriendRequest(senderId string, receiverId string) (code int, err error) {
	return response.Accepted, nil
}

// GetFriendList implements IFriendService.
func (mfs *_MockFriendService) GetFriendList(ownerId string) (data []dtos.FriendRequestResponse, code int, err error) {
	return []dtos.FriendRequestResponse{}, response.Success, nil
}

// GetFriendRequestList implements IFriendService.
func (mfs *_MockFriendService) GetFriendRequestList(ownerId string) (data []dtos.FriendRequestResponse, code int, err error) {
	return []dtos.FriendRequestResponse{}, response.Success, nil
}

// RemoveFriend implements IFriendService.
func (mfs *_MockFriendService) RemoveFriend(senderId string, receiverId string) (code int, err error) {
	return response.Accepted, nil
}

func NewMockFollowService() IFriendService {
	return &_MockFriendService{}
}
