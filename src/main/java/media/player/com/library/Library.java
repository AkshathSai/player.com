package media.player.com.library;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Library {

    private final ConcurrentHashMap<String, Video> videos = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Audio> audioSet = new ConcurrentHashMap<>();

    public Flux<String> getVideos() {
        return Flux.fromIterable(videos.keySet());
    }

    public void addVideo(Video video) {
        videos.put(video.name(), video);
    }

    public void addAudio(Audio audio) {
        audioSet.put(audio.name(), audio);
    }

    public Video getVideo(String fileName) {
        return videos.get(fileName);
    }

    public String getVideoFilePath(String fileName) {
        Video video = videos.get(fileName);
        return video != null ? video.path() : null;
    }

    public String getAudioFilePath(String fileName) {
        Audio audio = audioSet.get(fileName);
        return audio != null ? audio.path() : null;
    }

    public void removeVideo(String fileName) {
        videos.remove(fileName);
    }

    public void removeAudio(String fileName) {
        audioSet.remove(fileName);
    }
}

record Video (String name, String path, SOURCE source) implements Media, Serializable {
}

record Audio (String name, String path) implements Media, Serializable {
}

enum SOURCE {
    LOCAL, WEB
}