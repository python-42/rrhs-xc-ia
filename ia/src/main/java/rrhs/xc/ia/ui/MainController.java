package rrhs.xc.ia.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.util.SearchChangeListener;

public class MainController {
    
    private ObservableList<Athlete> viewableAthletes;
    private ObservableList<Meet> viewableMeets;

    private List<Athlete> allAthletes = new ArrayList<>();
    private List<Meet> allMeets = new ArrayList<>();

    @FXML
    private ListView<Athlete> athleteView;

    @FXML
    private TextField athleteSearch;

    @FXML
    private ListView<Meet> meetView;

    @FXML
    private TextField meetSearch;

    @FXML
    private void initialize() {
        viewableAthletes = athleteView.getItems();
        viewableMeets = meetView.getItems();

        meetSearch.textProperty().addListener(new SearchChangeListener<Meet>(viewableMeets, allMeets));

        athleteSearch.textProperty().addListener(new SearchChangeListener<Athlete>(viewableAthletes, allAthletes));
    }
    
    public void setAthletes(List<Athlete> list) {
        allAthletes.clear();
        allAthletes.addAll(list);
        viewableAthletes.clear();
        viewableAthletes.addAll(list);
        athleteSearch.setText("");
    }

    public void setMeets(List<Meet> list) {
        allMeets.clear();
        allMeets.addAll(list);
        viewableMeets.clear();
        viewableMeets.addAll(list);
        meetSearch.setText("");
    }

}
