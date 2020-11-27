package sample.controller;

import javafx.animation.AnimationTimer;
import sample.model.Model;
import sample.view.View;

public class PulseTimer extends AnimationTimer {

    private int counter = 1;
    private Model model;
    private View view;

    long biggestSpace = 0;

    public PulseTimer(View view, Model model) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void handle(long now) {
        doHandle();
    }


    private void doHandle() {
//        Color [][] pixelModel = model.getPixelModel();
//        if(counter > pixelModel.length || counter > pixelModel[0].length) stop();
        boolean ready = model.addPixels(counter);
        if(ready) {
            stop();
            return;
        }
        view.getMainScene().writePixels(model.getPixelModel());

        model.refillHole();

        counter += 1;

        //TODO Save counter step

//        long space = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        if(space > 200000000) {
//            if(space > biggestSpace) biggestSpace = space;
//            System.out.println("Biggest space: "+biggestSpace+ "  Total space: "+Runtime.getRuntime().totalMemory());
//        }

        //stop wenn Button fehlt
    }
}
