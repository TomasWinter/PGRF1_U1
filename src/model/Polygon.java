package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final List<Point2D> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void setPoint(Point2D point, int index)
    {
        points.set(index,point);
    }

    public void addPoint(Point2D point) {
        points.add(point);
    }

    public Point2D getPoint(int index) {
        return points.get(index);
    }

    public int  getSize() {
        return points.size();
    }
}
