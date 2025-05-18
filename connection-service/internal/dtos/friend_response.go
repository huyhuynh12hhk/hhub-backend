package dtos

type FriendRequestResponse struct {
	Id         string `json:"id"`
	SenderId   string `json:"from"`
	ReceiverId string `json:"to"`
	Status     string `json:"status"`
	CreatedAt  string `json:"createdAt"`
}
