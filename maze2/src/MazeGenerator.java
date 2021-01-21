import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MazeGenerator {
    private Stack<Cell> cellStack = new Stack<>();
    private List<Cell> unvisited;
    private Cell currCell, nextCell;
    private int speed, traversed;
    private static int totalCells = MazeView.getTotalCells();
    private Timer timer;
    private boolean generated;

    public MazeGenerator(List<Cell> cellGrid, JPanel panel) {
        setGenerated(false);
        traversed = 1;
        totalCells = cellGrid.size();
        currCell = cellGrid.get(0);     // start at 0
        cellStack.push(currCell);
        currCell.setVisited(true);
        speed = Gui.getSpeed();
        timer = new Timer(speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unvisited = currCell.getUnvisitedNeighbors(cellGrid);
                if (unvisited.size() > 0) {     // if there are unvisited neighbors, push next cell onto stack and make it current
                    nextCell = currCell.getNext(cellGrid);
                    nextCell.setVisited(true);
                    cellStack.push(nextCell);
                    currCell = nextCell;
                    currCell.setVisited(true);
                    traversed++;
                } else {        // otherwise there are no unvisited neighbors, so backtrack
                    currCell = cellStack.pop();
                }
                panel.repaint();

                if (traversed == totalCells) {      // finished generating
                    JOptionPane.showMessageDialog(null, "Hooray, the maze is generated!", "Finished generating", JOptionPane.INFORMATION_MESSAGE);
                    setGenerated(true);
                    System.out.println("Finished");
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public void setGenerated(boolean b) {
        generated = b;
    }
    public boolean getGenerated() {
        return generated;
    }

}
