import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
public class TransactionManager
{
    private final List<Transaction> transactions = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TransactionFileHandler fileHandler = new TransactionFileHandler("transactions.txt", formatter);

    public void addTransaction() {
        System.out.print("Enter type (income/expense): ");
        String type = scanner.nextLine().toLowerCase();

        String category;
        if (type.equals("income")) {
            System.out.print("Enter category (salary/business): ");
        } else if (type.equals("expense")) {
            System.out.print("Enter category (food/rent/travel): ");
        } else {
            System.out.println("Invalid type!");
            return;
        }

        category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        LocalDate date;

        try {
            date = LocalDate.parse(dateStr, formatter);
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            int currentYear = LocalDate.now().getYear();

            if (month < 1 || month > 12) {
                System.out.println("Invalid month: must be between 1 and 12.");
                return;
            }

            if (day < 1 || day > 30) {
                System.out.println("Invalid day: must be between 1 and 30.");
                return;
            }

            if (year > currentYear) {
                System.out.println("Invalid year: cannot be in the future.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
            return;
        }


        Transaction transaction = new Transaction(type, category, amount, date);
        transactions.add(transaction);
        fileHandler.appendTransaction(transaction);

        System.out.println("Transaction added and saved to file.");
    }

    public void showMonthlySummary() {
        Map<String, Double> summary = new HashMap<>();
        System.out.print("Enter month (yyyy-MM): ");
        String month = scanner.nextLine();

        for (Transaction t : transactions) {
            if (t.getDate().toString().startsWith(month)) {
                String key = t.getType() + " - " + t.getCategory();
                summary.put(key, summary.getOrDefault(key, 0.0) + t.getAmount());
            }
        }

        if (summary.isEmpty()) {
            System.out.println("No transactions for the given month.");
            return;
        }

        System.out.println("\n--- Monthly Summary for " + month + " ---");
        summary.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public void loadFromFile() {
        transactions.addAll(fileHandler.loadTransactions());
    }
    public void loadFromOuterFile() {
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();
        List<Transaction> transactions = fileHandler.loadFromFile(path);
        System.out.println("Loaded " + transactions.size() + " transactions.");
    }
}
