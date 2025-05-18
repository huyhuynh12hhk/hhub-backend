package shared.hub.auth.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import shared.hub.auth.dto.request.SaveUserToElasticRequest;


public interface UserSearchRepository extends ElasticsearchRepository<SaveUserToElasticRequest, String> {
}
