package jspread.core.util;


import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author VIOX
 */
public final class SpreadParameters {

    private static SpreadParameters si;
    private static ConcurrentHashMap ISParameters = new ConcurrentHashMap();
    private final static String version = "V0.0.2";

    public static SpreadParameters getSingleInstance() {
        // si will be null the first time this is called.
        //hay que generar el constructor especializado
        if (null == si) {
            si = new SpreadParameters();
        }
        return si;
    }

    private SpreadParameters() {
    }

    public static String getVersion() {
        return version;
    }

    public static void addParameter(String id, Object parameter) {
        ISParameters.put(id, parameter);
    }

    public static Object getPatameter(String id) {
        return ISParameters.get(id);
    }

    public static void clearParameters() {
        ISParameters = null;
    }
}
