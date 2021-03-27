package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import model.Key;
import model.Model;
import view.View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class Controller {
    private View view;
    private Model model;
    private PulseTimer timer;
    private Properties properties = new Properties();

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        initData();

        try {
            InputStream input = new FileInputStream("src/GoogleApi.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initAnimation(){
        view.getMainScene().writePixels(model.getPixelModel());
        timer = new PulseTimer(view, model);
        timer.start();
    }

    private void initData() {
        Map m = view.getSizeOfCanvas();
        model.generatePixelModel(m);
    }

    public void deleteAllData() {
        timer.stop();
        initData();
        view.getMainScene().writePixels(model.getPixelModel());
        model.deleteHoleData();
    }

    public void sliderValueChanged(double value){
        value /= 100;
        model.setChangeRate(value);
    }

    public void flowageValueChanged(boolean flowage){
        model.setFlowageMode(flowage);
    }

    public void deleteHoleInDataAndRefill(long size, MouseEvent event) {

        Key middlePixel = new Key((int) event.getSceneX(), (int) event.getSceneY());
        model.makeHole(middlePixel, (int) size/10);
        timer.start();
    }

    public void setImage(String query){

        URL url = null;
        try {
            String apiKey = properties.getProperty("googleApiKey");
            String searchEngineID = properties.getProperty("searchEngineID");


            url = new URL("https://www.googleapis.com/customsearch/v1?key="+apiKey+"&cx="+searchEngineID+"&searchType=image&imgSize=medium&q="+query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int status = 0;
        try {
            status = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(status==200) {

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
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

            //TODO Hide API Key

            con.disconnect();

            Gson g = new Gson();
            JsonObject json = g.fromJson(content.toString(), JsonObject.class);
            JsonArray items = json.get("items").getAsJsonArray();
            String link = items.get(0).getAsJsonObject().get("link").toString();
            link = link.substring(1, link.length() - 1);

            URL linkUrl = null;
            try {
                linkUrl = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return;
            }
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(linkUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            model.addImage(SwingFXUtils.toFXImage(bufferedImage, null));
            view.getMainScene().writePixels(model.getPixelModel());
        }
    }
}