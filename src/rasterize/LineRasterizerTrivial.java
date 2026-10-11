package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {

        // vyreslení svislé úsečky
        if (x1 == x2) {
            if (y1 < y2)
                for (int y = y1; y <= y2; y++)
                    raster.setPixel(x1, y, 0xffffff);
            else
                for (int y = y2; y <= y1; y++)
                    raster.setPixel(x1, y, 0xffffff);
            return;
        }

        //rovnice přímky y = kx + q
        float k = (float) (y2 - y1) / (x2 - x1);
        float q = -k * x2 + y2;

        // Pokud je úsečka moc strmá, prohodí se ose přes kteropu se iteruje
        if (Math.abs(k) > 1) {
            // výpočet nových hodnot pro rovnici
            k = (float) (x2 - x1) / (y2 - y1);
            q = -k * y2 + x2;

            if (y1 > y2) // prohození hodnot y, aby y1 vždy bylo menší
            {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++) {
                int x = (int) (k * y + q);

                raster.setPixel(x, y, 0xffffff);
            }
        } else {
            if (x1 > x2) // prohození hodnot x, aby x1 vždy bylo menší
            {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = x1; x <= x2; x++) {
                int y = (int) (k * x + q);

                raster.setPixel(x, y, 0xffffff);
            }
        }
    }
}
