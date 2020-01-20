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


    /**
     * <p>gives back the name of the library owner. this name is initialised when the file
     * library is created. the owner is the only person which can access the contents of
     * this specific library</p>
     * @return a the clear name of the library owner
     * @author Pichler Martin
     * @since summer 2019
     */
    public String getOwner() {
        return owner;
    }

    /**
     * <p>sets a new owner of the library. this method could be removed in a future update,
     * because it may not be necessary to updated the library owner. this option should be
     * evaluated more closely in the future.</p>
     * @author Pichler Martin
     * @since summer 2019
     */
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
