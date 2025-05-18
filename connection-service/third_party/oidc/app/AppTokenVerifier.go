package oauth

import (
	"context"
	"fmt"
	"hhub/connection-service/global"

	"github.com/coreos/go-oidc/v3/oidc"
)

func AppTokenVerifier() *oidc.IDTokenVerifier {

	ctx := context.Background()


	provider, err := oidc.NewProvider(ctx, global.Config.Auth.Url)
	if err != nil {
		panic(fmt.Sprintf("Error occur when init oidc %+v", err))
	}

	verifier := provider.Verifier(&oidc.Config{
		SkipClientIDCheck: true,
	})

	return verifier
}
