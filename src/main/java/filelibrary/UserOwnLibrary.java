package filelibrary;

import java.util.List;

public class UserOwnLibrary implements Library {
    String path;
    String owner;
    String name;

    @Override
    public List<String> sortByFileSize() {
        return null;
    }

    @Override
    public List<String> sortByOperator(SortOperator sortOperator) {
        return null;
    }
}
