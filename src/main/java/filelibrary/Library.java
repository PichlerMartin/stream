package filelibrary;

import java.util.List;

/**
 * <p>Ein Interface das, im weitesten Sinne, das Verzeichnis darstellt, in welches
 * der Benutzer (oder der anonyme öffentliche Benutzer) seine Dateien speichert</p>
 *
 * <p>interface, which in the furthest way, should display a virtual directory, which
 * should help the user (or the anonymous public user) to store their data</p>
 *
 * @author Pichler Martin
 * @since summer 2019
 */
@SuppressWarnings("unused")
public interface Library {
    /**
     * <p>Holds a maximum amount of 5 files to prevent
     * hard-drive littering</p>
     *
     * @since summer 2019
     */
    int max_capacity = 5;

    String getPath();

    String getName();

    /**
     * <p>gives back the current torrent file-list</p>
     * @return a list of {@link TorrentInFileSystem}
     * @author Pichler Martin
     * @since summer 2019
     */
    List<TorrentInFileSystem> getContents();

    /**
     * <p>sets a whole list of torrent files as the current list</p>
     * @param torrents a whole list of torrent file
     * @author Pichler Martin
     * @since summer 2019
     */
    void setContents(List<TorrentInFileSystem> torrents);

    /**
     * <p>adds a torrent file to the current list of files</p>
     * @param torrent a torrent file
     * @see TorrentInFileSystem
     * @author Pichler Martin
     * @since summer 2019
     */
    void addTorrent(TorrentInFileSystem torrent);

    /**
     * <p>Nach der Dateigr&ouml;&szlig;e soll jede Library geordnet werden k&ouml;nnen</p>
     * <p>the current list of files should be sorted according to file-size</p>
     *
     * @return: the current list of sorted files
     *
     * @author Pichler Martin
     * @since summer 2019
     */
    List<String> sortByFileSize();

    /**
     * <p>Ein Operator wird &uuml;bergeben, nach dem dann geordnet wird</p>
     * <p>the list of current torrent files is being sorted according to a
     * certain sort operator</p>
     *
     * @param sortOperator: sort operator, criteria according to which is sorted {@link SortOperator}
     * @return a sorted list
     *
     * @author Pichler Martin
     * @since summer 2019
     */
    default List<String> sortByOperator(SortOperator sortOperator) {
        return null;
    }

    /**
     * <p>Faktoren, nach denen eine Dateiliste geordnet werden kann</p>
     * <p>parameters which can be used to sort a list of files</p>
     *
     * @see Enum
     * @see Comparable
     *
     * @author Pichler Martin
     * @since summer 2019
     */
    enum SortOperator {
        LEECHERS,
        NAME,
        PEERS,
        SEEDERS,
        SIZE;

        SortOperator() {
        }
    }
}
