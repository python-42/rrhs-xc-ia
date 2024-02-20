package rrhs.xc.ia.ui.controller;

import java.util.List;

import org.jfree.chart.ChartPanel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.data.mem.Season;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.StringUtils;

public class AthleteController implements SceneController {

    @FXML private Button homeBtn;
    @FXML private Button exportBtn;

    @FXML private Tab graphTab;

    @FXML private TableView<Race> table;
    @FXML private TableColumn<Race, String> meetName;
    @FXML private TableColumn<Race, String> meetDate;
    @FXML private TableColumn<Race, String> time;
    @FXML private TableColumn<Race, String> avgPace;
    @FXML private TableColumn<Race, String> split1;
    @FXML private TableColumn<Race, String> split2;
    @FXML private TableColumn<Race, String> split3;
    @FXML private TableColumn<Race, Integer> place;
    @FXML private TableColumn<Race, String> varsity;

    @FXML private TilePane statsPane;

    private Athlete athlete;

    @FXML
    public void initialize() {
        homeBtn.setOnAction(event -> homeBtn.fireEvent(new SceneEvent("main")));
        exportBtn.setOnAction(event -> PDFExportable.export(athlete.getName() + " Summary.pdf", "Export Athlete", athlete));

        meetName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetName()));
        meetDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetDate().toString()));
        time.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getTimeSeconds())));
        avgPace.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getAverageSplitSeconds())));
        split1.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileOneSplitSeconds())));
        split2.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileTwoSplitSeconds())));
        split3.setCellValueFactory(cellData -> new SimpleStringProperty(StringUtils.formatTime(cellData.getValue().getMileThreeSplitSeconds())));
        place.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlace()).asObject());
        varsity.setCellValueFactory(cellData -> new SimpleStringProperty(
            (cellData.getValue().getLevel() == Level.VARSITY_BOYS || cellData.getValue().getLevel() == Level.VARSITY_GIRLS)
            ? "Yes" : "No"
        ));

        statsPane.setPrefColumns(5);
        statsPane.setHgap(15);
        statsPane.setAlignment(Pos.TOP_CENTER);
        statsPane.setPadding(new Insets(50));
    }

    @Override
    public void setupAthletes(List<Athlete> list) {
        // expect only one athlete
        athlete = list.get(0);
        SwingNode graph = new SwingNode();
        graph.setContent(new ChartPanel(athlete.getGraph()));

        graphTab.setContent(graph);

        table.getItems().clear();
        table.getItems().addAll(athlete.getRaces());

        setupStats();
    }

    private void setupStats() {
        statsPane.getChildren().clear();
        Border b = new Border(new BorderStroke(Paint.valueOf("black"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM));

        for (Season season : athlete.getSeasons()) {
            VBox box = new VBox();
            box.setBorder(b);
            box.setPadding(new Insets(10));
            box.setAlignment(Pos.CENTER);
            ObservableList<Node> children = box.getChildren();

            Text title = new Text(season.name());
            title.setFont(new Font(15));
            title.setUnderline(true);            
            children.add(title);

            children.add(new Label("Time Drop: " + StringUtils.formatTime(athlete.getSeasonTimeDropSeconds(season))));
            children.add(new Label("Average Time: " + StringUtils.formatTime(athlete.getSeasonAverageTime(season))));
            children.add(new Label("Average Mile 1 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 1))));
            children.add(new Label("Average Mile 2 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 2))));
            children.add(new Label("Average Mile 3 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 3))));

            Race best = athlete.getBestSeasonRace(season);
            if (best != null) {
                children.add(new Label("Best Race: " + best.getMeetName().trim() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")"));
            }

            statsPane.getChildren().add(box);
        }
        VBox box = new VBox();
        box.setBorder(b);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        ObservableList<Node> children = box.getChildren();

        Text title = new Text("CAREER");
        title.setFont(new Font(15));
        title.setUnderline(true);
        children.add(title);

        children.add(new Label("Time Drop: " + StringUtils.formatTime(athlete.getCareerTimeDropSeconds())));
        children.add(new Label("Average Time: " + StringUtils.formatTime(athlete.getCareerAverageTime())));
        children.add(new Label("Average Mile 1 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(1))));
        children.add(new Label("Average Mile 2 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(2))));
        children.add(new Label("Average Mile 3 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(3))));

        Race best = athlete.getBestCareerRace();
        if (best != null) {
            children.add(new Label("Best Race: " + best.getMeetName().trim() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")"));
        }

        statsPane.getChildren().add(box);

    }

    @Override
    public void setupMeets(List<Meet> list) {}
    
}
