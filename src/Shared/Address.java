package Shared;

import java.io.Serializable;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class Address implements Serializable {
    private String name;
    private String iban;

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public Address(String name, String iban) {
        this.name = name;
        this.iban = iban;
    }
}
