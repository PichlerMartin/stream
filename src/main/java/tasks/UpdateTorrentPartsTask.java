package tasks;

import javafx.concurrent.Task;

import java.util.HashMap;

/**
 * <p>rump of Task-pattern, which should be used to update the future user interface where
 * the user is able to select certain parts of a torrent package. this will be implemented in
 * the future, but until then the current release needs overview.</p>
 *
 * @author PichlerMartin
 * @since january 2020
 * @see "https://stackoverflow.com/questions/33614564/live-updating-listview-javafx"
 * @deprecated is still under development
 */
@Deprecated
public class UpdateTorrentPartsTask extends Task<HashMap<String, Boolean>> {

    private HashMap<String, Boolean> TorrentParts = new HashMap<>();

    /**
     * <p>this constructor takes a hash-map of torrentparts and assigns them to the
     * according local</p>
     * @param torrentparts map of torrentparts
     */
    public UpdateTorrentPartsTask(HashMap<String, Boolean> torrentparts) {
        this.TorrentParts.putAll(torrentparts);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public HashMap<String, Boolean> call() {
        return this.TorrentParts;
    }
}