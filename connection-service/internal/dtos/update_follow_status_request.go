package dtos

type UpdateFollowStatusRequest struct{
	Status string `json:"status"`
	Target string `json:"target"`	
}

