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
    public void addPoint(Point2D point,int index) {
        points.add(index, point);
    }

    public Point2D getPoint(int index) {
        return points.get(index);
    }

    public int  getSize() {
        return points.size();
    }

    public int getClosestEdge(int x, int y)
    {
        double closestHypoDif = Double.POSITIVE_INFINITY;
        double closestDistance = Double.POSITIVE_INFINITY;
        int closestPointIndex = -1;

        for (int i = 0; i < points.size(); i++)
        {
            Point2D pointA = points.get(i);
            Point2D pointB = points.get((i + 1) % points.size());

            double hypoEdge = Point2D.getMagnitudeBetween(pointA.getX(),pointA.getY(),pointB.getX(),pointB.getY());
            double nearEdge = Point2D.getMagnitudeBetween(pointA.getX(), pointA.getY(), x, y);
            double farEdge = Point2D.getMagnitudeBetween(pointB.getX(), pointB.getY(), x, y);
            double height = Math.pow(nearEdge,2) / hypoEdge;

            double halfA = Math.sqrt(Math.abs(nearEdge*nearEdge - height*height));
            double halfB = Math.sqrt(Math.abs(farEdge*farEdge - height*height));

            if (height <= closestDistance)
            {
                if (halfA+halfB-hypoEdge > 0.1f) continue;

                closestDistance = height;
                closestPointIndex = i;
            }
        }

        return closestPointIndex;
    }

    public Point2D getClosestPoint(int x, int y)
    {
        double closestDistance = Double.POSITIVE_INFINITY;
        int closestPointIndex = 0;

        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            double magnitude = Point2D.getMagnitudeBetween(x,y,point.getX(),point.getY());
            if (magnitude <= closestDistance)
            {
                closestDistance = magnitude;
                closestPointIndex = i;
            }
        }

        return points.get(closestPointIndex);
    }
}
