import BankServer.BankAccount;
import Shared.Address;
import Shared.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class BankAccountTest {
    private BankAccount account;

    @Before
    public void before() {
        account = new BankAccount(50, "NL56ABNA0123456789", "Sven", "de Vries", "6005NA", 27, new Date(), "s@hotmail.com", 20, 10);
        account.addAddress(new Address("Quint Aartsen", "NL04INGB0123456789"));
        account.addTransaction(new Transaction(new Date(), "NL04INGB0123456789", 1, "koffie"));
    }

    @After
    public void after() {
        account = null;
    }

    @Test
    public void testGetAmount() {
        assertEquals(50, account.getAmount(), 0);
    }

    @Test
    public void testGetIban() {
        assertEquals("NL56ABNA0123456789", account.getIban());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Sven", account.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("de Vries", account.getLastName());
    }

    @Test
    public void testGetPostalCode() {
        assertEquals("6005NA", account.getPostalCode());
    }

    @Test
    public void testGetHouseNumber() {
        assertEquals(27, account.getHouseNumber(), 0);
    }

    @Test
    public void testGetDateOfBirth() {
        assertEquals(new Date().toString(), account.getDateOfBirth().toString());
    }

    @Test
    public void testGetEmail() {
        assertEquals("s@hotmail.com", account.getEmail());
    }

    @Test
    public void testGetLimitInAddressbook() {
        assertEquals(20, account.getLimitInAddressbook(), 0);
    }

    @Test
    public void testGetLimitOutAddressbook() {
        assertEquals(10, account.getLimitOutAddressbook(), 0);
    }

    @Test
    public void testGetAddressbook() {
        assertEquals("Quint Aartsen", account.getAddressbook().get(0).getName());
    }

    @Test
    public void testGetTransactionHistory() {
        assertEquals("koffie", account.getTransactionHistory().get(0).getDescription());
    }

    @Test
    public void testEditAccount() {
        account.editAccount("Dane", "Naebers", "6005LB", 32, new Date(), "d@hotmail.com");
        assertEquals("Dane", account.getFirstName());
    }

    @Test
    public void testEditLimits() {
        account.editLimits(50, 40);
        assertEquals(50, account.getLimitInAddressbook(), 0);
    }

    @Test
    public void testAddAddress() {
        account.addAddress(new Address("Dane Naebers", "NL38ABNA0123456788"));
        assertEquals(2, account.getAddressbook().size());
    }

    @Test
    public void testDeleteAddress() {
        //Delete incorrect address
        account.deleteAddress(new Address("No one", "fail"));
        assertEquals(1, account.getAddressbook().size());
        //Delete correct address
        account.deleteAddress(new Address("Quint Aartsen", "NL04INGB0123456789"));
        assertEquals(0, account.getAddressbook().size());
    }

    @Test
    public void testAddTransaction() {

        account.addTransaction(new Transaction(new Date(), "NL04INGB0123456789", -5, "zwemmen"));
        assertEquals(2, account.getTransactionHistory().size());
    }

    @Test
    public void testMakeTransactionFalse() {
        account.makeTransaction("Dane Naebers", new Transaction(new Date(), "NL04INGB0123456789", -5, "zwemmen"), false);
        assertEquals(1, account.getAddressbook().size());
        assertEquals(2, account.getTransactionHistory().size());
        assertEquals(45, account.getAmount(), 0);
    }

    @Test
    public void testMakeTransactionTrue() {
        account.makeTransaction("Dane Naebers", new Transaction(new Date(), "NL04INGB0123456789", -5, "zwemmen"), true);
        assertEquals(2, account.getAddressbook().size());
        assertEquals(2, account.getTransactionHistory().size());
        assertEquals(45, account.getAmount(), 0);
    }

    @Test
    public void testMakeRequest() {
    }

    @Test
    public void testReceiveTransaction() {
        account.receiveTransaction(new Transaction(new Date(), "NL04INGB0123456789", -10, "zwemmen"));
        assertEquals(2, account.getTransactionHistory().size());
        assertEquals(40, account.getAmount(), 0);
    }

    @Test
    public void testChangeAmount() {
        //Negative amount
        account.changeAmount(-15);
        assertEquals(35, account.getAmount(), 0);
        //Positive amount
        account.changeAmount(20);
        assertEquals(55, account.getAmount(), 0);
    }
}