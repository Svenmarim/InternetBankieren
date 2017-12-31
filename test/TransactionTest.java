import Shared.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class TransactionTest {
    private Transaction transaction;

    @Before
    public void before() {
        transaction = new Transaction(new Date(), "NL56ABNA0123456789", 10, "bios");
    }

    @After
    public void after() {
        transaction = null;
    }

    @Test
    public void testGetDate() {
        assertEquals(new Date(), transaction.getDate());
    }

    @Test
    public void testGetIban() {
        assertEquals("NL56ABNA0123456789", transaction.getIban());
    }

    @Test
    public void testGetAmount() {
        assertEquals(10, transaction.getAmount(), 0);
    }

    @Test
    public void testGetDescription() {
        assertEquals("bios", transaction.getDescription());
    }
}