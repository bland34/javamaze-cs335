import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private boolean visited, backtracked, solved;
    private Cell next;
    private int x, y;
    private float width, height, rows, columns;
    private int cellWidth, cellHeight, startX, startY, xPadding, yPadding;

    private Cell n, e, s, w;

    private boolean[] walls = {true, true, true, true};     // north, east, south, west i.e. clockwise

    public Cell(int xc, int yc) {
        setX(xc);
        setY(yc);
        setVisited(false);
        setBacktracked(false);
        setSolved(false);
        calcDimensions();
        resetWalls();
    }

    public void resetWalls() {
        for (int i = 0; i < walls.length; i++) {
            walls[i] = true;
        }
    }

    public void removeNorthWall(Cell c) {
        c.walls[0] = false;
    }
    public void removeSouthWall(Cell c) {
        c.walls[2] = false;
    }
    public void removeEastWall(Cell c) {
        c.walls[1] = false;
    }
    public void removeWestWall(Cell c) {
        c.walls[3] = false;
    }

    // changes color of cell to display backtracking or start or end cell
    public void changeCellColor(Graphics g, Color c) {
        g.setColor(c);
        g.fillRect(startX + 1, startY + 1, cellWidth - 1, cellHeight - 1);
    }

    // get random neighbor of cell given a list of unvisited neighbors
    public Cell getRandomCell(List<Cell> unvisited, List<Cell> cellGrid) {
        Cell randomCell;
        if (unvisited.size() > 0) {
            int r = getRandomInteger(0, unvisited.size() - 1);
            randomCell = unvisited.get(r);
            next = cellGrid.get(cellGrid.indexOf(randomCell));
            return randomCell;
        }
        return null;
    }

    // get nextCell from unvisited list and return it
    public Cell getNext(List<Cell> cellGrid) {
        List<Cell> unvisited = getUnvisitedNeighbors(cellGrid);

        Cell randomCell = getRandomCell(unvisited, cellGrid);
        removeWall(randomCell);
        return randomCell;
    }

    // find direction of next cell and remove the wall separating it
    public void removeWall(Cell next) {
        if (next == n) {
            removeNorthWall(this);
            removeSouthWall(next);
        } else if (next == e) {
            removeEastWall(this);
            removeWestWall(next);
        } else if (next == s) {
            removeSouthWall(this);
            removeNorthWall(next);
        } else if (next == w) {
            removeWestWall(this);
            removeEastWall(next);
        }
    }

    // checks if cell exists in the grid currently
    public Cell checkExists(List<Cell> cellGrid, Cell c) {
        for (int i = 0; i < cellGrid.size(); i++) {
            if (c.getY() == cellGrid.get(i).getY() && c.getX() == cellGrid.get(i).getX()) {
                return cellGrid.get(i);
            }
        }
        return null;
    }

    // for the generator: finds unvisited neighbors around current cell and returns list
    public List<Cell> getUnvisitedNeighbors(List<Cell> cellGrid) {
        List<Cell> unvisited = new ArrayList<>(4);

        n = checkExists(cellGrid, new Cell(x, y - 1));
        e = checkExists(cellGrid, new Cell(x + 1, y));
        s = checkExists(cellGrid, new Cell(x, y + 1));
        w = checkExists(cellGrid, new Cell(x - 1, y));

        if (null != n) {
            if (!n.visited) {
                unvisited.add(n);
            }
        }
        if (null != e) {
            if (!e.visited) {
                unvisited.add(e);
            }
        }
        if (null != s) {
            if (!s.visited) {
                unvisited.add(s);
            }
        }
        if (null != w) {
            if (!w.visited) {
                unvisited.add(w);
            }
        }

        return unvisited;
    }

    // checks if a cell is in the found neighbors
    public boolean inNeighbors(List<Cell> neighbors, Cell c) {
        for (Cell n : neighbors) {
            if (c == n) {
                return true;
            }
        }
        return false;
    }

    // given list of valid solution neighbors, find the one that follows the right wall rule
    public Cell rightWallRule(List<Cell> neighbors, Cell previousCell) {
        if (neighbors.size() == 1) {
            return neighbors.get(0);
        }

        boolean northExists = inNeighbors(neighbors, n);
        boolean eastExists = inNeighbors(neighbors, e);
        boolean southExists = inNeighbors(neighbors, s);
        boolean westExists = inNeighbors(neighbors, w);

        if (previousCell == n) {    // return west
            if (w != null && westExists) {
                return w;
            }
            if (s != null && southExists) {
                return s;
            }
            if (e != null && eastExists) {
                return e;
            }
        }
        if (previousCell == e) { // return north
            if (n != null && northExists) {
                return n;
            }
            if (w != null && westExists) {
                return w;
            }
            if (s != null && southExists) {
                return s;
            }
        }
        if (previousCell == s) { // return east
            if (e != null && eastExists) {
                return e;
            }
            if (n != null && northExists) {
                return n;
            }
            if (w != null && westExists) {
                return w;
            }
        }
        if (previousCell == w) { // return south
            if (s != null && southExists) {
                return s;
            }
            if (e != null && eastExists) {
                return e;
            }
            if (n != null && northExists) {
                return n;
            }
        }
        return null;
    }

    // given list of valid solution neighbors, find the one that follows the left wall rule
    public Cell leftWallRule(List<Cell> neighbors, Cell previousCell) {
        if (neighbors.size() == 1) {
            return neighbors.get(0);
        }
        boolean northExists = inNeighbors(neighbors, n);
        boolean eastExists = inNeighbors(neighbors, e);
        boolean southExists = inNeighbors(neighbors, s);
        boolean westExists = inNeighbors(neighbors, w);

        if (previousCell == n) {    // return east
            if (e != null && eastExists) {
                return e;
            }
            if (s != null && southExists) {
                return s;
            }
            if (w != null && westExists) {
                return e;
            }
        }
        if (previousCell == e) { // return south
            if (s != null && southExists) {
                return n;
            }
            if (w != null && westExists) {
                return w;
            }
            if (n != null && northExists) {
                return s;
            }
        }
        if (previousCell == s) { // return west
            if (w != null && westExists) {
                return e;
            }
            if (n != null && northExists) {
                return n;
            }
            if (e != null && eastExists) {
                return w;
            }
        }
        if (previousCell == w) { // return north
            if (n != null && northExists) {
                return s;
            }
            if (e != null && eastExists) {
                return e;
            }
            if (s != null && southExists) {
                return n;
            }
        }
        return null;
    }

    // for solving: finds possible neighbors to choose from and returns a list
    public List<Cell> getPossibleNeighbors(List<Cell> cellGrid) {
        List<Cell> neighbors = new ArrayList<>(4);

        n = checkExists(cellGrid, new Cell(x, y - 1));
        e = checkExists(cellGrid, new Cell(x + 1, y));
        s = checkExists(cellGrid, new Cell(x, y + 1));
        w = checkExists(cellGrid, new Cell(x - 1, y));

        if (null != n && !n.walls[2] && !n.solved && !n.backtracked) {
            neighbors.add(n);
        }
        if (null != e && !e.walls[3] && !e.solved && !e.backtracked) {
            neighbors.add(e);
        }
        if (null != s && !s.walls[0] && !s.solved && !s.backtracked) {
            neighbors.add(s);
        }
        if (null != w && !w.walls[1] && !w.solved && !w.backtracked) {
            neighbors.add(w);
        }
        return neighbors;
    }

    // calculate dimension of each cell based on resolution of maze
    public void calcDimensions() {
        width = MazeView.getMazeWidth();
        height = MazeView.getMazeHeight();
        rows = MazeView.getRows();
        columns = MazeView.getColumns();
        xPadding = MazeView.getxStart();
        yPadding = MazeView.getyStart();

        if (rows < columns) {
            cellWidth = Math.round(width / columns);
            cellHeight = cellWidth;
            startX = Math.round(x * (width / columns) + xPadding);
            startY = Math.round(y * (width / columns) + yPadding);
            return;
        }
        if (rows > columns) {
            cellHeight = Math.round(height / rows);
            cellWidth = cellHeight;
            startX = Math.round(x * (height / rows) + xPadding);
            startY = Math.round(y * (height / rows) + yPadding);
            return;
        }

        cellHeight = Math.round(height / rows);
        cellWidth = Math.round(width / columns);

        startX = Math.round(x * (width / columns) + xPadding);
        startY = Math.round(y * (height / rows) + yPadding);
    }

    // draw cell
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(startX, startY, cellWidth, cellHeight);

        g.setColor(Color.black);
        if (!walls[0]) { // north
            g.drawLine(startX + 1, startY, startX + cellWidth - 1, startY);
            if (solved) {
                g.setColor(Color.blue);
                g.drawLine(startX + 1, startY, startX + cellWidth - 1, startY);
            }
            if (backtracked) {
                g.setColor(Color.magenta);
                g.drawLine(startX + 1, startY, startX + cellWidth - 1, startY);
            }
        }
        g.setColor(Color.black);
        if (!walls[1]) { // east
            g.drawLine(startX + cellWidth, startY - 1, startX + cellWidth, startY + cellHeight - 1);
            if (solved) {
                g.setColor(Color.blue);
                g.drawLine(startX + cellWidth, startY - 1, startX + cellWidth, startY + cellHeight - 1);
            }
            if (backtracked) {
                g.setColor(Color.magenta);
                g.drawLine(startX + cellWidth, startY - 1, startX + cellWidth, startY + cellHeight - 1);
            }
        }
        g.setColor(Color.black);
        if (!walls[2]) { // south
            g.drawLine(startX + 1, startY + cellHeight, startX + cellWidth - 1, startY + cellHeight);
            if (solved) {
                g.setColor(Color.blue);
                g.drawLine(startX + 1, startY + cellHeight, startX + cellWidth - 1, startY + cellHeight);
            }
            if (backtracked) {
                g.setColor(Color.magenta);
                g.drawLine(startX + 1, startY + cellHeight, startX + cellWidth - 1, startY + cellHeight);
            }
        }
        g.setColor(Color.black);
        if (!walls[3]) { // west
            g.drawLine(startX, startY + 1, startX, startY + cellHeight - 1);
            if (solved) {
                g.setColor(Color.blue);
                g.drawLine(startX, startY + 1, startX, startY + cellHeight - 1);
            }
            if (backtracked) {
                g.setColor(Color.magenta);
                g.drawLine(startX, startY + 1, startX, startY + cellHeight - 1);
            }
        }

        if (solved) {
            changeCellColor(g, Color.blue);
        }

        if (backtracked) {
            changeCellColor(g, Color.MAGENTA);
        }

    }

    public static int getRandomInteger(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }

    public int getX() {
        return x;
    }
    public void setX(int s) {
        x = s;
    }
    public int getY() {
        return y;
    }
    public void setY(int s) {
        y = s;
    }

    public void setSolved(boolean b) {
        solved = b;
    }
    public boolean getSolved() {
        return solved;
    }

    public boolean getBacktracked() {
        return backtracked;
    }
    public void setBacktracked(boolean b) {
        backtracked = b;
    }

    // check if a cell is visited
    public boolean getVisited() {
        return visited;
    }
    public void setVisited(boolean b) {
        visited = b;
    }
}
