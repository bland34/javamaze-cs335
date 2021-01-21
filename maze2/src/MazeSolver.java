import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MazeSolver {
    private Stack<Cell> cellStack = new Stack<>();
    private List<Cell> unsolved;
    private Cell currCell, nextCell, previousCell;
    private int speed, totalCells, traversed = 1;
    private Timer timer;
    private boolean isSolved;

    public MazeSolver(List<Cell> cellGrid, JPanel panel) {
        setSolved(false);
        totalCells = cellGrid.size();
        currCell = cellGrid.get(0);
        cellStack.push(currCell);
        currCell.setSolved(true);
        speed = Gui.getSpeed();
        timer = new Timer(speed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cellStack.size() < 2) { // find previous cell to find direction the solver is coming from
                    previousCell = cellStack.peek();
                }

                unsolved = currCell.getPossibleNeighbors(cellGrid); // find neighbors

                if (unsolved.size() > 0) {  // if neighbors exist, find right wall cell and push
                    nextCell = currCell.rightWallRule(unsolved, previousCell);
                    previousCell = currCell;
//                    nextCell = currCell.leftWallRule(unsolved, previousCell);
                    currCell.setSolved(true);
                    cellStack.push(nextCell);
                    currCell = nextCell;
                    calculatePercentage();
                    traversed++;
                } else {    // otherwise pop since dead end
                    currCell = cellStack.pop();
                    previousCell = currCell;
                    currCell.setBacktracked(true);
                }
                panel.repaint();    // draw

                if (currCell == cellGrid.get(cellGrid.size() - 1)) {    // if cell is the end cell stop solver
                    JOptionPane.showMessageDialog(null, "Hooray, the maze is solved!", "Winner", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Solved");
                    setSolved(true);
                    timer.stop();
                }

            }
        });
        timer.start();
    }

    // helper method to visualize algorithm in console
    public void printList(List<Cell> cells) {
        System.out.println("Current -> X: " + currCell.getX() + "     Y: " + currCell.getY());
        for (Cell c : cells) {
            System.out.println("Unsolved -> X: " + c.getX() + "     Y: " + c.getY());
        }
        System.out.println("----------------------------------------------");
        System.out.println("\n");
    }

    // calculated total percentage of board visited, not counting backtracking
    public int calculatePercentage() {
        float per = ((float)traversed / totalCells);
        return Math.round(per * 100);
    }

    public void stopSolver() {
        timer.stop();
    }

    public void startSolver() {
        timer.start();
    }

    public void setSolved(boolean b) {
        isSolved = b;
    }
    public boolean getSolved() {
        return isSolved;
    }

}
