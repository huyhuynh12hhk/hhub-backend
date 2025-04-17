package mappers

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
)

func FollowsToResponses(friends []models.Follow) []dtos.FollowResponse {
	var results []dtos.FollowResponse
	for _, follow := range friends {
		results = append(results, follow.ToResponse())
	}
	return results
}

func FollowRequestRequestToModel(request *dtos.FollowRequest) models.Follow {
	return models.Follow{
		SubscriberId: request.Subscriber.Id,
		Subscriber:   UserVOToModel(&request.Subscriber),
		ProducerId:   request.Producer.Id,
		Producer:     UserVOToModel(&request.Producer),
		State:        models.PERSONALIZE,
	}
}
