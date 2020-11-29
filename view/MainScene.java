package view;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import controller.Controller;
import javafx.scene.paint.Color;
import model.Key;
import model.PixelPoint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    @FXML
    private TextField searchField;

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

    public void searchForImage(){
        String query = searchField.getText();
        System.out.println(query);
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDJ66meroj6dcVcTjzASEWRLMJSWidPCMg&cx=63af735600f800916&searchType=image&imgSize=medium&q="+query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int status = 0;
        try {
            status = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(status==200) {

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine = "";
            StringBuffer content = new StringBuffer();
            while (true) {
                try {
                    if (!((inputLine = in.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                content.append(inputLine);
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //TODO Refactor Image Code

            //TODO Hide API Key

            //TODO What if there is no Internet?

            //TODO Maybe bigger?
            con.disconnect();

            Gson g = new Gson();
            JsonObject json = g.fromJson(content.toString(), JsonObject.class);
            json.toString();
            JsonArray items = json.get("items").getAsJsonArray();
            String link = items.get(0).getAsJsonObject().get("link").toString();
            link = link.substring(1, link.length()-1);
            System.out.println(link);

            URL linkUrl = null;
            try {
                linkUrl = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(linkUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            controller.setImage(image);
        }
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
