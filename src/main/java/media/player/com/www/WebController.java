package media.player.com.www;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.player.com.library.Library;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {

    private final Library library;
    private final YTSAPIClient ytsAPIClient;
    private final Aggregator aggregator;

    /*@GetMapping("/")
    public Mono<Rendering> index() {
        // Will be async resolved by Spring WebFlux before calling the view
        final Flux<String> playlistStream = Flux.fromIterable(this.library.getVideos());
        return Mono.just(Rendering.view("index").modelAttribute("videos", playlistStream).build());
    }*/

    @GetMapping("/")
    public Mono<String> index(Model model) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(this.library.getVideos());

        model.addAttribute("videos", reactiveDataDrivenMode);
        return Mono.just("index");
    }

    @GetMapping("/test")
    @ResponseBody
    public Mono<Aggregator.AggregatedResponse> test() {
        return aggregator.getMovies();
    }

    @GetMapping("/test2")
    @ResponseBody
    public Mono<YTSAPIClient.YTSMoviesRecord> test2() {
        return ytsAPIClient.getMostWatchedMovies(1);
    }

}
