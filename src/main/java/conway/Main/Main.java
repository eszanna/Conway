package conway.Main;

import javax.swing.*;

import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class Main {

    public static Color deadCellColor;
    public static String selectedRule;

    public static void main(String[] args) {
        
        //just to make it a bit nicer
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a new JFrame for the menu
        JFrame menuFrame = new JFrame("Game Menu");
        menuFrame.setSize(800, 800);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridBagLayout());
        menuFrame.setFont(new Font("Arial", Font.BOLD, 20));
        menuFrame.setBackground(Color.ORANGE);

        //for better positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        

        // Create a JLabel and JComboBox for the grid type selection (hexa or square)
        JLabel gridTypeLabel = new JLabel("Choose the type of grid:", SwingConstants.CENTER);
        gridTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] gridTypes = {"Square", "Hexagon"};
        JComboBox<String> gridTypeSelection = new JComboBox<>(gridTypes);
        gridTypeSelection.setPreferredSize(new Dimension(100, 30));
        gridTypeSelection.setFont(new Font("Arial", Font.ITALIC, 16));
        menuFrame.add(gridTypeLabel, gbc);
        menuFrame.add(gridTypeSelection, gbc);

        String gridSizeInfo = "<html><font size='3'>Enter a size for the grid! <br>(It IS NOT EQUAL to the # of hexagons.)<br>(recommended size for hexagonal grid is between 5-40)<br><font size='3'>SIZE OF GRID:</html>";
        JLabel gridSizeLabel = new JLabel(gridSizeInfo, SwingConstants.LEFT);
        gridSizeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTextField gridSizeInput = new JTextField();
        gridSizeInput.setPreferredSize(new Dimension(100, 30));
        menuFrame.add(gridSizeLabel, gbc);
        menuFrame.add(gridSizeInput, gbc);
        
        String polygonSizeInfo = "<html><font size='3'>Enter a size for the polygons!<br>Doesn't matter much because you can ZOOM in and out by scrolling with the mouse. <br><font size='3'>SIZE OF POLYGON:</html>";
        JLabel polygonSizeLabel = new JLabel(polygonSizeInfo, SwingConstants.CENTER);
        polygonSizeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTextField polygonSizeInput = new JTextField();
        polygonSizeInput.setPreferredSize(new Dimension(100, 30));
        menuFrame.add(polygonSizeLabel, gbc);
        menuFrame.add(polygonSizeInput, gbc);

        // Create a JLabel and JComboBox for the color selection
        JLabel colorSelectionLabel = new JLabel("Choose the color for dead cells:", SwingConstants.CENTER);
        colorSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] colors = {"WHITE", "GRAY", "LIGHT_GRAY", "PINK", "RED", "ORANGE", "GREEN", "MAGENTA"};
        JComboBox<String> colorSelection = new JComboBox<>(colors);
        colorSelection.setPreferredSize(new Dimension(100, 30));
        colorSelection.setFont(new Font("Arial", Font.ITALIC, 16));
        menuFrame.add(colorSelectionLabel, gbc);
        menuFrame.add(colorSelection, gbc);

        // Create a JLabel and JComboBox for the rules selection
        JLabel rulesSelectionLabel = new JLabel("Choose the set of rules:", SwingConstants.CENTER);
        rulesSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        String[] rules = {"Default", "High Life", "Move"};
        JComboBox<String> rulesSelection = new JComboBox<>(rules);
        rulesSelection.setPreferredSize(new Dimension(100, 30));
        rulesSelection.setFont(new Font("Arial", Font.ITALIC, 16));
        rulesSelection.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
               if (event.getStateChange() == ItemEvent.SELECTED) {
                  selectedRule = event.getItem().toString();
               }
            }       
        });
        menuFrame.add(rulesSelectionLabel, gbc);
        menuFrame.add(rulesSelection, gbc);
        selectedRule = (String) rulesSelection.getSelectedItem();

        //loading and repeating the save option there
        JButton loadButton = new JButton("LOAD HEXAGONS");
        loadButton.setFont(new Font("Arial", Font.BOLD, 16));
        loadButton.setBorder(new EmptyBorder(10, 10, 10, 10));
            menuFrame.add(loadButton, gbc);
            loadButton.addActionListener(event -> {

            HexagonalGrid hexagonalGrid = (HexagonalGrid) Grid.loadGridFromFile("gridState.txt");
            
            JFrame frame = new JFrame("Conway's Game of Life on Hexagons");
                HexagonalGridDrawer drawer = new HexagonalGridDrawer(hexagonalGrid, selectedRule);
                drawer.setDeadCellColor(hexagonalGrid.getDeadCellColor());
                //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                saveButton.setFont(new Font("Arial", Font.BOLD, 16));
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(e -> {
                    hexagonalGrid.saveGridToFile("gridState.txt");
                    System.out.println("Grid state is saved in gridState.txt");
                });

                // Create a new JButton for the pause button
                JButton pauseButton = new JButton("Pause");
                pauseButton.setPreferredSize(new Dimension(100, 30));
                pauseButton.setBackground(Color.BLUE); 
                pauseButton.setForeground(Color.BLACK);
                pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(pauseButton, BorderLayout.WEST);
                pauseButton.addActionListener(pause -> {
                    // Pause the game
                    drawer.getTimer().cancel();
                    drawer.setTimer(null);
                });

                //Button to continue the game
                JButton restartButton = new JButton("Continue");
                restartButton.setBackground(Color.GREEN);
                restartButton.setForeground(Color.BLACK);
                restartButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(restartButton, BorderLayout.EAST);
                restartButton.addActionListener(continue2 -> {
                    if (drawer.getTimer() == null) {
                        drawer.startGame(); 
                    }
                });

                //describing the rules for a better understanding to enjoy the "game" more :)
                JTextArea rulesetArea = new JTextArea();
                rulesetArea.setText(hexagonalGrid.getRuleDetails()); 
                rulesetArea.setLineWrap(true);
                rulesetArea.setWrapStyleWord(true);
                rulesetArea.setEditable(false); 
                rulesetArea.setFont(new Font("Serif", Font.PLAIN, 12));
                frame.add(rulesetArea, BorderLayout.NORTH);

                frame.setVisible(true);

                // Start the game loop
                drawer.startGame();

            System.out.println("Grid state is loaded from gridState.txt");
        });

        JButton loadSquareButton = new JButton("LOAD SQUARES");
        loadSquareButton.setFont(new Font("Arial", Font.BOLD, 16));
        loadSquareButton.setBorder(new EmptyBorder(10, 10, 10, 10));
            menuFrame.add(loadSquareButton, gbc);
            loadSquareButton.addActionListener(event -> {

                SquareGrid squareGrid = (SquareGrid) Grid.loadGridFromFile("SquareGridState.txt");
            
            JFrame frame = new JFrame("Conway's Game of Life on Squares");
                SquareGridDrawer drawer = new SquareGridDrawer(squareGrid, selectedRule);
                drawer.setDeadCellColor(squareGrid.getDeadCellColor());
                //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);

                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                saveButton.setFont(new Font("Arial", Font.BOLD, 16));
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(e -> {
                    squareGrid.saveGridToFile("SquareGridState.txt");
                    System.out.println("Grid state is saved in SquareGridState.txt");
                });

                // Create a new JButton for the pause button
                JButton pauseButton = new JButton("Pause");
                pauseButton.setPreferredSize(new Dimension(100, 30));
                pauseButton.setBackground(Color.BLUE); 
                pauseButton.setForeground(Color.BLACK);
                pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(pauseButton, BorderLayout.WEST);
                pauseButton.addActionListener(pause2 -> {
                    // Pause the game
                    drawer.getTimer().cancel();
                    drawer.setTimer(null);
                });

                //Button to continue the game
                JButton restartButton = new JButton("Continue");
                restartButton.setBackground(Color.GREEN);
                restartButton.setForeground(Color.BLACK);
                restartButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(restartButton, BorderLayout.EAST);
                restartButton.addActionListener(continue3 -> {
                    if (drawer.getTimer() == null) {
                        drawer.startGame(); 
                    }
                });

                //describing the rules for a better understanding to enjoy the game :)
                JTextArea rulesetArea = new JTextArea();
                rulesetArea.setText(squareGrid.getRuleDetails()); 
                rulesetArea.setLineWrap(true);
                rulesetArea.setWrapStyleWord(true);
                rulesetArea.setEditable(false); 
                rulesetArea.setFont(new Font("Serif", Font.PLAIN, 12));
                frame.add(rulesetArea, BorderLayout.NORTH);


                frame.setVisible(true);

                // Start the game loop
                drawer.startGame();
            System.out.println("Grid state is loaded from SquareGridState.txt");
        });

        // Create a JButton for the "START" button
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(Color.GREEN);
        startButton.setPreferredSize(new Dimension(100, 50)); // Set the size of the button
        startButton.setMargin(new Insets(10, 10, 10, 10)); // Add some space around the button
        

        startButton.addActionListener(e -> {
            try {
                int gridSize = Integer.parseInt(gridSizeInput.getText());
                int polygonSize = Integer.parseInt(polygonSizeInput.getText());

                // Check if the numbers are negative
                if (gridSize < 0 || polygonSize < 0) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter a positive number.", "Nice try tho", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            //convert the color string to a Color object
            String selectedColor = (String) colorSelection.getSelectedItem();
            switch (Objects.requireNonNull(selectedColor)) {
                case "WHITE":
                    deadCellColor = Color.WHITE;
                    break;
                case "GRAY":
                    deadCellColor = Color.GRAY;
                    break;
                case "LIGHT_GRAY":
                    deadCellColor = Color.LIGHT_GRAY;
                    break;
                case "PINK":
                    deadCellColor = Color.PINK;
                    break;
                case "RED":
                    deadCellColor = Color.RED;
                    break;
                case "ORANGE":
                    deadCellColor = Color.ORANGE;
                    break;
                case "GREEN":
                    deadCellColor = Color.GREEN;
                    break;
                case "MAGENTA":
                    deadCellColor = Color.MAGENTA;
                    break;
                default:
                    deadCellColor = Color.PINK;
                    break;
            }

            // Determine the type of grid
            ///HEXAGON
            String selectedGridType = (String) gridTypeSelection.getSelectedItem();
            if (Objects.requireNonNull(selectedGridType).equals("Hexagon")) {
                HexagonalGrid hexagonalGrid = new HexagonalGrid(gridSize, deadCellColor, selectedRule);
                
                JFrame frame = new JFrame("Conway's Game of Life");
                HexagonalGridDrawer drawer = new HexagonalGridDrawer(hexagonalGrid, selectedRule);
                drawer.setDeadCellColor(hexagonalGrid.getDeadCellColor());

                //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);

                drawer.sethexSize(polygonSize);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(event -> {
                    hexagonalGrid.saveGridToFile("gridState.txt");
                    System.out.println("Grid state is saved in gridState.txt");
                });

                //create a new JButton for the pause button
                JButton pauseButton = new JButton("Pause");
                pauseButton.setPreferredSize(new Dimension(100, 30));
                pauseButton.setBackground(Color.BLUE); 
                pauseButton.setForeground(Color.BLACK);
                pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(pauseButton, BorderLayout.WEST);
                pauseButton.addActionListener(event -> {
                    // Pause the game
                    drawer.getTimer().cancel();
                    drawer.setTimer(null);
                });

                //button to continue the game
                JButton restartButton = new JButton("Continue");
                restartButton.setBackground(Color.GREEN);
                restartButton.setForeground(Color.BLACK);
                restartButton.setFont(new Font("Arial", Font.BOLD, 20));
                frame.add(restartButton, BorderLayout.EAST);
                restartButton.addActionListener(event -> {
                    if (drawer.getTimer() == null) {
                        drawer.startGame(); 
                    }
                });

                //describing the rules for a better understanding to enjoy the game :)
                JTextArea rulesetArea = new JTextArea();
                rulesetArea.setText(hexagonalGrid.getRuleDetails()); 
                rulesetArea.setLineWrap(true);
                rulesetArea.setWrapStyleWord(true);
                rulesetArea.setEditable(false); 
                rulesetArea.setFont(new Font("Serif", Font.PLAIN, 12));
                frame.add(rulesetArea, BorderLayout.NORTH);

                frame.setVisible(true);

                // Start the game loop
                drawer.startGame();

            } else //when we choose square grid
             {
                SquareGrid squaregrid= new SquareGrid(gridSize, deadCellColor, selectedRule);

                JFrame frame = new JFrame("Conway's Game of Life on Squares");
                SquareGridDrawer drawer = new SquareGridDrawer(squaregrid, selectedRule);
                drawer.setDeadCellColor(squaregrid.getDeadCellColor());

                 //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);

                drawer.setsquareSize(polygonSize);

                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //save button
                JButton saveButton = new JButton("SAVE");
                saveButton.setFont(new Font("Arial", Font.BOLD, 16));
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(saving -> {
                    squaregrid.saveGridToFile("squareGridState.txt");
                    System.out.println("Grid state is saved in squareGridState.txt");
                });

                // Create a new JButton for the pause button
                JButton pauseButton = new JButton("Pause");
                pauseButton.setBackground(Color.BLUE); 
                pauseButton.setForeground(Color.BLACK);
                pauseButton.setFont(new Font("Arial", Font.BOLD, 20)); 
                frame.add(pauseButton, BorderLayout.WEST);
                pauseButton.addActionListener(pausing -> {
                    // Pause the game
                    drawer.getTimer().cancel();
                    drawer.setTimer(null);
                });

                JButton restartButton = new JButton("Continue");
                restartButton.setBackground(Color.GREEN); 
                restartButton.setForeground(Color.BLACK); 
                restartButton.setFont(new Font("Arial", Font.BOLD, 20)); 
                frame.add(restartButton, BorderLayout.EAST);
                restartButton.addActionListener(continueing -> {
                    if (drawer.getTimer() == null) {
                        drawer.startGame();
                    }
                });

                //describing the rules for a better understanding to enjoy the game :)
                JTextArea rulesetArea = new JTextArea();
                rulesetArea.setText(squaregrid.getRuleDetails()); 
                rulesetArea.setLineWrap(true);
                rulesetArea.setWrapStyleWord(true);
                rulesetArea.setEditable(false); 
                rulesetArea.setFont(new Font("Serif", Font.PLAIN, 12));
                frame.add(rulesetArea, BorderLayout.NORTH);

                frame.setVisible(true);

                // Start the game loop
                drawer.startGame();
            }

            // Close the menu frame
            menuFrame.dispose();
            } catch (NumberFormatException ex) {
                // Show error message if the input is not a number
                JOptionPane.showMessageDialog(null, "Error: Please enter a number.", "Nice try tho", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuFrame.add(startButton, gbc);

        // Make the menu frame visible
        menuFrame.setVisible(true);
    }
}
