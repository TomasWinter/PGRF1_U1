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
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    List<Polygon> polygonList = new ArrayList<>();
    List<Line> lineList = new ArrayList<>();

    private boolean currentlyEditing = false;
    private Shape currentShape = Shape.LINE;
    private Point2D currentPoint = null;

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

                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    currentPoint = findClosestPoint(e.getX(),e.getY());
                }
                else if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if(currentShape == Shape.LINE) {

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

                        drawScene();
                    }
                    else if(currentShape == Shape.POLYGON) {

                        if (currentlyEditing) return;
                        currentlyEditing = true;

                        if (polygonList.isEmpty()) polygonList.add(new Polygon());

                        Point2D point = new Point2D(e.getX(), e.getY());
                        polygonList.getLast().addPoint(point);

                        drawScene();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if (currentlyEditing)
                        currentlyEditing = false;
                }
                currentPoint = null;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentPoint != null)
                {
                    currentPoint.set(e.getX(),e.getY());
                    drawScene();
                }
                else if (currentlyEditing && currentShape == Shape.LINE)
                {
                    int endX = e.getX();
                    int endY = e.getY();
                    lineList.getLast().getEnd().set(endX, endY);
                    drawScene();
                }
                else if (currentlyEditing && currentShape == Shape.POLYGON)
                {
                    int endX = e.getX();
                    int endY = e.getY();
                    polygonList.getLast().setPoint(new Point2D(endX, endY),polygonList.getLast().getSize()-1);
                    drawScene();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_V)
                {
                    if (currentShape == Shape.LINE) currentShape = Shape.POLYGON;
                    else currentShape = Shape.LINE;
                    System.out.println("Switched editing to "+String.valueOf(currentShape));
                }
                else if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_C)
                {
                    lineList.clear();
                    polygonList.clear();
                    drawScene();
                }
                else if (!currentlyEditing && e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    polygonList.add(new Polygon());
                }
            }
        });
    }

    private Point2D findClosestPoint(int x, int y)
    {
        Point2D closestPoint = null;
        double closestPointDistance = Double.POSITIVE_INFINITY;

        for (Line l : lineList)
        {
            double magnitude = Point2D.getMagnitudeBetween(x,y,l.getStart().getX(),l.getStart().getY());
            if (magnitude <= closestPointDistance)
            {
                closestPointDistance = magnitude;
                closestPoint = l.getStart();
            }
            magnitude = Point2D.getMagnitudeBetween(x,y,l.getEnd().getX(),l.getEnd().getY());
            if (magnitude <= closestPointDistance)
            {
                closestPointDistance = magnitude;
                closestPoint = l.getEnd();
            }
        }

        for (Polygon p : polygonList)
        {
            for (int i = 0; i < p.getSize(); i++)
            {
                Point2D point = p.getPoint(i);
                double magnitude = Point2D.getMagnitudeBetween(point.getX(), point.getY(), x, y);
                if (magnitude <= closestPointDistance)
                {
                    closestPointDistance = magnitude;
                    closestPoint = point;
                }
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
