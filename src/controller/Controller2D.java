package controller;

import model.Line;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller2D {
    private final Panel panel;

    public enum Shape
    {
        LINE, POLYGON
    }

    // Rasterizers
    private final LineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;

    List<Polygon> polygonList = new ArrayList<>();
    List<Line> lineList = new ArrayList<>();

    //Editing
    private boolean currentlyEditing = false;
    private Shape currentShape = Shape.LINE;
    private Point2D currentPoint = null;
    private  Polygon currentEditPolygon = null;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) // Kliknutím pravým tlačítkem se označí nejbližší bod v určitém dosahu pro editaci
                {
                    currentlyEditing = true;
                    currentPoint = findClosestPoint(e.getX(),e.getY());
                }
                else if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if(currentShape == Shape.LINE) { // Vytvoří se čára a konečný bod se označí pro editaci aby se vytvořila pružná čára

                        if (currentlyEditing) return;
                        currentlyEditing = true;

                        Line newLine = new Line();
                        int startX = e.getX();
                        int startY = e.getY();
                        int endX = e.getX();
                        int endY = e.getY();
                        newLine.setStart(startX, startY);
                        newLine.setEnd(endX, endY);
                        lineList.add(newLine);

                        currentPoint = newLine.getEnd();

                        drawScene();
                    }
                    else if(currentShape == Shape.POLYGON) { // Přidá se bod do posledního vytvořeného polygonu a bod se označí pro editaci kvůli pružné čáře

                        if (currentlyEditing) return;
                        currentlyEditing = true;

                        if (polygonList.isEmpty()) polygonList.add(new Polygon());

                        Point2D point = new Point2D(e.getX(), e.getY());

                        Polygon polygon = polygonList.getLast();
                        if (polygon.getSize() > 2) // když má polygon alespoň 3 body, následující bod se přidá na existující hranu
                        {
                            int index = polygon.getClosestEdge(e.getX(),e.getY());
                            if (index > -1)
                            {
                                polygon.addPoint(point, index+1);
                                currentPoint = polygon.getPoint(index+1);
                            }
                        }
                        else
                        {
                            polygon.addPoint(point);
                            currentPoint = polygon.getPoint(polygon.getSize()-1);
                        }
                        currentEditPolygon = polygon;

                        drawScene();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // při puštění tlačítka se vymažou hodnoty atributů pro editaci
                if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    currentlyEditing = false;
                    currentPoint = null;
                }
                currentEditPolygon = null;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // pozice editovaného bodu se upraví když se pohne myš
                if (currentPoint != null)
                {
                    currentPoint.set(e.getX(),e.getY());
                    drawScene();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Klávesa V změní jaký tvar se vytváří
                if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_V)
                {
                    if (currentShape == Shape.LINE) currentShape = Shape.POLYGON;
                    else currentShape = Shape.LINE;
                    System.out.println("Switched editing to "+currentShape);
                }
                // Klávesa C vymaže všechny tvary
                else if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_C)
                {
                    lineList.clear();
                    polygonList.clear();
                    drawScene();
                }
                // Klávesou ENTER se začne vytvářet nový polygon
                else if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    polygonList.add(new Polygon());
                }
                // Klávesou DELETE se smaže editovaný bod (Pouze pro polygon)
                else if (currentlyEditing && e.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    if (currentEditPolygon == null || currentPoint == null) return;
                    currentEditPolygon.removePoint(currentPoint);
                    drawScene();
                }
            }
        });
    }

    // Funkce najde nejbližší bod ze všech tvarů
    private Point2D findClosestPoint(int x, int y)
    {
        Point2D closestPoint = null;
        double closestPointDistance = Double.POSITIVE_INFINITY;

        for (Line l : lineList)
        {
            // Vzdálenost nejbližšího bodu úsečky se porovná s momentálně nejbližšim bodem
            Point2D point = l.getClosestPoint(x,y);
            double magnitude = Point2D.getMagnitudeBetween(point.getX(), point.getY(), x, y);
            if (magnitude <= closestPointDistance)
            {
                closestPointDistance = magnitude;
                closestPoint = point;
            }
        }

        for (Polygon p : polygonList)
        {
            // Vzdálenost nejbližšího bodu polygonu se porovná s momentálně nejbližšim bodem
            Point2D point = p.getClosestPoint(x,y);
            double magnitude = Point2D.getMagnitudeBetween(point.getX(), point.getY(), x, y);
            if (magnitude <= closestPointDistance)
            {
                closestPointDistance = magnitude;
                closestPoint = point;
                currentEditPolygon = p; // nejbližší polygon se přidá do currentEditPolygon, aby se z něj mohli smazat body, pokud uživatel zmáčkne delete
            }
        }

        if (closestPointDistance > 5) return null;

        return closestPoint;
    }

    private void drawScene() {
        panel.getRaster().clear();

        for (Line l : lineList)
            lineRasterizer.rasterize(l);
        for (Polygon p : polygonList)
            polygonRasterizer.rasterize(p);

        panel.repaint();
    }
}
