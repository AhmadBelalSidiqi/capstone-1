package com.pluralsight;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    static LocalDateTime currentDateAndTime = LocalDateTime.now();
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
                case "l"->{
                    sortTransactionsList(false);
                    ledger();
                }
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
                default ->
                        System.out.println("Wrong Input");
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
    // TODO: Going back to home screen
    public static void report() {
        String menu = """
                Please choose one of the following:
                1) Month to Date
                2) Previous Month
                3) Year to Date
                4) Previous Year
                5) Search by Vendor
                0) Go back to ledger Page
                H) Home Screen
                """;
        boolean running = true;
        do {
            System.out.println(menu);
            String input = scanner.nextLine();
            switch (input.toLowerCase()){


                case "1" ->
                        monthToDate();

                case "2"->
                        previousMonth();

                case "3"->
                        yearToDate();

                case "4" ->
                        previousYear();

                case "5"->
                        searchByVendor();

                case "0"->
                        running=false;

                case "h"->
                        homeScreenMenu();

                default ->
                        System.out.println("wrong Input");

            }
        }while (running);

    }

    public static void monthToDate() {
        LocalDate month = LocalDate.of(currentDateAndTime.getYear(),currentDateAndTime.getMonth(),1);
        LocalTime time = LocalTime.of(1,1,1);
        LocalDateTime thisMonth = LocalDateTime.of(month,time);
        for (Transaction transaction : transactions){
            if (transaction.getDateAndTime().isAfter(thisMonth)){
                displayTransaction(transaction);
            }
        }

    }

    public static void previousMonth() {
        LocalTime time = LocalTime.of(1,1);
        LocalDate lastMonth = LocalDate.of(currentDateAndTime.getYear(),currentDateAndTime.getMonth().minus(1),1);
        LocalDate month = LocalDate.of(currentDateAndTime.getYear(),currentDateAndTime.getMonth(),1);
        LocalDateTime previousMonth = LocalDateTime.of(lastMonth,time);
        LocalDateTime thisMonth = LocalDateTime.of(month,time);
        for (Transaction transaction : transactions){
            if (transaction.getDateAndTime().isAfter(previousMonth) && transaction.getDateAndTime().isBefore(thisMonth)){
                displayTransaction(transaction);
            }
        }
    }

    public static void yearToDate() {
        for (Transaction transaction : transactions){
            // every transaction in this year and before current date and time , we want to avoid transaction that would be place in the future of current date and time.
            if (transaction.getDateAndTime().getYear() == currentDateAndTime.getYear() && transaction.getDateAndTime().isBefore(currentDateAndTime)){
                displayTransaction(transaction);
            }
        }

    }

    public static void previousYear() {
        int previousYear = currentDateAndTime.getYear()-1;
        for (Transaction transaction : transactions){
            if (transaction.getDateAndTime().getYear() == previousYear){
                displayTransaction(transaction);
            }
        }
    }

    public static void searchByVendor() {
        System.out.println("Please enter the vendor name: ");
        String vendor = scanner.nextLine();
        for (Transaction transaction : transactions){
            if(transaction.getVendor().equalsIgnoreCase(vendor)){
                displayTransaction(transaction);
            }
        }
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
    public static void sortTransactionsList(boolean ascending){

        if ( ascending) {
            transactions.sort(Comparator.comparing(Transaction::getDateAndTime));
        } else {
            transactions.sort(Comparator.comparing(Transaction::getDateAndTime).reversed());

        }

    }


}
