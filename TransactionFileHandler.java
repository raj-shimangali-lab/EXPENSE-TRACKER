import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class TransactionFileHandler
{
    private final String fileName;
    private final DateTimeFormatter formatter;

    public TransactionFileHandler(String fileName, DateTimeFormatter formatter) {
        this.fileName = fileName;
        this.formatter = formatter;
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> loadedTransactions = new ArrayList<>();
        try {
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 4) continue;

                    String type = parts[0];
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3], formatter);

                    loadedTransactions.add(new Transaction(type, category, amount, date));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
        return loadedTransactions;
    }

    public void appendTransaction(Transaction transaction) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.printf("%s,%s,%.2f,%s%n",
                    transaction.getType(), transaction.getCategory(), transaction.getAmount(), transaction.getDate());
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
    public static List<Transaction> loadFromFile(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String type = parts[0];
                LocalDate date = LocalDate.parse(parts[3]);
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                System.out.println("TYPE:-"+type + " ; DATE:-" + date + " ; CATEGORY:- " + category + " ; AMOUNT:- " + amount);
                transactions.add(new Transaction(type, category, amount, date));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }
}
