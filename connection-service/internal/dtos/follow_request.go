package dtos

type FollowRequest struct {
	SubscriberId string `json:"subscriber"`
	ProducerId   string `json:"producer"`
}
