package main

import (
	"hhub/connection-service/global"
	"hhub/connection-service/internal/initializer"
	"strconv"
)

func main() {

	app := initializer.Build()

	

	app.Run(":"+strconv.Itoa(global.Config.Server.Port))
}
