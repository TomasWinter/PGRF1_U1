package rasterize;

import model.Point2D;
import model.Polygon;
import raster.Raster;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;


    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if (polygon == null) return;
        if (polygon.getSize() < 2) return;

        if (polygon.getSize() == 1)
        {
            Point2D point = polygon.getPoint(0);
            lineRasterizer.getRaster().setPixel(point.getX(), point.getY(), 0xffffff);
            return;
        }

        for (int i = 0; i < polygon.getSize(); i++) {
            Point2D pointA = polygon.getPoint(i);
            Point2D pointB = polygon.getPoint((i + 1) % polygon.getSize());
            lineRasterizer.rasterize(pointA.getX(),  pointA.getY(), pointB.getX(), pointB.getY());
        }
    }


    public boolean isPointOnPolygonEdge(Point2D point, Polygon polygon)
    {
        for (int i = 0; i < polygon.getSize(); i++) {
            Point2D pointA = polygon.getPoint(i);
            Point2D pointB = polygon.getPoint((i + 1) % polygon.getSize());
            if (lineRasterizer.isPointOnLine(point, pointA.getX(),  pointA.getY(), pointB.getX(), pointB.getY()))
                return true;
        }
        return false;
    }
}
