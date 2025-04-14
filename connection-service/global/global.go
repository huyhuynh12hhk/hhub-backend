package global

import (
	"hhub/connection-service/global/env"

	"gorm.io/gorm"
)

var (
	Config env.Config
	MySQL  *gorm.DB
)
