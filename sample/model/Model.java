package sample.model;

import javafx.scene.paint.Color;

import java.util.*;

public class Model {

private Color[] [] deprecatedModel;
private int [] firstPixel;
double changerate = 0.1;
double changeRateUserComponent = 0.1;
private int lastMinimalCounterState = 1;
int cornerCounter = 0;

private final Color startTransparentColor = new Color(0,0,0,0);

//New
private Map<Key, PixelPoint> pixelModel = new HashMap<Key, PixelPoint>();
private PixelPoint middlePixel;
private int granularity = 400;
private PixelPoint notColoured;
private int height;
private int width;
private boolean flowageMode = true;
private List<PixelPoint> holeMiddles = new ArrayList<PixelPoint>();
private List<Double> holeRadius = new ArrayList<Double>();

//TODO Use 2d Points import for coordinates
//
// TODO 
// TODO Check every corner before stopping fillCorners


    private ArrayList<Integer[]> kitHole = new ArrayList<Integer[]>();
private boolean tokenKitHole = true;

    private boolean fillCorner() {
//
//        //TODO Eventuell wie fillHoles ?
//        cornerCounter = 0;
//        if(! deprecatedModel[0][0].equals(startTransparentColor) && ! deprecatedModel[deprecatedModel.length-1][0].equals(startTransparentColor)) {
////            System.out.println("Ended");
//            return true;
//        }
//            for(int i = firstPixel[0]; i< deprecatedModel.length; i++) {
//            for(int v = 0; v< deprecatedModel[0].length; v++){
//                if(deprecatedModel[i][v].equals(startTransparentColor)) {
//                    deprecatedModel[i][v] = determineColorByOtherPixels(i, v);
//                    cornerCounter++;
//                }
//            }
//            if(cornerCounter > 1) break;
//            }
//
//        cornerCounter = 0;
//        for(int i = firstPixel[0]; i< deprecatedModel.length; i--) {
//            for(int v = 0; v< deprecatedModel[0].length; v++){
//                if(deprecatedModel[i][v].equals(startTransparentColor)) {
//                    deprecatedModel[i][v] = determineColorByOtherPixels(i, v);
//                    cornerCounter++;
//                }
//            }
//            if(cornerCounter > 1) break;
//        }

        return false;
    }

    public boolean addPixels(int counter) {
//        if(! pixelModel[firstPixel[0]][0].equals(startTransparentColor)) {
////        if(lastMinimalCounterState > firstPixel[1]) {
////            System.out.println("last "+lastMinimalCounterState);
//            return fillCorner();
//        }

        //TODO Verästelung

        //TODO Fließbewegung

        return floatMovement(counter);
    }

    private boolean floatMovement(int counter) {

            if (counter <= granularity || holeMiddles.size() > 0) {
                //Naive Approach over the whole map. On my computer there are no performance problems.
                //If there are problems, you could use a copy of pixelModel and remove the set pixels maybe

                double currentAltitude = 1 - (double)counter / granularity;
                double oldAltitude = 1 - (double) (counter - 1) / granularity;

                pixelModel.forEach( (key, pixel) -> {
                        if (pixel.getAltitude() >= currentAltitude && pixel.getAltitude() < oldAltitude) {
                            pixel.setColor(determineNewPixelColor(pixel,1));
                        }
                        else if(pixel.isHoleBorder()) {
                            pixel.setColor(determineNewPixelColor(pixel,1));
                            pixel.setHoleBorder(false);
                        }
                        else{

                            for(int i=0; i<holeMiddles.size(); i++){
                                double distance = holeMiddles.get(i).distance(pixel);
                                Double radius = holeRadius.get(i);
                                if(radius == -1) {
                                    holeMiddles.remove(i);
                                    holeRadius.remove(i);
                                }
                                else if(distance <= radius && distance > radius -1.0){
                                    pixel.setHoleBorder(true);
                                }
                            }
                        }
                });

                for(int i=0; i<holeRadius.size(); i++){
                    holeRadius.set(i, holeRadius.get(i)-1);
                }
                return false;
            }

            else return true;
    }

    private Color determineNewPixelColor(PixelPoint pixel, int searchArea) {
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;
        int includedPixels = 0;

        for(int x=-searchArea; x<searchArea+1; x++){
            for(int y=-searchArea; y<searchArea+1; y++){
                PixelPoint p = pixelModel.getOrDefault(new Key((int) pixel.getX()+x, (int) pixel.getY()+y), middlePixel);
                if(! p.getColor().equals(startTransparentColor)){
                    red += p.getColor().getRed();
                    green += p.getColor().getGreen();
                    blue += p.getColor().getBlue();
                    includedPixels+=1;
                }
            }
        }
        if(includedPixels == 0) {
            return determineNewPixelColor(pixel, searchArea+1);
            //Yeah, I can use recursion in a probably meaningful context
        }

        red/=includedPixels;
        green/=includedPixels;
        blue/=includedPixels;
        red += (Math.random() - 0.5)*changerate;
        if(red > 1.0) red = 1.0;
        else if(red < 0.0) red = 0.0;

        green += (Math.random() - 0.5)*changerate;
        if(green > 1.0) green = 1.0;
        else if(green < 0.0) green = 0.0;

        blue += (Math.random() - 0.5)*changerate;
        if(blue > 1.0) blue = 1.0;
        else if(blue < 0.0) blue = 0.0;

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
                pixelModel.put(new Key(x,y), pixel);
                //TODO
            }
        }
        pixelModel.put(new Key((int) middlePixel.getX(), (int) middlePixel.getY()), middlePixel);

        this.setChangeRate(changeRateUserComponent);

        if(flowageMode) {
            granularity = 200;
            addRandomObstacles();
        }
        else  granularity = 400;
    }

    private void addRandomObstacles() {
        for(int i=0; i<100000; i++) {
//            long space = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//            if(space > 200000000) System.out.println("Used space: "+space+ "  Total space: "+Runtime.getRuntime().totalMemory());

            Key pos = new Key((int) (Math.random() * width), (int) (Math.random() * height));
//            System.out.println(pos.getX()+ "             "+pos.getY());
            PixelPoint p = pixelModel.get(pos);
            double newAlt = p.getAltitude() - Math.random()/300;
            int size = 5;
            for (int x = (int)(- Math.random()*size); x < (int)(Math.random()*size); x++) {
                for (int y = (int) (- Math.random()*size); y < (int)(Math.random()*size); y++) {
//                    System.out.println(x+" "+y);
                    Key k = new Key(pos.getX()+x, pos.getY()+y);
                    if(pixelModel.containsKey(k)){
//                        System.out.println(k.getX()+"  "+k.getY());
                        PixelPoint pn = pixelModel.get(k);
                        pn.setAltitude(newAlt <= 0.0 ? pn.getAltitude() : newAlt);
                    }

                }
            }
        }
    }


    //TODO Gradle

    public void makeHole(Key middlePosition, int radius) {
        PixelPoint middle = pixelModel.get(middlePosition);
        pixelModel.forEach( (key, pixel) -> {
            double distance = middle.distance(pixel);
            if(distance <= radius) {
                pixel.setColor(startTransparentColor);
                if(distance > radius-1) {
                    pixel.setHoleBorder(true);
                }
            }
        });
        holeMiddles.add(middle);
        holeRadius.add(radius-1.0);
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

    public Color[][] getDeprecatedModel() {
        return deprecatedModel;
    }

    public void deleteHoleData() {
        this.holeMiddles.clear();
        this.holeRadius.clear();
    }

    public double getChangeRateUserComponent() {
        return changeRateUserComponent;
    }

    public void setChangeRate (double changeRateUserComponent) {
        this.changeRateUserComponent = changeRateUserComponent;
        this.changerate = changeRateUserComponent;

    }

    public Map<Key, PixelPoint> getPixelModel() {
        return pixelModel;
    }

    public void setPixelModel(Map<Key, PixelPoint> pixelModel) {
        this.pixelModel = pixelModel;
    }

    public void setFlowageMode(boolean flowage) {
        flowageMode = flowage;
//        if(flowageMode) granularity = 400;
//        else granularity = 50;
    }
}