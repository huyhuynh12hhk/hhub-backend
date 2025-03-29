package shared.hub.application.service.access;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;

import shared.hub.application.model.request.AuthenticationRequest;
import shared.hub.application.model.request.IntrospectRequest;
import shared.hub.application.model.request.LogoutRequest;
import shared.hub.application.model.request.RefreshRequest;
import shared.hub.application.model.response.AuthenticationResponse;
import shared.hub.application.model.response.IntrospectResponse;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;
}
