package media.player.com.www;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        var poolSize = 500;
        var provider = ConnectionProvider.builder("CPool")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5)
                .build();

        var httpClient = HttpClient.create(provider)
                .compress(true)
                .keepAlive(true);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter((clientRequest, next) -> next.exchange(clientRequest)
                        .onErrorResume(e -> Mono.error(new RuntimeException("An error occurred", e))))
                .build();
    }

    @Bean
    YTSAPIClient ytsapiClient(WebClient webClient) {
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(YTSAPIClient.class);
    }
}
