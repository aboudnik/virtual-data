package org.boudnik.data.platform;

import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 01/11/2017
 */
public class OBJ {

    public abstract static class Type<I, C> implements Comparable<C> {
        I value;

        public void set(I value) {
            this.value = value;
        }

        public I get() {
            return value;
        }
    }

    public class DOUBLE extends Type<Double, Double> {
        @Override
        public int compareTo(Double o) {
            return Double.compare(value, o);
        }
    }

    public class INT extends Type<Integer, Integer> {
        @Override
        public int compareTo(Integer o) {
            return Integer.compare(value, o);
        }
    }

    public class STRING extends Type<String, String> {
        @Override
        public int compareTo(String o) {
            return value.compareTo(o);
        }
    }

    public class DATE extends Type<Long, Date> {
        @Override
        public int compareTo(Date o) {
            return Long.compare(value, o.getTime());
        }
    }
}
