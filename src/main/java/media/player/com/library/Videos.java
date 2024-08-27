package media.player.com.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
class VideoController {

    final VideoService videoService;
    final ResourceLoader resourceLoader;
    private final Library library;

    /*@GetMapping(value = "/{fileName}", produces = "video/mp4")
    public Mono<Resource> playVideoFromFS(@PathVariable("fileName") String fileName,
                                          @RequestHeader(value = "Range", required = false) String range) {
        log.info(range);
        // return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + videoService.getFilePath(fileName)));
        return videoService.loadResourceAsync(fileName)
                .subscribeOn(Schedulers.boundedElastic()); // Offload blocking call to bounded elastic scheduler
    }*/

    /*@GetMapping(value = "/{fileName}", produces = "video/mp4")
    public Mono<ResponseEntity<Resource>> playVideoFromFS(@PathVariable("fileName") String fileName,
                                                          @RequestHeader(value = "Range", required = false) String range) {
        log.info(range);
        return videoService.loadResourceAsync(fileName)
                .map(resource -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .body(resource))
                .defaultIfEmpty(ResponseEntity.noContent().build())
                .subscribeOn(Schedulers.boundedElastic()); // Offload blocking call to bounded elastic scheduler
    }*/

    /*@GetMapping(value = "/{fileName}", produces = "video/mp4")
    Mono<ResponseEntity<Resource>> playVideoFromFS(@PathVariable("fileName") String fileName,
                                                   @RequestHeader(value = "Range", required = false) String range) {
        log.info(range);
        return videoService.loadResource(fileName)
                .subscribeOn(Schedulers.boundedElastic()) // Offload blocking call to bounded elastic scheduler
                .map(resource -> ResponseEntity.ok()
                        // .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, "video/webm")
                        .body(resource))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }*/

    @GetMapping("/{fileName}")
    ResponseEntity<UrlResource> getFullVideo(@PathVariable("fileName") String fileName,
                                             @RequestHeader HttpHeaders headers) throws MalformedURLException {
        log.debug("{}", headers.getRange());
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        var video = new UrlResource(Paths.get(library.getVideoFilePath(decodedFileName)).toUri().toURL());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory
                        .getMediaType(video)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(video);
    }

    /*@GetMapping(value = "/{fileName}", produces = "video/mp4")
    public Mono<Resource> playVideoFromFS(@PathVariable("fileName") String fileName,
                                          @RequestHeader(value = "Range", required = false) String range) throws MalformedURLException {
        log.info(range);
        Path filePath = Paths.get(videoService.getFilePath(fileName)); // Adjust the path according to your storage location
        return Mono.fromSupplier(() -> {
            try {
                return new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }*/

}

@Service
@RequiredArgsConstructor
class VideoService {

    final Library library;
    final ResourceLoader resourceLoader;

    Mono<String> getFilePath(String fileName) {
        return Mono.fromSupplier(() -> {
            var decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            return library.getVideoFilePath(decodedFileName);
        });
    }

    Mono<Resource> loadResource(String fileName) {
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + library.getVideoFilePath(URLDecoder.decode(fileName, StandardCharsets.UTF_8))));
    }

    Mono<Resource> loadResourceAsync(String fileName) {
        return Mono.fromSupplier(() -> {
                    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                    String filePath = library.getVideoFilePath(decodedFileName);
                    //var video = library.getVideo(decodedFileName);
                    return resourceLoader.getResource("file:" + filePath);
        }).subscribeOn(Schedulers.boundedElastic()); // Offload blocking call
    }

    Mono<UrlResource> loadURLResourceAsync(String fileName) {
        return Mono.fromCallable(() -> {
                    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                    String filePath = library.getVideoFilePath(decodedFileName);
                    if (filePath == null) {
                        throw new RuntimeException("File not found: " + decodedFileName);
                    }
                    return Paths.get(filePath).toUri().toURL();
                }).map(UrlResource::new)
                .subscribeOn(Schedulers.boundedElastic());
    }

}