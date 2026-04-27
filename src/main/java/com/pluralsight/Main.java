package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();
    public static void main(String[] args) {
        homeScreenMenu();
        displayAllTransactions();

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
    //TODO: Display the negative amounts
    public static void displayPayments() {
    }
    // TODO: Display the positive amounts
    public static void displayDeposits() {
    }
    // TODO: Make customized reports
    public static void report() {
    }

    public static void displayTransaction(Transaction transaction){
        System.out.printf("Date: %s\t Description: %s\t Vendor: %s\t Amount: %.2f %n",
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


}
