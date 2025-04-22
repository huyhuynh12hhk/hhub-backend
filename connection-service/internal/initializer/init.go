package initializer

import (
	"github.com/gin-gonic/gin"
)

// Entrypoint of all initialize setting before starting the server
func Build() *gin.Engine {
	// Configuration must be first of all
	AddConfiguration()
	// Infrastructure must be before Controller layer
	AddInfrastructure()
	// Controller must be added for Routing working
	AddControllers()

	r := UseRouting()

	return r
}
