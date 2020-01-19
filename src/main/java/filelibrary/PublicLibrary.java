package filelibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>this class implements the interface Library which enables it to store torrent files as an image-like
 * transition from the actual windows file system. it solely implements its methods but is not built out in
 * any way</p>
 *
 * <p>if you want to further expand or build this class please consider the native java file implementations
 * or consider relying on a open-source solution from the web instead.</p>
 * {@inheritDoc}
 * @author Pichler Martin
 * @since summer 2019
 * @see java.nio.file.FileSystem
 */
public class PublicLibrary implements Library {
    String path;
    String name;
    private List<TorrentInFileSystem> contents = new ArrayList<>();

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public String getPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public List<TorrentInFileSystem> getContents() {
        return contents;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public void setContents(List<TorrentInFileSystem> torrents) {
        this.contents = torrents;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public void addTorrent(TorrentInFileSystem torrent) {
        this.contents.add(torrent);
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     * @return: returns a sorted list, according to the file size
     */
    @Override
    public List<String> sortByFileSize() {
        return null;
    }
}
