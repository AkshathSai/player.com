package media.player.com.www;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpBinAPI {

    final WebClient webClient;

    Mono<HttpBinResponse> get() {
        
        return webClient.get()
                .uri("https://httpbin.org/get")
                .retrieve()
                .bodyToMono(HttpBinResponse.class)
                .onErrorResume(e -> {
                    log.error("Error occurred during HTTP GET: {}", e.getMessage());
                    return Mono.just(new HttpBinResponse(Map.of(), new HttpBinResponse.Headers(
                            null, null, null, null, null, null, null, null, null, null, null
                    ), "unknown", "unknown"));
                });
    }

    record HttpBinResponse(
            Map<String, Object> args,
            Headers headers,
            String origin,
            String url
    ) {
        record Headers(
                String Accept,
                String AcceptEncoding,
                String AcceptLanguage,
                String Host,
                String Priority,
                String Referer,
                String SecFetchDest,
                String SecFetchMode,
                String SecFetchSite,
                String UserAgent,
                String XAmznTraceId
        ) {
        }
    }

}