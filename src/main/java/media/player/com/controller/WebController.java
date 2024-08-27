package media.player.com.controller;

import lombok.RequiredArgsConstructor;
import media.player.com.library.Library;
import media.player.com.www.YTSAPIClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final Library library;
    private final YTSAPIClient ytsAPIClient;

    @GetMapping("/")
    public Mono<Rendering> index() {
        // Will be async resolved by Spring WebFlux before calling the view
        final Flux<String> playlistStream = Flux.fromIterable(this.library.getVideos());
        return Mono.just(Rendering.view("index").modelAttribute("videos", playlistStream).build());
    }

    @GetMapping("/test")
    @ResponseBody
    public Mono<YTSAPIClient.YTSMoviesRecord> test() {
        return ytsAPIClient.getMostWatchedMovies(1);
    }

}
