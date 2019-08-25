package support;

import testui.Controller;
import testui.UI_Controller;

public class StreamContext {
    private final static StreamContext instance = new StreamContext();

    public static StreamContext getInstance() {
        return instance;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private Controller controller = new UI_Controller();

    public Controller currentController() {
        return controller;
    }
}