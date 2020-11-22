package sample.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import sample.model.Model;
import sample.view.View;

import java.nio.IntBuffer;

public class PulseTimer extends AnimationTimer {

    private int counter = 1;
    private Model model;
    private View view;

    public PulseTimer(View view, Model model) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void handle(long now) {
        doHandle();
    }


    private void doHandle() {
        Color [][] pixelModel = model.getPixelModel();
//        if(counter > pixelModel.length || counter > pixelModel[0].length) stop();
        boolean ready = model.addPixels(counter);
        if(ready) {
            stop();
            return;
        }
        view.getMainScene().writePixels(pixelModel);
        counter+=1;

        //stop wenn Button fehlt
    }
}
