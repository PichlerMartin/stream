package meta;

import java.nio.file.Paths;

public class Globals {
    /**
     * Testing-paths
     */
    public static String TORRENT_FILE = "src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";
    public static String ABSOLUTE_PATH_TORRENT_FILE = "C:\\Users\\Pichler Martin\\Desktop\\BlueSoft\\Diplomarbeit\\stream\\src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";
    public static String DOWNLOAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "Downloads").toString();
    public static String MAGNET_LINK = "magnet:?xt=urn:btih:eed0984ee5d0d9d150d6b1a0e7e57e0b8ef7e61e&dn=KNOPPIX_V8.2-2018-05-10-EN";

    /**
     * Conditional varibales
     */
    public static Boolean DIRECTORY_SELECTED = false;
    public static Integer PORT = -1;
    public static boolean USE_DEFAULT_PORT = true;
    public static Boolean DOWNLOAD_ALL = true;
    public static Boolean USE_MAGNET_LINK = true;
    public static Boolean USE_TORRENT_FILE = false;
    public static Boolean SEED_AFTER_DOWNLOAD = false;

    /**
     * String-formatters
     */
    public static final String FORMAT_DOWNLOADING_VERBOSE = "Downloading from %3d peers... Ready: %.2f%%, Target: %.2f%%,  Down: %s, Up: %s, Elapsed: %s, Remaining: %s";
    public static final String FORMAT_SEEDING = "Download is complete, seeding to %d peers... Up: %s";
    public static final String FORMAT_DOWNLOADING_SILENT = "Downloading %s (%,d B)";
}
