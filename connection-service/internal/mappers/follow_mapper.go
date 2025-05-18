package mappers

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
)

func FollowsToResponses(friends []models.Follow) []dtos.FollowResponse {
	var results = []dtos.FollowResponse{}
	for _, follow := range friends {
		results = append(results, follow.ToResponse())
	}
	return results
}

func FollowRequestRequestToModel(request *dtos.FollowRequest) models.Follow {
	return models.Follow{
		SubscriberId: request.SubscriberId,
		ProducerId:   request.ProducerId,
		State:        models.PERSONALIZE,
	}
}
