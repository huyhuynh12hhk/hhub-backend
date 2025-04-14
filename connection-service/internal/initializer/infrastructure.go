package initializer

import "hhub/connection-service/third_party/database/mysql"

func AddInfrastructure() {
	// Add persistence initialization logic here
	// This could include database connections, ORM setup, etc.
	mysql.AddMySQL()
}