package org.boudnik.data.platform.tests;

import joinery.DataFrame;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Alexandre_Boudnik
 * @since 01/19/17 15:05
 */
public class SystemTest {
    @Test
    public void end2End() throws IOException {
        DataFrame<Object> lists = DataFrame
                .readCsv(String.format("%s?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d",
                        "http://real-chart.finance.yahoo.com/table.csv",
                        "^GSPC",           // symbol for S&P 500
                        0, 2, 2008,        // start date
                        11, 31, 2008       // end date
                ));
        System.out.println("lists = " + lists);
    }
}
