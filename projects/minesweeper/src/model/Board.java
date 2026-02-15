package model;
import java.util.Random;
public class Board {
    private final Cell[][] cells;
    private final int cols;
    private final int rows;
    private final int totalMines;

    /**
     * Constructor for the Board class.
     * @param rows
     * @param cols
     * @param totalMines
     */
    public Board(int rows, int cols, int totalMines) {
        this.cols = cols;
        this.rows = rows;
        this.totalMines = totalMines;
        this.cells = new Cell[rows][cols];
        initializeCells();
    }

    /** 
     * Initializes the cells of the board to be empty (not mines).
     */
    private void initializeCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.cells[r][c] = new Cell();
            }
        }
    }

    /** 
     * Places the mines on the board.
     * @param safeRow
     * @param safeCol
     */
    public void placeMines(int safeRow, int safeCol) {
        Random rand = new Random();
        int placed = 0;

        while (placed < totalMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            /** 
             * Ensures that the randomly selected cell is not already a mine and is not the safe cell (the first cell clicked by the player).
             */
            if (!cells[r][c].isMine() && !(r == safeRow && c == safeCol)) {
                cells[r][c].setMine();
                placed++;
            }
        }
        computeAdjacentCounts();
    }

    /** 
     * Computes the number of mines adjacent to each cell.
     */
    private void computeAdjacentCounts() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (this.cells[r][c].isMine()) {
                    continue;
                }
                
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) {
                            continue;
                        }
                        int newR = r + dr;
                        int newC = c + dc;

                        if (inBounds(newR, newC) && cells[newR][newC].isMine()) {
                            count++;
                        }
                    }
                }
                cells[r][c].setAdjacentMines(count);
            }
        }
    }

    /** 
     * Checks if the given row and column are within the bounds of the board.
     */
    private boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Reveals the cell at the given row and column.
     * @param r
     * @param c
     * @return
     */
    public boolean reveal(int r, int c) {
        if (!inBounds(r, c)) {
            return false;
        }
        Cell cell = cells[r][c];

        if (cell.isRevealed() || cell.isFlagged()) {
            return false;
        }
        cell.setRevealed();

        if (cell.isMine()) {
            return true; // Game over
        }
    
        if (cell.getAdjacentMines() == 0) {
            /** 
             * If the revealed cell has no adjacent mines, we perform a flood fill to reveal all connected empty cells and their adjacent numbers.
             */
            floodFill(r,c);
        }

        return false;
    }

    /**
     * Performs a flood fill to reveal all connected empty cells and their adjacent numbers.
     * @param r
     * @param c
     */
    private void floodFill(int r, int c) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newR = r + dr;
                int newC = c + dc;

                if (!inBounds(newR, newC)) {
                    continue;
                }

                Cell neighbor = cells[newR][newC];
                if (!neighbor.isRevealed() && !neighbor.isFlagged() && !neighbor.isMine()) {
                    neighbor.setRevealed();

                    if (neighbor.getAdjacentMines() == 0) {
                        floodFill(newR, newC);
                    }
                }
            }
         }
    }

public void print(boolean revealAll) {
    // column headers
    System.out.print("   ");
    for (int c = 0; c < cols; c++) System.out.print(c + " ");
    System.out.println();

    for (int r = 0; r < rows; r++) {
        // row header
        System.out.printf("%2d ", r);

        for (int c = 0; c < cols; c++) {
            Cell cell = cells[r][c];
            char ch;

            if (revealAll) {
                if (cell.isMine()) ch = '*';
                else if (cell.getAdjacentMines() == 0) ch = '.';
                else ch = (char) ('0' + cell.getAdjacentMines());
            } else {
                if (cell.isFlagged()) ch = 'F';
                else if (!cell.isRevealed()) ch = '#';
                else if (cell.isMine()) ch = '*';
                else if (cell.getAdjacentMines() == 0) ch = '.';
                else ch = (char) ('0' + cell.getAdjacentMines());
            }

            System.out.print(ch + " ");
        }
        System.out.println();
    }
}


    public void toggleFlag(int r, int c) {
        if (!inBounds(r, c)) return;
        cells[r][c].toggleFlagged();
    }

    public boolean isWin() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (!cell.isMine() && !cell.isRevealed()) return false;
            }
        }
        return true;
    }

    public int getRows() { 
        return rows; 
    }
    
    public int getCols() { 
        return cols; 
    }

}
