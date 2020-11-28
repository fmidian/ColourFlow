package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Model {

private final Color startTransparentColor = new Color(0,0,0,0);

private Map<Key, PixelPoint> pixelModel = new HashMap<Key, PixelPoint>();
private PixelPoint middlePixel;
private int granularity = 400;
private int height;
private int width;
private boolean flowageMode = true;
private List<PixelPoint> middlePointsOfHoles = new ArrayList<PixelPoint>();
private List<Double> radiusOfHoles = new ArrayList<Double>();
private double changerate = 0.1;


    public boolean addPixels(int counter) {
        //Naive Approach over the whole map. On my computer there are no performance problems.
        //If there are problems, you could use a copy of pixelModel and remove the set pixels maybe

        if (counter <= granularity || middlePointsOfHoles.size() > 0) {
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
                    for(int i = 0; i< middlePointsOfHoles.size(); i++){
                        double distance = middlePointsOfHoles.get(i).distance(pixel);
                        Double radius = radiusOfHoles.get(i);
                        if(radius == -1) {
                            middlePointsOfHoles.remove(i);
                            radiusOfHoles.remove(i);
                        }
                        else if(distance <= radius && distance > radius -1.0){
                            pixel.setHoleBorder(true);
                        }
                    }
                }
            });

            for(int i = 0; i< radiusOfHoles.size(); i++){
                radiusOfHoles.set(i, radiusOfHoles.get(i)-1);
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

    public void generatePixelModel(Map<String, Integer> size) {
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

        if(flowageMode) {
            granularity = 200;
            addRandomObstacles();
        }
        else  granularity = 400;
    }

    private void addRandomObstacles() {
        Instant start = Instant.now();

        Key pos;
        int size = 5;
        final SplittableRandom random = new SplittableRandom();
        for(int i=0; i<100000; i++) {
            pos = new Key((random.nextInt(width)), random.nextInt(height));
            PixelPoint p = pixelModel.get(pos);
            double newAlt = p.getAltitude() - random.nextDouble()/300;
            if(newAlt > 0){
                for (int x = -random.nextInt(size); x < random.nextInt(size); x++) {
                    for (int y = -random.nextInt(size); y < random.nextInt(size); y++) {
                        Key k = new Key(pos.getX()+x, pos.getY()+y);
                        if(pixelModel.containsKey(k)){
                            PixelPoint pn = pixelModel.get(k);
                            pn.setAltitude(newAlt);
                        }
                        else break;
                    }
                }
            }

        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);
    }

    /*
            final SplittableRandom random = new SplittableRandom();
        for(int i=0; i<100000; i++) {
            pos = new Key((random.nextInt(width)), random.nextInt(height));
     */

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
        middlePointsOfHoles.add(middle);
        radiusOfHoles.add(radius-1.0);
    }

    public void deleteHoleData() {
        this.middlePointsOfHoles.clear();
        this.radiusOfHoles.clear();
    }

    public void setChangeRate (double changeRateUserComponent) {
        this.changerate = changeRateUserComponent;
    }

    public Map<Key, PixelPoint> getPixelModel() {
        return pixelModel;
    }

    public void setFlowageMode(boolean flowage) {
        flowageMode = flowage;
    }

    public void addImage(Image image){
        Map<Key, PixelPoint> map = new HashMap<Key, PixelPoint>();

        double smallestRed = 1.0;
        double biggestRed = 0.0;
        double imageSmallestRed = 1.0;
        double imageBiggestRed = 0.0;

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        int startX = (int) (width - imageWidth)/2;
        int startY = (int) (height - imageHeight)/2;

        if(startX > 0 && startY >0){
//            for (int x = 0; x < imageWidth; x++) {
//                for (int y = 0; y < imageHeight; y++) {
//                    double red = pixelModel.get(new Key(x,y)).getColor().getRed();
//                    if(red < smallestRed) smallestRed = red;
//                    else if(red > biggestRed) biggestRed = red;
//
//                    double imageRed = image.getPixelReader().getColor(x, y).getRed();
//                    if(imageRed < imageSmallestRed) imageSmallestRed = imageRed;
//                    else if(imageRed > imageBiggestRed) imageBiggestRed = imageRed;
//                }
//            }
//            System.out.println("Smallest Red "+smallestRed+"    "+"Biggest Red "+biggestRed);
//            System.out.println("Smallest Image Red "+imageSmallestRed+"    "+"Biggest ImageRed "+imageBiggestRed);

            //TODO Umso mittiger, umso eher BildPixel

            double imageRatio = 0.3;
            PixelPoint imageMiddle = new PixelPoint((int) imageWidth/2, (int) imageHeight/2, 0);
            double longestDistance = imageMiddle.distance(new PixelPoint(0,0,0));

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    double red = image.getPixelReader().getColor(x, y).getRed();
                    double green = image.getPixelReader().getColor(x, y).getGreen();
                    double blue = image.getPixelReader().getColor(x, y).getBlue();

//                    if(red>=0.95 && green>=0.95 && blue>=0.95) continue;

                    PixelPoint imagePixel = new PixelPoint(x,y,0);
                    double dist = imagePixel.distance(imageMiddle);
                    imageRatio = 1 - dist/longestDistance - 0.3;
                    if(imageRatio<0) imageRatio = 0;

                    PixelPoint p = pixelModel.get(new Key(x+startX,y+startY));
                    Color old = p.getColor();
                    p.setColor(new Color(old.getRed()*(1-imageRatio)+red*imageRatio, old.getGreen()*(1-imageRatio)+green*imageRatio, old.getBlue()*(1-imageRatio)+blue*imageRatio, 1));
                }
            }
        }

    }
}