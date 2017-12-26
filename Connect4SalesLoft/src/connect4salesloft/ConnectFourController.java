/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4salesloft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;

/**
 *
 * @author David
 */
public class ConnectFourController {

    //Initialize JavaFX components
    @FXML
    private GridPane connectGrid;
    @FXML
    private Label playerLabel;
    @FXML
    private Button addToken1;
    @FXML
    private Button addToken2;
    @FXML
    private Button addToken3;
    @FXML
    private Button addToken4;
    @FXML
    private Button addToken5;
    @FXML
    private Button addToken6;
    @FXML
    private Button addToken7;
    @FXML
    private CheckBox connectCheck;
    public ArrayList<Button> buttonArrays = new ArrayList();
    public ArrayList<Circle> circleArrays = new ArrayList();
    public Boolean addTokenBoolean = true;
    public int count = 1;
    public Boolean winnerBool = false;
    boolean breakWhile = false;

    public void addToken() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //Add a token at the column that the user has chosen, and make sure that is added to the next available row.
        Method methodNumRows = connectGrid.getClass().getDeclaredMethod("getNumberOfRows");
        Method methodNumCols = connectGrid.getClass().getDeclaredMethod("getNumberOfColumns");
        methodNumRows.setAccessible(true);
        methodNumCols.setAccessible(true);
        Integer rows = (Integer) methodNumRows.invoke(connectGrid);
        Integer cols = (Integer) methodNumCols.invoke(connectGrid);
        ArrayList<ArrayList<Integer>> results = new ArrayList();
        int rowNum = 0;
        int colNum = 0;
        for (int i = 0; i < buttonArrays.size(); i++) {
            if (buttonArrays.get(i).isFocused()) {
                colNum = Integer.parseInt(buttonArrays.get(i).getId().replace("addToken", "")) - 1;
                for (int j = rows - 1; j > -1; j--) {
                    Node node = getNodeByRowColumnIndex(j, colNum, connectGrid);
                    if (node == null && addTokenBoolean) {
                        results = createCircle(rows, cols, j, colNum);
                        rowNum = j;
                        playerLabel.setText(playerLabel.getText().contains("One") && connectGrid.getChildren().size() > 1 ? "Ready Player Two" : "Ready Player One");
                    }
                    if (node != null && j == 0) {
                        columnFull();
                    }
                }
            }
        }
        if (connectCheck.isSelected()) {
            computerAI(rows, cols, results, rowNum, colNum);
        }
        addTokenBoolean = true;
    }

    void playerWins() {
        //Alert the player that a player has won the game.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Player " + playerLabel.getText().replace("Ready Player", "") + " has won the game! Press okay to start another game.",
                ButtonType.OK);;
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
    }

    ArrayList<ArrayList<Integer>> createCircle(int rows, int cols, int row, int col) {
        //Create a token and add it the the row/column chosen.
        Circle circle = new Circle(25, playerLabel.getText().contains("One") ? Color.BLUE : Color.RED);
        circleArrays.add(circle);
        connectGrid.add(circle, col, row);
        GridPane.setHalignment(circle, HPos.CENTER);
        GridPane.setValignment(circle, VPos.CENTER);
        Node nodeCenter = getNodeByRowColumnIndex(row, col, connectGrid);
        addTokenBoolean = false;
        checkWinner(rows, cols, col, row, circle.getFill(), false);
        return checkNumber(rows, cols, col, row, circle.getFill());
    }

    void columnFull() {
        //Warning to user that the column they chose is full and must choose another column to add token to.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No more tokens may be added to this column. Please",
                ButtonType.OK);;
        alert.initStyle(StageStyle.UTILITY);
        String resultString = "cancel";
        Optional<ButtonType> result = alert.showAndWait();
    }

    public Integer checkWinner(int rows, int cols, int colNum, int rowNum, Paint fill, boolean aiRunning) {
        int numCount = 0;
        ArrayList<Integer> rowVal = checkRow(rows, cols, colNum, rowNum, fill, true, aiRunning);
        ArrayList<Integer> colVal = checkColumn(rows, cols, colNum, rowNum, fill, true, aiRunning);
        ArrayList<Integer> diagMainVal = checkDiagonalMain(rows, cols, colNum, rowNum, fill, true, aiRunning);
        ArrayList<Integer> diagSecVal = checkDiagonalSecondary(rows, cols, colNum, rowNum, fill, true, aiRunning);
        numCount = Math.max(Math.max(Math.max(Math.max(numCount, rowVal.get(0)), colVal.get(0)), diagMainVal.get(0)), diagSecVal.get(0));
        return numCount;
    }

    public ArrayList<ArrayList<Integer>> checkNumber(int rows, int cols, int colNum, int rowNum, Paint fill) {
        ArrayList<Integer> rowVal = checkRow(rows, cols, colNum, rowNum, fill, false, false);
        ArrayList<Integer> colVal = checkColumn(rows, cols, colNum, rowNum, fill, false, false);
        ArrayList<Integer> diagMainVal = checkDiagonalMain(rows, cols, colNum, rowNum, fill, false, false);
        ArrayList<Integer> diagSecVal = checkDiagonalSecondary(rows, cols, colNum, rowNum, fill, false, false);
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>(Arrays.asList(rowVal, colVal, diagMainVal, diagSecVal));
        return results;
    }

    public boolean checkNodeWinner(Node node, boolean winnerBool, boolean breakWhile, Paint fill, int origRow, int origCol, boolean aiRunning) {
        if (node != null && fill == ((Circle) node).getFill() && !winnerBool) {
            //If the color is the same, increase the count.
            count = count + 1;
            if (count == 4 && !aiRunning) {
                //If the count is 4, winner is shown and game resets.
                winnerBool = true;
                playerWins();
                clearSystem();
                breakWhile = true;
            }
        } else {
            //Break out of while loop because there will not be 4 in a row.
            breakWhile = true;
        }
        return breakWhile;
    }

    public Boolean_NextNullArray checkNodeNumber(Node node, boolean winnerBool, boolean breakWhile, Paint fill, int colNum, int rowNum) {
        //Check if the node at a given row/column index is the same color as desired color
        int nextRowNull = -1;
        int nextColNull = -1;
        if (node != null && fill == ((Circle) node).getFill() && !winnerBool) {
            //If color is the same, increase the count.
            count = count + 1;
        } else if (node != null && fill != ((Circle) node).getFill() && !winnerBool) {
            //If the color is not the same break out of while loop.
            breakWhile = true;
        } else {
            //If node is null, store the row/column index.
            nextRowNull = rowNum;
            nextColNull = colNum;
        }
        return new Boolean_NextNullArray(breakWhile, new ArrayList<Integer>(Arrays.asList(nextRowNull, nextColNull)));
    }

    public ArrayList<Integer> checkRow(int rows, int cols, int colNum, int rowNum, Paint fill, boolean checkWinner, boolean aiRunning) {
        //Check number of similar tokens in each row
        count = 1;
        winnerBool = false;
        breakWhile = false;
        ArrayList<Integer> nextRowNull = new ArrayList();
        ArrayList<Integer> nextColNull = new ArrayList();
        int colCount = colNum + 1;
        //Check column to the right of starting point for given row.
        while (colCount < cols) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in the row.
            Node node = getNodeByRowColumnIndex(rowNum, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowNum);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next column component for given row.
            colCount = breakWhile ? cols : colCount + 1;
        }
        colCount = colNum - 1;
        //Check column to the left of starting point for given row.
        breakWhile = false;
        while (colCount > -1) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in the row.
            Node node = getNodeByRowColumnIndex(rowNum, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowNum);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next column component for given row.
            colCount = breakWhile ? -1 : colCount - 1;
        }
        ArrayList<Integer> finalVal = new ArrayList();
        finalVal.add(count);
        if (!checkWinner) {
            for (int i = 0; i < nextRowNull.size(); i++) {
                finalVal.add(nextRowNull.get(i));
                finalVal.add(nextColNull.get(i));
            }
        }
        return finalVal;
    }

    public ArrayList<Integer> checkColumn(int rows, int cols, int colNum, int rowNum, Paint fill, boolean checkWinner, boolean aiRunning) {
        //Check number of similar tokens in each column
        count = 1;
        winnerBool = false;
        breakWhile = false;
        ArrayList<Integer> nextRowNull = new ArrayList();
        ArrayList<Integer> nextColNull = new ArrayList();
        int rowCount = rowNum + 1;
        //Check row below given starting point for given column.
        while (rowCount < rows) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in the column.
            Node node = getNodeByRowColumnIndex(rowCount, colNum, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colNum, rowCount);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row component for given column.
            rowCount = breakWhile ? rows : rowCount + 1;
        }
        rowCount = rowNum - 1;
        breakWhile = false;
        //Check row aboveß given starting point for given column.
        while (rowCount > -1) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in the column.
            Node node = getNodeByRowColumnIndex(rowCount, colNum, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colNum, rowCount);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row component for given column.
            rowCount = breakWhile ? -1 : rowCount - 11;
        }
        ArrayList<Integer> finalVal = new ArrayList();
        finalVal.add(count);
        for (int i = 0; i < nextRowNull.size(); i++) {
            finalVal.add(nextRowNull.get(i));
            finalVal.add(nextColNull.get(i));
        }
        return finalVal;
    }

    public ArrayList<Integer> checkDiagonalMain(int rows, int cols, int colNum, int rowNum, Paint fill, boolean checkWinner, boolean aiRunning) {
        //Check number of similar tokens in each main diagnoal (diagonal from right to left)
        count = 1;
        winnerBool = false;
        breakWhile = false;
        ArrayList<Integer> nextRowNull = new ArrayList();
        ArrayList<Integer> nextColNull = new ArrayList();
        int rowCount = rowNum + 1;
        int colCount = colNum - 1;
        //Check the rows/columns to the left and bottom of the grid from the starting point.
        while (rowCount < rows && colCount > -1) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in each diagonal.
            Node node = getNodeByRowColumnIndex(rowCount, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowCount);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row/column component.
            rowCount = breakWhile ? rows : rowCount + 1;
            colCount = breakWhile ? -1 : colCount - 1;
        }
        rowCount = rowNum - 1;
        colCount = colNum + 1;
        //Check the rows/columns to the right and top of the grid from the starting point.
        breakWhile = false;
        while (rowCount > -1 && colCount < cols) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in each diagonal.
            Node node = getNodeByRowColumnIndex(rowCount, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowCount);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row/column component.
            rowCount = breakWhile ? -1 : rowCount - 1;
            colCount = breakWhile ? cols : colCount + 1;
        }
        ArrayList<Integer> finalVal = new ArrayList();
        finalVal.add(count);
        for (int i = 0; i < nextRowNull.size(); i++) {
            finalVal.add(nextRowNull.get(i));
            finalVal.add(nextColNull.get(i));
        }
        return finalVal;
    }

    public ArrayList<Integer> checkDiagonalSecondary(int rows, int cols, int colNum, int rowNum, Paint fill, boolean checkWinner, boolean aiRunning) {
        //Check number of similar tokens in each secondary diagnoal (diagonal from left to right)
        count = 1;
        winnerBool = false;
        breakWhile = false;
        ArrayList<Integer> nextRowNull = new ArrayList();
        ArrayList<Integer> nextColNull = new ArrayList();
        int rowCount = rowNum - 1;
        int colCount = colNum - 1;
        //Check the rows/columns to the left and top of the grid from the starting point.
        while (rowCount > -1 && colCount > -1) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in each diagonal.
            Node node = getNodeByRowColumnIndex(rowCount, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowCount);
                breakWhile = nextNull.getBool();
                if (nextNull.getArrayList().get(0) > -1) {
                    nextRowNull.add(nextNull.getArrayList().get(0));
                    nextColNull.add(nextNull.getArrayList().get(1));
                }
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row/column component.
            rowCount = breakWhile ? -1 : rowCount - 1;
            colCount = breakWhile ? -1 : colCount - 1;
        }
        rowCount = rowNum + 1;
        colCount = colNum + 1;
        breakWhile = false;
        //Check the rows/columns to the right and bottom of the grid from the starting point.
        while (rowCount < rows && colCount < cols) {
            // Get each node and check whether there are 4 in a row if checking for a winner. Otherwise check the number of similar tokens in each diagonal.
            Node node = getNodeByRowColumnIndex(rowCount, colCount, connectGrid);
            if (checkWinner) {
                breakWhile = checkNodeWinner(node, winnerBool, breakWhile, fill, rowNum, colNum, aiRunning);
            } else {
                Boolean_NextNullArray nextNull = checkNodeNumber(node, winnerBool, breakWhile, fill, colCount, rowCount);
                breakWhile = nextNull.getBool();
                nextRowNull.add(nextNull.getArrayList().get(0));
                nextColNull.add(nextNull.getArrayList().get(1));
            }
            //If criteria to break the while loop met, leave the while loop. Otherwise go to the next row/column component.
            rowCount = breakWhile ? rows : rowCount + 1;
            colCount = breakWhile ? cols : colCount + 1;
        }
        ArrayList<Integer> finalVal = new ArrayList();
        finalVal.add(count);
        for (int i = 0; i < nextRowNull.size(); i++) {
            finalVal.add(nextRowNull.get(i));
            finalVal.add(nextColNull.get(i));
        }
        return finalVal;
    }

    public void clearSystem() {
        //If winner encountered clear the system for a new game.ß
        connectGrid.getChildren().removeAll(circleArrays);
        connectGrid.setGridLinesVisible(true);
        playerLabel.setText("Ready Player One");
    }

    public void computerAI(int rows, int cols, ArrayList<ArrayList<Integer>> results, int rowNum, int colNum) {
        simpleAI(rows,cols,results);
        //advancedAI(rows, cols, rowNum, colNum);
    }

    public void simpleAI(int rows, int cols, ArrayList<ArrayList<Integer>> results) {

        //If the computer AI is enabled, allow the computer to play and check whether it has won the game.
        if (connectCheck.isSelected() && playerLabel.getText().contains("Two")) {
            boolean tokenAdded = false;
            int checkVal = 2;
            while (!tokenAdded & checkVal > -1) {
                //While no token has been added to the board, find the row, column, or diagonal that the player has the most tokens in and place a token to block opponent.
                if (results.get(0).get(0) > checkVal) {
                    int length = (results.get(0).size() - 1) / 2;
                    for (int i = 0; i < length; i++) {
                        Node node = getNodeByRowColumnIndex(results.get(0).get(2 * i + 1) + 1, results.get(0).get(2 * i + 2), connectGrid);
                        if (node != null | results.get(0).get(1) == rows - 1 && !tokenAdded) {
                            tokenAdded = true;
                            createCircle(rows, cols, results.get(0).get(2 * i + 1), results.get(0).get(2 * i + 2));
                            playerLabel.setText(playerLabel.getText().contains("One") && connectGrid.getChildren().size() > 1 ? "Ready Player Two" : "Ready Player One");
                        }
                    }
                } else if (results.get(1).get(0) > checkVal) {
                    createCircle(rows, cols, results.get(1).get(1), results.get(1).get(2));
                    tokenAdded = true;
                    playerLabel.setText(playerLabel.getText().contains("One") && connectGrid.getChildren().size() > 1 ? "Ready Player Two" : "Ready Player One");
                } else if (results.get(2).get(0) > 2) {
                    int length = (results.get(2).size() - 1) / 2;
                    for (int i = 0; i < length; i++) {
                        Node node = getNodeByRowColumnIndex(results.get(2).get(2 * i + 1) + 1, results.get(2).get(2 * i + 2), connectGrid);
                        if (node != null && !tokenAdded) {
                            tokenAdded = true;
                            createCircle(rows, cols, results.get(2).get(2 * i + 1), results.get(2).get(2 * i + 2));
                            playerLabel.setText(playerLabel.getText().contains("One") && connectGrid.getChildren().size() > 1 ? "Ready Player Two" : "Ready Player One");
                        }
                    }
                } else if (results.get(3).get(0) > checkVal) {
                    int length = (results.get(3).size() - 1) / 2;
                    for (int i = 0; i < length; i++) {
                        Node node = getNodeByRowColumnIndex(results.get(3).get(2 * i + 1) + 1, results.get(3).get(2 * i + 2), connectGrid);
                        if (node != null && !tokenAdded) {
                            tokenAdded = true;
                            createCircle(rows, cols, results.get(3).get(2 * i + 1), results.get(3).get(2 * i + 2));
                            playerLabel.setText(playerLabel.getText().contains("One") && connectGrid.getChildren().size() > 1 ? "Ready Player Two" : "Ready Player One");
                        }
                    }
                }
                if (!tokenAdded && checkVal == 0 && !winnerBool) {
                    //In the off chance that no token was added in the while loop, add a token to a random column.
                    int colVal = 0;
                    while (!tokenAdded & colVal != cols - 1) {
                        for (int j = rows - 1; j > -1; j--) {
                            Node node = getNodeByRowColumnIndex(j, colVal, connectGrid);
                            if (node == null && !tokenAdded) {
                                tokenAdded=true;
                                createCircle(rows, cols, j, colVal);
                            }
                        }
                        colVal++;
                    }
                }
                checkVal--;
            }

        }
    }

    public void advancedAI(int rows, int cols, int rowNum, int colNum) {
        //Monte Carlo Tree Analysis. Not finished.
        boolean addTokenAI = true;
        ArrayList<Integer> losses = new ArrayList();
        for (int i = 0; i < cols; i++) {
            losses.add(0);
        }
        int depth = 5;
        for (int numCols = 0; numCols < cols; numCols++) {
            GridPane simulationGrid = new GridPane();
            Circle circle = new Circle(25, Color.BLUE);
            simulationGrid.add(circle, colNum, rowNum);
            for (int j = rows - 1; j > -1; j--) {
                Node node = getNodeByRowColumnIndex(j, numCols, simulationGrid);
                if (node == null && addTokenAI) {
                    addTokenAI = false;
                    Circle circleComputer = new Circle(25, Color.RED);
                    simulationGrid.add(circleComputer, numCols, j);
                }
            }
            for (int depNum = 0; depNum < depth; depNum++) {
                for (int i = 0; i < cols; i++) {
                    addTokenAI = true;
                    for (int j = rows - 1; j > -1; j--) {
                        Node node = getNodeByRowColumnIndex(j, i, simulationGrid);
                        if (node == null && addTokenAI) {
                            addTokenAI = false;
                            Circle circleComputer = new Circle(25, Color.BLUE);
                            simulationGrid.add(circleComputer, i, j);
                            int loss = checkWinner(rows, cols, i, j, Color.BLUE, true);
                            if (loss > 3) {
                                losses.set(numCols, losses.get(numCols) + 1);
                            }
                        }
                    }
                    addTokenAI = true;
                    for (int j = 0; j < cols; j++) {
                        for (int k = rows - 1; k > -1; k--) {
                            Node node = getNodeByRowColumnIndex(k, j, simulationGrid);
                            if (node == null && addTokenAI) {
                                addTokenAI = false;
                                Circle circleComputer = new Circle(25, Color.RED);
                                simulationGrid.add(circleComputer, j, k);
                            }
                        }
                    }
                }
            }
        }
        int columnAdd = 0;
        for (int i = 0; i < cols; i++) {
            int maxLosses = 100000;
            if (losses.get(i) < maxLosses) {
                maxLosses = losses.get(i);
                columnAdd = i;
            }
        }
        addTokenAI = true;
        for (int j = rows - 1; j > -1; j--) {
            Node node = getNodeByRowColumnIndex(j, columnAdd, connectGrid);
            if (node == null && addTokenAI) {
                addTokenAI = false;
                createCircle(rows, cols, j, columnAdd);
                playerLabel.setText("Ready Player One");
            }
        }
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        //Get the column and row index of a given node
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (gridPane.getRowIndex(node) != null & gridPane.getColumnIndex(node) != null) {
                if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            }
        }
        return result;
    }

    public void initialize() {
        //Initialize Scene and add all buttons to button array list
        buttonArrays = new ArrayList<Button>(Arrays.asList(addToken1, addToken2, addToken3, addToken4, addToken5, addToken6, addToken7));
    }
}
