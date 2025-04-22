// TODO: Implement this later in the right way.
// package shared.hub.gateway.application.configuration;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.Ordered;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// @Component
// public class StripPrefixFirst implements GatewayFilter, Ordered {
//    private final int parts;
//
//    public StripPrefixFirst(@Value("${app.api-prefix}") String apiPrefix) {
//        this.parts = apiPrefix.split("/").length;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//        String strippedPath = path.substring(parts);
//        ServerHttpRequest modifiedRequest = request.mutate().path(strippedPath).build();
//        return chain.filter(exchange.mutate().request(modifiedRequest).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // Execute before Spring Security filters
//    }
// }
