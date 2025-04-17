package repositories_follow

import (
	"errors"
	"fmt"
	"hhub/connection-service/global"
	"hhub/connection-service/internal/models"

	"gorm.io/gorm"
)

type _FollowRepository struct{}

// CreateFollow implements IFollowRepository.
func (r *_FollowRepository) CreateFollow(model *models.Follow) *models.Follow {
	var MDb = *global.MySQL

	fmt.Printf("\n\nRepo: Create Follow: %+v\n", model)
	subscriber := model.Subscriber
	producer := model.Producer
	MDb.FirstOrCreate(&subscriber, models.UserInfo{UID: subscriber.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", subscriber)

	MDb.FirstOrCreate(&producer, models.UserInfo{UID: producer.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", producer)

	result := MDb.Create(&model)

	if on, _ := onError(result, nil, "Issue when create follow"); on {
		return nil
	}

	fmt.Printf("Repo: Create Follow result: %+v\n", model.ID)

	return model
}

// UpdateStatusFollow implements IFollowRepository.
func (r *_FollowRepository) UpdateFollow(model *models.Follow) *models.Follow {
	var MDb = *global.MySQL

	fmt.Printf("\n\nRepo: Create Follow: %+v\n", model)
	subscriber := model.Subscriber
	producer := model.Producer
	MDb.FirstOrCreate(&subscriber, models.UserInfo{UID: subscriber.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", subscriber)

	MDb.FirstOrCreate(&producer, models.UserInfo{UID: producer.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", producer)

	result := MDb.Create(&model)

	if on, _ := onError(result, nil, "Issue when create follow"); on {
		return nil
	}

	fmt.Printf("Repo: Create Follow result: %+v\n", model.ID)

	return model
}

// DeleteFollow implements IFollowRepository.
func (r *_FollowRepository) DeleteFollow(requestId string) bool {
	result := global.MySQL.Delete(&models.FriendRequest{}, requestId)

	if on, _ := onError(result, nil, "Issue when delete follow"); on {
		return false
	}
	return true
}

// GetFollowsByProducerId implements IFollowRepository.
func (r *_FollowRepository) GetFollowsByProducerId(producerId string) []models.Follow {
	var follows []models.Follow
	global.MySQL.
		Where(models.Follow{ProducerId: producerId}).
		Preload("Producer").
		Preload("Subscriber").
		Find(&follows)

	fmt.Printf("\n\nRepo: Follows: %+v\n", follows)

	return follows
}

// GetFollowsBySubscriberId implements IFollowRepository.
func (r *_FollowRepository) GetFollowsBySubscriberId(subscriberId string) []models.Follow {
	var follows []models.Follow
	s:= global.MySQL.
		Where(models.Follow{SubscriberId: subscriberId, State: models.PERSONALIZE}).
		Preload("Producer").
		Preload("Subscriber").
		Find(&follows)

	fmt.Printf("\n\nRepo: GetFollowsBySubscriberId: %+v\n", s.Error)

	return follows
}

// GetFollowsBySubscriberIdAndProducerId implements IFollowRepository.
func (r *_FollowRepository) GetFollowsBySubscriberIdAndProducerId(subscriberId string, producerId string) *models.Follow {
	var follow models.Follow
	result := global.MySQL.
		Model(&models.Follow{SubscriberId: subscriberId, ProducerId: producerId}).
		First(&follow)

	if on, _ := onError(result, gorm.ErrRecordNotFound, "Follow not found"); on {
		return nil
	}

	fmt.Printf("\n\nRepo: Follow details: %+v\n", follow)
	return &follow
}

func onError(result *gorm.DB, typeErr error, msg string) (bool, error) {
	if typeErr != nil && errors.Is(result.Error, typeErr) {
		fmt.Printf("Error trace: %+v", result.Error)
		return true, errors.New(msg)
	}
	if result.Error != nil {
		fmt.Printf("Error trace: %+v", result.Error)
		return true, errors.New(msg)
	}

	return false, nil
}

func NewFollowRepository() IFollowRepository {
	return &_FollowRepository{}
}
