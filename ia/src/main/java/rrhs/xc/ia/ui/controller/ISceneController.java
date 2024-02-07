package rrhs.xc.ia.ui.controller;

import javafx.scene.Scene;

/**
 * This interface describes a contract stating that the implementing class is a
 * FXML controller, and that the controller will provide the scene it is running
 * under. This can be done by calling the <code>getScene()</code> method of any
 * FXML object.
 */
public interface ISceneController {

    public Scene getScene();

}
