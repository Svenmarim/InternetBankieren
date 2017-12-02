package BankServer;

import Shared.Address;
import Shared.Transaction;

import java.util.Date;

/**
 * InternetBankieren Created by Sven de Vries on 1-12-2017
 */
public class BankAccount {
    private double amount;
    private String iban;
    private String firstName;
    private String lastName;
    private String postalCode;
    private int houseNumber;
    private Date dateOfBirth;
    private String email;
    private double limitInAddressbook;
    private double limitOutAddressbook;

    public double getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
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

    public double getLimitInAddressbook() {
        return limitInAddressbook;
    }

    public double getLimitOutAddressbook() {
        return limitOutAddressbook;
    }

    public BankAccount(double amount, String iban, String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email, double limitIn, double limitOut) {
        this.amount = amount;
        this.iban = iban;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.limitInAddressbook = limitIn;
        this.limitOutAddressbook = limitOut;
    }

    public void editAccount(String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {

    }

    public void editLimits(double limitIn, double limitOut) {

    }

    public void deleteAddress(Address address) {

    }

    public boolean makeTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) {
        return false;
    }

    public boolean makeRequest(double amount, String name, String ibanReceiver, String description) {
        return false;
    }

    public boolean receiveTransaction(Transaction transaction) {
        return false;
    }

    public void changeAmount(double amount) {

    }
}
