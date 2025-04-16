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
	SubscriberId uuid.UUID   `gorm:"column:subscriber_id; type:char(36);not null;index:idx_subscriber_id"`
	Subscriber   UserInfo  `gorm:"foreignKey:subscriber_id"`
	TargetId     uuid.UUID   `gorm:"column:target_id; type:char(36);not null;index:idx_target_id"`
	Target       UserInfo   `gorm:"foreignKey:target_id"`
	State        followState `gorm:"column:state; type:enum('ALL','PERSONALIZE','NONE');not null"`
}

func (Follow) TableName() string {
	return "connection_follow"
}
