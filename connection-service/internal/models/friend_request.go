package models

import (
	"database/sql/driver"
	"fmt"
	"hhub/connection-service/internal/dtos"

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
	SenderId   string       `gorm:"column:sender_id; type:char(36);not null;uniqueIndex:idx_friend_pair_id"`
	// Sender     UserInfo     `gorm:"foreignKey:SenderId;references:UID"`
	ReceiverId string       `gorm:"column:receiver_id; type:char(36);not null;uniqueIndex:idx_friend_pair_id"`
	// Receiver   UserInfo     `gorm:"foreignKey:ReceiverId;references:UID"`
	State      connectState `gorm:"column:state; type:enum('WAITING','ACCEPTED','DECLINED','DISABLED');not null"`
}

func (FriendRequest) TableName() string {
	return "connection_friend_request"
}

func (m *FriendRequest) ToResponse() dtos.FriendRequestResponse{
	
	return dtos.FriendRequestResponse{
		Id: fmt.Sprint(m.ID),
		SenderId: m.SenderId,
		ReceiverId: m.ReceiverId,
		Status: string(m.State),
		CreatedAt: m.CreatedAt.Format("2025-01-01T00:00:00-0000"),
	}
}

