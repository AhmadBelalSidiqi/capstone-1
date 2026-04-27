package com.pluralsight;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        homeScreenMenu();

    }

    public static void homeScreenMenu() {
        String menu = """
                Please chose on of the following option:
                D) -Add deposit-
                P) -Make Payments(Debit)-
                L) -ledger-
                X) -Exit-
                """;
        System.out.println(menu);
        String input = scanner.nextLine();
        boolean running = true;
        do {
            switch (input.toLowerCase()){
                case "a"->
                    addDeposit();
                case "d"->
                    makePayments();
                case "l"->
                    ledger();
                // TODO: better way?
                case  "x"->
                   running = exit();
            }

        }while (running);
    }
    // TODO: Make the addDeposit method
    public static void addDeposit() {
    }
    // TODO: Make the makePayments method
    public static void makePayments() {
    }
    // TODO: Make the ledger method
    public static void ledger() {
    }
    public static boolean exit() {
        return false;
    }
}
