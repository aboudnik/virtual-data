package org.boudnik.data.platform.tests;

import joinery.DataFrame;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandre_Boudnik
 * @since 01/13/17 14:29
 */
public class DataFrameTest {
    @Test
    public void main() throws IOException {
        @SuppressWarnings("deprecation") List<Integer> col = DataFrame
                .readCsv(String.format("%s?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d",
                        "http://real-chart.finance.yahoo.com/table.csv",
                        "^GSPC",           // symbol for S&P 500
                        0, 2, 2008,        // start date
                        11, 31, 2008       // end date
                ))
                .retain("Date", "Close")
                .groupBy(row -> Date.class.cast(row.get(0)).getMonth())
                .mean()
                .sortBy("Close")
                .tail(3)
                .apply(value -> Number.class.cast(value).intValue())
                .col("Close");
        System.out.println("col = " + col);
    }
}
