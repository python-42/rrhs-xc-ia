package rrhs.xc.ia.ui.controller;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.StringUtils;

public class MeetController implements SceneController {

    private Meet meet;

    @FXML private TableView<Race> varsityBoys;
    @FXML private TableView<Race> varsityGirls;
    @FXML private TableView<Race> jvBoys;
    @FXML private TableView<Race> jvGirls;

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    @FXML private TextField nameBox;
    @FXML private DatePicker dateBox;

    @FXML private VBox varsityBoysStats;
    @FXML private VBox varsityGirlsStats;
    @FXML private VBox jvBoysStats;
    @FXML private VBox jvGirlsStats;

    @FXML private TitledPane varsityBoysStatsContainer;
    @FXML private TitledPane varsityGirlsStatsContainer;
    @FXML private TitledPane jvBoysStatsContainer;
    @FXML private TitledPane jvGirlsStatsContainer;
    

    @SuppressWarnings("unchecked")
    @FXML
    private void initialize() {
        cancelBtn.setOnAction(event -> cancelBtn.fireEvent(new SceneEvent("main")));
        saveBtn.setOnAction(event -> {
            try {
                DatabaseManager.getInstance().resolveAll(List.of(meet));
                DatabaseManager.getInstance().resolveAll(meet.getRaceList());
                saveBtn.fireEvent(new SceneEvent("main"));
            } catch (SQLException e) {
                System.out.println("An SQL exception occurred while trying to save meet information");
                Notifications.create()
                .title("SQL Error Occurred")
                .text("An error occurred while trying to save meet: " + e.getMessage())
                .showError();
                e.printStackTrace();
            }
        });

        nameBox.textProperty().addListener((observable, oldVal, newVal) -> meet.setName(newVal));
        dateBox.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal == null) {
                dateBox.setValue(oldVal);
            } else {
                meet.setDate(newVal);
            }
        });

        for (TableView<Race> table : List.of(varsityBoys, varsityGirls, jvBoys, jvGirls)) {
            var name = (TableColumn<Race, String>) table.getColumns().get(0);
            name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAthleteName()));

            var time = (TableColumn<Race, String>) table.getColumns().get(1);
            time.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getTimeSeconds())));
            time.setCellFactory(TextFieldTableCell.forTableColumn());
            time.setOnEditCommit(event -> {
                event.getRowValue().setTimeSeconds(StringUtils.deFormatTime(event.getNewValue()));
                table.refresh();
            });

            var place = (TableColumn<Race, Integer>) table.getColumns().get(2);
            place.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlace()).asObject());
            place.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            place.setOnEditCommit(event -> event.getRowValue().setPlace(event.getNewValue()));

            var avgPace = (TableColumn<Race, String>) table.getColumns().get(3);
            avgPace.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getAverageSplitSeconds())));

            var split1 = (TableColumn<Race, String>) table.getColumns().get(4);
            split1.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileOneSplitSeconds())));
            split1.setCellFactory(TextFieldTableCell.forTableColumn());

            var split2 = (TableColumn<Race, String>) table.getColumns().get(5);
            split2.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileTwoSplitSeconds())));
            split2.setCellFactory(TextFieldTableCell.forTableColumn());

            var split3 = (TableColumn<Race, String>) table.getColumns().get(6);
            split3.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileThreeSplitSeconds())));
        }        
    }

    @Override
    public void setupAthletes(List<Athlete> list) {}

    @Override
    public void setupMeets(List<Meet> list) {
        //expect only one
        meet = list.get(0);

        for (TableView<Race> table : List.of(varsityBoys, varsityGirls, jvBoys, jvGirls)) {
            table.getItems().clear();
        }

        Map<Level, List<Race>> map = meet.getRaces();
        varsityBoys.getItems().addAll(map.getOrDefault(Level.VARSITY_BOYS, List.of()));
        varsityGirls.getItems().addAll(map.getOrDefault(Level.VARSITY_GIRLS, List.of()));
        jvBoys.getItems().addAll(map.getOrDefault(Level.JV_BOYS, List.of()));
        jvGirls.getItems().addAll(map.getOrDefault(Level.JV_GIRLS, List.of()));

        for (TableView<Race> table : List.of(varsityBoys, varsityGirls, jvBoys, jvGirls)) {
            table.refresh();
        }

        nameBox.setText(meet.getName());
        dateBox.setValue(meet.getDate());
        setupStatistics();
    }

    private void setupStatistics() {
        for (Entry<Level, Entry<VBox, TitledPane>> entry : List.of(
            new SimpleEntry<Level, Entry<VBox, TitledPane>>(Level.VARSITY_BOYS, new SimpleEntry<VBox, TitledPane>(varsityBoysStats, varsityBoysStatsContainer)),
            new SimpleEntry<Level, Entry<VBox, TitledPane>>(Level.VARSITY_GIRLS, new SimpleEntry<VBox, TitledPane>(varsityGirlsStats, varsityGirlsStatsContainer)),
            new SimpleEntry<Level, Entry<VBox, TitledPane>>(Level.JV_BOYS, new SimpleEntry<VBox, TitledPane>(jvBoysStats, jvBoysStatsContainer)),
            new SimpleEntry<Level, Entry<VBox, TitledPane>>(Level.JV_GIRLS, new SimpleEntry<VBox, TitledPane>(jvGirlsStats, jvGirlsStatsContainer))
        )) 
        {
            Level level = entry.getKey();
            ObservableList<Node> children = entry.getValue().getKey().getChildren();
            children.clear();

            children.add(new Text("Average Time: " + StringUtils.formatTime(meet.getAverageTimeSeconds(level))));
            children.add(new Text("Average Mile 1 Split: " + StringUtils.formatTime(meet.getAverageSplitSeconds(1, level))));
            children.add(new Text("Average Mile 2 Split: " + StringUtils.formatTime(meet.getAverageSplitSeconds(2, level))));
            children.add(new Text("Average Mile 3 Split: " + StringUtils.formatTime(meet.getAverageSplitSeconds(3, level))));
            children.add(new Text("Time Spread: " + StringUtils.formatTime(meet.getTimeSpreadSeconds(level))));
            children.add(new Text("Place Spread: " + meet.getPlaceSpread(level)));

            entry.getValue().getValue().setExpanded(meet.getPlaceSpread(level) != -1);
        }
    }
    
}
