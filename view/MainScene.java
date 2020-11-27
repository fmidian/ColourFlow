package view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import controller.Controller;
import model.Key;
import model.PixelPoint;

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

    @FXML
    private CheckBox flowage;

    private Controller controller;

    private LocalDateTime mousePressed;
    private LocalDateTime mouseReleased;

    @FXML
    public void mousePressed() {
        mousePressed = LocalDateTime.now();
    }

    @FXML
    public void mouseReleased(MouseEvent event) {
        mouseReleased = LocalDateTime.now();
        long diff = ChronoUnit.MILLIS.between(mousePressed, mouseReleased);
        controller.deleteHoleInDataAndRefill(diff, event);
    }

    @FXML
    public void buttonStartAnimation() {
        controller.initAnimation();
        boolean sepiaTone = Math.random() > 0.95;
        if(sepiaTone) drawingGround.setEffect(new SepiaTone());
        else drawingGround.setEffect(null);
    }

    @FXML
    public void sliderSet(){
        controller.sliderValueChanged(slider.getValue());
    }

    @FXML
    public void flowageChange(){
        controller.flowageValueChanged(flowage.isSelected());
    }

    @FXML
    public void buttonClear() {
        controller.deleteAllData();
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

    public void writePixels(Map<Key, PixelPoint> pm){
        GraphicsContext gc = drawingGround.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();

        pm.forEach( (key, pixel) -> {
            pixelWriter.setColor(key.getX(),key.getY(),pixel.getColor());
        });
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
