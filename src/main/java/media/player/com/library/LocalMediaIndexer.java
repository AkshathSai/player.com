package media.player.com.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMediaIndexer {

    public static boolean indexed = false;
    private final UnaryOperator<String> decodePathSegment = pathSegment -> UriUtils.decode(pathSegment, StandardCharsets.UTF_8.name());
    private final Library library;

    public void indexLocalMedia(String... locations) throws IOException {
        findLocalMediaFiles(locations)
                .thenCompose(paths -> {
                    Set<Path> musicPaths = filterPaths(paths, ".mp3", ".flac");
                    Set<Path> moviePaths = filterPaths(paths, ".mp4", ".mkv", ".avi", ".mpeg");

                    try {
                        Set<Video> finalVideos = createVideoEntities(moviePaths);
                        Set<Audio> finalAudioSet = createMusicEntities(musicPaths);

                        CompletableFuture<Void> moviesFuture = CompletableFuture.runAsync(() -> {
                            finalVideos.forEach(library::addVideo);
                            log.info("Finished Indexing Movies");
                        });

                        CompletableFuture<Void> musicFuture = CompletableFuture.runAsync(() -> {
                            finalAudioSet.forEach(library::addAudio);
                            log.info("Finished Indexing Music");
                        });

                        return CompletableFuture.allOf(moviesFuture, musicFuture)
                                .thenRun(() -> {
                                    indexed = true;
                                    log.info("Finished Indexing Content");
                                });
                    } catch (IOException e) {
                        log.error("Error creating media entities", e);
                        return CompletableFuture.failedFuture(e);
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Error indexing media", throwable);
                    return null;
                });
    }

    private CompletableFuture<Set<Path>> findLocalMediaFiles(String... locations) {
        final String pattern = "glob:**/*.{mp4,mpeg,mp3,mkv,flac}";

        Set<Path> matchingPaths = ConcurrentHashMap.newKeySet();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

        for (String location : locations) {
            try {
                Path start = Paths.get(location);
                if (Files.exists(start)) {
                    Files.walkFileTree(start, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                            if (matcher.matches(path)) {
                                matchingPaths.add(path);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
            } catch (IOException e) {
                log.error("Error finding personal media files in location: {}", location, e);
            }
        }

        return CompletableFuture.completedFuture(matchingPaths);
    }

    private Set<Path> filterPaths(Set<Path> paths, String... extensions) {
        return paths.parallelStream()
                .filter(path -> {
                    String pathString = path.toString().toLowerCase();
                    for (String ext : extensions) {
                        if (pathString.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toSet());
    }

    private Set<Video> createVideoEntities(Set<Path> paths) throws IOException {
        return paths.stream()
                .map(entry -> {
                    log.debug(entry.toString());
                    String encodedFileName = decodePathSegment.apply(entry.getFileName().toString());
                    return new Video(encodedFileName, entry.toString(), SOURCE.LOCAL);
                })
                .collect(Collectors.toSet());
    }

    private Set<Audio> createMusicEntities(Set<Path> paths) throws IOException {
        return paths.stream()
                .map(entry -> {
                    log.debug(entry.toString());
                    String encodedFileName = decodePathSegment.apply(entry.getFileName().toString());
                    return new Audio(encodedFileName, entry.toString());
                })
                .collect(Collectors.toSet());
    }
}
