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

	data, err := fc.followService.GetFollowingUsers(ownerId)
	if err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Success, data)
}

func (fc *FollowController) GetFollower(c *gin.Context) {
	ownerId := c.Param("ownerId")

	data, err := fc.followService.GetFollowers(ownerId)
	if err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Success, data)
}

func (fc *FollowController) CreateFollow(c *gin.Context) {
	var payload dtos.FollowRequest

	if err := c.ShouldBindBodyWithJSON(&payload); err != nil {
		response.ErrorResponse(c, response.ParamInvalid, 400)
		return
	}

	if err := fc.followService.CreateFollow(&payload); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.CreatedSuccess, nil)
}

func (fc *FollowController) UpdateFollowStatus(c *gin.Context) {

	var payload dtos.UpdateFollowStatusRequest

	if err := c.ShouldBindBodyWithJSON(&payload); err != nil {
		response.ErrorResponse(c, response.ParamInvalid, 400)
		return
	}

	if err := fc.followService.UpdateFollowStatus(&payload); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.Success, nil)
}

func (fc *FollowController) RemoveFollow(c *gin.Context) {

	subscriberId := c.Param("ownerId")
	targetId := c.Param("targetId")
	//TODO: auth approach that can extract sender id from jwt

	if err := fc.followService.RemoveFollow(subscriberId, targetId); err != nil {
		response.ErrorResponse(c, response.CommonError, 400)
		return
	}

	response.SuccessResponse(c, response.CreatedSuccess, nil)
}
