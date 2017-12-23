package Shared;

import java.io.Serializable;
import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 23-12-2017
 */
public class TempAccount implements Serializable {
    private double amount;
    private String iban;
    private String encryptedPassword;
    private String firstName;
    private String lastName;
    private String postalCode;
    private int houseNumber;
    private Date dateOfBirth;
    private String email;

    public double getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.encryptedPassword = password;
    }

    public TempAccount(double amount, String iban, String encryptedPassword, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {

        this.amount = amount;
        this.iban = iban;

        this.encryptedPassword = encryptedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }
}
