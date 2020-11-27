package sample.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.controller.Controller;
import sample.model.Key;
import sample.model.PixelPoint;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainScene {

    @FXML
    private Button startButton;

    @FXML
    private Button clearButton;

    @FXML
    private Canvas drawingGround;

    @FXML
    private Slider slider;

    private Controller controller;

    private LocalDateTime mousePressed;
    private LocalDateTime mouseReleased;

    public Canvas getDrawingGround() {
        return drawingGround;
    }

    @FXML
    public void mousePressed() {
        mousePressed = LocalDateTime.now();
    }

    @FXML
    public void mouseReleased(MouseEvent event) {
        mouseReleased = LocalDateTime.now();
        long diff = ChronoUnit.MILLIS.between(mousePressed, mouseReleased);
        System.out.println("Mousetime "+diff);
        controller.deleteHoleInDataAndRefill(diff, event);
    }

    @FXML
    public void buttonStartAnimation() {
        controller.initAnimation();
        boolean sepiaTone = Math.random() > 0.9;
        if(sepiaTone) drawingGround.setEffect(new SepiaTone());
        else drawingGround.setEffect(null);
    }

    @FXML
    public void sliderSet(){
        controller.sliderValueChanged(slider.getValue());
    }

    @FXML
    public void buttonClear() {
        controller.deleteAllData();
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

        //TODO Keine Casts
//        pixelWriter.setColor(0, 0, Color.CHOCOLATE);
    }

    public void writePixels(Map<Key, PixelPoint> pm){
        GraphicsContext gc = drawingGround.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();

        for(int x=0; x<drawingGround.getWidth(); x++){

            for(int y=0; y<drawingGround.getHeight(); y++) {
                pixelWriter.setColor(x,y,pm.get(new Key(x,y)).getColor());
            }

        }

        //TODO Keine Casts
//        pixelWriter.setColor(0, 0, Color.CHOCOLATE);
    }
}
