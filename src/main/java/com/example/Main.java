package com.example;

import javax.swing.*;
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
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridBagLayout());
        menuFrame.setFont(new Font("Arial", Font.BOLD, 14));
        menuFrame.setBackground(Color.ORANGE);

        //for better positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a JLabel and JComboBox for the grid type selection
        JLabel gridTypeLabel = new JLabel("Choose the type of grid:", SwingConstants.CENTER);
        String[] gridTypes = {"Square", "Hexagon"};
        JComboBox<String> gridTypeSelection = new JComboBox<>(gridTypes);
        menuFrame.add(gridTypeLabel, gbc);
        menuFrame.add(gridTypeSelection, gbc);

        // Create a JLabel and JTextField for the grid size input
        JLabel gridSizeLabel = new JLabel("Enter a size for the grid (#hexagons in a row: 2*size-1):", SwingConstants.CENTER);
        JTextField gridSizeInput = new JTextField();
        menuFrame.add(gridSizeLabel, gbc);
        menuFrame.add(gridSizeInput, gbc);

        JLabel polygonSizeLabel = new JLabel("Enter a size for the polygons:", SwingConstants.CENTER);
        JTextField polygonSizeInput = new JTextField();
        menuFrame.add(polygonSizeLabel, gbc);
        menuFrame.add(polygonSizeInput, gbc);

        // Create a JLabel and JComboBox for the color selection
        JLabel colorSelectionLabel = new JLabel("Choose the color for dead cells:", SwingConstants.CENTER);
        String[] colors = {"WHITE", "GRAY", "LIGHT_GRAY", "PINK", "RED", "ORANGE", "GREEN", "MAGENTA"};
        JComboBox<String> colorSelection = new JComboBox<>(colors);
        menuFrame.add(colorSelectionLabel, gbc);
        menuFrame.add(colorSelection, gbc);

        // Create a JLabel and JComboBox for the rules selection
        JLabel rulesSelectionLabel = new JLabel("Choose the set of rules:", SwingConstants.CENTER);
        String[] rules = {"Default", "High Life", "Move"};
        JComboBox<String> rulesSelection = new JComboBox<>(rules);
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
            menuFrame.add(loadButton, gbc);
            loadButton.addActionListener(event -> {

            HexagonalGrid hexagonalGrid = HexagonalGrid.loadGridFromFile("gridState.txt");
            
            JFrame frame = new JFrame("Conway's Game of Life on Hexagons");
                HexagonalGridDrawer drawer = new HexagonalGridDrawer(hexagonalGrid, selectedRule);
                drawer.setdeadCellColor(hexagonalGrid.getDeadCellColor());
                //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
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

            System.out.println("Grid state is loaded from gridState.txt");
        });

        JButton loadSquareButton = new JButton("LOAD SQUARES");
            menuFrame.add(loadSquareButton, gbc);
            loadSquareButton.addActionListener(event -> {

            SquareGrid squareGrid = SquareGrid.loadSquareGridFromFile("SquareGridState.txt");
            
            JFrame frame = new JFrame("Conway's Game of Life on Hexagons");
                SquareGridDrawer drawer = new SquareGridDrawer(squareGrid, selectedRule);
                drawer.setdeadCellColor(squareGrid.getDeadCellColor());
                //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);

                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(e -> {
                    squareGrid.saveSquareGridToFile("SquareGridState.txt");
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
        startButton.setBackground(Color.GREEN);
        startButton.setPreferredSize(new Dimension(100, 50)); // Set the size of the button
        startButton.setMargin(new Insets(10, 10, 10, 10)); // Add some space around the button
        startButton.addActionListener(e -> {
            // Parse the grid size
            int gridSize = Integer.parseInt(gridSizeInput.getText());

            // Parse the POLYGON size
            int polygonSize = Integer.parseInt(polygonSizeInput.getText());

            // Convert the color string to a Color object
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
                drawer.setdeadCellColor(hexagonalGrid.getDeadCellColor());

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

                // Create a new JButton for the pause button
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

                //Button to continue the game
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
                drawer.setdeadCellColor(squaregrid.getDeadCellColor());

                 //to make it movable across the screen
                JScrollPane scrollPane = new JScrollPane(drawer);
                drawer.setPreferredSize(new Dimension(1000, 1000));
                frame.add(scrollPane);

                drawer.setsquareSize(polygonSize);

                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //save button
                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(saving -> {
                    squaregrid.saveSquareGridToFile("squareGridState.txt");
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
        });
        menuFrame.add(startButton, gbc);

        // Make the menu frame visible
        menuFrame.setVisible(true);
    }
}