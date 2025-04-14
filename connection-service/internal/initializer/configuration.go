package initializer

import (
	"fmt"
	"hhub/connection-service/global"
	"os"

	"github.com/spf13/viper"
)

func AddConfiguration() {
	// Load environment setup here
	viper := viper.New()
	viper.AddConfigPath("./configs/")
	viper.SetConfigName(getConfigFile())
	viper.SetConfigType("yaml")

	// Read the configuration file
	if err:= viper.ReadInConfig(); err != nil {
		panic(fmt.Errorf("Fatal error config file: %w \n", err))
	}

	// Map to global settings
	if err:=viper.Unmarshal(&global.Config); err != nil {
		panic(fmt.Errorf("Fatal error config file: %w \n", err))
	}
	

}

func getConfigFile() string {
	env := os.Getenv("ENV")
	switch env {
	case "development":
		return "development"
	case "testing":
		return "testing"
	case "production":
		return "production"
	default:
		return "default"
	}
}
