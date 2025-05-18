package shared.hub.auth.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shared.hub.auth.mapper.UserMapper;
import shared.hub.auth.model.entity.AppUser;
import shared.hub.auth.model.event.UserSavedEvent;
import shared.hub.auth.repository.UserSearchRepository;

import java.util.Objects;

@Slf4j
@Component
public class SavedUserConsumer {

    @Autowired
    private UserSearchRepository userSearchRepository;

    @KafkaListener(
            topics = "${kafka.topic.user.saved}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleUserSaved(UserSavedEvent user) {
        log.info("Received user object: " + user.getEmail());

        userSearchRepository.save(
                Objects.requireNonNull(UserMapper.mapToUserSearchDoc(user))
        );

        log.info("Saved user to elk");
    }
}
