package media.player.com.library;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Library {

    public final ConcurrentHashMap<String, Video> videos = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Audio> audioSet = new ConcurrentHashMap<>();

    // Reactive sinks for broadcasting video and audio updates
    private final Sinks.Many<String> videoSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<String> audioSink = Sinks.many().multicast().onBackpressureBuffer();

    // Flux that subscribers consume for video updates
    public Flux<String> getVideoUpdates() {
        return videoSink.asFlux();
    }

    // Flux that subscribers consume for audio updates
    public Flux<String> getAudioUpdates() {
        return audioSink.asFlux();
    }

    // Fetch all video names
    public Flux<String> getVideos() {
        return Flux.fromIterable(videos.keySet());
    }

    // Add a video and publish the event
    public void addVideo(Video video) {
        videos.put(video.name(), video);
        videoSink.tryEmitNext("Video added: " + video.name());
    }

    // Add an audio file and publish the event
    public void addAudio(Audio audio) {
        audioSet.put(audio.name(), audio);
        audioSink.tryEmitNext("Audio added: " + audio.name());
    }

    // Get a video by its name
    public Video getVideo(String fileName) {
        return videos.get(fileName);
    }

    // Get the file path of a video
    public String getVideoFilePath(String fileName) {
        Video video = videos.get(fileName);
        return video != null ? video.path() : null;
    }

    // Get the file path of an audio file
    public String getAudioFilePath(String fileName) {
        Audio audio = audioSet.get(fileName);
        return audio != null ? audio.path() : null;
    }

    // Remove a video and publish the event
    public void removeVideo(String fileName) {
        videos.remove(fileName);
        videoSink.tryEmitNext("Video removed: " + fileName);
    }

    // Remove an audio file and publish the event
    public void removeAudio(String fileName) {
        audioSet.remove(fileName);
        audioSink.tryEmitNext("Audio removed: " + fileName);
    }
}

record Video(String name, String path, SOURCE source) implements Media, Serializable {
}

record Audio(String name, String path) implements Media, Serializable {
}

enum SOURCE {
    LOCAL, WEB
}