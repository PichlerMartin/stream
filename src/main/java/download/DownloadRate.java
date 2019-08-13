package download;

public class DownloadRate {
    private final double quantity;
    private final String measureUnit;
    private final long bytes;

    public String getMeasureUnit() {
        return measureUnit;
    }

    public long getBytes() {
        return bytes;
    }

    public double getQuantity() {
        return quantity;
    }


    public DownloadRate(long delta) {
        if (delta < 0L) {
            delta = 0L;
            this.quantity = 0.0D;
            this.measureUnit = "B";
        } else if (delta < 1024L) {
            this.quantity = (double)delta;
            this.measureUnit = "B";
        } else if (delta < 1048576L) {
            this.quantity = (double)(delta / 1024L);
            this.measureUnit = "KB";
        } else {
            this.quantity = (double)delta / 1048576.0D;
            this.measureUnit = "MB";
        }

        this.bytes = delta;
    }
}
