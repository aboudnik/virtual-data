package org.boudnik.data.platform;

/**
 * @author Alexandre_Boudnik
 * @since 01/11/2017
 */
public class Metadata {
    private static Metadata metadata = new Metadata();

    public static Metadata metadata() {
        return metadata;
    }

    private Metadata() {
    }

    OBJ newInstance() {
        return null;
    }

}
