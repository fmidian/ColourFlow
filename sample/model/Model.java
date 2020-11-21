package sample.model;

public class Model {

    private int countDown;

    public Model() {
        countDown = 10;
    }

    public int getCountDown() {
        return countDown;
    }

    public void tick() {
        if (countDown > 0) {
            countDown--;
        }
    }

}