package sample.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.model.Model;

import java.io.IOException;

public class View {

    public View(Model model, Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainSceneController controller = loader.getController();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

//        boolean cont = true;
//        int x = 1;
//        int y = 1;
//        while (cont) {
//            cont = controller.showOnePixel(x++,y++);
//            primaryStage.show();
//
//        }

    }
}
