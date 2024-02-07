package rrhs.xc.ia.ui.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import rrhs.xc.ia.ui.event.SceneEvent;

public class RosterController implements ISceneController {

    @FXML
    private Button cancelBtn;

    @FXML
    public void initialize() {
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                cancelBtn.fireEvent(new SceneEvent("main"));
            }
            
        });
    }

    @Override
    public Scene getScene() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScene'");
    }
    
}
