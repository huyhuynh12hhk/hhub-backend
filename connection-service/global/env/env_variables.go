package env

type Config struct {
	Server ServerSetting `mapstructure:"server"`
	MySQL  MySQLSettings `mapstructure:"mysql"`
}

type ServerSetting struct {
	Port   int    `mapstructure:"port"`
	Prefix string `mapstructure:"prefix"`
	Debug  bool   `mapstructure:"debug"`
}

type MySQLSettings struct {
	Host            string `mapstructure:"host"`
	Port            int    `mapstructure:"port"`
	Username        string `mapstructure:"user"`
	Password        string `mapstructure:"password"`
	Database        string `mapstructure:"database"`
	MaxIdleConns    int    `mapstructure:"maxIdleConns"`
	MaxOpenConns    int    `mapstructure:"maxOpenConns"`
	ConnMaxLifetime int    `mapstructure:"connMaxLifetime"`
}
