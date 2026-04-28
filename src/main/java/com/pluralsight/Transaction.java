package com.pluralsight;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction {
    private LocalDateTime dateAndTime;
    private String description;
    private String vendor;
    private double amount;


    public Transaction(LocalDateTime dateAndTime, String description, String vendor, double amount) {
        this.dateAndTime = dateAndTime;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // We need the formated dateAndTime to write it in text file.
    public String getDateAndTimeFormatted(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        return dateAndTime.format(formatter);
    }

    // region getter
    public LocalDateTime getDateAndTime() {return dateAndTime;}

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
    //endregion
}
