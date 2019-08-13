package filelibrary;

import java.util.List;

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

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<TorrentInFileSystem> getContents() {
        return contents;
    }

    @Override
    public void setContents(List<TorrentInFileSystem> torrents) {
        this.contents = torrents;
    }

    @Override
    public void addTorrent(TorrentInFileSystem torrent) {
        this.contents.add(torrent);
    }

    @Override
    public List<String> sortByFileSize() {
        return null;
    }

    @Override
    public List<String> sortByOperator(SortOperator sortOperator) {
        return null;
    }
}
