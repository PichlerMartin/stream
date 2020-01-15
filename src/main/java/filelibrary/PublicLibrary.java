package filelibrary;

import java.util.ArrayList;
import java.util.List;

public class PublicLibrary implements Library {
    String path;
    String name;
    private List<TorrentInFileSystem> contents = new ArrayList<>();

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
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

    /**
     * Nach der Dateigröße soll jede Library geordnet werden können
     *
     * @return: gibt die geordnete liste zurück
     */
    @Override
    public List<String> sortByFileSize() {
        return null;
    }
}
