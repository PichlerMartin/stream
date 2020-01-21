package support;

import testui.Controller;
import testui.UI_Controller;

/**
 * <p>StreamContext ist eine Klasse, die dazu gedacht ist als "globaler" Datenspeicher zu wirken.
 * Das hei&szlig;t, der urspr&uuml;ngliche Ersteller hatte die Intention einen globalen Controller zu
 * verwenden, um so Labels mit Statusmeldungen zu f&uuml;ttern.</p>
 *
 * <p>StreamContext is a class, which was initially thought out to be a global data-storage. it should
 * serve as a helper for controller classes and feed their label fields with data to display. this
 * thought process was already overthrown and the capabilities of a global data storage have been
 * carried out to {@link Globals}</p>
 *
 * @see javax.naming.Context
 * @author PichlerMartin
 * @since summer 2019
 */
public class StreamContext {
    private final static StreamContext instance = new StreamContext();
    private Controller controller = new UI_Controller();

    /**
     * <p>this getter method gives back a instance of the current context class in form of
     * a static field</p>
     * @return a instance of the static context class
     * @author PichlerMartin
     * @since summer 2019
     */
    public static StreamContext getInstance() {
        return instance;
    }

    /**
     * <p>this setter assigns a new controller to this StreamContext class</p>
     * @param controller a controller object of a fxml file
     * @author PichlerMartin
     * @since summer 2019
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * <p>gives back an reference to the with this context class associated controller</p>
     * @return a reference of the current associated controller
     * @author PichlerMartin
     * @since summer 2019
     */
    public Controller currentController() {
        return controller;
    }
}