package repositories_friend

import "hhub/connection-service/internal/models"

type MockFriendRepository struct{}

// CreateFriendRequest implements IFriendRepository.
func (m *MockFriendRepository) CreateFriendRequest(model *models.FriendRequest) *models.FriendRequest {

	model.ID = 1
	return model
}

// DeleteFriendRequest implements IFriendRepository.
func (m *MockFriendRepository) DeleteFriendRequest(requestId string) bool {
	if requestId == "1"{

		return true
	}
	return false
}

// GetFriendList implements IFriendRepository.
func (m *MockFriendRepository) GetFriendList(ownerId string) []models.FriendRequest {

	return []models.FriendRequest{
		
	}
}

// GetFriendRequestByReceiverId implements IFriendRepository.
func (m *MockFriendRepository) GetFriendRequestByReceiverId(receiverId string) []models.FriendRequest {
	return []models.FriendRequest{

	}
}

// GetFriendRequestBySenderId implements IFriendRepository.
func (m *MockFriendRepository) GetFriendRequestBySenderId(senderId string) []models.FriendRequest {
	return []models.FriendRequest{

	}
}

// GetFriendRequestBySenderIdAndReceiverId implements IFriendRepository.
func (m *MockFriendRepository) GetFriendRequestBySenderIdAndReceiverId(senderId string, receiverId string) *models.FriendRequest {
	
	fr := models.FriendRequest{
		ReceiverId: receiverId,
		SenderId: senderId,
	}

	return &fr


}

// UpdateFriendRequest implements IFriendRepository.
func (m *MockFriendRepository) UpdateFriendRequest(model *models.FriendRequest) *models.FriendRequest {
	
	return model
}

func NewMockFriendRepository() IFriendRepository {
	return &MockFriendRepository{}
}
