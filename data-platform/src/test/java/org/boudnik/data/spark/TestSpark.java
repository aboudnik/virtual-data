package org.boudnik.data.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Alexandre_Boudnik
 * @since 01/26/17 16:08
 */
public class TestSpark {
    private static final Pattern COMMA = Pattern.compile(",");

    @Test
    public void test() throws IOException, InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("JavaCustomReceiver").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        SQLContext sql = new SQLContext(sc);

        String query = String.format("%s?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d",
                "http://real-chart.finance.yahoo.com/table.csv",
                "^GSPC",           // symbol for S&P 500
                0, 2, 2008,        // start date
                11, 31, 2008       // end date
        );

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(query).openStream(), StandardCharsets.UTF_8))) {
            reader.readLine();
            for (String line; (line = reader.readLine()) != null; ) {
                lines.add(line);
            }
        }

        JavaRDD<Row> map = sc.parallelize(lines)
                .map(line -> line.split(","))
                .map(split -> RowFactory.create(
                        getDate(split[0]),
                        getDate(split[0]).getMonth(),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Double.parseDouble(split[4]),
                        Double.parseDouble(split[5]),
                        Double.parseDouble(split[6])
                ));

        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("Date", DataTypes.DateType, false),
                DataTypes.createStructField("Month", DataTypes.IntegerType, false),
                DataTypes.createStructField("Open", DataTypes.DoubleType, false),
                DataTypes.createStructField("High", DataTypes.DoubleType, false),
                DataTypes.createStructField("Low", DataTypes.DoubleType, false),
                DataTypes.createStructField("Close", DataTypes.DoubleType, false),
                DataTypes.createStructField("Volume", DataTypes.DoubleType, false),
                DataTypes.createStructField("Adj Close", DataTypes.DoubleType, false)
        });

        Dataset<Row> limit = sql
                .createDataFrame(map, schema)
                .select("Month", "Close")
                .groupBy("Month")
                .agg(functions.mean("Close").name("Mean"))
                .sort(new Column("Mean").desc())
                .limit(3);

        limit.show();
    }

    private static Date getDate(String lexicalXSDDate) {
        return new Date(DatatypeConverter.parseDate(lexicalXSDDate).getTimeInMillis());
    }
}