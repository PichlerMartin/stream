package download;

/**
 * <p>this class provides baisc information about the download rate of the current torrent
 * file. it contains the static fields quantity, measureUnit and bytes</p>
 *
 * @author PichlerMartin
 */
public class DownloadRate {
    private final double quantity;
    private final String measureUnit;
    private final long bytes;

    /**
     * <p>formats download rate depending on the amount, into either kilobytes, megabytes or
     * bytes. measureUnit is the unit, bytes the amount in bytes and quantity the amount after
     * applying the set unit</p>
     * @param x_rate delta rate in bytes
     */
    public DownloadRate(long x_rate) {
        if (x_rate < 0L) {
            x_rate = 0L;
            this.quantity = 0.0D;
            this.measureUnit = "B";
        } else if (x_rate < 1024L) {
            this.quantity = (double) x_rate;
            this.measureUnit = "B";
        } else if (x_rate < 1048576L) {
            this.quantity = (double) (x_rate / 1024L);
            this.measureUnit = "KB";
        } else {
            this.quantity = (double) x_rate / 1048576.0D;
            this.measureUnit = "MB";
        }

        this.bytes = x_rate;
    }

    /**
     * <p>measure unit can be B, KB or MB</p>
     * @return measure unit
     */
    public String getMeasureUnit() {
        return measureUnit;
    }

    /**
     * <p>returns the download rate in bytes</p>
     * @return download rate
     */
    public long getBytes() {
        return bytes;
    }

    /**
     * @return quantity, adjusted to measure unit
     */
    public double getQuantity() {
        return quantity;
    }
}
