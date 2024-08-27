package media.player.com.library;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Getter
@Setter
@Service
public class RuntimeHelper {

    private final String userHomePath = System.getProperty("user.home");
    private final String downloadsFolderPath;
    private final String musicFolderPath;
    private final String moviesFolderPath;
    private final String musicContentStore;
    private final String moviesContentStore;

    RuntimeHelper() {
        String os = System.getProperty("os.name").toLowerCase();
        log.info("OS {}", os);

        if (os.contains("win")) {
            downloadsFolderPath = userHomePath + File.separator + "Downloads";
            musicFolderPath = userHomePath + File.separator + ApplicationConstants.MUSIC;
            moviesFolderPath = userHomePath + File.separator + "Videos";
            musicContentStore = ApplicationConstants.MUSIC + File.separator;
            moviesContentStore = "Videos" + File.separator;
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
            //Need to revisit for linux
            downloadsFolderPath = userHomePath + File.separator + "Downloads";
            musicFolderPath = userHomePath + File.separator + ApplicationConstants.MUSIC;
            moviesFolderPath = userHomePath + File.separator + "Movies";
            musicContentStore = ApplicationConstants.MUSIC + File.separator;
            moviesContentStore = "Movies" + File.separator;
        } else {
            log.info("Unknown OS file folder structure! Using user.home");
            downloadsFolderPath = userHomePath;
            musicFolderPath = userHomePath;
            moviesFolderPath = userHomePath;
            musicContentStore = userHomePath;
            moviesContentStore = userHomePath;
        }

        log.info("Final locations paths");
        log.info("Downloads: {}", downloadsFolderPath);
        log.info("MUSIC: {}", musicContentStore);
        log.info("MOVIES: {}", moviesContentStore);
    }

}
