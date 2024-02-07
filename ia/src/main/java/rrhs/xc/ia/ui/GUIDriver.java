package rrhs.xc.ia.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;

public class GUIDriver extends Application {
    

    public GUIDriver(String[] args) {
        Application.launch(GUIDriver.class, args);
    }

    public GUIDriver() {}

    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(GUIDriver.class.getResourceAsStream("./fxml/IBMPlexMono-Light.ttf"), 0);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();


        //Temp code
        List<Athlete> a = new ArrayList<Athlete>();
        for (int i = 1; i < 40; i++) {
            a.add(new Athlete(null, "" + i, 2023));
        }

        controller.setAthletes(a);
        controller.setMeets(List.of(new Meet(null, "Conference 2023", LocalDate.of(2023, 10, 14), -1, -1, -1, -1)));
        //Remove above code

        stage.setResizable(false);
        stage.setTitle("JCrossCountry Tracker");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
