import java.util.Scanner;
import model.Board;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int rows = 10, cols = 10, mines = 12;
            Board board = new Board(rows, cols, mines); // FIX: rows, cols

            boolean firstMove = true;
            boolean gameOver = false;

            System.out.println("Commands:");
            System.out.println("  r <row> <col>   (reveal)");
            System.out.println("  f <row> <col>   (flag/unflag)");
            System.out.println("  q               (quit)");
            System.out.println();

            while (!gameOver) {
                board.print(false);
                System.out.print("> ");

                String cmd = sc.next();

                if (cmd.equalsIgnoreCase("q")) {
                    System.out.println("Bye!");
                    gameOver = true; // end this round
                    continue;
                }

                // only read r/c if command isn't q
                int r = sc.nextInt();
                int c = sc.nextInt();

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
