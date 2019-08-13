package filelibrary;

import java.util.List;

/**
 * Description
 * Ein Interface das, im weitesten Sinne, das Verzeichnis darstellt, in welches
 * der Benutzer (oder der anonyme öffentliche Benutzer) seine Dateien speichert
 */
public interface Library {
    /**
     * Holds a maximum amount of 5 files to prevent
     * hard-drive littering
     */
    int max_capacity = 5;

    public String getPath();

    public String getName();

    public List<TorrentInFileSystem> getContents();

    public void setContents(List<TorrentInFileSystem> torrents);

    public void addTorrent(TorrentInFileSystem torrent);

    /**
     * Description
     * Nach der Dateigröße soll jede Library geordnet werden können
     * @return: gibt die geordnete liste zurück
     */
    List<String> sortByFileSize();

    /**
     * Description
     * Ein Operator wird übergeben, nach dem dann geordnet wird
     * @param sortOperator: der "Sort-Operator" (ein Kriterium)
     * @return
     */
    default List<String> sortByOperator(SortOperator sortOperator){
        return null;
    }

    /**
     * Description
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
