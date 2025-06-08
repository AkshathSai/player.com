package media.player.com.www;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Aggregator {

    final YTSAPIClient ytsAPIClient;
    final HttpBinAPI httpBinAPI;

    public Mono<AggregatedResponse> getMovies() {
        Mono<HttpBinAPI.HttpBinResponse> httpBinResponseMono = httpBinAPI.get();
        Mono<YTSAPIClient.YTSMoviesRecord> latestMoviesMono = handleApiCall(ytsAPIClient.getLatestMovies());
        Mono<YTSAPIClient.YTSMoviesRecord> mustWatchMono = handleApiCall(ytsAPIClient.getMustWatch(1));
        Mono<YTSAPIClient.YTSMoviesRecord> mostWatchedMoviesMono = handleApiCall(ytsAPIClient.getMostWatchedMovies(1));
        Mono<YTSAPIClient.YTSMoviesRecord> mostLikedMono = handleApiCall(ytsAPIClient.getMostLiked(1));

        return Mono.zip(latestMoviesMono, mustWatchMono, mostWatchedMoviesMono, mostLikedMono, httpBinResponseMono)
                .flatMap(tuple -> {
                    YTSAPIClient.YTSMoviesRecord mostWatched = tuple.getT1();
                    YTSAPIClient.YTSMoviesRecord latest = tuple.getT2();
                    YTSAPIClient.YTSMoviesRecord mustWatch = tuple.getT3();
                    YTSAPIClient.YTSMoviesRecord mostLiked = tuple.getT4();
                    HttpBinAPI.HttpBinResponse httpBinResponse = tuple.getT5();
                    return Mono.just(new AggregatedResponse(httpBinResponse, mostWatched, latest, mustWatch, mostLiked));
                });
    }

    private Mono<YTSAPIClient.YTSMoviesRecord> handleApiCall(Mono<YTSAPIClient.YTSMoviesRecord> apiCall) {
        return apiCall.onErrorResume(e -> {
            log.error("Error occurred in API call: {}", e.getMessage());
            return Mono.just(new YTSAPIClient.YTSMoviesRecord("error", "Failed to fetch data", new YTSAPIClient.YTSData(List.of())));
        });
    }

    public record AggregatedResponse(
            HttpBinAPI.HttpBinResponse httpBinResponse,
            YTSAPIClient.YTSMoviesRecord mostWatched,
            YTSAPIClient.YTSMoviesRecord latest,
            YTSAPIClient.YTSMoviesRecord mustWatch,
            YTSAPIClient.YTSMoviesRecord mostLiked) {
    }
}
