package rrhs.xc.ia.ui.controller;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.Notifications;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.StringUtils;
import tornadofx.control.Fieldset;
import tornadofx.control.Form;

public class MeetController implements SceneController {

    private Meet meet;
    private List<Athlete> athletes;

    @FXML private TableView<Race> varsityBoys;
    @FXML private TableView<Race> varsityGirls;
    @FXML private TableView<Race> jvBoys;
    @FXML private TableView<Race> jvGirls;

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    @FXML private Button exportBtn;
    @FXML private Button editBtn;

    @FXML private TextField nameBox;
    @FXML private DatePicker dateBox;
    @FXML private Button deleteMeetBtn;

    @FXML private Label statusLabel;
    private int currentPriority = 10;

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
        exportBtn.setOnAction(event -> PDFExportable.export(meet.getName() + " Summary.pdf", "Export Meet", meet));
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

        editBtn.setOnAction(event -> setupModal());
        deleteMeetBtn.setOnAction(event -> {
            meet.requestDeletion();
            setStatus("Pending Deletion", 1);
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
                setStatus("Race information edited", 4);
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
    public void setupAthletes(List<Athlete> list) {
        this.athletes = list;
    }

    @Override
    public void setupMeets(List<Meet> list) {
        //expect only one
        meet = list.get(0);

        refreshTables();

        nameBox.setText(meet.getName());
        dateBox.setValue(meet.getDate());
        setupStatistics();

        if (meet.isNew()) {
            setStatus("New", 2);
        }

    }

    /**
     * Routing all status update calls through this method ensures that a more important message isn't overwritten by an unimportant one.
     * @param status The status message to display to the user
     * @param priority The priority this message should have. Closer to zero is more important. 
     */
    private void setStatus(String status, int priority) {
        if (priority <= currentPriority) {
            statusLabel.setText("Status: " + status);
            currentPriority = priority;
        }
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

    private boolean athleteHasRace(String name) {
        for (Race r : meet.getRaceList()) {
            if (r.getAthleteName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private void refreshTables() {
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
    }

    private void setupModal() {
        ListSelectionView<Athlete> athleteSelector = new ListSelectionView<Athlete>();
        athleteSelector.setTargetItems(FXCollections.observableArrayList(athletes));
        athleteSelector.setTargetHeader(new Label("Participants"));
        athleteSelector.setSourceHeader(new Label("Skipped"));

        Dialog<ButtonType> popup = new Dialog<ButtonType>();

        popup.getDialogPane().getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
        popup.getDialogPane().setContent(athleteSelector);

        final Button next = (Button) popup.getDialogPane().lookupButton(ButtonType.NEXT);
        next.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (athleteSelector.getTargetItems().size() == 0) {
                event.consume();
                Notifications.create()
                .title("Not enough athletes")
                .text("There must be at least 1 athlete participating in the meet")
                .showWarning();
            }
        });

        popup.initOwner(Window.getWindows().get(0));
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setHeight(500);
        popup.setWidth(500);
        popup.setResizable(false);

        Optional<ButtonType> rtn = popup.showAndWait();

        if (rtn.isPresent() && rtn.get() == ButtonType.NEXT) {
            removeDeselected(athleteSelector.getSourceItems());
            getInputForSelected(athleteSelector.getTargetItems(), popup);
        } else {
            Notifications.create()
            .title("Cancelled")
            .text("Meet race adding cancelled.")
            .showWarning();
        }

        refreshTables();
        setupStatistics();
    }

    private void removeDeselected(List<Athlete> athletes) {
        List<Race> races = meet.getRaceList();
        for (Athlete a : athletes) {
            for (Race r : races) {
                if (r.getAthleteName().equals(a.getName())) {
                    r.requestDeletion();
                }
            }
        }
    }

    private void getInputForSelected(List<Athlete> athletes, Dialog<ButtonType> popup) {
        Form form = new Form();

        Text name = new Text();
        TextField time = new TextField();
        TextField mile1Split = new TextField();
        TextField mile2Split = new TextField();
        TextField place = new TextField();
        ComboBox<Level> level = new ComboBox<Level>();

        level.setItems(FXCollections.observableArrayList(Level.values()));

        Fieldset nameField = form.fieldset();
        nameField.field("Name", name);

        Fieldset timeField = form.fieldset();
        timeField.field("Time", time);

        Fieldset split1Field = form.fieldset();
        split1Field.field("Mile Split 1", mile1Split);

        Fieldset split2Field = form.fieldset();
        split2Field.field("Mile Split 2", mile2Split);
        
        Fieldset placeField = form.fieldset();
        placeField.field("Place", place);

        Fieldset levelField = form.fieldset();
        levelField.field("Level", level);
        level.setValue(Level.VARSITY_BOYS);

        popup.getDialogPane().setContent(form);
        final Button next = (Button) popup.getDialogPane().lookupButton(ButtonType.NEXT);

        next.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                int i = Integer.parseInt(place.getText());
                if (i < 1) {
                    Notifications.create()
                    .title("Invalid input")
                    .text("Input for place was invalid. Place must be at least 1")
                    .showError();
                    event.consume();
                    
                } else if (!StringUtils.validTimeFormat(time.getText())) {
                    Notifications.create()
                    .title("Malformed input")
                    .text("Input for time was invalid. Please try again.")
                    .showError();

                    event.consume();
                } else if (!StringUtils.validTimeFormat(mile1Split.getText())) {
                    Notifications.create()
                    .title("Malformed input")
                    .text("Input for mile one split was invalid. Please try again.")
                    .showError();

                    event.consume();
                } else if(!StringUtils.validTimeFormat(mile2Split.getText())) {
                    Notifications.create()
                    .title("Malformed input")
                    .text("Input for mile two split was invalid. Please try again.")
                    .showError();

                    event.consume();
                }

            } catch(NumberFormatException e) {
                Notifications.create()
                .title("Malformed input")
                .text("Input for place was invalid. Please try again.")
                .showError();

                event.consume();
            }
        });
        
        Optional<ButtonType> rtn;
        Race race;
        for (Athlete a : athletes) {
            if (athleteHasRace(a.getName())) {
                continue;
            }
            setStatus("Performance(s) added", 3);
            name.setText(a.getName());
            time.clear();
            mile1Split.clear();
            mile2Split.clear();
            place.clear();

            popup.setWidth(500);
            rtn = popup.showAndWait();

            if (rtn.isPresent() && rtn.get() == ButtonType.NEXT) {
                race = new Race(
                    a.getName(),
                    null,
                    null,
                    level.getValue(),
                    a.determineSeason(meet.getDate()), 
                    StringUtils.deFormatTime(time.getText()),
                    StringUtils.deFormatTime(mile1Split.getText()),
                    StringUtils.deFormatTime(mile2Split.getText()),
                    Integer.parseInt(place.getText()), 
                    -1,
                    true
                );

                race.setAthleteID(a.getId());
                race.setMeetID(meet.getId());

                meet.addRace(race);
            }else {
                Notifications.create()
                .title("Cancelled")
                .text("Meet race adding cancelled. Any races entered previous to cancel will be entered.")
                .showWarning();

                break;
            }

        }
    }
    
}
