package shared.hub.domain.model.entity;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken extends BaseEntity {
    String tokenId;
    Date issueAt;
    Date expiryAt;
}
