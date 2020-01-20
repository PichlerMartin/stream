package filelibrary;

import java.util.List;

/**
 * <p>this class was created with the purpose to serve as a file-system for torrent-files, similar
 * to {@link PublicLibrary}, but with the modification of assigning a private field containing a
 * user name, which specifies a unique person, who has access to this library</p>
 *
 * <p></p>
 * {@inheritDoc}
 * @author Pichler Martin
 * @since summer 2019
 * @see java.nio.file.FileSystem
 * @see PublicLibrary
 */
@SuppressWarnings("unused")
public class UserOwnLibrary implements Library {
    private String path;
    private String owner;
    private String name;
    private List<TorrentInFileSystem> contents;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     * @author Pichler Martin
     * @since summer 2019
     */
    @Override
    public String getName() {
        return name;
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
     */
    @Override
    public List<String> sortByFileSize() {
        return null;
    }
}
