package rrhs.xc.ia.ui;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.util.Pair;
import rrhs.xc.ia.ui.controller.ISceneController;

public class SceneCollection {
    
    private static SceneCollection INSTANCE = null;

    private HashMap<String, Pair<Scene, ISceneController>> map = new HashMap<String, Pair<Scene, ISceneController>>();

    private SceneCollection() {}

    public static SceneCollection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SceneCollection();
        }
        return INSTANCE;
    }

    public Pair<Scene, ISceneController> get(String s) {
        return map.get(s);
    }

    public Scene getScene(String s) {
        return map.get(s).getKey();
    }

    public ISceneController getController(String s) {
        return map.get(s).getValue();
    }

    public void put(String s, Scene sc, ISceneController c) {
        map.put(s, new Pair<Scene,ISceneController>(sc, c));
    }
}
