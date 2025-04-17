package repositories_friend

import (
	"errors"
	"fmt"
	"hhub/connection-service/global"
	"hhub/connection-service/internal/models"

	"gorm.io/gorm"
)

type _FriendRepository struct{
	// TODO: consider inject db here alter for global using
}

// CreateFriendRequest implements IFriendRepository.
func (f *_FriendRepository) CreateFriendRequest(model *models.FriendRequest) *models.FriendRequest {
	var MDb = *global.MySQL

	fmt.Printf("\n\nRepo: Create Friend request: %+v\n", model)
	sender := model.Sender
	receiver := model.Receiver
	MDb.FirstOrCreate(&sender, models.UserInfo{UID: sender.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", sender)

	MDb.FirstOrCreate(&receiver, models.UserInfo{UID: receiver.UID})
	fmt.Printf("\n\nRepo: Create User Info: %+v\n", receiver)

	result := MDb.Create(&model)

	if on, _ := onError(result, nil, "Issue when create friend request"); on {
		return nil
	}

	fmt.Printf("Repo: Create Friend result: %+v\n", model.ID)

	return model
}

// UpdateStatusFriendRequest implements IFriendRepository.
func (f *_FriendRepository) UpdateFriendRequest(model *models.FriendRequest) *models.FriendRequest {

	result := global.MySQL.Model(&model).Updates(models.FriendRequest{
		State: model.State,
	})

	if on, _ := onError(result, nil, "Issue when update friend status"); on {
		return nil
	}

	// global.MySQL.Save(&model)
	return model

}

// DeleteFriendRequest implements IFriendRepository.
func (f *_FriendRepository) DeleteFriendRequest(requestId string) bool {
	result := global.MySQL.Delete(&models.FriendRequest{}, requestId)

	if on, _ := onError(result, nil, "Issue when delete friend request"); on {
		return false
	}
	return true
}

// GetFriendList implements IFriendRepository.
func (f *_FriendRepository) GetFriendList(ownerId string) []models.FriendRequest {
	var friends []models.FriendRequest
	global.MySQL.
		// Where("receiver_id = ?", ownerId).
		// Or("sender_id = ?", ownerId).
		Where(models.FriendRequest{ReceiverId: ownerId, State: models.ACCEPTED}).
		Or(models.FriendRequest{SenderId: ownerId}).
		Preload("Sender").
		Preload("Receiver").
		Find(&friends)

	fmt.Printf("\n\nRepo: Friend request: %+v\n", friends)

	return friends
}

// GetFriendRequestByReceiverId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestByReceiverId(receiverId string) []models.FriendRequest {
	var friends []models.FriendRequest
	global.MySQL.
		Where(models.FriendRequest{ReceiverId: receiverId, State: models.WAITING}).
		Preload("Sender").
		Preload("Receiver").
		Find(&friends)

	fmt.Printf("\n\nRepo: Friend requests: %+v\n", friends)

	return friends
}

// GetFriendRequestBySenderId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestBySenderId(senderId string) []models.FriendRequest {
	var friends []models.FriendRequest
	global.MySQL.
		Where(models.FriendRequest{SenderId: senderId}).
		// Where("sender_id = ?", senderId).
		Preload("Sender").
		Preload("Receiver").
		Find(&friends)

	fmt.Printf("\n\nRepo: Friend requests: %+v\n", friends)

	return friends
}

// GetFriendRequestBySenderIdAndReceiverId implements IFriendRepository.
func (f *_FriendRepository) GetFriendRequestBySenderIdAndReceiverId(senderId string, receiverId string) *models.FriendRequest {
	var friend models.FriendRequest
	result := global.MySQL.
		Model(&models.FriendRequest{SenderId: senderId, ReceiverId: receiverId}).
		First(&friend)

	if on, _ := onError(result, gorm.ErrRecordNotFound, "Friend request not found"); on {
		return nil
	}

	fmt.Printf("\n\nRepo: Friend request details: %+v\n", friend)
	return &friend
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

func NewFriendRepository() IFriendRepository {
	return &_FriendRepository{}
}
