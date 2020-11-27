package sample.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.Controller;
import sample.model.Model;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class View {

    private Controller controller;
    private MainScene mainScene;


    public View(Model model, Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainScene = loader.getController();
        primaryStage.setTitle("Colour-Progression");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        mainScene.setController(Objects.requireNonNull(controller));
    }

    public Map<String, Integer> getSizeOfCanvas () {
        return mainScene.getSizeOfCanvas();
    }

    public MainScene getMainScene() {
        return mainScene;
    }


}
