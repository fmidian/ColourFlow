package sample.model;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Map;

public class Model {

private Color[] [] pixelModel;
private int [] firstPixel;
private final Color startTransparentColor = new Color(0,0,0,0);
double changerate = Math.random()*0.2;
private int lastMinimalCounterState = 1;
int cornerCounter = 0;



    public Model() {
    }

    private boolean fillCorner() {
        cornerCounter = 0;
        if(! pixelModel[0][0].equals(startTransparentColor)) {
            System.out.println("Ended");
            return true;
        }
            for(int i=firstPixel[0]; i<pixelModel.length; i++) {
            for(int v=0; v<pixelModel[0].length; v++){
                if(pixelModel[i][v].equals(startTransparentColor)) {
                    pixelModel[i][v] = determineColorByOtherPixels(i, v);
                    cornerCounter++;
                }
            }
            if(cornerCounter > 1) break;
            }

        cornerCounter = 0;
        for(int i=firstPixel[0]; i<pixelModel.length; i--) {
            for(int v=0; v<pixelModel[0].length; v++){
                if(pixelModel[i][v].equals(startTransparentColor)) {
                    pixelModel[i][v] = determineColorByOtherPixels(i, v);
                    cornerCounter++;
                }
            }
            if(cornerCounter > 1) break;
        }

        return false;
    }

    public boolean addPixels(int counter) {
        if(! pixelModel[firstPixel[0]][0].equals(startTransparentColor)) {
//        if(lastMinimalCounterState > firstPixel[1]) {
//            System.out.println("last "+lastMinimalCounterState);
            return fillCorner();
        }
        int yPos = firstPixel[0] - counter;
        int xPos = firstPixel[1] - counter;
        double distance = 1;
        boolean allPixelsFilled = true;

        for(int z=lastMinimalCounterState; z<= counter; z++) {
            allPixelsFilled = true;
            yPos = firstPixel[0] - z;
            xPos = firstPixel[1] - z;

            for (yPos = yPos; yPos < firstPixel[0] + z; yPos++) {
//            yPos = yPos >= pixelModel.length ? pixelModel.length-1 : yPos;
                if (xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) {
                    continue;
                }
                distance = Math.sqrt((Math.pow(xPos - firstPixel[1],2)) + Math.pow((yPos - firstPixel[0]),2));
                if (distance <= counter) {
                    pixelModel[yPos][xPos] = determineColorByOtherPixels(yPos, xPos);
                } else {
                    allPixelsFilled = false;
                }
            }

            for (xPos = xPos; xPos < firstPixel[1] + z; xPos++) {
                if (xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) {
                    continue;
                }
//            xPos = xPos >= pixelModel[0].length ? pixelModel[0].length-1 : xPos;
//            yPos = yPos >= pixelModel.length ? pixelModel.length-1 : yPos;
                distance = Math.sqrt((Math.pow(xPos - firstPixel[1],2)) + Math.pow((yPos - firstPixel[0]),2));
                if (distance <= counter) {
                    pixelModel[yPos][xPos] = determineColorByOtherPixels(yPos, xPos);
                } else {
                    allPixelsFilled = false;
                }
            }

            for (yPos = yPos; yPos > firstPixel[0] - z; yPos--) {
                if (xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0){
                    continue;
                }
//            yPos = yPos<0 ? 0 : yPos;
//            xPos = xPos >= pixelModel[0].length ? pixelModel[0].length-1 : xPos;
                distance = Math.sqrt((Math.pow(xPos - firstPixel[1],2)) + Math.pow((yPos - firstPixel[0]),2));
                if (distance <= counter) {
                    pixelModel[yPos][xPos] = determineColorByOtherPixels(yPos, xPos);
                } else {
                    allPixelsFilled = false;
                }
            }

            for (xPos = xPos; xPos > firstPixel[1] - z; xPos--) {
                if (xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) {
                    continue;
                }
//            xPos = xPos<0 ? 0 : xPos;
//            yPos = yPos<0 ? 0 : yPos;
                double test = (Math.pow(xPos - firstPixel[1],2)) + Math.pow((yPos - firstPixel[0]),2);
                distance = Math.sqrt(test);
                if (distance <= counter) {
                    pixelModel[yPos][xPos] = determineColorByOtherPixels(yPos, xPos);
                } else {
                    allPixelsFilled = false;
                }
            }
            if(allPixelsFilled) lastMinimalCounterState = z>lastMinimalCounterState? z : lastMinimalCounterState;

        }
        return false;
    }

    private Color determineColorByOtherPixels(int yPos, int xPos) {
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;
        int includedPixels = 0;

        if(yPos > 0 && ! pixelModel[yPos-1][xPos].equals(startTransparentColor)) {
            red += pixelModel[yPos-1][xPos].getRed();
            green += pixelModel[yPos-1][xPos].getGreen();
            blue += pixelModel[yPos-1][xPos].getBlue();
            includedPixels+=1;
        }
        if(yPos > 0 && xPos > 0 && ! pixelModel[yPos-1][xPos-1].equals(startTransparentColor)) {
            red += pixelModel[yPos-1][xPos-1].getRed();
            green += pixelModel[yPos-1][xPos-1].getGreen();
            blue += pixelModel[yPos-1][xPos-1].getBlue();
            includedPixels+=1;
        }
        if(xPos > 0 && yPos <pixelModel.length-1 && ! pixelModel[yPos+1][xPos-1].equals(startTransparentColor)) {
            red += pixelModel[yPos+1][xPos-1].getRed();
            green += pixelModel[yPos+1][xPos-1].getGreen();
            blue += pixelModel[yPos+1][xPos-1].getBlue();
            includedPixels+=1;
        }
        if(yPos <pixelModel.length-1 && xPos < pixelModel[0].length-1 && ! pixelModel[yPos+1][xPos+1].equals(startTransparentColor)) {
            red += pixelModel[yPos+1][xPos+1].getRed();
            green += pixelModel[yPos+1][xPos+1].getGreen();
            blue += pixelModel[yPos+1][xPos+1].getBlue();
            includedPixels+=1;
        }
        if(includedPixels == 0) {
            red += pixelModel[firstPixel[1]][firstPixel[0]].getRed();
            green += pixelModel[firstPixel[1]][firstPixel[0]].getGreen();
            blue += pixelModel[firstPixel[1]][firstPixel[0]].getBlue();
            includedPixels+=1;
        }

        boolean modeChangeAll = Math.random() > 0.99995;
        red/=includedPixels;
        green/=includedPixels;
        blue/=includedPixels;
        double pickChangedColour = Math.random();
        if(modeChangeAll || pickChangedColour < 0.33) {
            red += (Math.random() - 0.5)*changerate;
            if(red > 1.0) red = 1.0;
            else if(red < 0.0) red = 0.0;
        }
        if(modeChangeAll || pickChangedColour > 0.66){
            green += (Math.random() - 0.5)*changerate;
            if(green > 1.0) green = 1.0;
            else if(green < 0.0) green = 0.0;
        }
        if(modeChangeAll || (pickChangedColour > 0.33 && pickChangedColour <0.66)) {
            blue += (Math.random() - 0.5)*changerate;
            if(blue > 1.0) blue = 1.0;
            else if(blue < 0.0) blue = 0.0;
        }

        return new Color(red, green, blue, 1);
    }

    public void generatePixelModel(Map<String, Integer> size) {
        this.pixelModel = new Color [size.get("height")][size.get("width")];
        for (Color[] row: pixelModel)
            Arrays.fill(row, startTransparentColor);
        firstPixel = new int[] {(int)Math.round(size.get("height") / 2.0), (int)Math.round(size.get("width") / 2.0)};
        Color startColor = new Color(Math.random(),Math.random(),Math.random(),1);
        pixelModel[firstPixel[0]][firstPixel[1]] = startColor;

        changerate = Math.random()*0.08+0.05;
        lastMinimalCounterState = 1;
    }

    public Color[][] getPixelModel() {
        return pixelModel;
    }


}