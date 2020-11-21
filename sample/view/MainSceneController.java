package sample.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.nio.ByteBuffer;

public class MainSceneController {

    @FXML
    private Button button;

    @FXML
    private Canvas drawingGround;

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

        writePixelsSimple(gc, height, width);



    }

    private void writePixelsSimple(GraphicsContext gc, double height, double width)
    {
        // Create the PixelWriter
        PixelWriter pixelWriter = gc.getPixelWriter();

        // Define the PixelFormat
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();

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

    private void writePixels(GraphicsContext gc)
    {
        // Define properties of the Image
        int spacing = 5;
        int imageWidth = 300;
        int imageHeight = 100;
        int rows = imageHeight/(RECT_HEIGHT + spacing);
        int columns = imageWidth/(RECT_WIDTH + spacing);

        // Get the Pixels
        byte[] pixels = this.getPixelsData();

        // Create the PixelWriter
        PixelWriter pixelWriter = gc.getPixelWriter();

        // Define the PixelFormat
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();

        // Write the pixels to the canvas
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < columns; x++)
            {
                int xPos = 50 + x * (RECT_WIDTH + spacing);
                int yPos = 50 + y * (RECT_HEIGHT + spacing);
                pixelWriter.setPixels(xPos, yPos, RECT_WIDTH, RECT_HEIGHT,
                        pixelFormat, pixels, 0, RECT_WIDTH * 3);
            }
        }
    }

    private byte[] getPixelsData()
    {
        // Create the Array
        byte[] pixels = new byte[RECT_WIDTH * RECT_HEIGHT * 3];
        // Set the ration
        double ratio = 1.0 * RECT_HEIGHT/RECT_WIDTH;
        // Generate pixel data
        for (int y = 0; y < RECT_HEIGHT; y++)
        {
            for (int x = 0; x < RECT_WIDTH; x++)
            {
                int i = y * RECT_WIDTH * 3 + x * 3;
                if (x <= y/ratio)
                {
                    pixels[i] = -1;
                    pixels[i+1] = 1;
                    pixels[i+2] = 0;
                }
                else
                {
                    pixels[i] = 1;
                    pixels[i+1] = 1;
                    pixels[i+2] = 0;
                }
            }
        }

        // Return the Pixels
        return pixels;
    }
}
