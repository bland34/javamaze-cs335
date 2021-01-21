import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class MazeView extends JPanel {
    public static int mazeWidth, mazeHeight, xStart, yStart;
    public static int rows, columns, totalCells;
    private static List<Cell> cellGrid = new ArrayList<>();
    private List<Cell> currentCells = new ArrayList<>();
    private boolean paint;
    private MazeGenerator mazeGenerator;
    private MazeSolver mazeSolver;

    public MazeView(int rowSize, int columnSize) {
        paint = false;
        setRows(rowSize);
        setColumns(columnSize);
        setTotalCells(rowSize, columnSize);
        defineMazeBorder();
        this.setBackground(Color.black);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Cell c = new Cell(j, i);
                cellGrid.add(c);
            }
        }
    }

    // redraw maze with new dimensions
    public void redrawMaze() {
        defineMazeBorder();
        this.setBackground(Color.black);
        cellGrid = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Cell c = new Cell(j, i);
                cellGrid.add(c);
            }
        }
        setRepaint(true);
    }

    public void createMazeGenerator() {
        mazeGenerator = new MazeGenerator(cellGrid, this);
    }

    public void createMazeSolver() {
        mazeSolver = new MazeSolver(cellGrid, this);
    }

    public void stopMazeSolver() {
        mazeSolver.stopSolver();
    }
    public void startMazeSolver() {
        mazeSolver.startSolver();
    }

    public int setPercentage() {
        return mazeSolver.calculatePercentage();
    }
    public boolean checkSolved() {
        return mazeSolver.getSolved();
    }

    public boolean getGenerated() {
        return mazeGenerator.getGenerated();
    }

    // define maze boundaries
    public void defineMazeBorder() {
        setMazeHeight(600);
        setMazeWidth(600);
        setxStart(32);
        setyStart(82);
    }

    public void setRepaint(boolean b) {
        paint = b;
    }

    // paint mazeview
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (paint) {
            g.setColor(Color.black);
            g.fillRect(xStart, yStart, 600, 600);
        }
        for (Cell c : cellGrid) {
            c.draw(g);
        }
        cellGrid.get(0).changeCellColor(g, Color.GREEN);
        cellGrid.get(cellGrid.size() - 1).changeCellColor(g, Color.red);
    }

    public static int getMazeWidth() {
        return mazeWidth;
    }
    public static int getMazeHeight() {
        return mazeHeight;
    }
    public static int getxStart() {
        return xStart;
    }
    public static int getyStart() {
        return yStart;
    }
    public static int getRows() {
        return rows;
    }
    public static int getColumns() {
        return columns;
    }
    public static void setMazeWidth(int s) {
        mazeWidth = s;
    }
    public static void setMazeHeight(int s) {
        mazeHeight = s;
    }
    public static void setxStart(int s) {
        xStart = s;
    }
    public static void setyStart(int s) {
        yStart = s;
    }
    public void setRows(int s) {
        rows = s;
    }
    public void setColumns(int s) {
        columns = s;
    }
    public static int getTotalCells() {
        return totalCells;
    }
    public static void setTotalCells(int r, int c) {
        totalCells = r * c;
    }
    public static void updateTotalCellsR(int r) {
        totalCells = r * columns;
    }
    public static void updateTotalCellsC(int c) {
        totalCells = rows * c;
    }
}
