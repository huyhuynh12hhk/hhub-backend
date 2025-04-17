package keycloak
// // Manually implement 

// import (
// 	"encoding/json"
// 	"fmt"
// 	"hhub/connection-service/global"
// 	"net/http"
// )

// func FetchJWKS() (map[string]interface{}, error) {
// 	path := "%s/realms/%s/protocol/openid-connect/certs"
// 	path = fmt.Sprintf(path, global.Config.KeyCloak.Url, global.Config.KeyCloak.Realm)
// 	resp, err := http.Get(path)
// 	if err != nil {
// 		return nil, err
// 	}
// 	defer resp.Body.Close()

// 	var jwks map[string]interface{}
// 	if err := json.NewDecoder(resp.Body).Decode(&jwks); err != nil {
// 		return nil, err
// 	}
// 	return jwks, nil
// }
