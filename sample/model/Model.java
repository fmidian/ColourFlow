package sample.model;

import javafx.scene.paint.Color;

import java.util.*;

public class Model {

private Color[] [] pixelModel;
private int [] firstPixel;
private final Color startTransparentColor = new Color(0,0,0,0);
double changerate = 0.1;
double changeRateUserComponent = 0.1;
private int lastMinimalCounterState = 1;
int cornerCounter = 0;

//New
private Map<Key, PixelPoint> pm = new HashMap<Key, PixelPoint>();
private PixelPoint middlePixel;
private final int granularity = 200;
private PixelPoint notColoured;
private int height;
private int width;

//TODO Use 2d Points import for coordinates
//
// TODO 
// TODO Check every corner before stopping fillCorners


    private ArrayList<Integer[]> kitHole = new ArrayList<Integer[]>();
private boolean tokenKitHole = true;

    private boolean fillCorner() {

        //TODO Eventuell wie fillHoles ?
        cornerCounter = 0;
        if(! pixelModel[0][0].equals(startTransparentColor) && ! pixelModel[pixelModel.length-1][0].equals(startTransparentColor)) {
//            System.out.println("Ended");
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

    public void addPixels(int counter) {
//        if(! pixelModel[firstPixel[0]][0].equals(startTransparentColor)) {
////        if(lastMinimalCounterState > firstPixel[1]) {
////            System.out.println("last "+lastMinimalCounterState);
//            return fillCorner();
//        }

        //TODO Verästelung

        //TODO Fließbewegung

        floatMovement(counter);
    }

    private void floatMovement(int counter) {
        for(int c = counter; c<counter+1; c++){
            if (c <= granularity) {
                //Naiver Ansatz über gesamte Map
                //TODO

                double currentAltitude = 1 - (double)c / granularity;
                double oldAltitude = 1 - (double) (c - 1) / granularity;
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        PixelPoint pixel = pm.get(new Key(x, y));
                        if (pixel.getAltitude() >= currentAltitude && pixel.getAltitude() < oldAltitude) {
                            pixel.setColor(detNewPixelColor(pixel,1));
                        }
                    }
                }
            }
        }



        //TODO Maybe copy pm and remove Values there
//        public void iterateUsingEntrySet(Map<String, Integer> map) {
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                System.out.println(entry.getKey() + ":" + entry.getValue());
//            }
//        }
    }

    private boolean circleMovement(int counter) {
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

    private Color detNewPixelColor (PixelPoint pixel, int searchArea) {
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;
        int includedPixels = 0;

        for(int x=-searchArea; x<searchArea+1; x++){
            for(int y=-searchArea; y<searchArea+1; y++){
                PixelPoint p = pm.getOrDefault(new Key((int) pixel.getX()+x, (int) pixel.getY()+y), middlePixel);
                if(! p.getColor().equals(startTransparentColor)){
                    red += p.getColor().getRed();
                    green += p.getColor().getGreen();
                    blue += p.getColor().getBlue();
                    includedPixels+=1;
                }
            }
        }
        if(includedPixels == 0) {
//            red += middlePixel.getColor().getRed();
//            green += middlePixel.getColor().getGreen();
//            blue += middlePixel.getColor().getBlue();
//            red=0;
//            blue=0;
//            green=0;
//            includedPixels+=1;
            System.out.println(searchArea);
            return detNewPixelColor(pixel, searchArea+1);
            //Yaaaai Rekursion
        }

        boolean modeChangeAll = Math.random() > 0.995;
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

    @Deprecated
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

    @Deprecated
    public void generatePixelModel(Map<String, Integer> size) {
//        this.pixelModel = new Color [size.get("height")][size.get("width")];
//        for (Color[] row: pixelModel)
//            Arrays.fill(row, startTransparentColor);
//        firstPixel = new int[] {(int)Math.round(size.get("height") / 2.0), (int)Math.round(size.get("width") / 2.0)};
//        Color startColor = new Color(Math.random(),Math.random(),Math.random(),1);
//        pixelModel[firstPixel[0]][firstPixel[1]] = startColor;
//
//        this.setChangeRate(changeRateUserComponent);
//        lastMinimalCounterState = 1;
        this.generateAltitude(size);
    }

    private void generateAltitude(Map<String, Integer> size) {
        Objects.requireNonNull(size);
        height = size.get("height");
        width = size.get("width");
        middlePixel = new PixelPoint((int)Math.round( width / 2.0), (int)Math.round( height/ 2.0), 1.0);
        middlePixel.setColor(new Color(Math.random(), Math.random(), Math.random(),1));
        double maximumDistance = middlePixel.distance(new PixelPoint (0,0,0));
        for(int x=0; x<(size.get("width")); x++){
            for(int y=0; y<(size.get("height")); y++) {
                PixelPoint pixel = new PixelPoint(x,y, 0, startTransparentColor);
                double altitude = 1 - pixel.distance(middlePixel) / maximumDistance;
                pixel.setAltitude(altitude);
                pm.put(new Key(x,y), pixel);
                //TODO
            }
        }
        pm.put(new Key((int) middlePixel.getX(), (int) middlePixel.getY()), middlePixel);

        this.setChangeRate(changeRateUserComponent);
        randomObstacle();
    }

    private void randomObstacle() {
        for(int i=0; i<10000; i++) {
            Key pos = new Key((int) (Math.random() * width), (int) (Math.random() * height));
//            System.out.println(pos.getX()+ "             "+pos.getY());
            PixelPoint p = pm.get(pos);
            double newAlt = p.getAltitude() - Math.random()/500;
            for (int x = (int)(- Math.random()*8); x < (int)(Math.random()*8); x++) {
                for (int y = (int) (- Math.random()*8); y < (int)(Math.random()*8); y++) {
//                    System.out.println(x+" "+y);
                    Key k = new Key(pos.getX()+x, pos.getY()+y);
                    if(pm.containsKey(k)){
//                        System.out.println(k.getX()+"  "+k.getY());
                        PixelPoint pn = pm.get(k);
                        pn.setAltitude(newAlt <= 0.0 ? pn.getAltitude() : newAlt);
                    }

                }
            }
        }
    }


    //TODO Gradle

    public void makeHole(int[] location, int radius) {
        double distance = -1;
        for(int i=0; i<pixelModel.length; i++) {
            for(int v=0; v<pixelModel[0].length; v++){
                distance = Math.sqrt((Math.pow(v - location[1],2)) + Math.pow((i - location[0]),2));
                if(distance <= radius) {
                    pixelModel[i][v] = startTransparentColor;
                }
            }
        }
    }

    public void initRefillHole(List<Integer[]> border){
        tokenKitHole = false;
//        System.out.println("Used by init");
        for(Integer[] b: border){
            this.kitHole.add(b);
        }
//        System.out.println("init ready");

        tokenKitHole = true;
    }

    public Color[][] getPixelModel() {
        return pixelModel;
    }

    public void refillHole(){
        if(tokenKitHole == false) {
            return;
        }
        tokenKitHole = false;
        Integer [] n;
        try {
            if (kitHole.size() > 0) {
                ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
                boolean contained = false;
                for (Integer[] pixel : kitHole) {
                    pixelModel[pixel[0]][pixel[1]] = determineColorByOtherPixels(pixel[0], pixel[1]);
                    if (pixel[0] == 0 || pixel[0] == pixelModel.length - 1 || pixel[1] == 0 || pixel[1] == pixelModel[0].length - 1) {
                        continue;
                    }
                    if (pixelModel[pixel[0] - 1][pixel[1] - 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] - 1, pixel[1] - 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals(n[0]) && x[1].equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }

                    if (pixelModel[pixel[0]][pixel[1] - 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0], pixel[1] - 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals(n[0]) && x[1].equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0] - 1][pixel[1]].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] - 1, pixel[1]};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals(n[0]) && x[1].equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0] + 1][pixel[1] - 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] + 1, pixel[1] - 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals(n[0]) && x[1].equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0] - 1][pixel[1] + 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] - 1, pixel[1] + 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals( n[0]) && x[1].equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0] + 1][pixel[1]].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] + 1, pixel[1]};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals( n[0]) && x[1].equals( n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0]][pixel[1] + 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0], pixel[1] + 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0].equals(n[0]) && x[1] .equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                    if (pixelModel[pixel[0] + 1][pixel[1] + 1].equals(startTransparentColor)){
                        n = new Integer[]{pixel[0] + 1, pixel[1] + 1};
                        contained = false;
                        for(Integer[] x: temp){
                            if(x[0] .equals(n[0]) && x[1] .equals(n[1])) {
                                contained = true;
                                break;
                            }
                        }
                        if(!contained) temp.add(n);
                    }
                }
                kitHole.clear();
                kitHole.addAll(temp);
            }
        }
        catch(ConcurrentModificationException e){
            System.out.println("Exception catched");
            //TODO
        }
        tokenKitHole = true;
    }

    public void deleteHoleData() {
        this.kitHole.clear();
    }

    public double getChangeRateUserComponent() {
        return changeRateUserComponent;
    }

    public void setChangeRate (double changeRateUserComponent) {
        this.changeRateUserComponent = changeRateUserComponent;
        this.changerate = changeRateUserComponent+0.05;

    }

    public Map<Key, PixelPoint> getPm() {
        return pm;
    }

    public void setPm(Map<Key, PixelPoint> pm) {
        this.pm = pm;
    }
}