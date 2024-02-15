package rrhs.xc.ia.ui.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.SearchChangeListener;

public class MainController implements SceneController{
    
    private ObservableList<Athlete> viewableAthletes;
    private ObservableList<Meet> viewableMeets;

    private List<Athlete> allAthletes = new ArrayList<>();
    private List<Meet> allMeets = new ArrayList<>();

    @FXML private ListView<Athlete> athleteView;
    @FXML private TextField athleteSearch;
    @FXML private Button editRosterBtn;

    @FXML private ListView<Meet> meetView;
    @FXML private TextField meetSearch;
    @FXML private Button newMeetBtn;

    @FXML
    private void initialize() {
        viewableAthletes = athleteView.getItems();
        viewableMeets = meetView.getItems();

        meetSearch.textProperty().addListener(new SearchChangeListener<Meet>(viewableMeets, allMeets));

        athleteSearch.textProperty().addListener(new SearchChangeListener<Athlete>(viewableAthletes, allAthletes));

        editRosterBtn.setOnAction(event -> editRosterBtn.fireEvent(new SceneEvent("roster")));

        newMeetBtn.setOnAction(event -> newMeetBtn.fireEvent(new SceneEvent("meetEdit", new Meet(
            new ArrayList<Race>(),
            "NEW MEET",
            LocalDate.now(),
            -1,
            true
        ))));

        athleteView.setOnMouseClicked(event -> {
            if (athleteView.getSelectionModel().getSelectedItem() != null) {
                athleteView.fireEvent(new SceneEvent("athlete", athleteView.getSelectionModel().getSelectedItem())); 
            }
        });

        meetView.setOnMouseClicked(event -> {
            if (meetView.getSelectionModel().getSelectedItem() != null) {
                meetView.fireEvent(new SceneEvent("meetEdit", meetView.getSelectionModel().getSelectedItem()));
            }
        });
    }

    @Override
    public void setupAthletes(List<Athlete> list) {
        if (list != null) {
            allAthletes.clear();
            viewableAthletes.clear();

            allAthletes.addAll(list);
            viewableAthletes.addAll(list);
        } 
    }

    @Override
    public void setupMeets(List<Meet> list) {
        if (list != null) {
            allMeets.clear();
            viewableMeets.clear();

            allMeets.addAll(list);
            viewableMeets.addAll(list);
        }
    }

}
