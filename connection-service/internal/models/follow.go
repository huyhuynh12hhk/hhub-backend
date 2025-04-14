package models

import (
	"database/sql/driver"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type followState string

const (
	ALL         followState = "ALL"
	PERSONALIZE followState = "PERSONALIZE"
	NONE        followState = "NONE"
)

func (ct *followState) Scan(value interface{}) error {
	*ct = followState(value.([]byte))
	return nil
}

func (ct followState) Value() (driver.Value, error) {
	return string(ct), nil
}

type Follow struct {
	gorm.Model
	SubscriberId   uuid.UUID   `gorm:"column:subscriber_id; type:char(36);not null;index:idx_subscriber_id"`
	SubscriberName string      `gorm:"column:subscriber_name; type:varchar(255);not null"`
	TargetId       uuid.UUID   `gorm:"column:target_id; type:char(36);not null;index:idx_target_id"`
	TargetName     string      `gorm:"column:target_name; type:varchar(255);not null"`
	State          followState `gorm:"column:state; type:enum('ALL','PERSONALIZE','NONE');not null"`
}

func (Follow) TableName() string {
	return "connection_follow"
}
