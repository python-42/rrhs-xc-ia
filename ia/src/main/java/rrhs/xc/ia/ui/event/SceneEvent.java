package rrhs.xc.ia.ui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class SceneEvent extends Event {

    public static final EventType<SceneEvent> SWITCH_SCENE = new EventType<>(Event.ANY, "SWITCH_SCENE");

    private final String desiredScene;

    public SceneEvent(String desiredScene) {
        super(SWITCH_SCENE);
        this.desiredScene = desiredScene;
    }

    public String getDesiredSceneName() {
        return desiredScene;
    }
    
}
