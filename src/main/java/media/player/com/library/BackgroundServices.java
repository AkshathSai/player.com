package media.player.com.library;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class BackgroundServices {

    @Lazy
    @Autowired
    RuntimeHelper runtimeHelper;

    @Lazy
    @Autowired
    LocalMediaIndexer localMediaIndexer;

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        try {
            log.info("Indexing user content");
            localMediaIndexer.indexLocalMedia(runtimeHelper.getMoviesFolderPath(), runtimeHelper.getMusicFolderPath(), runtimeHelper.getDownloadsFolderPath());
        } catch (IOException e) {
            log.error("Error indexing local media", e);
        }
    }

}
