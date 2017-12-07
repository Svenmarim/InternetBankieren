package BankServer;

import Shared.Address;
import Shared.Transaction;

import java.util.ArrayList;
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
    private ArrayList<Address> addressbook;
    private ArrayList<Transaction> transactionHistory;

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
        this.addressbook = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
    }

    public void editAccount(String firstName, String lastName, String postalCode, int houseNumber, Date dateOfBirth, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    public void editLimits(double limitIn, double limitOut) {
        this.limitInAddressbook = limitIn;
        this.limitOutAddressbook = limitOut;
    }

    public void deleteAddress(Address address) {
        this.addressbook.remove(address);
    }

    public void makeTransaction(double amount, String name, String ibanReceiver, String description, boolean addToAddress) {
        changeAmount(amount);
        transactionHistory.add(new Transaction(new Date(), ibanReceiver, amount, description));

        if(addToAddress){
            addressbook.add(new Address(name, ibanReceiver));
        }
    }

    public boolean makeRequest(double amount, String name, String ibanReceiver, String description) {
        return false;
    }

    public void receiveTransaction(Transaction transaction) {
        changeAmount(transaction.getAmount());
        transactionHistory.add(transaction);
    }

    public void changeAmount(double amount) {
        if(amount < 0){
            this.amount -= amount;
        } else if (amount > 0){
            this.amount += amount;
        }
    }
}
