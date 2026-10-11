package model;

import java.util.ArrayList;
import java.util.List;

//Třída pro ukládaní polygonů
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

    public void removePoint(Point2D point)
    {
        points.remove(point);
    }
    public void removePoint(int index)
    {
        points.remove(index);
    }

    public Point2D getPoint(int index) {
        return points.get(index);
    }

    public int  getSize() {
        return points.size();
    }

    // Metoda vrátí index bodu, který značí začátek hrany, která je nejblíž vstupnímu bodu
    // Metoda nefunguje v případě kdy je krátká hrana vedle dlouhé hrany, jelikoš pro zjištění blízkosti se využívá součet vzdálenosti
    public int getClosestEdge(int x, int y)
    {
        double closestDistance = Double.POSITIVE_INFINITY;
        int closestPointIndex = -1;

        for (int i = 0; i < points.size(); i++)
        {
            Point2D pointA = points.get(i);
            Point2D pointB = points.get((i + 1) % points.size());

            double nearEdge = Point2D.getMagnitudeBetween(pointA.getX(), pointA.getY(), x, y);
            double farEdge = Point2D.getMagnitudeBetween(pointB.getX(), pointB.getY(), x, y);

            double magnitude = nearEdge + farEdge;

            if (magnitude <= closestDistance)
            {
                closestDistance = magnitude;
                closestPointIndex = i;
            }
        }
        return closestPointIndex;
    }

    // Metoda vrátí který bod na polygonu je nejblíž vstupnímu bodu
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
