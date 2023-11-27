package conway.Main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.Color;

public abstract class Grid implements Serializable {
    protected int size;
    protected Color deadCellColor;
    protected String selectedRule;

    public Grid(int size, Color deadCellColor, String selectedRule) {
        this.size = size;
        this.deadCellColor = deadCellColor;
        this.selectedRule = selectedRule;
    }

    public Color getDeadCellColor() {
        return deadCellColor;
    }

    public String getselectedRule() {
        return this.selectedRule;
    }

    public int getSize() {
        return size;
    }

    //this is just a bit of explanation of the rules on the game screen
    public String getRuleDetails(){StringBuilder ruleDetails = new StringBuilder();
        ruleDetails.append("The current rule is: ").append(this.selectedRule).append("\n\n");
    
        //details about the rule
        if (this.selectedRule.equals("Default")) {
            ruleDetails.append("The default ruleset is:\n");
            ruleDetails.append("1. If a cell is alive and it has exactly 2 or 3 live neighbors, it stays alive.\n");
            ruleDetails.append("2. If a cell is dead and it has exactly 3 live neighbors, it becomes alive.\n");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");

        } else if (this.selectedRule.equals("High Life")) {
            ruleDetails.append("High Life ruleset is defined as follows:\n");
            ruleDetails.append("1. A dead cell comes to life if it has exactly 3 or 6 live neighbors.\n");
            ruleDetails.append("2. Survival: A live cell stays alive if it has 2 or 3 live neighbors.");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");
        } else if (this.selectedRule.equals("Move")) {
            ruleDetails.append("High Life ruleset is defined as follows:\n");
            ruleDetails.append("1. Births: A dead cell comes to life if it has 3, 6, 7, or 8 live neighbors.\n");
            ruleDetails.append("Survival: A live cell stays alive if it has 2, 4, or 5 live neighbors.");
            ruleDetails.append("3. In all other cases, a cell dies or remains dead.\n");
        }
    
        return ruleDetails.toString();
    }

    public void saveGridToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the grid to a file.");
            e.printStackTrace();
        }
    }

	  public static Grid loadGridFromFile(String filename) {
        Grid grid = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            grid = (Grid) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while loading the grid from a file.");
            e.printStackTrace();
        }
        return grid;
    }
    
    //to be overriden
    public abstract void initializeGrid();
    public abstract boolean isWithinBounds(int x, int y);
    public abstract void updateGameOfLife(String selectedRule);
}

