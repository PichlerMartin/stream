package meta;

import bt.metainfo.TorrentFile;

import java.util.HashMap;

public abstract class TorrentParts {
    private static HashMap<TorrentFile, Boolean> PARTS = new HashMap<>();

    public static HashMap<TorrentFile, Boolean> getPARTS() {
        return PARTS;
    }

    public static void setPARTS(TorrentFile name, boolean decision) {
        TorrentParts.PARTS.put(name, decision);
    }
}
