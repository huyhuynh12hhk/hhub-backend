package dtos

type FriendRequestResponse struct {
	Id        string `json:"id"`
	Sender    UserVO `json:"from"`
	Receiver  UserVO `json:"to"`
	Status    string `json:"status"`
	CreatedAt string `json:"createdAt"`
}
