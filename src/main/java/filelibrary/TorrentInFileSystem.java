package filelibrary;

/**
 * <p>this class represents a torrent file as it can be seen inside the file system of windows. it contains
 * the basic parameters such as path and name and the according getter and setter methods for these</p>
 *
 * <p>this classe is still pretty basic and is not actually used inside the actual project and should be expanded
 * if one considers using it further.</p>
 *
 * @see bt.metainfo.TorrentFile better implementation of a torrent file
 * @author Pichler Martin
 * @since summer 2019
 */
@SuppressWarnings("unused")
public class TorrentInFileSystem {
    private final String path;
    private final String name;

    /**
     * @param path the path of the torrent file
     * @param name the name of the torrent file
     * @author Pichler Martin
     * @since summer 2019
     */
    public TorrentInFileSystem(String path, String name) {
        this.path = path;
        this.name = name;
    }

    /**
     * @return the path of the torrent file
     * @author Pichler Martin
     * @since summer 2019
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the name of the torrent file
     * @author Pichler Martin
     * @since summer 2019
     */
    public String getName() {
        return name;
    }
}