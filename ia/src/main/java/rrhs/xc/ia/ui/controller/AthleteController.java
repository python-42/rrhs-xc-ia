package rrhs.xc.ia.ui.controller;

import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.jfree.chart.ChartPanel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
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


    @FXML private VBox freshmanStats;
    @FXML private VBox sophomoreStats;
    @FXML private VBox juniorStats;
    @FXML private VBox seniorStats;

    @FXML private VBox careerStats;

    @FXML private TitledPane freshmanStatsContainer;
    @FXML private TitledPane sophomoreStatsContainer;
    @FXML private TitledPane juniorStatsContainer;
    @FXML private TitledPane seniorStatsContainer;

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

        clearStats();
        setupStats();
    }

    private void clearStats() {
        freshmanStats.getChildren().clear();
        sophomoreStats.getChildren().clear();
        juniorStats.getChildren().clear();
        seniorStats.getChildren().clear();
        careerStats.getChildren().clear();

        freshmanStatsContainer.setExpanded(false);
        sophomoreStatsContainer.setExpanded(false);
        juniorStatsContainer.setExpanded(false);
        seniorStatsContainer.setExpanded(false);
    }

    private void setupStats() {
        HashMap<Season, SimpleEntry<TitledPane, VBox>> statMap = new HashMap<Season, SimpleEntry<TitledPane, VBox>>();
        statMap.put(Season.FRESHMAN, new SimpleEntry<TitledPane,VBox>(freshmanStatsContainer, freshmanStats));
        statMap.put(Season.SOPHOMORE, new SimpleEntry<TitledPane,VBox>(sophomoreStatsContainer, sophomoreStats));
        statMap.put(Season.JUNIOR, new SimpleEntry<TitledPane,VBox>(juniorStatsContainer, juniorStats));
        statMap.put(Season.SENIOR, new SimpleEntry<TitledPane,VBox>(seniorStatsContainer, seniorStats));

        for (Season season : athlete.getSeasons()) {
            ObservableList<Node> children = statMap.get(season).getValue().getChildren();

            children.add(new Text("Time Drop: " + StringUtils.formatTime(athlete.getSeasonTimeDropSeconds(season))));
            children.add(new Text("Average Time: " + StringUtils.formatTime(athlete.getSeasonAverageTime(season))));
            children.add(new Text("Average Mile 1 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 1))));
            children.add(new Text("Average Mile 2 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 2))));
            children.add(new Text("Average Mile 3 Split: " + StringUtils.formatTime(athlete.getAverageSeasonSplitSeconds(season, 3))));

            Race best = athlete.getBestSeasonRace(season);
            if (best != null) {
                children.add(new Text("Best Race: " + best.getMeetName().trim() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")"));
            }

            statMap.get(season).getKey().setExpanded(true);

        }
        ObservableList<Node> children = careerStats.getChildren();

        children.add(new Text("Time Drop: " + StringUtils.formatTime(athlete.getCareerTimeDropSeconds())));
        children.add(new Text("Average Time: " + StringUtils.formatTime(athlete.getCareerAverageTime())));
        children.add(new Text("Average Mile 1 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(1))));
        children.add(new Text("Average Mile 2 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(2))));
        children.add(new Text("Average Mile 3 Split: " + StringUtils.formatTime(athlete.getAverageCareerSplitSeconds(3))));

        Race best = athlete.getBestCareerRace();
        if (best != null) {
            children.add(new Text("Best Race: " + best.getMeetName().trim() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")"));
        }

    }

    @Override
    public void setupMeets(List<Meet> list) {}
    
}
