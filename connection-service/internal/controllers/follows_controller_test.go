package controllers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/models"
	services_follow "hhub/connection-service/internal/services/follow"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

var followCtr FollowController = *NewFollowController(services_follow.NewMockFollowService())

var cflRequest = dtos.FollowRequest{
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

var uflRequest = dtos.UpdateFollowStatusRequest{
	Status: string(models.ALL),
	Producer: dtos.UserVO{
		Id:       "uuid02",
		Name:     "User Two",
		ImageUrl: "",
	},
}

func TestCreateFollowShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.POST("/follows", followCtr.CreateFollow)

	jData, _ := json.Marshal(cflRequest)

	req := httptest.NewRequest("POST", "/follows", bytes.NewBuffer(jData))
	req.Header.Set("Content-Type", "application/json")
	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)


	assert.Equal(t, http.StatusCreated, res.Code)

}

func TestUpdateFollowStatusShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.PATCH("/:subscriberId", followCtr.UpdateFollowStatus)

	jData, _ := json.Marshal(uflRequest)

	req := httptest.NewRequest(
		"PATCH",
		fmt.Sprintf("/%s",
			cflRequest.Subscriber.Id),
		bytes.NewBuffer(jData))

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusAccepted, res.Code)

}

func TestRemoveFollowShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.DELETE("/:subscriberId/remove/:producerId", followCtr.RemoveFollow)

	req := httptest.NewRequest(
		"DELETE",
		fmt.Sprintf("/%s/remove/%s",
			cflRequest.Subscriber.Id,
			cflRequest.Producer.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusAccepted, res.Code)

}

func TestGetFollowerShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.GET("/:ownerId/followings", followCtr.GetFollower)

	req := httptest.NewRequest(
		"GET",
		fmt.Sprintf("/%s/followings",
			cflRequest.Producer.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusOK, res.Code)

}

func TestGetFollowingsShouldSuccess(t *testing.T) {
	gin.SetMode(gin.TestMode)
	router := gin.New()
	router.GET("/:ownerId/followers", followCtr.GetFollowings)

	req := httptest.NewRequest(
		"GET",
		fmt.Sprintf("/%s/followers",
			cflRequest.Subscriber.Id),
		nil)

	res := httptest.NewRecorder()
	router.ServeHTTP(res, req)

	assert.Equal(t, http.StatusOK, res.Code)

}
