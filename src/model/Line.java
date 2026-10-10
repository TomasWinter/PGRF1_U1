package model;

public class Line {
    Point2D start;
    Point2D end;



    public Point2D getStart() {
        return start;
    }

    public void setStart(int x, int y) {
        if (start == null) start = new Point2D(x,y);
        else start.set(x,y);
    }

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(int x, int y) {
        if (end == null) end = new Point2D(x,y);
        else end.set(x,y);
    }
}
