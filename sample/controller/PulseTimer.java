package sample.controller;

import javafx.animation.AnimationTimer;
import sample.model.Model;
import sample.view.View;

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
        boolean ready = model.addPixels(counter);
        if(ready) {
            stop();
            return;
        }
        view.getMainScene().writePixels(model.getPixelModel());
        counter += 1;
    }
}
