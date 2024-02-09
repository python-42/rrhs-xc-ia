package rrhs.xc.ia.ui;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.util.Pair;
import rrhs.xc.ia.ui.controller.SceneController;

public class SceneCollection {
    
    private static SceneCollection INSTANCE = null;

    private HashMap<String, Pair<Scene, SceneController>> map = new HashMap<String, Pair<Scene, SceneController>>();

    private SceneCollection() {}

    public static SceneCollection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SceneCollection();
        }
        return INSTANCE;
    }

    public Pair<Scene, SceneController> get(String s) {
        return map.get(s);
    }

    public Scene getScene(String s) {
        return map.get(s).getKey();
    }

    public SceneController getController(String s) {
        return map.get(s).getValue();
    }

    public void put(String s, Scene sc, SceneController c) {
        map.put(s, new Pair<Scene,SceneController>(sc, c));
    }
}
