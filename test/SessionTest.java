import BankServer.BankAccount;
import BankServer.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class SessionTest {
    private Session session;

    @Before
    public void before() throws RemoteException {
        session = new Session(new BankAccount(50, "NL56ABNA0123456789", "Sven", "de Vries", "6005NA", 27, new Date(), "s@hotmail.com", 20, 10));
    }

    @After
    public void after() {
        session = null;
    }

    @Test
    public void testIsSessionValidTrue() {
        Date date = new Date();
        date.setMinutes(1);
        session.setLastActivity(date);
        assertTrue(session.isSessionValid());
    }

    @Test
    public void testIsSessionValidFalse() {
        Date date = new Date();
        date.setHours(1);
        session.setLastActivity(date);
        assertFalse(session.isSessionValid());
    }
}