package downloads;

import bt.metainfo.Torrent;
import bt.metainfo.TorrentFile;
import bt.metainfo.TorrentId;
import bt.metainfo.TorrentSource;
import bt.tracker.AnnounceKey;
import org.springframework.beans.*;
import org.aopalliance.aop.*;
import com.google.common.math.DoubleMath;
import javax.crypto.*;
import org.slf4j.*;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Entity for representating a "Download-Unit"
 */
public class Download implements Torrent{

    /**
     * @since 1.3
     */
    @Override
    public TorrentSource getSource() {
        return null;
    }

    private org.yaml.snakeyaml.composer.Composer cmp = new Composer(new Parser() {
        @Override
        public boolean checkEvent(Event.ID choice) {
            return false;
        }

        @Override
        public Event peekEvent() {
            return null;
        }

        @Override
        public Event getEvent() {
            return null;
        }
    }, new Resolver());

    /**
     * @return Announce key, or {@link Optional#empty()} for trackerless torrents
     * @since 1.1
     */
    @Override
    public Optional<AnnounceKey> getAnnounceKey() {
        return Optional.empty();
    }

    /**
     * @return Torrent ID.
     * @since 1.0
     */
    @Override
    public TorrentId getTorrentId() {
        return null;
    }

    /**
     * @return Suggested name for this torrent.
     * @since 1.0
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * @return Size of a chunk, in bytes.
     * @since 1.0
     */
    @Override
    public long getChunkSize() {
        return 0;
    }

    /**
     * @return Sequence of SHA-1 hashes of all chunks in this torrent.
     * @since 1.0
     */
    @Override
    public Iterable<byte[]> getChunkHashes() {
        return null;
    }

    /**
     * @return Total size of all chunks in this torrent, in bytes.
     * @since 1.0
     */
    @Override
    public long getSize() {
        return 0;
    }

    /**
     * @return Information on the files contained in this torrent.
     * @since 1.0
     */
    @Override
    public List<TorrentFile> getFiles() {
        return null;
    }

    /**
     * @return True if this torrent is private (see BEP-27)
     * @since 1.0
     */
    @Override
    public boolean isPrivate() {
        return false;
    }

    /**
     * @return Creation time of the torrent
     * @since 1.5
     */
    @Override
    public Optional<Instant> getCreationDate() {
        return Optional.empty();
    }

    /**
     * @return Creator of the torrent (usually name and version of the program used to create the .torrent file)
     * @since 1.5
     */
    @Override
    public Optional<String> getCreatedBy() {
        return Optional.empty();
    }
}
