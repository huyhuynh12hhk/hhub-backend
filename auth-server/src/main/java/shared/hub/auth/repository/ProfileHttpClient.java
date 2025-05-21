package shared.hub.auth.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shared.hub.auth.dto.request.ProfileCreationRequest;
import shared.hub.auth.dto.response.ApiResponse;
import shared.hub.auth.dto.response.ProfileInfoResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile}"
//        configuration = {AuthenticationRequestInterceptor.class}
)
public interface ProfileHttpClient {
    @PostMapping(value = "profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileInfoResponse> createProfile(@RequestBody ProfileCreationRequest request);
}
