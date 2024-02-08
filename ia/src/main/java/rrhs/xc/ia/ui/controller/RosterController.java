package rrhs.xc.ia.ui.controller;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.ui.event.SceneEvent;

public class RosterController implements ISceneController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TableView<Athlete> table;

    @FXML
    private TableColumn<Athlete, String> nameCol;

    @FXML
    private TableColumn<Athlete, Integer> gradYearCol;

    @FXML
    public void initialize() {
        cancelBtn.setOnAction(event -> cancelBtn.fireEvent(new SceneEvent("main")));

        saveBtn.setOnAction(event -> {
            try {
                DatabaseManager.getInstance().resolveAll(table.getItems());
                saveBtn.fireEvent(new SceneEvent("main"));
            } catch (SQLException e) {
                System.out.println("SQLException occurred while trying to save data roster data.");
                e.printStackTrace();
            }
        });

        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        gradYearCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGradYear()).asObject());

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gradYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        nameCol.setEditable(true);
        gradYearCol.setEditable(true);

        nameCol.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
        });

        gradYearCol.setOnEditCommit(event -> {
            event.getRowValue().setGradYear(event.getNewValue());
        });
    }

    @Override
    public void setupAthletes(List<Athlete> list) {
        table.getItems().clear();
        table.getItems().addAll(list);
    }

    @Override
    public void setupMeets(List<Meet> list) {}
    
}
