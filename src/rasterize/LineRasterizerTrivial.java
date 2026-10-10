package rasterize;

import model.Point2D;
import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        if (x1 == x2)
        {
            if (y1 < y2)
                for (int y = y1; y <= y2; y++)
                    raster.setPixel(x1,y, 0xffffff);
            else
                for (int y = y2; y <= y1; y++)
                    raster.setPixel(x1,y, 0xffffff);
            return;
        }

        float k = (float)(y2 - y1)/(x2 - x1);
        float q = -k * x2 + y2;

        if (Math.abs(k) > 1)
        {
            k = (float)(x2 - x1)/(y2 - y1);
            q = -k * y2 + x2;

            if (y1 > y2)
            {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++)
            {
                int x = (int)(k*y +  q);

                raster.setPixel(x,y, 0xffffff);
            }
        }
        else
        {
            if (x1 > x2)
            {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = x1; x <= x2; x++)
            {
                int y = (int)(k*x +  q);

                raster.setPixel(x,y, 0xffffff);
            }
        }
    }

    @Override
    public boolean isPointOnLine(Point2D point, int x1, int y1, int x2, int y2) {
        if (x1 == x2)
        {
            if (y1 < y2)
                for (int y = y1; y <= y2; y++)
                {
                    if (arePointsNear(point.getX(), point.getY(), x1, y))
                        return true;
                }
            else
                for (int y = y2; y <= y1; y++)
                {
                    if (arePointsNear(point.getX(), point.getY(), x1, y))
                        return true;
                }
            return false;
        }

        float k = (float)(y2 - y1)/(x2 - x1);
        float q = -k * x2 + y2;

        if (Math.abs(k) > 1)
        {
            k = (float)(x2 - x1)/(y2 - y1);
            q = -k * y2 + x2;

            if (y1 > y2)
            {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++)
            {
                int x = (int)(k*y +  q);

                if (arePointsNear(point.getX(), point.getY(), x, y))
                    return true;
            }
        }
        else
        {
            if (x1 > x2)
            {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = x1; x <= x2; x++)
            {
                int y = (int)(k*x +  q);
                if (arePointsNear(point.getX(), point.getY(), x, y))
                    return true;
            }
        }

        return false;
    }
}
