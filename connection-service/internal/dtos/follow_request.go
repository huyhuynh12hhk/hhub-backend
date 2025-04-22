package dtos

type FollowRequest struct {
	Subscriber UserVO `json:"subscriber"`
	Producer   UserVO `json:"producer"`
}
