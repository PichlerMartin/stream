package support;

import bt.metainfo.TorrentFile;

import java.util.HashMap;

/**
 * <p>a helper class for the storage of the single parts of a torrent file. when this
 * class is instantiated it accepts one object of {@link TorrentFile} and a boolean variable
 * called decision which says whether this torrent part should be downloaded</p>
 * @author PichlerMartin
 * @since summer 2019
 */
public abstract class TorrentParts {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static HashMap<TorrentFile, Boolean> PARTS = new HashMap<>();

    /**
     * <p>this method takes one object of {@link TorrentFile}, which represents a torrent part, and
     * a boolean variable which represents a decision on whether this part should be downloaded, and
     * puts them into the private static hash-map where all parts of a torrent and their accompanying
     * decision, "download-markers" are stored.</p>
     * @param name the name of the torrent part
     * @param decision boolean representing the decision, if this part should be downloaded
     * @author PichlerMartin
     * @since summer 2019
     */
    public static void setPARTS(TorrentFile name, boolean decision) {
        TorrentParts.PARTS.put(name, decision);
    }
}
