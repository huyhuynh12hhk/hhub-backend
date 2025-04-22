package dtos

type UpdateFollowStatusRequest struct {
	Status   string `json:"status"`
	Producer UserVO `json:"producer"`
}
