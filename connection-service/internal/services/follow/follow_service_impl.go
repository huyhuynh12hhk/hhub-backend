package services_follow

import (
	"fmt"
	prefix "hhub/connection-service/global/cache"
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/mappers"
	"hhub/connection-service/internal/models"
	"hhub/connection-service/internal/pkg/response"
	repositories "hhub/connection-service/internal/repositories/follow"
	cache "hhub/connection-service/third_party/cache/redis"
	"time"
)

type _FollowService struct {
	followRepository repositories.IFollowRepository
	redisCache       cache.RedisClient
}

// CreateFollow implements IFollowService.
func (s *_FollowService) CreateFollow(request *dtos.FollowRequest) (*dtos.FollowResponse, int, error) {
	var record = mappers.FollowRequestRequestToModel(request)
	result := s.followRepository.CreateFollow(&record)

	// fmt.Printf("Create follow service: %+v\n", result)

	item := result.ToResponse()

	return &item, response.CreatedSuccess, nil
}

// RemoveFollow implements IFollowService.
func (s *_FollowService) RemoveFollow(subscriberId string, producerId string) (int, error) {
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
func (s *_FollowService) UpdateFollowStatus(subscriberId string, request *dtos.UpdateFollowStatusRequest) (int, error) {
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
func (s *_FollowService) GetFollowers(ownerId string) ([]dtos.FollowResponse, int, error) {
	// fmt.Println("Owner info: ", ownerId)

	var results []models.Follow
	callback := func() []models.Follow {
		return s.followRepository.GetFollowsByProducerId(ownerId)
	}

	results, err := cache.GetOrSetValues(
		&s.redisCache,
		prefix.FollowCachePrefix+ownerId,
		callback,
		10*time.Minute,
	)
	if err != nil {
		return nil, response.ServerError, err
	}

	// fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FollowsToResponses(results)

	return items, response.Success, nil
}

// GetFollowingUsers implements IFollowService.
func (s *_FollowService) GetFollowingUsers(ownerId string) ([]dtos.FollowResponse, int, error) {
	// fmt.Println("Owner info: ", ownerId)
	var results []models.Follow
	callback := func() []models.Follow {
		return s.followRepository.GetFollowsBySubscriberId(ownerId)
	}

	results, err := cache.GetOrSetValues(
		&s.redisCache,
		prefix.FollowCachePrefix+ownerId,
		callback,
		10*time.Minute,
	)
	if err != nil {
		return nil, response.ServerError, err
	}

	// fmt.Printf("Service:: Repo Result %+v\n", results)

	items := mappers.FollowsToResponses(results)

	return items, response.Success, nil
}

func NewFollowService(
	followRepository repositories.IFollowRepository,
	cache *cache.RedisClient,
) IFollowService {

	return &_FollowService{
		followRepository: followRepository,
		redisCache:       *cache,
	}
}
