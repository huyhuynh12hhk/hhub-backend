package dtos

type FollowRequest struct{
	Subscriber UserVO `json:"subscriber"`
	Target UserVO `json:"target"`	
}

