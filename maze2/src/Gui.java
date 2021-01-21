import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui extends JFrame implements ActionListener {

    public static MazeView mazeView;
    private JPanel gui = new JPanel();

    // gui stuff ----------------------
    private JButton startStop, generate, slow, normal, fast;
    private static boolean isGenerated = false, started = false, changed = false;
    private static SpinnerModel row = new SpinnerNumberModel(10, 10, 50, 1);
    private static SpinnerModel column = new SpinnerNumberModel(10, 10, 50, 1);
    private JSpinner rowSelector = new JSpinner(row);
    private JSpinner columnSelector = new JSpinner(column);
    private String currSpeed = "";
    private static int desiredSpeed;

    private static JLabel percentVisited;
    private JLabel speed = new JLabel("Speed of Generation/Solver: ");
    private JLabel r = new JLabel("Number of Rows: ");
    private JLabel c = new JLabel("Number of Columns: ");
    private static int percentage;

    private JPanel first, second, third;
    private int rows = 10, columns = 10;
    // --------------------------------

    // set the percentage of total cells visited
    public static void setPercentage() {
        percentage = mazeView.setPercentage();
        percentVisited.setText("Total % of maze visited by solver: " + percentage + "%");
    }

    public static boolean getGenerated() {
        return isGenerated;
    }
    public static void setGenerated(boolean s) {
        isGenerated = s;
    }
    public static boolean getStarted() {
        return started;
    }
    public static void setStarted(boolean s) {
        started = s;
    }

    // if speed is not set, default to fast
    public void isSpeedSet() {
        if (currSpeed.equals("")) {
            setCurrSpeed("Fast", fast, 25);
        }
    }

    public static int getSpeed() {
        return desiredSpeed;
    }

    // change speed of solver/generator
    public void setCurrSpeed(String sp, JButton button, int s) {
        currSpeed = sp;
        desiredSpeed = s;
        slow.setBackground(Color.gray);
        normal.setBackground(Color.gray);
        fast.setBackground(Color.gray);

        button.setBackground(Color.green);
    }

    public void addButtons() {
        startStop = new JButton("Start Solving");   // when button is clicked switch from start to stop
        generate = new JButton("Generate Maze");
        slow = new JButton("Slow");
        normal = new JButton("Normal");
        fast = new JButton("Fast");

        startStop.setFocusPainted(false);
        generate.setFocusPainted(false);
        slow.setFocusPainted(false);
        normal.setFocusPainted(false);
        fast.setFocusPainted(false);

        startStop.addActionListener(this);
        generate.addActionListener(this);
        slow.addActionListener(this);
        normal.addActionListener(this);
        fast.addActionListener(this);
    }

    public void dimensionListeners() {
        rowSelector.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner =  (JSpinner)(e.getSource());
                int numRows = (int)spinner.getValue();
                mazeView.setRows(numRows);
                rows = numRows;
                MazeView.updateTotalCellsR(numRows);
            }
        });
        columnSelector.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner =  (JSpinner)(e.getSource());
                int numCols = (int)spinner.getValue();
                mazeView.setColumns(numCols);
                columns = numCols;
                MazeView.updateTotalCellsC(numCols);
            }
        });
    }

    public void createView() {
        addButtons();
        mazeView.setRows(10);
        mazeView.setColumns(10);
        dimensionListeners();

        first = new JPanel();       // start and speed
        second = new JPanel();      // rows and columns
        third = new JPanel();       // generate and total %

        percentage = 0;
        percentVisited = new JLabel("Total % of maze visited: " + percentage + "%");
        gui.setLayout(new GridBagLayout());
        rowSelector.setEditor(new JSpinner.DefaultEditor(rowSelector));
        columnSelector.setEditor(new JSpinner.DefaultEditor(columnSelector));
        GridBagConstraints gb = new GridBagConstraints();

        // first panel - start and speed buttons
        first.setLayout(new GridBagLayout());
        gb.gridx = 1;
        gb.gridy = 0;
        first.add(generate, gb);
        gb.gridx = 1;
        gb.gridy = 1;
        gb.insets = new Insets(40, 0, 10, 0);
        first.add(speed, gb);
        gb.gridx = 0;
        gb.gridy = 2;
        gb.insets = new Insets(0, 20, 0, 0);
        first.add(slow, gb);
        gb.gridx = 1;
        gb.gridy = 2;
        gb.insets = new Insets(0, 0, 0, 0);
        first.add(normal, gb);
        gb.gridx = 2;
        gb.gridy = 2;
        gb.insets = new Insets(0, 0, 0, 20);
        first.add(fast, gb);

        // second panel
        second.setLayout(new GridBagLayout());
        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(0, 0, 5, 5);
        second.add(r, gb);
        gb.gridx = 1;
        gb.gridy = 0;
        gb.insets = new Insets(0, 0, 5, 0);
        second.add(rowSelector, gb);
        gb.gridx = 0;
        gb.gridy = 1;
        gb.insets = new Insets(10, 0, 0, 5);
        second.add(c, gb);
        gb.gridx = 1;
        gb.gridy = 1;
        gb.insets = new Insets(10, 0, 0, 0);
        second.add(columnSelector, gb);

        // third panel
        third.setLayout(new GridBagLayout());
        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(20, 0, 20, 0);
        third.add(startStop, gb);
        gb.gridx = 0;
        gb.gridy = 1;
        gb.insets = new Insets(20, 0, 0, 0);
        third.add(percentVisited, gb);

        gb.gridx = 0;
        gb.gridy = 0;
        gb.insets = new Insets(0, 0, 40, 0);
        gui.add(first, gb);
        gb.gridx = 0;
        gb.gridy = 1;
        gb.insets = new Insets(17, 0, 25, 0);
        gui.add(second, gb);
        gb.gridx = 0;
        gb.gridy = 2;
        gb.insets = new Insets(5, 0, 40, 0);
        gui.add(third, gb);
    }

    // edit percent label to reflect percentage of cells visited by solver
    public void startPercentTimer() {
        Timer timer = new Timer(desiredSpeed, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPercentage();
                if (mazeView.checkSolved()) {
                    setStarted(false);
                    setGenerated(false);
                    changed = false;
                    mazeView.stopMazeSolver();
                    startStop.setText("Start Solver");
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    // check if maze is finished generating
    public void checkGenerated() {
        Timer gen = new Timer(10, null);
        gen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mazeView.getGenerated()) {
                    setGenerated(true);
                    gen.stop();
                }
            }
        });
        gen.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();

        if (button == startStop) {
            if (!getGenerated()) {      // maze not generated yet, display error message
                JOptionPane.showMessageDialog(this, "Please wait until the maze is finished generating, or click the generate button if it hasn't started.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!getStarted()) { // solver not started yet, change button to stop and start solver
                if (!changed) {
                    startPercentTimer();
                    mazeView.createMazeSolver();
                    startStop.setText("Stop Solver");
                    setStarted(true);
                }
                if (changed) {
                    mazeView.startMazeSolver();
                }
                changed = true;

            } else if (getStarted()) {  // stop solver
                setStarted(false);
                mazeView.stopMazeSolver();
                startStop.setText("Start Solver");
            }
        } else if (button == slow) {
            setCurrSpeed("Slow", slow, 300);
        } else if (button == normal) {
            setCurrSpeed("Normal", normal, 150);
        } else if (button == fast) {
            setCurrSpeed("Fast", fast, 25);
        } else if (button == generate) {
            mazeView.redrawMaze();
            isSpeedSet();
            setStarted(false);
            checkGenerated();
            mazeView.createMazeGenerator();
        }
    }



    // ----------------------------------
    public void createContentPane() {
        this.setBackground(Color.black);

        mazeView = new MazeView(10, 10);
        gui = new JPanel();
        createView();

        this.add(gui, BorderLayout.WEST);
        this.add(mazeView, BorderLayout.CENTER);

        this.setSize(new Dimension(1000, 800));
        centerWindow(this);

        show();
    }

    public Gui() {
        super("Connor's Maze");
        createContentPane();
        rows = 10;
        columns = 10;
    }

    // centers window when it compiles
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        Gui app = new Gui();
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
    }

}
