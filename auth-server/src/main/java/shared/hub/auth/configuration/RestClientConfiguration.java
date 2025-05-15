package shared.hub.auth.configuration;//package lab.start.auth_server.configuration;
//
//import lab.start.auth_server.repository.ProfileHttpClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
//import org.springframework.boot.web.client.ClientHttpRequestFactories;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.support.RestClientAdapter;
//import org.springframework.web.service.invoker.HttpServiceProxyFactory;
//
//import java.time.Duration;
//
//@Configuration
//public class RestClientConfiguration {
//
//    @Bean
//    public ProfileHttpClient profileHttpClient(
//            @Value("${app.services.profile}")
//            String profileUrl
//    ) {
//        RestClient restClient = RestClient.builder()
//                .baseUrl(profileUrl)
//                .build();
//
//        var adapter = RestClientAdapter.create(restClient);
//        var proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
//        return proxyFactory.createClient(ProfileHttpClient.class);
//    }
//
//    private ClientHttpRequestFactory getClientRequestFactory() {
//        ClientHttpRequestFactorySettings clientHttpRequestFactorySettings = ClientHttpRequestFactorySettings.DEFAULTS
//                .withConnectTimeout(Duration.ofSeconds(3))
//                .withReadTimeout(Duration.ofSeconds(3));
//        return ClientHttpRequestFactories.get(clientHttpRequestFactorySettings);
//    }
//}
