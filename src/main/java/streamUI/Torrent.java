package streamUI;

/**
 * <p>this class represents a virtual value of a torrent object. it serves to demonstrate the capabilities
 * of the torrent-file model which is used to download torrent files from a network of torrent-trackers which
 * are connected over anonymous virtual private networks</p>
 *
 * <p>it contains several private fields which are described more accurately in their unique getter and
 * setter methods</p>
 *
 * <p>this class was initially thought as a theoretical object, more like a abstract class or an interface
 * than an actual class object, but this step was not pursued further after some concrete evaluation. the
 * development of this class is currently set out and is scheduled to resume after the first release in
 * april 2020</p>
 *
 * @see "https://en.wikipedia.org/wiki/BitTorrent_tracker"
 * @see "https://de.wikipedia.org/wiki/Virtual_Private_Network"
 * @see bt.metainfo.Torrent
 * @author TopeinerMarcel
 * @since summer 2019
 */
@SuppressWarnings("unused")
public class Torrent {

    private String number;
    private String status;
    private String name;
    private String progress;
    private String size;

    /**
     * <p>the overloaded default constructor of the Torrent class. it uses five (5) parameters which are
     * described below. it is used to create a new instance of Torrent</p>
     * @param number the consecutive number of the torrent file
     * @param status the current status of the torrent file, typ String may be replaced by an enum or a nested class
     * @param name the name of the torrent file
     * @param progress an unspecified progress of a process, e.g. download or upload
     * @param size the size of the torrent as string
     * @see bt.metainfo.Torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public Torrent (String number, String status, String name, String progress, String size) {
        this.number = number;
        this.status = status;
        this.name = name;
        this.progress = progress;
        this.size = size;
    }

    /**
     * <p>getter method for the consecutive number of the torrent</p>
     * @return the number of the torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public String getNumber() {
        return number;
    }

    /**
     * <p>getter method for the current status of the torrent</p>
     * @return the status of the torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public String getStatus() {
        return status;
    }

    /**
     * <p>sets a new status for the torrent of this class</p>
     * @param status the new status
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * <p>getter method for the name of the torrent</p>
     * @return the name of the torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public String getName() {
        return name;
    }

    /**
     * <p>sets a new name for the torrent of this class</p>
     * @param name the new name
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>getter method for the progress of the torrent</p>
     * @return the progress of the torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public String getProgress() {
        return progress;
    }

    /**
     * <p>sets a new progress status for the torrent of this class</p>
     * @param progress the new status
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }

    /**
     * <p>getter method for the size of the torrent</p>
     * @return the size of the torrent
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public String getSize() {
        return size;
    }

    /**
     * <p>sets a new size for the torrent of this class</p>
     * @param size the new size
     * @author TopeinerMarcel
     * @since summer 2019
     */
    public void setSize(String size) {
        this.size = size;
    }
}