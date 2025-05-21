package services_follow

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
	"hhub/connection-service/internal/pkg/response"
	repositories_follow "hhub/connection-service/internal/repositories/follow"
	cache "hhub/connection-service/third_party/cache/redis"
	"testing"

	"github.com/stretchr/testify/assert"
)



var service IFollowService = NewFollowService(repositories_follow.NewMockFollowRepository(), cache.NewRedisMock())

var cRequest = dtos.FollowRequest{
	SubscriberId: "uuid01",
	ProducerId:   "uuid02",
}

var uRequest = dtos.UpdateFollowStatusRequest{
	Status: string(models.ALL),
	Producer: dtos.UserVO{
		Id:       "uuid02",
		Name:     "User Two",
		ImageUrl: "",
	},
}

func setup(t *testing.T) {

	
}

func TestCreateFollowShouldSuccess(t *testing.T) {
	rs, code, _ := service.CreateFollow(&cRequest)

	assert.Equal(t, response.CreatedSuccess, code)
	assert.Equal(t, cRequest.SubscriberId, rs.SubscriberId)
}

func TestRemoveFollowShouldSuccess(t *testing.T) {
	code, _ := service.RemoveFollow(cRequest.SubscriberId, cRequest.ProducerId)

	assert.Equal(t, response.Accepted, code)
}

func TestUpdateFollowStatusShouldSuccess(t *testing.T) {
	code, _ := service.UpdateFollowStatus(cRequest.SubscriberId, &uRequest)

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
