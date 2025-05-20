import java.util.Scanner;

public class ExpenseTracker
{
    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionManager transactionManager = new TransactionManager();

    public static void main(String[] args) {
        transactionManager.loadFromFile();

        while (true) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Transaction");
            System.out.println("2. Show Monthly Summary");
            System.out.println("3. Load Transactions from File");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> transactionManager.addTransaction();
                case 2 -> transactionManager.showMonthlySummary();
                case 3 -> transactionManager.loadFromOuterFile();
                case 4 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
