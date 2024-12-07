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

    public Mono<AggregatedResponse> getMovies() {
        Mono<YTSAPIClient.YTSMoviesRecord> latestMoviesMono = handleApiCall(ytsAPIClient.getLatestMovies());
        Mono<YTSAPIClient.YTSMoviesRecord> mustWatchMono = handleApiCall(ytsAPIClient.getMustWatch(1));
        Mono<YTSAPIClient.YTSMoviesRecord> mostWatchedMoviesMono = handleApiCall(ytsAPIClient.getMostWatchedMovies(1));
        Mono<YTSAPIClient.YTSMoviesRecord> mostLikedMono = handleApiCall(ytsAPIClient.getMostLiked(1));

        return Mono.zip(latestMoviesMono, mustWatchMono, mostWatchedMoviesMono, mostLikedMono)
                .map(tuple -> {
                    YTSAPIClient.YTSMoviesRecord mostWatched = tuple.getT1();
                    YTSAPIClient.YTSMoviesRecord latest = tuple.getT2();
                    YTSAPIClient.YTSMoviesRecord mustWatch = tuple.getT3();
                    YTSAPIClient.YTSMoviesRecord mostLiked = tuple.getT4();
                    return new AggregatedResponse(mostWatched, latest, mustWatch, mostLiked);
                });
    }

    private Mono<YTSAPIClient.YTSMoviesRecord> handleApiCall(Mono<YTSAPIClient.YTSMoviesRecord> apiCall) {
        return apiCall.onErrorResume(e -> {
            log.error("Error occurred in API call: {}", e.getMessage());
            return Mono.just(new YTSAPIClient.YTSMoviesRecord("error", "Failed to fetch data", new YTSAPIClient.YTSData(List.of())));
        });
    }

    record AggregatedResponse(
            YTSAPIClient.YTSMoviesRecord mostWatched,
            YTSAPIClient.YTSMoviesRecord latest,
            YTSAPIClient.YTSMoviesRecord mustWatch,
            YTSAPIClient.YTSMoviesRecord mostLiked) {
    }
}
