package sample.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PixelPoint extends Point2D
{
    private double altitude;
    //From 0.0 to 1.0

    private Color color =  new Color(0,0,0,0);


    public PixelPoint(int x, int y, double altitude) {
        super(x,y);
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
