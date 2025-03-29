package shared.hub.application.mapper;

import org.mapstruct.Mapper;

import shared.hub.application.model.request.CreateUserRequest;
import shared.hub.application.model.request.ProfileCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(CreateUserRequest request);
}
