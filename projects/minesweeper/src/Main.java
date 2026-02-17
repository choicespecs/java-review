import java.util.Scanner;
import java.util.Set;
import model.Board;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Minesweeper!");

        while (true) {
            int rows = 0, cols = 0, mines = 0;
            Board board;
            System.out.println("Create board:");
            System.out.println("b - beginner (9x9, 10 mines)");
            System.out.println("i - intermediate (16x16, 40 mines)");
            System.out.println("e - expert (30x16, 99 mines)");
            System.out.println("c - custom");
            String rowColInput = sc.nextLine();
            if (rowColInput.equalsIgnoreCase("b")) {
                rows = 9;
                cols = 9;
                mines = 10;
            } else if (rowColInput.equalsIgnoreCase("i")) {
                rows = 16;
                cols = 16;
                mines = 40;
            } else if (rowColInput.equalsIgnoreCase("e")) {
                rows = 16;
                cols = 30;
                mines = 99;
            } else if (rowColInput.equalsIgnoreCase("c")) {
                System.out.println("Create custom board <rows> <cols> <mines>");
                String[] parts = rowColInput.trim().split("\\s+");
                try {
                    if (parts.length > 3) {
                        System.out.println("Invalid format. Example: 10 10 12");
                        continue;
                    }
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    int m = Integer.parseInt(parts[2]);

                    if (m <= 0 || c <= 0 || r <= 0) {
                        System.out.println("Rows, Mines, Cols must be positive and greater than 0");
                        continue;
                    }
                    if (m >= r * c) {
                        System.out.println("Mines must be less than the number of cells");
                        continue;
                    }
                    rows = r;
                    cols = c;
                    mines = m;
                    
                } catch (NumberFormatException e) {
                    System.out.println("Row and column must be numbers.");
                    continue;
                }
            } else {
                System.out.println("Invalid option. Use b/i/e/c.");
                continue;
            }
            board = new Board(rows, cols, mines);

            boolean firstMove = true;
            boolean gameOver = false;

            System.out.println("Commands:");
            System.out.println("  r <row> <col>   (reveal)");
            System.out.println("  f <row> <col>   (flag/unflag)");
            System.out.println("  q               (quit)");
            System.out.println();

            Set<String> commands = Set.of("r", "f", "q");

            while (!gameOver) {
                board.print(false);
                System.out.print("> ");

                String cmd = sc.next();
                
                if (!commands.contains(cmd)) {
                    System.out.println("Invalid command use r/f/q.");
                    continue;
                }

                if (cmd.equalsIgnoreCase("q")) {
                    System.out.println("Bye!");
                    gameOver = true; // end this round
                    continue;
                }

                // only read r/c if command isn't q
                int r = sc.nextInt();
                int c = sc.nextInt();

                if (r < 0 || r >= rows || c < 0 || c >= cols) {
                    System.out.println("Invalid row/column.");
                    continue;
                }

                if (cmd.equalsIgnoreCase("f")) {
                    board.toggleFlag(r, c);
                    continue;
                }

                if (cmd.equalsIgnoreCase("r")) {
                    if (firstMove) {
                        board.placeMines(r, c); // first click safe
                        firstMove = false;
                    }

                    boolean hitMine = board.reveal(r, c);
                    if (hitMine) {
                        System.out.println("BOOM! You hit a mine.");
                        board.print(true); // reveal all
                        gameOver = true;
                    } else if (board.isWin()) {
                        System.out.println("You win!");
                        board.print(true);
                        gameOver = true;
                    }
                } else {
                    System.out.println("Unknown command. Use r/f/q.");
                }
            }

            // IMPORTANT: consume leftover newline from nextInt()/next()
            sc.nextLine();

            System.out.print("Play again? (y/n): ");
            String again = sc.nextLine().trim().toLowerCase();
            if (!again.equals("y") && !again.equals("yes")) {
                break;
            }
        }

        sc.close();
    }
}
