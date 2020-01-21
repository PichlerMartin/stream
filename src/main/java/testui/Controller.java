package testui;

import javafx.scene.control.Label;

/**
 * <p>this interface was created with the purpose of providing a reference model for the
 * creation of future controller classes. it is intended to contain several methods which
 * should be implemented in the inheriting controller classes.</p>
 *
 * <p>the primary concept was thought out to provide a controller interface for every other
 * controller class except the main controller class {@link streamUI.UI_Controller_main_page}</p>
 * @see Select_Controller
 * @see UI_Controller
 * @see Controller
 * @author PichlerMartin
 * @since december 2019
 */
public interface Controller {
    /**
     * <p>this getter gives back a reference to the status label of this controller class,
     * where a status of the controller or the displayed content is stored</p>
     * @return the status label of this controller
     * @see Label
     * @author PichlerMartin
     * @since december 2019
     */
    Label getLabel();

    /**
     * <p>this setter accepts one object of the java fx label class and should set it to
     * one label of the inheriting controller class, for display of a status-display</p>
     * @param status a label field for the status of the controller, or the displayed content
     * @see Label
     * @author PichlerMartin
     * @since december 2019
     */
    void setLabel(Label status);
}
