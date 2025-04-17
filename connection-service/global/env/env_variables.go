package env

type Config struct {
	Server   ServerSetting   `mapstructure:"server"`
	MySQL    MySQLSetting    `mapstructure:"mysql"`
	KeyCloak KeyCloakSetting `mapstructure:"keycloak"`
}

type ServerSetting struct {
	Port   int    `mapstructure:"port"`
	Prefix string `mapstructure:"prefix"`
	Debug  bool   `mapstructure:"debug"`
}

type MySQLSetting struct {
	Host            string `mapstructure:"host"`
	Port            int    `mapstructure:"port"`
	Username        string `mapstructure:"user"`
	Password        string `mapstructure:"password"`
	Database        string `mapstructure:"database"`
	MaxIdleConns    int    `mapstructure:"maxIdleConns"`
	MaxOpenConns    int    `mapstructure:"maxOpenConns"`
	ConnMaxLifetime int    `mapstructure:"connMaxLifetime"`
}

type KeyCloakSetting struct {
	Url    string `mapstructure:"url"`
	Realm  string `mapstructure:"realm"`
	Client string `mapstructure:"client"`
	Secret string `mapstructure:"secret"`
}
