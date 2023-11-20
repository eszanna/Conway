package com.example;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class Main {

    public static Color deadCellColor;

    public static void main(String[] args) {
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


        JButton loadButton = new JButton("LOAD HEXAGONS");
            menuFrame.add(loadButton, gbc);
            loadButton.addActionListener(event -> {

            HexagonalGrid hexagonalGrid = HexagonalGrid.loadGridFromFile("gridState.txt");
            
            JFrame frame = new JFrame("Conway's Game of Life on Hexagons");
                HexagonalGridDrawer drawer = new HexagonalGridDrawer(hexagonalGrid, Color.WHITE);
                frame.add(drawer);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(e -> {
                    hexagonalGrid.saveGridToFile("gridState.txt");
                    System.out.println("Grid state is saved in gridState.txt");
                });


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
                SquareGridDrawer drawer = new SquareGridDrawer(squareGrid, Color.WHITE);
                frame.add(drawer);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(e -> {
                    squareGrid.saveSquareGridToFile("SquareGridState.txt");
                    System.out.println("Grid state is saved in SquareGridState.txt");
                });


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
            String selectedGridType = (String) gridTypeSelection.getSelectedItem();
            if (Objects.requireNonNull(selectedGridType).equals("Hexagon")) {
                HexagonalGrid hexagonalGrid = new HexagonalGrid(gridSize, deadCellColor);
                

                JFrame frame = new JFrame("Conway's Game of Life");
                HexagonalGridDrawer drawer = new HexagonalGridDrawer(hexagonalGrid, deadCellColor);
                drawer.sethexSize(polygonSize);
                frame.add(drawer);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(event -> {
                    hexagonalGrid.saveGridToFile("gridState.txt");
                    System.out.println("Grid state is saved in gridState.txt");
                });


                frame.setVisible(true);

                // Start the game loop
                drawer.startGame();

            } else {
                SquareGrid squaregrid= new SquareGrid(gridSize, deadCellColor);

                JFrame frame = new JFrame("Conway's Game of Life on squares");
                SquareGridDrawer drawer = new SquareGridDrawer(squaregrid, deadCellColor);
                drawer.setsquareSize(polygonSize);

                frame.add(drawer);
                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton saveButton = new JButton("SAVE");
                frame.add(saveButton, BorderLayout.SOUTH);
                saveButton.addActionListener(event -> {
                    squaregrid.saveSquareGridToFile("squareGridState.txt");
                    System.out.println("Grid state is saved in squareGridState.txt");
                });

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