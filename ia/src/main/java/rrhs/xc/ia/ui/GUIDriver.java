package rrhs.xc.ia.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.ui.controller.SceneController;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.FXMLFilter;
import rrhs.xc.ia.util.StringUtils;

public class GUIDriver extends Application {
    
    private DatabaseManager db;

    public GUIDriver(String[] args) {
        Application.launch(GUIDriver.class, args);
    }

    public GUIDriver() {}

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException, SQLException {
        loadAllFXML();

        db = DatabaseManager.getInstance();
        db.connect();
        db.createDatabase();

        Scene scene = SceneCollection.getInstance().getScene("main");
        scene.addEventHandler(SceneEvent.SWITCH_SCENE, new EventHandler<SceneEvent>() {

            @Override
            public void handle(SceneEvent event) {
                Scene s = SceneCollection.getInstance().getScene(event.getDesiredSceneName());
                s.addEventHandler(SceneEvent.SWITCH_SCENE, this);

                try {
                    setup(event);
                    stage.setScene(s);
                } catch (SQLException e) {
                    // TODO add GUI popup
                    System.out.println("SQLException occurred while trying to switch to scene " + event.getDesiredSceneName());
                    e.printStackTrace();
                }
            }
        });

        setup(new SceneEvent("main"));

        stage.setResizable(false);
        stage.setTitle("JCrossCountry Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void loadAllFXML() throws IOException, URISyntaxException{
        ClassLoader resourceLoader = ClassLoader.getSystemClassLoader();
        Font.loadFont(resourceLoader.getResourceAsStream("IBMPlexMono-Light.ttf"), 0);

        File fxmlDir = new File(resourceLoader.getResource("./").toURI());

        FXMLLoader loader;
        
        for (File f : fxmlDir.listFiles(new FXMLFilter())) {
            loader = new FXMLLoader(resourceLoader.getResource(f.getName()));
            SceneCollection.getInstance().put(StringUtils.removeFilExtension(f), new Scene(loader.load()), loader.getController());

        }
    }

    private void setup(SceneEvent event) throws SQLException {
        SceneController controller = SceneCollection.getInstance().getController(event.getDesiredSceneName());

        switch (event.getDesiredSceneName()) {
            case "main":
                controller.setupAthletes(db.getAllAthletes());
                controller.setupMeets(db.getAllMeets());
                break;
            case "roster":
                controller.setupAthletes(db.getAllAthletes());
                break;
            case "athlete":
                controller.setupAthletes(List.of((Athlete)event.getRelevantObject()));
                break;
            case "meetEdit":
                controller.setupMeets(List.of((Meet)event.getRelevantObject()));
                break;
            default:
                break;
        }
    }
}
