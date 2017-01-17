package org.boudnik.data.platform.tests;

import org.boudnik.data.platform.DataBlock;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexandre_Boudnik
 * @since 01/11/2017
 */
public class DataBlockTest {

    private DataBlock salesLoudoun2017JAN;
    private DataBlock salesLoudoun2017FEB;
    private DataBlock salesFairfax2017JAN;

    @Before
    public void setup() throws MalformedURLException {
        salesLoudoun2017JAN = new DataBlock(new URL("ftp://localhost/sales"), new HashMap<String, DataBlock.Limitation>(){{
            put("date", new DataBlock.Limitation.Range("2017/01/01", "2017/01/31"));
            put("zip", new DataBlock.Limitation.Value(20176));
        }});
        salesLoudoun2017FEB = new DataBlock(new URL("ftp://localhost/sales"), new HashMap<String, DataBlock.Limitation>(){{
            put("date", new DataBlock.Limitation.Range("2017/02/01", "2017/02/28"));
            put("zip", new DataBlock.Limitation.Value(20176));
        }});
        salesFairfax2017JAN = new DataBlock(new URL("ftp://localhost/sales"), new HashMap<String, DataBlock.Limitation>(){{
            put("date", new DataBlock.Limitation.Range("2017/01/01", "2017/01/31"));
            put("zip", new DataBlock.Limitation.Value(22030));
        }});
    }

    @Test
    public void testInt() throws MalformedURLException {
        assertTrue(salesLoudoun2017JAN.getLimitation("zip").hasCommon(salesLoudoun2017FEB.getLimitation("zip")));
        assertFalse(salesLoudoun2017JAN.getLimitation("zip").hasCommon(salesFairfax2017JAN.getLimitation("zip")));
    }

    @Test
    public void testString() throws MalformedURLException {
        assertTrue(salesLoudoun2017JAN.getLimitation("date").hasCommon(salesFairfax2017JAN.getLimitation("date")));
        assertFalse(salesLoudoun2017FEB.getLimitation("date").hasCommon(salesFairfax2017JAN.getLimitation("date")));
    }
}