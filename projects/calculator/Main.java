import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simple Calculator");
        System.out.println("Commands: add x y | sub x y | mul x y | div x y | exit");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            handleCommand(line);
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static void handleCommand(String line) {
        String[] parts = line.split("\\s+");

        if (parts.length != 3) {
            System.out.println("Invalid format. Example: add 3 5");
            return;
        }

        String op = parts[0];

        try {
            int a = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);

            int result;
            switch (op) {
                case "add":
                    result = a + b;
                    break;
                case "sub":
                    result = a - b;
                    break;
                case "mul":
                    result = a * b;
                    break;
                case "div":
                    result = a / b;
                    break;
                default:
                    System.out.println("Unknown command: " + op);
                    return;
            }

            System.out.println("= " + result);

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid integers.");
        } catch (ArithmeticException e) {
            System.out.println("Math error: " + e.getMessage());
        }
    }
}
