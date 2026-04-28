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

    public static void main(String[] args) {
        homeScreenMenu();
        System.out.println("Thank for using our app");

    }

    public static void homeScreenMenu() {
        String menuText = """
                Please chose on of the following option:
                D) -Add deposit-
                P) -Make Payments(Debit)-
                L) -ledger-
                X) -Exit-
                """;
        boolean running = true;
        do {
            System.out.println(menuText);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "d" -> addDeposit();
                case "p" -> makePayments();
                case "l" -> {
                    sortTransactionsList();
                    showLedgerMenu();
                }
                // TODO: better way?
                case "x" -> running = false;
                default -> System.out.println("Wrong Input");

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
                H) Home - Go back to the home page
                """;
        boolean running = true;
        do {
            System.out.println(ledgerMenu);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "a" -> displayAllTransactions();
                case "d" -> displayDeposits();
                case "p" -> displayPayments();
                case "r" -> showReportMenu();
                case "h" -> running = false;
                default -> System.out.println("Wrong Input");
            }

        } while (running);

    }

    public static void customSearch() {
        System.out.println("Start date(press Enter to skip): ");
        LocalDateTime startDate = getDate();
        System.out.println("End Date(press Enter to skip): ");
        LocalDateTime endDate = getDate();
        System.out.println("Description(press Enter to skip): ");
        String description = scanner.nextLine().trim();
        System.out.println("Vendor(press Enter to skip): ");
        String vendor = scanner.nextLine().trim();
        System.out.println("Amount(press Enter to skip):");
        String amountInput = scanner.nextLine().trim();

        ArrayList<Transaction> result = applyFilter(startDate, endDate, description, vendor, amountInput);
        if (result.isEmpty()) {
            System.out.println("No transaction matched your criteria.");
        } else {
            writeReportOfThisTransactions(result, "CustomSearch");
        }
    }


    private static ArrayList<Transaction> applyFilter(LocalDateTime startDate, LocalDateTime endDate, String description, String vendor, String amountInput) {
        ArrayList<Transaction> result;
        result = filterByDate(startDate, endDate);
        result = filterByDescription(description, result);
        result = filterByVendor(vendor, result);
        result = filterByAmount(amountInput, result);

        return result;

    }

    private static ArrayList<Transaction> filterByDate(LocalDateTime startDate, LocalDateTime endDate) {
        ArrayList<Transaction> dateTimeFilteredTransactions = new ArrayList<>();
        boolean isStartDate = startDate != null;
        boolean isEndDate = endDate != null;
        for (Transaction transaction : transactions) {
            if (isStartDate && transaction.getDateAndTime().isBefore(startDate)) {
                continue;
            }
            if (isEndDate && transaction.getDateAndTime().isAfter(endDate)) {
                continue;
            }
            dateTimeFilteredTransactions.add(transaction);

        }
        return dateTimeFilteredTransactions;
    }

    private static ArrayList<Transaction> filterByAmount(String amountInput, ArrayList<Transaction> result) {
        ArrayList<Transaction> filterByAmount = new ArrayList<>();
        if (amountInput.isEmpty())
            return result;
        else {
            double amount = Double.parseDouble(amountInput);
            for (Transaction transaction : result) {
                if (transaction.getAmount() == amount) {
                    filterByAmount.add(transaction);
                }
            }
            return filterByAmount;
        }
    }

    private static ArrayList<Transaction> filterByVendor(String vendor, ArrayList<Transaction> result) {
        ArrayList<Transaction> filterByVendor = new ArrayList<>();
        if (vendor.isEmpty()) {
            return result;
        } else {
            for (Transaction transaction : result) {
                if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                    filterByVendor.add(transaction);
                }
            }
            return filterByVendor;
        }
    }

    private static ArrayList<Transaction> filterByDescription(String description, ArrayList<Transaction> result) {
        ArrayList<Transaction> filteredByDescription = new ArrayList<>();
        if (description.isEmpty()) {
            return result;
        } else {
            for (Transaction transaction : result) {
                if (transaction.getDescription().contains(description)) {
                    filteredByDescription.add(transaction);
                }
            }
            return filteredByDescription;
        }
    }


    public static void addDeposit() {
        boolean running = true;
        do {
            System.out.println("Please enter the year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            LocalDateTime userDateTime = getUserDateTime(year);
            System.out.println("Please enter the description: ");
            String description = scanner.nextLine().trim();
            System.out.println("Please Enter the vendor name: ");
            String vendor = scanner.nextLine().trim();
            System.out.println("Please Enter the amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            transactions.add(new Transaction(userDateTime, description, vendor, amount));


            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))) {
                running = false;
            }
        } while (running);
        saveTransactionToFile();
    }

    public static void displayDeposits() {

        sortTransactionsList();
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                displayTransaction(transaction);
                deposits.add(transaction);

            }
        }
        if(userWantsReport()){
            String name = "deposits";
            writeReportOfThisTransactions(deposits, name);
        }

    }

    public static void makePayments() {
        boolean running = true;
        do {
            System.out.println("Please enter the year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            LocalDateTime userDateTime = getUserDateTime(year);
            System.out.println("Please enter the description: ");
            String description = scanner.nextLine().trim();
            System.out.println("Please Enter the vendor name: ");
            String vendor = scanner.nextLine().trim();
            System.out.println("Please Enter the amount: ");
            // amount multiple to minus one to make it negative.
            double amount = (Double.parseDouble(scanner.nextLine().trim())) * -1;
            transactions.add(new Transaction(userDateTime, description, vendor, amount));

            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))) {
                running = false;
            }
        } while (running);
        saveTransactionToFile();
    }

    public static void displayPayments() {
        sortTransactionsList();
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                displayTransaction(transaction);
                payments.add(transaction);
            }
        }
        if(userWantsReport()){
            String name = "Payments";
            writeReportOfThisTransactions(payments, name);
        }

    }


    public static void showReportMenu() {
        String menu = """
                Please choose one of the following:
                1) Month to Date
                2) Previous Month
                3) Year to Date
                4) Previous Year
                5) Search by Vendor
                6) Custom Search
                0) Go back
                """;
        boolean running = true;
        do {
            System.out.println(menu);
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {


                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> running = false;
                default -> System.out.println("wrong Input");

            }
        } while (running);

    }

    public static void monthToDate() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDate month = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalTime time = LocalTime.of(1, 1, 1);
        LocalDateTime thisMonth = LocalDateTime.of(month, time);
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().isAfter(thisMonth)) {
                result.add(transaction);
            }
        }
        displayTransactions(result);
        System.out.println("Do you wan to generate a report (Yes/No): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "monthToDate";
            writeReportOfThisTransactions(result, name);
        }
    }

    public static void previousMonth() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalTime time = LocalTime.of(1, 1);
        LocalDate lastMonth = LocalDate.of(now.getYear(), now.getMonth().minus(1), 1);
        LocalDate month = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDateTime previousMonth = LocalDateTime.of(lastMonth, time);
        LocalDateTime thisMonth = LocalDateTime.of(month, time);
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().isAfter(previousMonth) && transaction.getDateAndTime().isBefore(thisMonth)) {
                result.add(transaction);
            }
        }
        displayTransactions(result);
        System.out.println("Do you wan to generate a report (Yes/No): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "PreviousMonth";
            writeReportOfThisTransactions(result, name);
        }
    }

    public static void yearToDate() {
        ArrayList<Transaction> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Transaction transaction : transactions) {
            // every transaction in this year and before current date and time , we want to avoid transaction that would be place in the future of current date and time.
            if (transaction.getDateAndTime().getYear() == now.getYear() && transaction.getDateAndTime().isBefore(now)) {
                result.add(transaction);
            }
        }
        displayTransactions(result);
        System.out.println("Do you want to generate a repot(yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "YearToDate";
            writeReportOfThisTransactions(result, name);
        }
    }

    public static void previousYear() {
        ArrayList<Transaction> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        int previousYear = now.getYear() - 1;
        for (Transaction transaction : transactions) {
            if (transaction.getDateAndTime().getYear() == previousYear) {
                result.add(transaction);
            }
        }
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "PreviousYear";
            writeReportOfThisTransactions(result, name);
        }
    }

    public static void searchByVendor() {
        ArrayList<Transaction> result = new ArrayList<>();
        System.out.println("Please enter the vendor name: ");
        String vendorInput = scanner.nextLine();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorInput)) {
                result.add(transaction);
            }
        }
        if (result.isEmpty()) {
            System.out.println("No transactions found for vendor: " + vendorInput);
            return;
        }
        displayTransactions(result);
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            String name = "v:" + vendorInput;
            writeReportOfThisTransactions(result, name);
        }
    }


    public static void displayAllTransactions() {
        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    public static void displayTransactions(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    public static void displayTransaction(Transaction transaction) {
        System.out.printf("Date: %-20s\t Description: %-30s\t Vendor: %-15s\t Amount: %.2f %n",
                transaction.getDateAndTimeFormated(),
                transaction.getDescription(),
                transaction.getVendor(),
                transaction.getAmount());

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

    public static void sortTransactionsList() {
        transactions.sort(Comparator.comparing(Transaction::getDateAndTime).reversed());
    }

    public static void writeReportOfThisTransactions(ArrayList<Transaction> transactions, String name) {
        String fileLocation = "src/main/resources/transactions" + name.trim() + ".csv";
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

    public static void saveTransactionToFile() {
        sortTransactionsList();
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/Transcations.csv");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (Transaction transaction : transactions) {
                writer.write(transaction.getDateAndTimeFormated() + "|"
                        + transaction.getDescription() + "|"
                        + transaction.getVendor() + "|"
                        + transaction.getAmount() + "|");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean userWantsReport() {
        System.out.println("Do you want to generate a report (yes/no):");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }

    // region getUserDatetime

    public static LocalDateTime getUserDateTime(int userYear) {

        int month = getUserMonth();
        int day = getUserDay();
        int hour = getUserHour();
        int min = getUserMin();
        int sec = getUserSec();
        LocalDate date = LocalDate.of(userYear, month, day);
        LocalTime time = LocalTime.of(hour, min, sec);
        return LocalDateTime.of(date, time);
    }

    private static LocalDateTime getDate() {
        System.out.println("Please enter the year: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        } else {
            int year = Integer.parseInt(input);
            return getUserDateTime(year);
        }

    }

    private static int getUserDay() {
        while (true) {
            System.out.println("Please enter the day (1-31): ");
            int day = Integer.parseInt(scanner.nextLine().trim());
            if (day > 0 && day < 31)
                return day;
        }
    }

    public static int getUserMonth() {
        while (true) {
            System.out.println("Please enter a month(1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());
            if (month > 0 && month < 13) {
                return month;
            }
        }
    }

    private static int getUserSec() {
        while (true) {
            System.out.println("Please enter the sec(1-60): ");
            int sec = Integer.parseInt(scanner.nextLine().trim());
            if (sec > 0 && sec <= 60)
                return sec;
        }

    }

    private static int getUserHour() {
        while (true) {
            System.out.println("Please enter the hour(1-24): ");
            int hour = Integer.parseInt(scanner.nextLine().trim());
            if (hour > 0 && hour <= 24)
                return hour;
        }
    }

    private static int getUserMin() {
        while (true) {
            System.out.println("Please enter the minute(1-60): ");
            int min = Integer.parseInt(scanner.nextLine().trim());
            if (min > 0 && min <= 60)
                return min;
        }
    }
    // endregion
}
