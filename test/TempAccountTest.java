import Shared.TempAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class TempAccountTest {
    private TempAccount tempAccount;

    @Before
    public void before() {
        tempAccount = new TempAccount(50, "NL56ABNA0123456789", "KnS2", "Sven", "de Vries", "6005NA", 27, new Date(), "s@hotmail.com");
    }

    @After
    public void after() {
        tempAccount = null;
    }

    @Test
    public void testGetAmount() {
        assertEquals(50, tempAccount.getAmount(), 0);
    }

    @Test
    public void testGetIban() {
        assertEquals("NL56ABNA0123456789", tempAccount.getIban());
    }

    @Test
    public void testGetPassword() {
        assertEquals("KnS2", tempAccount.getPassword());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Sven", tempAccount.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("de Vries", tempAccount.getLastName());
    }

    @Test
    public void testGetPostalCode() {
        assertEquals("6005NA", tempAccount.getPostalCode());
    }

    @Test
    public void testGetHouseNumber() {
        assertEquals(27, tempAccount.getHouseNumber());
    }

    @Test
    public void testGetDateOfBirth() {
        assertEquals(new Date(), tempAccount.getDateOfBirth());
    }

    @Test
    public void testGetEmail() {
        assertEquals("s@hotmail.com", tempAccount.getEmail());
    }

    @Test
    public void testSetPassword() {
        tempAccount.setPassword("newPassword");
        assertEquals("newPassword", tempAccount.getPassword());
    }
}