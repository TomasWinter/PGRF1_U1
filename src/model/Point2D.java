package model;

public class Point2D {
    private int x, y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }


    // Metoda vrátí délku vektoru na základě pythagorovi věty
    public static double getMagnitude(int x, int y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    // Metoda vrátí vzdálenost mezi 2 body
    public static double getMagnitudeBetween(int x1, int y1, int x2, int y2) {
        return Point2D.getMagnitude(Math.abs(x2 - x1), Math.abs(y2 - y1));
    }
}
