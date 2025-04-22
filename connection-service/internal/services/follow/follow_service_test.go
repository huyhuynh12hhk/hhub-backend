package services_follow

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
	"hhub/connection-service/internal/pkg/response"
	repositories_follow "hhub/connection-service/internal/repositories/follow"
	"testing"

	"github.com/stretchr/testify/assert"
)

// func setupMock() repositories_follow.IFollowRepository {

// }

var service IFollowService = NewFollowService(repositories_follow.NewMockFollowRepository())

var cRequest = dtos.FollowRequest{
	Subscriber: dtos.UserVO{
		Id:       "uuid01",
		Name:     "User One",
		ImageUrl: "",
	},
	Producer: dtos.UserVO{
		Id:       "uuid02",
		Name:     "User Two",
		ImageUrl: "",
	},
}

var uRequest = dtos.UpdateFollowStatusRequest{
	Status: string(models.ALL),
	Producer: dtos.UserVO{
		Id:       "uuid02",
		Name:     "User Two",
		ImageUrl: "",
	},
}

func TestCreateFollowShouldSuccess(t *testing.T) {
	rs, code, _ := service.CreateFollow(&cRequest)

	assert.Equal(t, response.CreatedSuccess, code)
	assert.Equal(t, cRequest.Subscriber.Id, rs.Subscriber.Id)
}

func TestRemoveFollowShouldSuccess(t *testing.T) {
	code, _ := service.RemoveFollow(cRequest.Subscriber.Id, cRequest.Producer.Id)

	assert.Equal(t, response.Accepted, code)
}

func TestUpdateFollowStatusShouldSuccess(t *testing.T) {
	code, _ := service.UpdateFollowStatus(cRequest.Subscriber.Id, &uRequest)

	assert.Equal(t, response.Accepted, code)
}

func TestGetFollowersShouldSuccess(t *testing.T) {
	rs, code, _ := service.GetFollowers("uuuid03")

	assert.Equal(t, response.Success, code)
	assert.Equal(t, []dtos.FollowResponse{}, rs)
}

func TestGetFollowingsShouldSuccess(t *testing.T) {
	rs, code, _ := service.GetFollowingUsers("uuuid03")

	assert.Equal(t, response.Success, code)
	assert.Equal(t, []dtos.FollowResponse{}, rs)
}