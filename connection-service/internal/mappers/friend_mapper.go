package mappers

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
)

// import "hhub/connection-service/internal/models"

func FriendRequestsToResponses(friends []models.FriendRequest) []dtos.FriendRequestResponse {
	var results []dtos.FriendRequestResponse
	for _, friend := range friends {
		results = append(results, friend.ToResponse())
	}
	return results
}

func AddFriendRequestToModel(request *dtos.AddFriendRequest) models.FriendRequest {
	return models.FriendRequest{
		SenderId: request.Sender.Id,
		Sender: UserVOToModel(&request.Sender),
		ReceiverId: request.Receiver.Id,
		Receiver: UserVOToModel(&request.Receiver),
		State: models.WAITING,
	}
}
