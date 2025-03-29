package shared.hub.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {
    @Column(name = "name", unique = true)
    String name;

    String description;
}
