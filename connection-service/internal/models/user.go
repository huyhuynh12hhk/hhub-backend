package models

import "hhub/connection-service/internal/dtos"

type UserInfo struct {
	UID      string `gorm:"column:user_id; type:char(36);not null;primaryKey"`
	Name     string `gorm:"column:user_name; type:varchar(255);not null"`
	ImageUrl string `gorm:"column:user_image_url; type:varchar(255)"`
}

func (UserInfo) TableName() string {
	return "connection_user_info"
}

func (m *UserInfo) ToResponse() dtos.UserVO{
	
	return dtos.UserVO{
		Id: m.UID,
		Name: m.Name,
		ImageUrl: m.ImageUrl,
	}
}