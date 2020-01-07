package tasks;

import javafx.concurrent.Task;

import java.util.HashMap;

/**
 * https://stackoverflow.com/questions/33614564/live-updating-listview-javafx
 */
public class UpdateTorrentPartsTask extends Task<HashMap<String, Boolean>> {

    private HashMap<String, Boolean> torrentparts = new HashMap<>();

    public UpdateTorrentPartsTask(HashMap<String, Boolean> torrentparts) {
        this.torrentparts.putAll(torrentparts);
    }

    @Override
    public HashMap<String, Boolean> call() {
        return this.torrentparts;
    }
}