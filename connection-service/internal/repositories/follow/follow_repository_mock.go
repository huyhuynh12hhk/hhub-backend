package repositories_follow

import "hhub/connection-service/internal/models"

type MockFollowRepository struct{}

// CreateFollow implements IFollowRepository.
func (m *MockFollowRepository) CreateFollow(model *models.Follow) *models.Follow {
	return model
}

// DeleteFollow implements IFollowRepository.
func (m *MockFollowRepository) DeleteFollow(requestId string) bool {
	return requestId == "1"
}

// GetFollowsByProducerId implements IFollowRepository.
func (m *MockFollowRepository) GetFollowsByProducerId(producerId string) []models.Follow {
	return []models.Follow{

	}
}

// GetFollowsBySubscriberId implements IFollowRepository.
func (m *MockFollowRepository) GetFollowsBySubscriberId(subscriberId string) []models.Follow {
	return []models.Follow{
		
	}
}

// GetFollowsBySubscriberIdAndProducerId implements IFollowRepository.
func (m *MockFollowRepository) GetFollowsBySubscriberIdAndProducerId(subscriberId string, producerId string) *models.Follow {
	return &models.Follow{
		SubscriberId: subscriberId,
		ProducerId: producerId,
	}
}

// UpdateFollow implements IFollowRepository.
func (m *MockFollowRepository) UpdateFollow(model *models.Follow) *models.Follow {
	return model
}

func NewMockFollowRepository() IFollowRepository {
	return &MockFollowRepository{}
}
