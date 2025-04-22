package controllers

import (
	"hhub/connection-service/internal/dtos"
	"hhub/connection-service/internal/pkg/response"
	services "hhub/connection-service/internal/services/follow"

	"github.com/gin-gonic/gin"
)

type FollowController struct {
	followService services.IFollowService
}

func NewFollowController(
	followService services.IFollowService,
) *FollowController {
	return &FollowController{
		followService: followService,
	}
}

func (fc *FollowController) GetFollowings(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, code, err := fc.followService.GetFollowingUsers(ownerId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, data)
}

func (fc *FollowController) GetFollower(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, code, err := fc.followService.GetFollowers(ownerId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, data)
}

func (fc *FollowController) CreateFollow(c *gin.Context) {
	var payload dtos.FollowRequest

	if err := c.ShouldBindBodyWithJSON(&payload); err != nil {
		response.ErrorResponse(c, response.ParamInvalid)
		return
	}

	data, code, err := fc.followService.CreateFollow(&payload)

	
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, data)
}

func (fc *FollowController) UpdateFollowStatus(c *gin.Context) {

	subscriberId := c.Param("subscriberId")
	var payload dtos.UpdateFollowStatusRequest

	if err := c.ShouldBindBodyWithJSON(&payload); err != nil {
		response.ErrorResponse(c, response.ParamInvalid)
		return
	}

	code, err := fc.followService.UpdateFollowStatus(subscriberId, &payload)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, nil)
}

func (fc *FollowController) RemoveFollow(c *gin.Context) {

	subscriberId := c.Param("ownerId")
	producerId := c.Param("producerId")
	//TODO: auth approach that can extract sender id from jwt

	code, err := fc.followService.RemoveFollow(subscriberId, producerId)
	if err != nil {
		response.ErrorResponse(c, code)
		return
	}

	response.SuccessResponse(c, code, nil)
}
