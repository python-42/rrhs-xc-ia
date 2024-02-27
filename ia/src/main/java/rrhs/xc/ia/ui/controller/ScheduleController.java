package rrhs.xc.ia.ui.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.ui.event.SceneEvent;

public class ScheduleController implements SceneController {

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView<Meet> table;
    @FXML private TableColumn<Meet, String> nameCol;
    @FXML private TableColumn<Meet, LocalDate> dateCol;
    @FXML private TableColumn<Meet, String> changedCol;

    @FXML
    public void initialize() {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cancelBtn.setOnAction(event -> cancelBtn.fireEvent(new SceneEvent("main")));

        deleteBtn.setOnAction(event -> {
            for (Meet m : table.getSelectionModel().getSelectedItems()) {
                m.requestDeletion();
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

        addBtn.setOnAction(event -> table.getItems().add(new Meet(null, "New Meet", LocalDate.now(), -1, true)));

        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dateCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<LocalDate>(cellData.getValue().getDate()));
        changedCol.setCellValueFactory(cellData -> new SimpleStringProperty(getChangedValue(cellData.getValue())));
        
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        nameCol.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            table.refresh();
        });

        dateCol.setOnEditCommit(event -> {
            event.getRowValue().setDate(event.getNewValue());
            table.refresh();
        });
    }

    private String getChangedValue(Meet m) {
        if (m.needsDeletion()) {
            return "Pending Deletion";
        }
        if (m.isNew()) {
            return "New entry";
        }
        if (m.isModified()) {
            return "Properties changed";
        }
        return "No changes";
    }

    @Override
    public void setupAthletes(List<Athlete> list) {}

    @Override
    public void setupMeets(List<Meet> list) {
        table.getItems().clear();
        table.getItems().addAll(list);
    }
    
}
