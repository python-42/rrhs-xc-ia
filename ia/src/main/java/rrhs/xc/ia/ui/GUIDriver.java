package rrhs.xc.ia.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rrhs.xc.ia.data.database.DatabaseManager;
import rrhs.xc.ia.ui.controller.ISceneController;
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
        Font.loadFont(GUIDriver.class.getResourceAsStream("./fxml/IBMPlexMono-Light.ttf"), 0);
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
                    setup(event.getDesiredSceneName());
                    stage.setScene(s);
                } catch (SQLException e) {
                    // TODO add GUI popup
                    System.out.println("SQLException occurred while trying to switch to scene " + event.getDesiredSceneName());
                    e.printStackTrace();
                }
            }
        });

        setup("main");

        stage.setResizable(false);
        stage.setTitle("JCrossCountry Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void loadAllFXML() throws IOException, URISyntaxException{
        File fxmlDir = new File(getClass().getResource("./fxml/").toURI());

        FXMLLoader loader;
        
        for (File f : fxmlDir.listFiles(new FXMLFilter())) {
            loader = new FXMLLoader(getClass().getResource("./fxml/" + f.getName()));
            SceneCollection.getInstance().put(StringUtils.removeFilExtension(f), new Scene(loader.load()), loader.getController());

        }
    }

    private void setup(String name) throws SQLException {
        ISceneController controller = SceneCollection.getInstance().getController(name);

        switch (name) {
            case "main":
                controller.setupAthletes(db.getAllAthletes());
                controller.setupMeets(db.getAllMeets());
                break;
            case "roster":
                controller.setupAthletes(db.getAllAthletes());
                break;
            default:
                break;
        }
    }
}
