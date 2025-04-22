package controllers

import (
	"github.com/gin-gonic/gin"
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/pkg/response"
	services "hhub/connection-service/internal/services/friend"
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
		response.ErrorResponse(c, response.ParamInvalid)
		return
	}
	// fmt.Printf("has json  %+v\n", payload)

	data, code, err := fc.friendService.CreateFriendRequest(&payload)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	 

	response.SuccessResponse(c, code, data)
}

func (fc *FriendController) DeclineFriendRequest(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	code, err := fc.friendService.DeclineFriendRequest(senderId, receiverId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, nil)
}

func (fc *FriendController) AcceptFriendRequest(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	code, err := fc.friendService.AcceptFriendRequest(senderId, receiverId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, nil)
}

func (fc *FriendController) RemoveFriend(c *gin.Context) {

	receiverId := c.Param("receiverId")
	senderId := c.Param("senderId")
	//TODO: auth approach that can extract sender id from jwt

	code, err := fc.friendService.RemoveFriend(senderId, receiverId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, nil)
}

func (fc *FriendController) GetFriendList(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, code, err := fc.friendService.GetFriendList(ownerId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, data)
}

func (fc *FriendController) GetFriendRequestList(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, code, err := fc.friendService.GetFriendRequestList(ownerId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, data)
}
