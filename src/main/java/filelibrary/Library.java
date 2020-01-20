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

    /**
     * <p>gives back the root path of the current file library. use this path to access all
     * subsidary elements stored in the library directory.</p>
     * @return a list of {@link TorrentInFileSystem}
     * @author Pichler Martin
     * @since summer 2019
     */
    String getPath();

    /**
     * <p>gives back the name of the root path of current file library. use this name to
     * display it on different output-elements such as labels, command-line messages and
     * so on.</p>
     * @return a list of {@link TorrentInFileSystem}
     * @author Pichler Martin
     * @since summer 2019
     */
    String getName();

    /**
     * <p>gives back the current torrent file-list which is stored inside the ArrayList
     * of the file-library.</p>
     *
     * @see java.util.ArrayList
     * @return a list of {@link TorrentInFileSystem}
     * @author Pichler Martin
     * @since summer 2019
     */
    List<TorrentInFileSystem> getContents();

    /**
     * <p>sets a whole list of torrent files as the current list. through this method one can
     * bulk assign a list of torrent file into a newly created file-library</p>
     * @param torrents a whole list of torrent file
     * @author Pichler Martin
     * @since summer 2019
     */
    void setContents(List<TorrentInFileSystem> torrents);

    /**
     * <p>adds a torrent file to the current list of files. the torrent file is inserted into
     * the current ArrayList of files stored.</p>
     * @param torrent a torrent file
     * @see TorrentInFileSystem
     * @see java.util.ArrayList
     * @author Pichler Martin
     * @since summer 2019
     */
    void addTorrent(TorrentInFileSystem torrent);

    /**
     * <p>Nach der Dateigr&ouml;&szlig;e soll jede Library geordnet werden k&ouml;nnen</p>
     * <p>the current list of files should be sorted according to file-size. this is especially
     * helpful if one needs a evaluation of the used disk-space.</p>
     *
     * @return: returns a sorted list, according to the file size
     *
     * @author Pichler Martin
     * @since summer 2019
     */
    List<String> sortByFileSize();

    /**
     * <p>Ein Operator wird &uuml;bergeben, nach dem dann geordnet wird</p>
     * <p>the list of current torrent files is being sorted according to a
     * certain sort operator, which can either be LEECHERS, NAME, PEERS SEEDERS or SIZE</p>
     *
     * @param sortOperator: sort operator, criteria according to which is sorted {@link SortOperator}
     * @return a sorted list
     * @see SortOperator
     *
     * @author Pichler Martin
     * @since summer 2019
     */
    default List<String> sortByOperator(SortOperator sortOperator) {
        return null;
    }

    /**
     * <p>Faktoren, nach denen eine Dateiliste geordnet werden kann</p>
     * <p>parameters which can be used to sort a list of files. those consist of LEECHERS,
     * NAME, PEERS, SEEDERS and SIZE</p>
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
