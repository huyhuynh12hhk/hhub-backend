package mysql

// TODO: First use gorm code first approach then optimize

import (
	"fmt"
	"hhub/connection-service/global"
	"hhub/connection-service/internal/models"
	"time"

	driver "gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func AddMySQL() {
	m := global.Config.MySQL

	dsn := "%s:%s@tcp(%s:%v)/%s?charset=utf8mb4&parseTime=True&loc=Local"
	dsn = fmt.Sprintf(dsn, m.Username, m.Password, m.Host, m.Port, m.Database)
	// fmt.Println("Connection String: ",dsn)
	db, err := gorm.Open(driver.Open(dsn), &gorm.Config{})
	handleError(err, "Failed to connect to database")
	global.MySQL = db
	// Set the connection pool settings
	setPool()
	migrateTables()

}

func handleError(err error, errString string) {
	if err != nil {
		// Some log here

		panic(fmt.Errorf("Fatal error config file: %w \n", err))
	}
}

func setPool() {
	m := global.Config.MySQL
	sqlDB, err := global.MySQL.DB()
	handleError(err, "Failed to get database instance")

	sqlDB.SetMaxIdleConns(m.MaxIdleConns)
	sqlDB.SetMaxOpenConns(m.MaxOpenConns)
	sqlDB.SetConnMaxLifetime(time.Duration(m.ConnMaxLifetime))
}

func migrateTables(){
	err:= global.MySQL.AutoMigrate(
		&models.FriendRequest{},
		&models.Follow{},
		&models.UserInfo{},
	)
	handleError(err, "Failed to migrate database")
}