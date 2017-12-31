import Shared.Address;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class AddressTest {
    private Address address;

    @Before
    public void before() {
        address = new Address("Sven de Vries", "NL56ABNA0123456789");
    }

    @After
    public void after() {
        address = null;
    }

    @Test
    public void testGetName() {
        assertEquals("Sven de Vries", address.getName());
    }

    @Test
    public void testGetIban() {
        assertEquals("NL56ABNA0123456789", address.getIban());
    }
}