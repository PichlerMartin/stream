package streamUI;

public class Torrent {

    private String number = null;
    private String status = null;
    private String name = null;
    private String progress = null;
    private String size = null;

    public Torrent (String number, String status, String name, String progress, String size) {
        this.number = number;
        this.status = status;
        this.name = name;
        this.progress = progress;
        this.size = size;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}