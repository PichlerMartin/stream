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


    public DownloadRate(long x_rate) {
        if (x_rate < 0L) {
            x_rate = 0L;
            this.quantity = 0.0D;
            this.measureUnit = "B";
        } else if (x_rate < 1024L) {
            this.quantity = (double)x_rate;
            this.measureUnit = "B";
        } else if (x_rate < 1048576L) {
            this.quantity = (double)(x_rate / 1024L);
            this.measureUnit = "KB";
        } else {
            this.quantity = (double)x_rate / 1048576.0D;
            this.measureUnit = "MB";
        }

        this.bytes = x_rate;
    }
}
