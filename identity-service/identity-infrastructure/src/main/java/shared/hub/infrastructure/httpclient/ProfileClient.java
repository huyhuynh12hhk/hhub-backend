// package shared.hub.infrastructure.httpclient;
//
// import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
//
// import com.hub.identity.configuration.AuthenticationRequestInterceptor;
// import com.hub.identity.dtos.ApiResponse;
// import com.hub.identity.dtos.request.ProfileCreationRequest;
// import com.hub.identity.dtos.response.UserProfileResponse;
//
// @FeignClient(
//        name = "profile-service",
//        url = "${app.services.profile}",
//        configuration = {AuthenticationRequestInterceptor.class})
// public interface ProfileClient {
//    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
//    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
// }
