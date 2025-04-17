package models

import (
	"database/sql/driver"
	"fmt"
	"hhub/connection-service/internal/dtos"

	"gorm.io/gorm"
)

type followState string

const (
	ALL         followState = "ALL"
	PERSONALIZE followState = "PERSONALIZE"
	NONE        followState = "NONE"
)

var followStateMap = map[string]followState{
	string(ALL):         ALL,
	string(PERSONALIZE): PERSONALIZE,
	string(NONE):        NONE,
}

func ParseFollowStatus(s string) (followState, bool) {
    status, ok := followStateMap[s]
    return status, ok
}

func (ct *followState) Scan(value interface{}) error {
	*ct = followState(value.([]byte))
	return nil
}

func (ct followState) Value() (driver.Value, error) {
	return string(ct), nil
}

type Follow struct {
	gorm.Model
	SubscriberId string      `gorm:"column:subscriber_id; type:char(36);not null;uniqueIndex:idx_follow_pair_id"`
	Subscriber   UserInfo    `gorm:"foreignKey:SubscriberId;references:UID"`
	ProducerId   string      `gorm:"column:producer_id; type:char(36);not null;uniqueIndex:idx_follow_pair_id"`
	Producer     UserInfo    `gorm:"foreignKey:ProducerId;references:UID"`
	State        followState `gorm:"column:state; type:enum('ALL','PERSONALIZE','NONE');not null"`
}

func (Follow) TableName() string {
	return "connection_follow"
}

func (m *Follow) ToResponse() dtos.FollowResponse {

	return dtos.FollowResponse{
		Id:         fmt.Sprint(m.ID),
		Subscriber: m.Subscriber.ToResponse(),
		Producer:   m.Producer.ToResponse(),
		Status:     string(m.State),
		CreatedAt:  m.CreatedAt.Format("2025-01-01T00:00:00-0000"),
	}
}
