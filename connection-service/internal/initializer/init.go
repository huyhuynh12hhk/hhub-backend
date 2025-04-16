package initializer

import (
	"hhub/connection-service/global"
	"strconv"
)

// Entrypoint of all initialize setting before starting the server
func Run() {
	// Configuration must be first of all
	AddConfiguration()
	// Infrastructure must be before Controller layer
	AddInfrastructure()
	// Controller must be added for Routing working
	AddControllers()

	r:= UseRouting()
	r.Run(":"+strconv.Itoa(global.Config.Server.Port))
}
