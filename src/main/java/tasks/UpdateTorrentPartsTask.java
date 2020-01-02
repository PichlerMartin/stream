package tasks;

import bt.metainfo.TorrentFile;
import javafx.concurrent.Task;

import java.util.HashMap;

import static java.lang.String.join;

/**
 * https://stackoverflow.com/questions/33614564/live-updating-listview-javafx
 */
public class UpdateTorrentPartsTask extends Task<HashMap<String, Boolean>> {

    private HashMap<String, Boolean> torrentparts = new HashMap<>();

    public UpdateTorrentPartsTask(TorrentFile name, Boolean decision) {
        torrentparts.put(join("/", name.getPathElements()), decision);
    }

    @Override
    public HashMap call() throws Exception {
        return this.torrentparts;
    }
}