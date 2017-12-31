import Shared.TempBank;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * InternetBankieren Created by Sven de Vries on 31-12-2017
 */
public class TempBankTest {
    private TempBank tempBank;

    @Before
    public void before() {
        tempBank = new TempBank("SNS Bank", "SNSB");
    }

    @After
    public void after() {
        tempBank = null;
    }

    @Test
    public void testGetName() {
        assertEquals("SNS Bank", tempBank.getName());
    }

    @Test
    public void testGetShortcut() {
        assertEquals("SNSB", tempBank.getShortcut());
    }

    @Test
    public void testToString() {
        assertEquals("SNS Bank", tempBank.toString());
    }
}