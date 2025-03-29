package shared.hub.domain.model.entity;

import java.util.Date;

import jakarta.persistence.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken extends BaseEntityAudit {
    String tokenId;
    Date issueAt;
    Date expiryAt;
}
