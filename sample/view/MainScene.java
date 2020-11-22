package sample.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.controller.Controller;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainScene {

    @FXML
    private Button button;

    @FXML
    private Canvas drawingGround;

    private Controller controller;

    public Canvas getDrawingGround() {
        return drawingGround;
    }

    public void buttonStartAnimation() {
        controller.initAnimation();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Map<String, Integer> getSizeOfCanvas () {
        Objects.requireNonNull(drawingGround);
        Integer height = (int) drawingGround.getHeight();
        Integer width = (int) drawingGround.getWidth();
        System.out.println(height);


        Map<String, Integer> size = new HashMap<String, Integer>();
        size.put("height", height);
        size.put("width", width);
        return size;
    }

    public void writePixels(Color [][] pixels){
        GraphicsContext gc = drawingGround.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();

        for(int i=0; i<pixels.length; i++) {
            for(int y=0; y<pixels[0].length; y++) {
                pixelWriter.setColor(y, i, pixels[i][y]);
            }
        }
//        pixelWriter.setColor(0, 0, Color.CHOCOLATE);
    }
}
