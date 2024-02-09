package rrhs.xc.ia.ui.controller;

import java.util.List;

import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;

/**
 * This interface describes a contract stating that the implementing class is a
 * FXML controller. The implementing class also agrees that it display at least one
 * athlete or at least one meet in some way. It may display more than one or either, or
 * more than one of both. The implementation should only implement the method
 * which accesses the list that it needs, and should be aware that callers are
 * allowed to pass any length list or null for the argument of either list.
 */
public interface SceneController {

    public void setupAthletes(List<Athlete> list);

    public void setupMeets(List<Meet> list);

}
