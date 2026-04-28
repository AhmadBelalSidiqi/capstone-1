package com.pluralsight;


import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = loadOldTransactions();
    static ArrayList<Transaction> filteredTransactions = new ArrayList<>();
    static boolean toHomeScreeMenu = false;

    public static void main(String[] args) {
        homeScreenMenu();
        System.out.println("Thank for using our app");

    }

    public static void homeScreenMenu() {
        String menu = """
                Please chose on of the following option:
                D) -Add deposit-
                P) -Make Payments(Debit)-
                L) -ledger-
                X) -Exit-
                """;
        boolean running = true;
        do {
            System.out.println(menu);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "d" -> addDeposit();
                case "p" -> makePayments();
                case "l" -> {
                    sortTransactionsList(false);
                    showLedgerMenu();
                }
                // TODO: better way?
                case "x" -> running = false;
                default -> System.out.println("Wrong Input");

            }
        } while (running);
    }

    public static void makePayments() {
        boolean running = true;
        do {
            System.out.println("Please enter the description: ");
            String description = scanner.nextLine().trim();
            System.out.println("Please Enter the vendor name: ");
            String vendor = scanner.nextLine().trim();
            System.out.println("Please Enter the amount: ");
            // amount multiple to minus one to make it negative.
            double amount = (Double.parseDouble(scanner.nextLine().trim())) * -1;
            transactions.add(new Transaction(description, vendor, amount));

            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))) {
                running = false;
            }
        } while (running);
    }

    public static void showLedgerMenu() {
        String ledgerMenu = """
                Please choose one of the following option:
                A) ALL- Display all entries
                D) Deposits- Display only the deposits into the account
                P) Payments- Display only the payments into the account
                R) Report
                X) Go back to home screen
                """;
        boolean running = true;
        do {
            System.out.println(ledgerMenu);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "a" -> displayAllTransactions();
                case "d" -> displayDeposits();
                case "p" -> displayPayments();
                case "r" -> {
                    showReportMenu();
                    if (toHomeScreeMenu)
                        return;
                }
                case "x" -> running = false;
                default -> System.out.println("Wrong Input");
            }

        } while (running);

    }

    public static void showReportMenu() {
        String menu = """
                Please choose one of the following:
                1) Month to Date
                2) Previous Month
                3) Year to Date
                4) Previous Year
                5) Search by Vendor
                0) Go back
                H) Home Screen
                """;
        boolean running = true;
        do {
            filteredTransactions.clear();
            System.out.println(menu);
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {


                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "0" -> running = false;
                case "h" -> {
                    toHomeScreeMenu = true;
                    return;
                }
                default -> System.out.println("wrong Input");

            }
        } while (running);

    }

    public static void addDeposit() {
        boolean running = true;
        do {
            System.out.println("Please enter the description: ");
            String description = scanner.nextLine().trim();
            System.out.println("Please Enter the vendor name: ");
            String vendor = scanner.nextLine().trim();
            System.out.println("Please Enter the amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            transactions.add(new Transaction(description, vendor, amount));

            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))) {
                running = false;
            }
        } while (running);
    }

    public static void displayPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                displayTransaction(transaction);
                payments.add(transaction);
            }
        }
        System.out.println("Do you want to generate a report(Yes/No): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "Payments";
            writeReportToFile(payments, name);
        }
    }

    public static void displayDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                displayTransaction(transaction);
                deposits.add(transaction);

            }
            System.out.println("Do you want to generate a report (yes/no):");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                String name = "deposits";
                writeReportToFile(deposits, name);
            }
        }
    }

    public static void monthToDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate month = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalTime time = LocalTime.of(1, 1, 1);
        LocalDateTime thisMonth = LocalDateTime.of(month, time);
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().isAfter(thisMonth)) {
                filteredTransactions.add(transaction);
            }
        }
        String name = "monthToDate";
        writeReportToFile(filteredTransactions, name);
    }

    public static void previousMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime time = LocalTime.of(1, 1);
        LocalDate lastMonth = LocalDate.of(now.getYear(), now.getMonth().minus(1), 1);
        LocalDate month = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDateTime previousMonth = LocalDateTime.of(lastMonth, time);
        LocalDateTime thisMonth = LocalDateTime.of(month, time);
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().isAfter(previousMonth) && transaction.getDateAndTime().isBefore(thisMonth)) {
                filteredTransactions.add(transaction);
            }
        }
        String name = "PreviousMonth";
        writeReportToFile(filteredTransactions, name);
    }

    public static void yearToDate() {
        LocalDateTime now = LocalDateTime.now();
        for (Transaction transaction : transactions) {
            // every transaction in this year and before current date and time , we want to avoid transaction that would be place in the future of current date and time.
            if (transaction.getDateAndTime().getYear() == now.getYear() && transaction.getDateAndTime().isBefore(now)) {
                filteredTransactions.add(transaction);
            }
        }
        String name = "YearToDate";
        writeReportToFile(filteredTransactions, name);
    }

    public static void previousYear() {
        LocalDateTime now = LocalDateTime.now();
        int previousYear = now.getYear() - 1;
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().getYear() == previousYear) {
                filteredTransactions.add(transaction);
            }
        }
        String name = "PreviousYear";
        writeReportToFile(filteredTransactions, name);
    }

    public static void searchByVendor() {
        System.out.println("Please enter the vendor name: ");
        String vendorInput = scanner.nextLine();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorInput)) {
                filteredTransactions.add(transaction);
            }
        }
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for vendor: " + vendorInput);
            return;
        }
        String name = "v:" + vendorInput;
        writeReportToFile(filteredTransactions, name);
    }

    public static void displayTransaction(Transaction transaction) {
        System.out.printf("Date: %-20s\t Description: %-30s\t Vendor: %-15s\t Amount: %.2f %n",
                transaction.getDateAndTimeFormated(),
                transaction.getDescription(),
                transaction.getVendor(),
                transaction.getAmount());

    }

    public static void displayAllTransactions() {
        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    public static ArrayList<Transaction> loadOldTransactions() {
        ArrayList<Transaction> oldTransactions = new ArrayList<>();
        String fileLocation = "src/main/resources/OldTranscations.cvs";
        try {
            FileReader fileReader = new FileReader(fileLocation);
            BufferedReader reader = new BufferedReader(fileReader);
            String header = "date|time|description|vendor|amount";
            String line;
            while ((line = reader.readLine()) != null) {
                // Handling the header if it exits
                if (!line.equalsIgnoreCase(header)) {
                    String[] lineSpilt = line.split("\\|");
                    String date = lineSpilt[0];
                    String time = lineSpilt[1];
                    String dateTimeString = date + "T" + time;
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                    String description = lineSpilt[2];
                    String vendor = lineSpilt[3];
                    double amount = Double.parseDouble(lineSpilt[4]);
                    oldTransactions.add(new Transaction(dateTime, description, vendor, amount));

                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e);
        }
        return oldTransactions;
    }

    public static void sortTransactionsList(boolean ascending) {

        if (ascending) {
            transactions.sort(Comparator.comparing(Transaction::getDateAndTime));
        } else {
            transactions.sort(Comparator.comparing(Transaction::getDateAndTime).reversed());

        }

    }

    public static void writeReportToFile(ArrayList<Transaction> transactions, String name) {
        String fileLocation = "src/main/resources/transactions" + name.trim() + ".cvs";
        try {
            FileWriter fileWriter = new FileWriter(fileLocation, false);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write("Date|Time|Description|Vendor|Amount");
            writer.newLine();
            for (Transaction transaction : transactions) {
                writer.append(transaction.getDateAndTimeFormated())
                        .append("|").append(transaction.getDescription())
                        .append("|").append(transaction.getVendor())
                        .append("|").append(String.valueOf(transaction.getAmount()));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("IO Exception: " + e);
        }
        System.out.println("Report Generated successfully");
    }


}
