package media.player.com.library;

import org.springframework.stereotype.Component;
import reactor.core.Disposable;

@Component
public class LibrarySubscriber {

    private final Library library;
    private Disposable videoSubscription;
    private Disposable audioSubscription;

    public LibrarySubscriber(Library library) {
        this.library = library;

        // Subscribe to video updates
        videoSubscription = library.getVideoUpdates()
                .subscribe(event -> System.out.println("Video Event: " + event));

        // Subscribe to audio updates
        audioSubscription = library.getAudioUpdates()
                .subscribe(event -> System.out.println("Audio Event: " + event));
    }

    // Cleanup resources when the service/component is destroyed
    public void cleanup() {
        if (videoSubscription != null) {
            videoSubscription.dispose();
        }
        if (audioSubscription != null) {
            audioSubscription.dispose();
        }
    }
}
