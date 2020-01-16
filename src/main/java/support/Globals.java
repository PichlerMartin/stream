package support;

import java.nio.file.Paths;

/**
 * <p>Within this class are some global-like constants declared which serve as default values during
 * the testing phase. Below String-formatters are the string formatting patterns which are applied
 * on the console output of the application. Conditional variables are also global variables
 * which are ultimately used in creating a object of {@link client.StreamClient#main(String[])}  StreamClient}
 * </p>
 *
 * @author  PichlerMartin
 * @since   0.01.4
 */
@SuppressWarnings("all")
public class Globals {
    //region String-formatters
    /**
     * this is a formatter string for the display of the downloading stats, e.g. peer count and download speed
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static final String FORMAT_DOWNLOADING_VERBOSE = "Downloading from %3d peers... Ready: %.2f%%, Target: %.2f%%,  Down: %s, Up: %s, Elapsed: %s, Remaining: %s";
    /**
     * this is a formatter string for the display of the seeding state
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static final String FORMAT_SEEDING = "Download is complete, seeding to %d peers... Up: %s";
    /**
     * this is a formatter string for the display of minimal dowloading stats
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static final String FORMAT_DOWNLOADING_SILENT = "Downloading %s (%,d B)";
    /**
     * this is a formatter string for the display of the file-selection in the command line
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static final String FORMAT_DOWNLOAD_PART = "Download '%s'? (hit <Enter> or type 'y' to confirm or type 'n' to skip)";
    /**
     * this is a formatter string for the display of the "invalid key pressed" message
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static final String FORMAT_ILLEGAL_KEYPRESS = "*** Invalid key pressed. Please, use only <Enter>, 'y' or 'n' ***";
    //endregion String-formatters

    //region Global constants

    /**
     * first testing string for .torrent-file testing
     *
     * @author  PichlerMartin
     * @since   0.01.4
     * {@value #TORRENT_FILE}
     */
    public static String TORRENT_FILE = "src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";

    /**
     * first testing string for .torrent-file testing, absolute path
     *
     * @author  PichlerMartin
     * @since   0.01.4
     * {@value #ABSOLUTE_PATH_TORRENT_FILE}
     */
    public static String ABSOLUTE_PATH_TORRENT_FILE = "C:\\Users\\Pichler Martin\\Desktop\\BlueSoft\\Diplomarbeit\\stream\\src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";

    /**
     * second testing string for .torrent-file testing
     *
     * @author  PichlerMartin
     * @since   0.01.5
     * {@value #TORRENT_FILE2}
     */
    public static String TORRENT_FILE2 = "src\\main\\resources\\torrents\\A97DE1B7DA9E60BD3C5E23D850C0406A5D0AA1AF.torrent";

    /**
     * second testing string for .torrent-file testing, absolute path
     *
     * @author  PichlerMartin
     * @since   0.01.5
     * {@value #ABSOLUTE_PATH_TORRENT_FILE2}
     */
    public static String ABSOLUTE_PATH_TORRENT_FILE2 = "C:\\Users\\Pichler Martin\\Desktop\\BlueSoft\\Diplomarbeit\\stream\\src\\main\\resources\\torrents\\A97DE1B7DA9E60BD3C5E23D850C0406A5D0AA1AF.torrent";

    /**
     * default download directory, links to user's download directory
     *
     * @author  PichlerMartin
     * @since   0.01.4
     * {@value #DOWNLOAD_DIRECTORY}
     */
    public static String DOWNLOAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "Downloads").toString();

    /**
     * first magnet link fr testing purposes, contains KNOPPIX linux distribution
     *
     * @author  PichlerMartin
     * @since   0.01.4
     * {@value #MAGNET_LINK}
     */
    public static String MAGNET_LINK = "magnet:?xt=urn:btih:eed0984ee5d0d9d150d6b1a0e7e57e0b8ef7e61e&dn=KNOPPIX_V8.2-2018-05-10-EN";

    /**
     * second magnet link for testing purposes, contains illegal software
     *
     * @author  PichlerMartin
     * @since   0.01.5
     * {@value #MAGNET_LINK2}
     */
    public static String MAGNET_LINK2= "magnet:?xt=urn:btih:a97de1b7da9e60bd3c5e23d850c0406a5d0aa1af&dn=Betternet+VPN+For+Windows+5.3.0.433+Premium+Pre-Activated%5BBabuPC&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969";
    //endregion Global constants

    //region Global conditional variables
    /**
     * boolean for if directory is already selected
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static Boolean DIRECTORY_SELECTED = false;

    /**
     * boolean for custom port, default -1 if standard port
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static Integer PORT = -1;

    /**
     * boolean for if default port should be used
     *
     * @author  PichlerMartin
     * @since   0.01.4
     */
    public static Boolean USE_DEFAULT_PORT = true;

    /**
     * boolean for if all files should be downloaded
     *
     * @author  PichlerMartin
     * @since   0.01.5
     */
    public static Boolean DOWNLOAD_ALL = true;

    /**
     * boolean for if magnet uri/link should be used for downloading
     *
     * @author  PichlerMartin
     * @since   0.01.5
     */
    public static Boolean USE_MAGNET_LINK = true;

    /**
     * boolean for if .torrent-file should be used for downloading
     *
     * @author  PichlerMartin
     * @since   0.01.5
     */
    public static Boolean USE_TORRENT_FILE = false;

    /**
     * boolean for if should seed after download
     *
     * @author  PichlerMartin
     * @since   0.01.5
     */
    public static Boolean SEED_AFTER_DOWNLOAD = false;
    //endregion Global conditional variables
}