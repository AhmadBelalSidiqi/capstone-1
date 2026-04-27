package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = loadOldTransactions();
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
            switch (input.toLowerCase()){
                case "d"->
                    addDeposit();
                case "p"->
                    makePayments();
                case "l"->
                    ledger();
                // TODO: better way?
                case  "x"->
                   running = false;
                default ->
                    System.out.println("Wrong Input");

            }
        }while (running);
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
            transactions.add(new Transaction(description,vendor,amount));

            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))){
                running=false; }
        }while (running);
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
            double amount = (Double.parseDouble(scanner.nextLine().trim()))*-1;
            transactions.add(new Transaction(description,vendor,amount));

            System.out.println("do you want add another one yes/no: ");
            String input = scanner.nextLine().trim();
            if ((input.equalsIgnoreCase("no"))){
                running=false; }
        }while (running);
    }
    public static void ledger() {
        String menu = """
                Please choose one of the following option:
                A) ALL- Display all entries
                D) Deposits- Display only the deposits into the account
                P) Payments- Display only the payments into the account
                R) Report
                X) Exit
                """;
        boolean running = true;
        do { System.out.println(menu);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()){
                case "a"->
                        displayAllTransactions();
                case "d"->
                        displayDeposits();
                case "p"->
                        displayPayments();
                case "r"->
                        report();
                case "x"->
                        running = false;
                default -> System.out.println("Wrong Input");
            }

        }while (running);

    }

    public static void displayPayments() {
        for (Transaction transaction : transactions){
            if (transaction.getAmount()<0){
                displayTransaction(transaction);
            }
        }
    }
    public static void displayDeposits() {
        for (Transaction transaction : transactions){
            if (transaction.getAmount()>0){
                displayTransaction(transaction);
            }
        }
    }
    // TODO: Make customized reports
    public static void report() {
    }

    public static void displayTransaction(Transaction transaction){
        System.out.printf("Date: %-20s\t Description: %-30s\t Vendor: %-15s\t Amount: %.2f %n",
                transaction.getDateAndTimeFormated(),
                transaction.getDescription(),
                transaction.getVendor(),
                transaction.getAmount());

    }

    public static void displayAllTransactions(){
        for (Transaction transaction : transactions){
            displayTransaction(transaction);
        }
    }
    public static ArrayList<Transaction> loadOldTransactions(){
        ArrayList<Transaction> oldTransactions = new ArrayList<>();
        String fileLocation = "src/main/resources/OldTranscations.cvs";
        try {
            FileReader fileReader = new FileReader(fileLocation);
            BufferedReader reader = new BufferedReader(fileReader);
            String header = "date|time|description|vendor|amount";
            String line;
            while ((line = reader.readLine())!=null){
                // Handling the header if it exits
                if (!line.equalsIgnoreCase(header)){
                    String[] lineSpilt = line.split("\\|");
                    String date = lineSpilt[0];
                    String time = lineSpilt[1];
                    String dateTimeString = date + "T" + time;
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                    String description = lineSpilt[2];
                    String vendor = lineSpilt[3];
                    double amount = Double.parseDouble(lineSpilt[4]);
                    oldTransactions.add(new Transaction(dateTime,description,vendor,amount));

                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("IO Exception: "+ e);
        }
        return oldTransactions;
    }


}
