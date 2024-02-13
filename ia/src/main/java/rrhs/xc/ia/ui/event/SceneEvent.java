package rrhs.xc.ia.ui.event;

import javafx.event.Event;
import javafx.event.EventType;
import rrhs.xc.ia.data.i.SQLDataObject;

public class SceneEvent extends Event {

    public static final EventType<SceneEvent> SWITCH_SCENE = new EventType<>(Event.ANY, "SWITCH_SCENE");

    private final String desiredScene;
    private final SQLDataObject obj;

    public SceneEvent(String desiredScene) {
        super(SWITCH_SCENE);
        this.desiredScene = desiredScene;
        obj = null;
    }

    public SceneEvent(String desiredScene, SQLDataObject obj) {
        super(SWITCH_SCENE);
        this.desiredScene = desiredScene;
        this.obj = obj;
    }

    public String getDesiredSceneName() {
        return desiredScene;
    }

    public SQLDataObject getRelevantObject() {
        return obj;
    }
    
}
