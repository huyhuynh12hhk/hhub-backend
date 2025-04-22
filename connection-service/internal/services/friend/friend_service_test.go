package services_friend

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/pkg/response"
	repositories_friend "hhub/connection-service/internal/repositories/friend"
	services_follow "hhub/connection-service/internal/services/follow"
	"testing"

	"github.com/stretchr/testify/assert"
)

var service IFriendService = NewFriendService(
	repositories_friend.NewMockFriendRepository(),
	services_follow.NewMockFollowService(),
)

var cRequest = dtos.AddFriendRequest{
	Sender: dtos.UserVO{
		Id:       "uuid01",
		Name:     "User One",
		ImageUrl: "",
	},
	Receiver: dtos.UserVO{
		Id:       "uuid02",
		Name:     "User Two",
		ImageUrl: "",
	},
}

func TestCreateFriendRequestShouldSuccess(t *testing.T) {
	rs, code, _ := service.CreateFriendRequest(&cRequest)

	assert.Equal(t, response.CreatedSuccess, code)
	assert.Equal(t, cRequest.Sender.Id, rs.Sender.Id)
}

func TestAcceptFriendRequestShouldSuccess(t *testing.T) {
	code, _ := service.AcceptFriendRequest(cRequest.Sender.Id, cRequest.Receiver.Id)

	assert.Equal(t, response.Accepted, code)
}

func TestDeclineFriendRequestShouldSuccess(t *testing.T) {
	code, _ := service.DeclineFriendRequest(cRequest.Sender.Id, cRequest.Receiver.Id)

	assert.Equal(t, response.Accepted, code)
}

func TestRemoveFriendShouldSuccess(t *testing.T) {
	code, _ := service.RemoveFriend(cRequest.Sender.Id, cRequest.Receiver.Id)

	assert.Equal(t, response.Accepted, code)
}

func TestGetFriendListShouldSuccess(t *testing.T) {
	rs, code, _ := service.GetFriendList("uuuid03")

	assert.Equal(t, response.Success, code)
	assert.Equal(t, []dtos.FriendRequestResponse{}, rs)
}

func TestGetFriendRequestListShouldSuccess(t *testing.T) {
	rs, code, _ := service.GetFriendRequestList("uuuid03")

	assert.Equal(t, response.Success, code)
	assert.Equal(t, []dtos.FriendRequestResponse{}, rs)
}
