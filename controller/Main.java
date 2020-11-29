package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Main extends Application {

    //Dependecies of the App: JavaFX and GSON
    // Maybe there will be a gradle file in the future

    @Override
    public void start(Stage primaryStage) throws Exception{
        Model model = new Model();
        View view = new View(model, primaryStage);
        Controller controller = new Controller(model, view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
