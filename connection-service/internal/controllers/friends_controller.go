package controllers

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/pkg/response"
	services "hhub/connection-service/internal/services/friend"

	"github.com/gin-gonic/gin"
)

type FriendController struct {
	friendService services.IFriendService
}

func NewFriendController(
	friendService services.IFriendService,
) *FriendController {
	return &FriendController{
		friendService: friendService,
	}
}

// var Friend = new(_FriendController)

func (fc *FriendController) AddFriend(c *gin.Context) {

	var payload dtos.AddFriendRequest

	if err := c.ShouldBindBodyWithJSON(&payload); err != nil {
		response.ErrorResponse(c, response.ParamInvalid, 400)
		return
	}

	if err := fc.friendService.CreateFriendRequest(&payload); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.CreatedSuccess, nil)
}

func (fc *FriendController) DeclineFriendRequest(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	if err := fc.friendService.DeclineFriendRequest(senderId, receiverId); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Accepted, nil)
}

func (fc *FriendController) AcceptFriendRequest(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	if err := fc.friendService.AcceptFriendRequest(senderId, receiverId); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Accepted, nil)
}

func (fc *FriendController) RemoveFriend(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	if err := fc.friendService.RemoveFriend(senderId, receiverId); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Accepted, nil)
}

func (fc *FriendController) GetFriendList(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, err := fc.friendService.GetFriendList(ownerId)
	if err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.CreatedSuccess, data)
}

func (fc *FriendController) GetFriendRequestList(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, err := fc.friendService.GetFriendRequestList(ownerId)
	if err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.CreatedSuccess, data)
}
