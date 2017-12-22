package Shared;

import java.io.Serializable;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Transaction implements Serializable {
    private Date date;
    private String iban;
    private double amount;
    private String description;

    public Date getDate() {
        return this.date;
    }

    public String getIban() {
        return iban;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Transaction(Date date, String iban, double amount, String description) {
        this.date = date;
        this.iban = iban;
        this.amount = amount;
        this.description = description;
    }
}
