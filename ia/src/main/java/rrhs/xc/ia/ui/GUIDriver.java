package rrhs.xc.ia.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.ui.event.SceneEvent;
import rrhs.xc.ia.util.FXMLFilter;
import rrhs.xc.ia.util.StringUtils;

public class GUIDriver extends Application {
    

    public GUIDriver(String[] args) {
        Application.launch(GUIDriver.class, args);
    }

    public GUIDriver() {}

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        Font.loadFont(GUIDriver.class.getResourceAsStream("./fxml/IBMPlexMono-Light.ttf"), 0);
        loadAllFXML();

        MainController controller = (MainController) SceneCollection.getInstance().getController("main");

        //Temp code
        List<Athlete> a = new ArrayList<Athlete>();
        for (int i = 1; i < 40; i++) {
            a.add(new Athlete(null, "" + i, 2023));
        }

        controller.setAthletes(a);
        controller.setMeets(List.of(new Meet(null, "Conference 2023", LocalDate.of(2023, 10, 14), -1, -1, -1, -1)));
        //Remove above code

        Scene scene = SceneCollection.getInstance().getScene("main");

        scene.addEventHandler(SceneEvent.SWITCH_SCENE, new EventHandler<SceneEvent>() {

            @Override
            public void handle(SceneEvent event) {
                Scene s = SceneCollection.getInstance().getScene(event.getDesiredSceneName());
                s.addEventHandler(SceneEvent.SWITCH_SCENE, this);

                stage.setScene(s);
            }
            
        });

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
}
