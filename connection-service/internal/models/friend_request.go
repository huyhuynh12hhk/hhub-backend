package models

import (
	"database/sql/driver"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type connectState string

const (
	WAITING  connectState = "WAITING"
	ACCEPTED connectState = "ACCEPTED"
	DECLINED connectState = "DECLINED"
	DISABLED connectState = "DISABLED"
)

func (ct *connectState) Scan(value interface{}) error {
	*ct = connectState(value.([]byte))
	return nil
}

func (ct connectState) Value() (driver.Value, error) {
	return string(ct), nil
}

type FriendRequest struct {
	gorm.Model
	// UUID uuid.UUID `gorm:"column:uuid; type:char(36);primaryKey;not null;unique;index:idx_uuid"`
	SenderId   uuid.UUID    `gorm:"column:sender_id; type:char(36);not null;index:idx_sender_id"`
	Sender     UserInfo   `gorm:"foreignKey:sender_id"`
	ReceiverId uuid.UUID    `gorm:"column:receiver_id; type:char(36);not null;index:idx_receiver_id"`
	Receiver   UserInfo    `gorm:"foreignKey:receiver_id"`
	State      connectState `gorm:"column:state; type:enum('WAITING','ACCEPTED','DECLINED','DISABLED');not null"`
}

func (FriendRequest) TableName() string {
	return "connection_friend_request"
}
