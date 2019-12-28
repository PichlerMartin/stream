package meta;

import java.nio.file.Paths;

public class Globals {
    public static String TORRENT_FILE = "src\\main\\resources\\torrents\\KNOPPIX_V8.2-2018-05-10-EN.torrent";
    public static String DOWNLOAD_DIRECTORY = Paths.get(System.getProperty("user.home"), "Downloads").toString();
    public static String MAGNET_LINK = "magnet:?xt=urn:btih:eed0984ee5d0d9d150d6b1a0e7e57e0b8ef7e61e&dn=KNOPPIX_V8.2-2018-05-10-EN";
    public static Boolean DIRECTORY_SELECTED = false;
    public static Integer PORT = -1;
    public static boolean USE_DEFAULT_PORT = true;
    public static Boolean DOWNLOAD_ALL = true;
    public static Boolean SEED_AFTER_DOWNLOAD = false;
}
