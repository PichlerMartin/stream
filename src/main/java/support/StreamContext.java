package support;

import testui.Controller;
import testui.UI_Controller;

/**
 * StreamContext ist eine Klasse, die dazu gedacht ist als "globaler" Datenspeicher zu wirken.
 * Das heißt, der ursprüngliche Ersteller hatte die Intention einen globalen Controller zu
 * verwenden, um so Labels mit Statusmeldungen zu füttern.
 */
public class StreamContext {
    private final static StreamContext instance = new StreamContext();
    private Controller controller = new UI_Controller();

    public static StreamContext getInstance() {
        return instance;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller currentController() {
        return controller;
    }
}