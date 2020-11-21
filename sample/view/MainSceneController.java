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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class MainSceneController {

    @FXML
    private Button button;

    @FXML
    private Canvas drawingGround;

    @FXML
    private Pane pane;

    private static final int RECT_WIDTH = 25;
    private static final int RECT_HEIGHT = 25;

    private int pos = 20;

    public void buttonClicked() {
        System.out.println("Button clicked!");
        GraphicsContext gc = drawingGround.getGraphicsContext2D();
//        gc.setFill(Color.BISQUE);
//        gc.fillOval(5, 5, 100, 90);
//        gc.strokeLine(50, 50, 70, 60);
//        gc.strokeText("Text", 10, 20);
//        gc.strokeArc(320, 10, 50, 50, 40, 80, ArcType.ROUND);
//        gc.strokeText("This is a stroked Text", 10, 50);


        double height = drawingGround.getHeight() / 2;
        double width = drawingGround.getWidth() / 2;

//        showAnimation();
//        showOnePixel(100,100);

    }

    private void showAnimation() {
//        var rect = new Rectangle(20, 20, 60, 60);
//        var circle = new Circle(1);

        Text text = new Text();
        text.setText("Hab dich auch lieb");
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font(50));
        text.setEffect(new Lighting());
        text.setFill(Color.SALMON);


        double sceneWidth = pane.getWidth();
        double msgWidth = text.getLayoutBounds().getWidth();

        KeyValue initKeyValue = new KeyValue(text.translateXProperty(), -500);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);

        KeyValue endKeyValue = new KeyValue(text.translateXProperty(), 2.0
                * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(3), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

        pane.getChildren().addAll(text);


//        var tl = new Timeline();
//
//        tl.setCycleCount(2);
//        tl.setAutoReverse(true);
//
//        var kv = new KeyValue(text.caretPositionProperty(), 200);
//
//        var kf = new KeyFrame(Duration.millis(2000), kv);
//        tl.getKeyFrames().addAll(kf);
//
//
//
//        tl.play();
//
//        pane.getChildren().addAll(text);
//        pane.getParent().setVisible(true);
    }

    public boolean showOnePixel(int x, int y) {

        GraphicsContext gc = drawingGround.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException exc) {
                throw new Error("Unexpected interruption", exc);
            }
            Platform.runLater(() -> pixelWriter.setColor(x, y, Color.BLUEVIOLET));
        });
        thread.setDaemon(true);
        thread.start();

//        double height = drawingGround.getHeight() / 2;
//        double width = drawingGround.getWidth() / 2;

        boolean cont = true;
        if(y > drawingGround.getHeight() || y<0) cont = false;
        return cont;
    }

    private void writePixelsSimple(GraphicsContext gc, double height, double width)
    {
        // Create the PixelWriter
        PixelWriter pixelWriter = gc.getPixelWriter();

        // Define the PixelFormat
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();

        int h = (int) height;
        int w = (int) width;

        Color c = new Color(Math.random(),Math.random(),Math.random(),Math.random());

//            System.out.println("Wert von w "+w);
        colorSome(true,false, w, h, height, c, pixelWriter);
        colorSome(true,true, w, h, height, c, pixelWriter);
        colorSome(false,true, w, h, height, c, pixelWriter);
        colorSome(false,false, w, h, height, c, pixelWriter);


    }


    private void colorSome(boolean oben, boolean links, int w, int h, double height, Color c, PixelWriter pixelWriter) {
        int wi = 1;
        int hi = 1;
        if (oben) hi = -1;
        if (links) wi = -1;
        for (w = w; w < 500 && w > 0; w = w + wi) {
            h = (int) height;
            double red = c.getRed() + (Math.random() - 0.5) * 0.1;
            if (red > 1) red = 1;
            else if (red < 0) red = 0;
            double green = c.getGreen() + (Math.random() - 0.5) * 0.1;
            if (green > 1) green = 1;
            else if (green < 0) green = 0;
            double blue = c.getBlue() + (Math.random() - 0.5) * 0.1;
            if (blue > 1) blue = 1;
            else if (blue < 0) blue = 0;
            c = new Color(red, green, blue, 1);

            for (h = h; h < 500 && h > 0; h = h + hi) {
//                System.out.println(w+"  "+h);
//            c.deriveColor(c.getBlue(), c.getGreen(), c.getHue(), c.getRed());

                pixelWriter.setColor(w, h, c);
            }
        }
    }

}
