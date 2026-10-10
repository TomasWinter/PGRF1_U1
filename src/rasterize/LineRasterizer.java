package rasterize;

import model.Line;
import model.Point2D;
import raster.Raster;

public abstract class LineRasterizer {
    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public abstract void rasterize(int x1, int y1, int x2, int y2);
    public void rasterize(Line line)
    {
        if (line == null || line.getStart() == null || line.getEnd() == null) return;
        rasterize(line.getStart().getX(),line.getStart().getY(),line.getEnd().getX(),line.getEnd().getY());
    }

    public boolean arePointsNear(int x1, int y1, int x2, int y2)
    {
        return Math.abs(x1-x2) <= 5 && Math.abs(y1-y2) <= 5;
    }

    public abstract boolean isPointOnLine(Point2D point,int x1, int y1, int x2, int y2);
    public boolean isPointOnLine(Point2D point, Line line)
    {
        return isPointOnLine(point, line.getStart().getX(),line.getStart().getY(),line.getEnd().getX(),line.getEnd().getY());
    }

    public Raster getRaster() {
        return raster;
    }
}
