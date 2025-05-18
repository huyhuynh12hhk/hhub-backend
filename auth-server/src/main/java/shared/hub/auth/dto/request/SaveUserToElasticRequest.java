package shared.hub.auth.dto.request;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "users")
@Data
public class SaveUserToElasticRequest {
    @Id
    private String id;
    private String username;
    private String email;
    private String image;
    private String createdDate;
    private boolean active;
}
