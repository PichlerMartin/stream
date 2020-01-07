package filelibrary;

public class TorrentInFileSystem {
    private final String path;
    private final String name;

    public TorrentInFileSystem(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}