package support;

import java.nio.file.Paths;

/**
 * <p>Within this class are some global-like constants declared which serve as default values during
 * the testing phase. Below String-formatters are the string formatting patterns which are applied
 * on the console output of the application. Conditional variables are also global variables
 * which are ultimately used in creating a object of {@link client.StreamClient StreamClient}
 * </p>
 *
 * @author  PichlerMartin
 * @since   0.00.5
 */
@SuppressWarnings("all")
public class Globals {
    /**
     * String-formatters
     */
    public static final String FORMAT_DOWNLOADING_VERBOSE = "Downloading from %3d peers... Ready: %.2f%%, Target: %.2f%%,  Down: %s, Up: %s, Elapsed: %s, Remaining: %s";
    public static final String FORMAT_SEEDING = "Download is complete, seeding to %d peers... Up: %s";
    public static final String FORMAT_DOWNLOADING_SILENT = "Downloading %s (%,d B)";
    public static final String FORMAT_DOWNLOAD_PART = "Download '%s'? (hit <Enter> or type 'y' to confirm or type 'n' to skip)";
    public static final String FORMAT_ILLEGAL_KEYPRESS = "*** Invalid key pressed. Please, use only <Enter>, 'y' or 'n' ***";
    /**
     * Testing-paths
     */
    public static String TORRENT_FILE = "src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";
    public static String ABSOLUTE_PATH_TORRENT_FILE = "C:\\Users\\Pichler Martin\\Desktop\\BlueSoft\\Diplomarbeit\\stream\\src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";
    public static String TORRENT_FILE2 = "src\\main\\resources\\torrents\\A97DE1B7DA9E60BD3C5E23D850C0406A5D0AA1AF.torrent";
    public static String ABSOLUTE_PATH_TORRENT_FILE2 = "C:\\Users\\Pichler Martin\\Desktop\\BlueSoft\\Diplomarbeit\\stream\\src\\main\\resources\\torrents\\A97DE1B7DA9E60BD3C5E23D850C0406A5D0AA1AF.torrent";
    public static String DOWNLOAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "Downloads").toString();
    public static String MAGNET_LINK = "magnet:?xt=urn:btih:eed0984ee5d0d9d150d6b1a0e7e57e0b8ef7e61e&dn=KNOPPIX_V8.2-2018-05-10-EN";
    public static String MAGNET_LINK2= "magnet:?xt=urn:btih:a97de1b7da9e60bd3c5e23d850c0406a5d0aa1af&dn=Betternet+VPN+For+Windows+5.3.0.433+Premium+Pre-Activated%5BBabuPC&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969";
    /**
     * Conditional varibales
     */
    public static Boolean DIRECTORY_SELECTED = false;
    public static Integer PORT = -1;
    public static Boolean USE_DEFAULT_PORT = true;
    public static Boolean DOWNLOAD_ALL = true;
    public static Boolean USE_MAGNET_LINK = true;
    public static Boolean USE_TORRENT_FILE = false;
    public static Boolean SEED_AFTER_DOWNLOAD = false;
}