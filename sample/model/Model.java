package sample.model;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Map;

public class Model {

private Color[] [] pixelModel;
private int [] firstPixel;


    public Model() {
    }

    public boolean addPixels(int counter) {
        if(! pixelModel[1][0].equals(new Color(0,0,0,0))) return true;
        int yPos = firstPixel[0] - counter;
        int xPos = firstPixel[1] - counter;

        for(yPos=yPos; yPos < firstPixel[0]+counter; yPos++){
//            yPos = yPos >= pixelModel.length ? pixelModel.length-1 : yPos;
            if(xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) continue;
            pixelModel[yPos][xPos] = new Color(Math.random(),Math.random(),Math.random(),1);
        }
        for(xPos=xPos; xPos < firstPixel[1]+counter; xPos++){
            if(xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) continue;
//            xPos = xPos >= pixelModel[0].length ? pixelModel[0].length-1 : xPos;
//            yPos = yPos >= pixelModel.length ? pixelModel.length-1 : yPos;
            pixelModel[yPos][xPos] = new Color(Math.random(),Math.random(),Math.random(),1);
        }
        for(yPos=yPos; yPos > firstPixel[0]-counter; yPos--){
            if(xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) continue;
//            yPos = yPos<0 ? 0 : yPos;
//            xPos = xPos >= pixelModel[0].length ? pixelModel[0].length-1 : xPos;
            pixelModel[yPos][xPos] = new Color(Math.random(),Math.random(),Math.random(),1);
        }
        for(xPos=xPos; xPos > firstPixel[1]-counter; xPos--){
            if(xPos >= pixelModel[0].length || xPos < 0 || yPos >= pixelModel.length || yPos < 0) continue;
//            xPos = xPos<0 ? 0 : xPos;
//            yPos = yPos<0 ? 0 : yPos;
            pixelModel[yPos][xPos] = new Color(Math.random(),Math.random(),Math.random(),1);
        }

        return false;
    }

    public void generatePixelModel(Map<String, Integer> size) {
        this.pixelModel = new Color [size.get("height")][size.get("width")];
        for (Color[] row: pixelModel)
            Arrays.fill(row, new Color(0,0,0,0));
        firstPixel = new int[] {(int)Math.round(size.get("height") / 2.0), (int)Math.round(size.get("width") / 2.0)};
        Color startColor = new Color(Math.random(),Math.random(),Math.random(),1);
        pixelModel[firstPixel[0]][firstPixel[1]] = startColor;
        for(int i=0; i<pixelModel.length; i--) {
            for (int y = 0; y < pixelModel[0].length; y++) {
                pixelModel[i][y] = Color.BLACK;
            }
            i+=pixelModel.length;
        }
//        pixelModel[0][0] = Color.BLACK;
//        pixelModel[pixelModel.length-1][0] = Color.BLACK;
//        pixelModel[0][0] = Color.BLACK;


//        System.out.println(pixelModel);
//        for(int i=0; i<pixelModel.length; i++) {
//            for (int y = 0; y < pixelModel[0].length; y++) {
//                if (pixelModel[i][y] != null) System.out.print(pixelModel[i][y] + " " + i + " " + y + " ||");
////                System.out.println(" ");
//            }
//        }
    }

    public Color[][] getPixelModel() {
        return pixelModel;
    }


}