package jspread.core.db.util;

/**
 *
 * @author iox
 */
public final class TablesNFieldsUtil {

//    MySQL Type || Java Type
//CHAR	String
//VARCHAR	String
//LONGVARCHAR	String
//NUMERIC	java.math.BigDecimal
//DECIMAL	java.math.BigDecimal
//BIT	boolean
//TINYINT	byte
//SMALLINT	short
//INTEGER	int
//BIGINT	long
//REAL	float
//FLOAT	double
//DOUBLE	double
//BINARY	byte []
//VARBINARY	byte []
//LONGVARBINARY	byte []
//DATE	java.sql.Date
//TIME	java.sql.Time
//TIMESTAMP	java.sql.Tiimestamp
    //
    //
    public final String fieldTypeIdentifier(String StrType, String db) {

        String TypeData = "UndefinedType";


        if (StrType.indexOf("varchar") != -1) {
            TypeData = "String";
        } else if (StrType.indexOf("text") != -1) {
            TypeData = "String";
        } else if (StrType.indexOf("tinyint") != -1) {
            TypeData = "byte";
        } else if (StrType.indexOf("int") != -1) {
            TypeData = "int";
        } else if (StrType.indexOf("double") != -1) {
            TypeData = "double";
        } else if (StrType.indexOf("float") != -1) {
            TypeData = "float";
        } else if (StrType.indexOf("timestamp") != -1) {
            TypeData = "Timestamp";
        } else if (StrType.indexOf("datetime") != -1) {
            TypeData = "Date";
        } else if (StrType.indexOf("date") != -1) {
            TypeData = "Date";
        } else if (StrType.indexOf("bit") != -1) {
            TypeData = "boolean";
        } else {
            TypeData = "Database not supported....yet!";
        }

        return TypeData;
    }

    public final int fieldSizeLimit(String type, String db) {
        int limit = -1;
        if (db.equalsIgnoreCase("mysql")) {
            if (type.contains(")")) {
                String lim = type.substring(type.lastIndexOf("(") + 1, type.lastIndexOf(")"));
                limit = Integer.parseInt(lim);
            } else {
                limit = 35000;
            }
        }
        return limit;
    }
}
