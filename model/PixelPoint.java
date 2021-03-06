package model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PixelPoint extends Point2D
{
    private double altitude;
    //From 0.0 to 1.0

    private Color color =  new Color(0,0,0,0);

    private boolean isHoleBorder = false;

    public PixelPoint(int x, int y, double altitude) {
        super(x,y);
        this.altitude = altitude;
    }

    public PixelPoint(int x, int y, double altitude, Color color) {
        super(x,y);
        this.altitude = altitude;
        this.color = color;
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

    public boolean isHoleBorder() {
        return isHoleBorder;
    }

    public void setHoleBorder(boolean holeBorder) {
        isHoleBorder = holeBorder;
    }
}
