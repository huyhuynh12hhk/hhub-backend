package controllers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"hhub/connection-service/internal/dtos"
	services_friend "hhub/connection-service/internal/services/friend"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

var friendCtr FriendController = *NewFriendController(services_friend.NewMockFollowService())

var cfrRequest = dtos.AddFriendRequest{
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

func TestAddFriendShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.POST("/friends", friendCtr.AddFriend)

	jData, _ := json.Marshal(cfrRequest)

	req := httptest.NewRequest("POST", "/friends", bytes.NewBuffer(jData))
	req.Header.Set("Content-Type", "application/json")
	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusCreated, res.Code)

}

func TestAcceptFriendRequestShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.PATCH("/:receiverId/accept/:senderId", friendCtr.AcceptFriendRequest)

	req := httptest.NewRequest(
		"PATCH",
		fmt.Sprintf("/%s/accept/%s",
			cfrRequest.Receiver.Id,
			cfrRequest.Sender.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusAccepted, res.Code)

}

func TestDeclineFriendRequestShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.PATCH("/:receiverId/decline/:senderId", friendCtr.DeclineFriendRequest)

	req := httptest.NewRequest(
		"PATCH",
		fmt.Sprintf("/%s/decline/%s",
			cfrRequest.Receiver.Id,
			cfrRequest.Sender.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusAccepted, res.Code)

}

func TestRemoveFriendShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.DELETE("/:senderId/remove/:receiverId", friendCtr.RemoveFriend)

	req := httptest.NewRequest(
		"DELETE",
		fmt.Sprintf("/%s/remove/%s",
			cfrRequest.Receiver.Id,
			cfrRequest.Sender.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusAccepted, res.Code)

}

func TestGetFriendListShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.GET("/:ownerId", friendCtr.GetFriendList)

	req := httptest.NewRequest(
		"GET",
		fmt.Sprintf("/%s",
			cfrRequest.Sender.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusOK, res.Code)

}

func TestGetFriendRequestListShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.GET("/:ownerId", friendCtr.GetFriendRequestList)

	req := httptest.NewRequest(
		"GET",
		fmt.Sprintf("/%s",
			cfrRequest.Sender.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusOK, res.Code)

}
