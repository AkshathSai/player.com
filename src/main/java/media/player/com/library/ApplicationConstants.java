package media.player.com.library;

public final class ApplicationConstants {

    public static final String LOCAL_MEDIA = "LOCAL";
    public static final String TORRENT = "TORRENT";
    public static final String MUSIC = "Music";
    public static final String YIFY_TRACKERS = "&tr=http://track.one:1234/announce&tr=udp://glotorrents.pw:6969/announce&tr=udp://tracker.opentrackr.org:1337/announce&tr=udp://torrent.gresille.org:80/announce&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.coppersurfer.tk:6969&tr=udp://tracker.leechers-paradise.org:6969&tr=udp://p4p.arenabg.ch:1337&tr=udp://tracker.internetwarriors.net:1337";
    public static final String UDP_TRACKERS = "&tr=udp://coppersurfer.tk:6969/announce&tr=udp://tracker.trackerfix.com:80/announce&tr=udp://open.demonii.com:1337/announce&tr=udp://tracker.openbittorrent.com:80/announce&tr=udp://glotorrents.pw:6969/announce&tr=udp://tracker.istole.it:80/announce&tr=udp://tracker.publicbt.com:80/announce&tr=http://tracker.trackerfix.com/announce&tr=udp://glotorrents.com:6969/announce";
    public static final int[] ports = new int[] {6891, 6892};

    public static final String VIDEO = "/video";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int CHUNK_SIZE = 314700;
    public static final int BYTE_RANGE = 1024;
    public static final Long ChunkSize = 1000000L;

    private ApplicationConstants() {

    }
}
