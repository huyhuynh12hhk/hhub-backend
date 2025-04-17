package services_follow

import (
	"fmt"
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/mappers"
	"hhub/connection-service/internal/models"
	"hhub/connection-service/internal/pkg/response"
	repositories "hhub/connection-service/internal/repositories/follow"
)

type _FollowService struct {
	followRepository repositories.IFollowRepository
}

// CreateFollow implements IFollowService.
func (s *_FollowService) CreateFollow(request *dtos.FollowRequest) (data *dtos.FollowResponse, code int, err error) {
	var record = mappers.FollowRequestRequestToModel(request)
	result := s.followRepository.CreateFollow(&record)

	fmt.Printf("Create follow service: %+v\n", result)

	item := record.ToResponse()

	return &item, response.CreatedSuccess, nil
}

// RemoveFollow implements IFollowService.
func (s *_FollowService) RemoveFollow(subscriberId string, producerId string) (code int, err error) {
	// Find the request
	record := s.followRepository.GetFollowsBySubscriberIdAndProducerId(subscriberId, producerId)

	// If not exist
	if record == nil {
		return response.NotFound, fmt.Errorf("not found follow")
	}

	// When exist change request state
	// In current logic just disable not hard delete
	record.State = models.NONE

	s.followRepository.UpdateFollow(record)

	return response.Accepted, nil
}

// UpdateFollowStatus implements IFollowService.
func (s *_FollowService) UpdateFollowStatus(subscriberId string, request *dtos.UpdateFollowStatusRequest) (code int, err error) {
	// Find the request
	record := s.followRepository.GetFollowsBySubscriberIdAndProducerId(subscriberId, request.Producer.Id)

	// If not exist
	if record == nil {
		return response.NotFound, fmt.Errorf("not found follow")
	}

	// When exist change request state
	status, ok := models.ParseFollowStatus(request.Status)
	if !ok {
		return response.ParamInvalid, fmt.Errorf("status not allowed")
	}
	record.State = status
	s.followRepository.UpdateFollow(record)

	return response.Accepted, nil
}

// GetFollowers implements IFollowService.
func (s *_FollowService) GetFollowers(ownerId string) (data []dtos.FollowResponse, code int, err error) {
	fmt.Println("Owner info: ", ownerId)
	results := s.followRepository.GetFollowsByProducerId(ownerId)

	fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FollowsToResponses(results)
	if items == nil{
		items = []dtos.FollowResponse{}
	}

	return items, response.Success, nil
}

// GetFollowingUsers implements IFollowService.
func (s *_FollowService) GetFollowingUsers(ownerId string) (data []dtos.FollowResponse, code int, err error) {
	fmt.Println("Owner info: ", ownerId)
	results := s.followRepository.GetFollowsBySubscriberId(ownerId)

	fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FollowsToResponses(results)
	if items == nil{
		items = []dtos.FollowResponse{}
	}

	return items, response.Success, nil
}

func NewFollowService(
	followRepository repositories.IFollowRepository,
) IFollowService {
	return &_FollowService{
		followRepository: followRepository,
	}
}
