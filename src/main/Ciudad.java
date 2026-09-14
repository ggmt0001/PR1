package main;

public class Ciudad {

    private final int id;
    private final double x;
    private final double y;

    public Ciudad(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}