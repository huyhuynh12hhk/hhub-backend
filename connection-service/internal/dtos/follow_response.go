package dtos

type FollowResponse struct {
	Id         string `json:"id"`
	Subscriber UserVO `json:"from"`
	Producer   UserVO `json:"to"`
	Status     string `json:"status"`
	CreatedAt  string `json:"createdAt"`
}
