package keycloak

import (
	"context"
	"fmt"
	"hhub/connection-service/global"

	"github.com/coreos/go-oidc/v3/oidc"
)

func KeycloakVerifier() *oidc.IDTokenVerifier {

	ctx := context.Background()

	path := fmt.Sprintf("%s/realms/%s", global.Config.KeyCloak.Url, global.Config.KeyCloak.Realm)

	// panic(fmt.Sprintf("auth url: %+v",path))
	provider, err := oidc.NewProvider(ctx, path)
	if err != nil {
		panic(fmt.Sprintf("Error occur when init oidc %+v", err))
	}

	verifier := provider.Verifier(&oidc.Config{
		// ClientID: global.Config.KeyCloak.Client, 
		SkipClientIDCheck: true,
	})

	return verifier
}
