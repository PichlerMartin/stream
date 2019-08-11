package filelibrary;

import java.util.List;

public class PublicLibrary implements Library {
    String path;
    String name;

    /**
     * Description
     * Nach der Dateigröße soll jede Library geordnet werden können
     *
     * @return: gibt die geordnete liste zurück
     */
    @Override
    public List<String> sortByFileSize() {
        return null;
    }
}
