package dtos


type AddFriendRequest struct {
	SenderId   string `json:"sender"`
	ReceiverId string `json:"receiver"`
}

