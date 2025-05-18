package services_friend

import (
	"fmt"
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/mappers"
	"hhub/connection-service/internal/models"
	"hhub/connection-service/internal/pkg/response"
	repositories "hhub/connection-service/internal/repositories/friend"
	services_follow "hhub/connection-service/internal/services/follow"
)

type _FriendService struct {
	friendRepository repositories.IFriendRepository
	followService    services_follow.IFollowService
}

func NewFriendService(
	friendRepository repositories.IFriendRepository,
	followService services_follow.IFollowService,
) IFriendService {
	return &_FriendService{
		friendRepository: friendRepository,
		followService:    followService,
	}
}

// CreateFriendRequest implements IFriendService.
func (s *_FriendService) CreateFriendRequest(request *dtos.AddFriendRequest) (*dtos.FriendRequestResponse, int, error) {
	var record = mappers.AddFriendRequestToModel(request)

	result := s.friendRepository.CreateFriendRequest(&record)

	// fmt.Printf("Create friend request service: %+v\n", result)

	// After make friend request, auto follow this user
	follow := dtos.FollowRequest{
		SubscriberId: request.SenderId,
		ProducerId:   request.ReceiverId,
	}
	if _, codeF, errF := s.followService.CreateFollow(&follow); errF != nil {
		return nil, codeF, fmt.Errorf("error when create follow")
	}

	item := result.ToResponse()
	return &item, response.CreatedSuccess, nil
}

// AcceptFriendRequest implements IFriendService.
func (s *_FriendService) AcceptFriendRequest(senderId string, receiverId string) (int, error) {
	// Find the request
	record := s.friendRepository.GetFriendRequestBySenderIdAndReceiverId(senderId, receiverId)

	// If not exist
	if record == nil {
		return response.NotFound, fmt.Errorf("not found friend request")
	}

	// When exist change request state
	record.State = models.ACCEPTED

	s.friendRepository.UpdateFriendRequest(record)

	follow := dtos.FollowRequest{
		SubscriberId: receiverId,
		ProducerId:   senderId,
	}

	if _, codeF, errF := s.followService.CreateFollow(&follow); errF != nil {
		return codeF, fmt.Errorf("error when create follow")
	}

	return response.Accepted, nil
}

// DeclineFriendRequest implements IFriendService.
func (s *_FriendService) DeclineFriendRequest(senderId string, receiverId string) (int, error) {
	// Find the request
	record := s.friendRepository.GetFriendRequestBySenderIdAndReceiverId(senderId, receiverId)

	// If not exist
	if record == nil {
		return response.NotFound, fmt.Errorf("not found friend request")
	}

	// When exist change request state
	record.State = models.DECLINED

	s.friendRepository.UpdateFriendRequest(record)

	return response.Accepted, nil
}

// RemoveFriend implements IFriendService.
func (s *_FriendService) RemoveFriend(senderId string, receiverId string) (int, error) {
	// Find the request
	record := s.friendRepository.GetFriendRequestBySenderIdAndReceiverId(senderId, receiverId)

	// If not exist
	if record == nil {
		return response.NotFound, fmt.Errorf("not found friend request")
	}

	// When exist change request state
	// In current logic just disable not hard delete
	record.State = models.DISABLED

	s.friendRepository.UpdateFriendRequest(record)

	return response.Accepted, nil
}

// GetFriendList implements IFriendService.
func (s *_FriendService) GetFriendList(ownerId string) ([]dtos.FriendRequestResponse, int, error) {
	// fmt.Println("Owner info: ", ownerId)
	results := s.friendRepository.GetFriendList(ownerId)

	// fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FriendRequestsToResponses(results)
	if items == nil {
		items = []dtos.FriendRequestResponse{}
	}

	return items, response.Success, nil
}

// GetFriendRequestList implements IFriendService.
func (s *_FriendService) GetFriendRequestList(ownerId string) ([]dtos.FriendRequestResponse, int, error) {
	// fmt.Println("Owner info: ", ownerId)
	results := s.friendRepository.GetFriendRequestByReceiverId(ownerId)

	// fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FriendRequestsToResponses(results)
	if items == nil {
		items = []dtos.FriendRequestResponse{}
	}

	return items, response.Success, nil
}
