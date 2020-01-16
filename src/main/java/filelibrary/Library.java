package filelibrary;

import java.util.List;

/**
 * Ein Interface das, im weitesten Sinne, das Verzeichnis darstellt, in welches
 * der Benutzer (oder der anonyme öffentliche Benutzer) seine Dateien speichert
 */
public interface Library {
    /**
     * Holds a maximum amount of 5 files to prevent
     * hard-drive littering
     */
    int max_capacity = 5;

    String getPath();

    String getName();

    List<TorrentInFileSystem> getContents();

    void setContents(List<TorrentInFileSystem> torrents);

    void addTorrent(TorrentInFileSystem torrent);

    /**
     * Nach der Dateigröße soll jede Library geordnet werden können
     *
     * @return: gibt die geordnete liste zurück
     */
    List<String> sortByFileSize();

    /**
     * Ein Operator wird übergeben, nach dem dann geordnet wird
     *
     * @param sortOperator: der "Sort-Operator" (ein Kriterium)
     * @return
     */
    default List<String> sortByOperator(SortOperator sortOperator) {
        return null;
    }

    /**
     * Faktoren, nach denen eine Dateiliste geordnet werden kann
     */
    enum SortOperator {
        NAME,
        SIZE,
        PEERS,
        SEEDERS,
        LEECHERS;

        SortOperator() {
        }
    }
}
