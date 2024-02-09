package rrhs.xc.ia.ui.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.ui.event.SceneEvent;

public class RosterController implements SceneController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<Athlete> table;

    @FXML
    private TableColumn<Athlete, String> nameCol;

    @FXML
    private TableColumn<Athlete, Integer> gradYearCol;

    @FXML
    private TableColumn<Athlete, String> changedCol;

    @FXML
    public void initialize() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cancelBtn.setOnAction(event -> cancelBtn.fireEvent(new SceneEvent("main")));

        deleteBtn.setOnAction(event -> {
            for (Athlete a : table.getSelectionModel().getSelectedItems()) {
                a.requestDeletion();
                table.refresh();
            }
        });

        saveBtn.setOnAction(event -> {
            try {
                DatabaseManager.getInstance().resolveAll(table.getItems());
                saveBtn.fireEvent(new SceneEvent("main"));
            } catch (SQLException e) {
                System.out.println("SQLException occurred while trying to save data roster data.");
                e.printStackTrace();
            }
        });

        addBtn.setOnAction(event -> {
            table.getItems().add(new Athlete(null, "New Athlete", getCurrentFreshmanGradYear(), -1, true));
        });

        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        gradYearCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGradYear()).asObject());
        changedCol.setCellValueFactory(cellData -> new SimpleStringProperty(getChangedValue(cellData.getValue())));
        
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gradYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        nameCol.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            table.refresh();
        });

        gradYearCol.setOnEditCommit(event -> {
            event.getRowValue().setGradYear(event.getNewValue());
            table.refresh();
        });
    }

    private String getChangedValue(Athlete a) {
        if (a.needsDeletion()) {
            return "Pending Deletion";
        }
        if (a.isNew()) {
            return "New entry";
        }
        if (a.isModified()) {
            return "Properties changed";
        }
        return "No changes";
    }

    private int getCurrentFreshmanGradYear() {
        LocalDate d = LocalDate.now();
        if (d.getMonthValue() < 6) {
            return d.getYear() + 3;
        }
        return d.getYear() + 4;
    }

    @Override
    public void setupAthletes(List<Athlete> list) {
        table.getItems().clear();
        table.getItems().addAll(list);
    }

    @Override
    public void setupMeets(List<Meet> list) {}
    
}
