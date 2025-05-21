package dtos

type FollowResponse struct {
	Id           string `json:"id"`
	SubscriberId string `json:"fromId"`
	ProducerId   string `json:"toId"`
	Status       string `json:"status"`
	CreatedAt    string `json:"createdAt"`
}
