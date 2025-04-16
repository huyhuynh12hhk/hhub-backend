package dtos

type AddFriendRequest struct{
	Sender UserVO `json:"sender"`
	Receiver UserVO `json:"receiver"`	
}

