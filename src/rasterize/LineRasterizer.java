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

    public Raster getRaster() {
        return raster;
    }
}
