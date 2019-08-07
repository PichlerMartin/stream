package filelibrary;

import bt.metainfo.Torrent;
import bt.metainfo.TorrentFile;
import bt.metainfo.TorrentId;
import bt.metainfo.TorrentSource;
import bt.tracker.AnnounceKey;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class StreamTorrent implements Torrent {
    @Override
    public TorrentSource getSource() {
        return null;
    }

    @Override
    public Optional<AnnounceKey> getAnnounceKey() {
        return Optional.empty();
    }

    @Override
    public TorrentId getTorrentId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public long getChunkSize() {
        return 0;
    }

    @Override
    public Iterable<byte[]> getChunkHashes() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public List<TorrentFile> getFiles() {
        return null;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public Optional<Instant> getCreationDate() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.empty();
    }
}
