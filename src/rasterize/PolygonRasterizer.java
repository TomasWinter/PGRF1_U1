package rasterize;

import model.Point2D;
import model.Polygon;

public class PolygonRasterizer {
    private final LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if (polygon == null) return;
        if (polygon.getSize() == 0) return;

        if (polygon.getSize() == 1) //vykreslení bodu, když polygon má jen jeden bod
        {
            Point2D point = polygon.getPoint(0);
            lineRasterizer.getRaster().setPixel(point.getX(), point.getY(), 0xffffff);
            return;
        }

        for (int i = 0; i < polygon.getSize(); i++) {
            Point2D pointA = polygon.getPoint(i);
            Point2D pointB = polygon.getPoint((i + 1) % polygon.getSize());
            lineRasterizer.rasterize(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
        }
    }
}
