package media.player.com.library;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
@CrossOrigin(origins = "*")
public class LibraryReactiveController {

    private final Library library;

    // Fetch the current list of videos
    @GetMapping("/videos")
    public Flux<String> getVideoList() {
        return library.getVideos();
    }

    // Fetch the current list of audio files
    @GetMapping("/audios")
    public Flux<String> getAudioList() {
        return Flux.fromIterable(library.audioSet.keySet()); // Direct access to audioSet
    }

    // Other existing methods (for updates, add, and remove)...

    @PostMapping("/videos")
    public void addVideo(@RequestBody Video video) {
        library.addVideo(video);
    }

    @DeleteMapping("/videos/{fileName}")
    public void removeVideo(@PathVariable String fileName) {
        library.removeVideo(fileName);
    }

    @PostMapping("/audios")
    public void addAudio(@RequestBody Audio audio) {
        library.addAudio(audio);
    }

    @DeleteMapping("/audios/{fileName}")
    public void removeAudio(@PathVariable String fileName) {
        library.removeAudio(fileName);
    }
}