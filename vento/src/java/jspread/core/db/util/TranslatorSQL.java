package jspread.core.db.util;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import jspread.core.db.DeveloperQUID;

/**
 *
 * @author Hewlet
 */
public final class TranslatorSQL {

    public static String getSQLTranslated(String dbms, String Instruction, String Table, String Condition) throws ClassNotFoundException, SQLException {
        StringBuilder Query = new StringBuilder();
        //String delimiter = "\\,";
        //String StrSplit[] = Fields.split(delimiter);
        DeveloperQUID quid = new DeveloperQUID("sqlserver");
        LinkedList ListFields = null;
        ListFields = quid.getFieldsTable(Table, dbms);
        Iterator it = ListFields.iterator();
        String StrFields = "";

        if (Instruction.equals("SELECT")) {
            while (it.hasNext()) {

                StrFields = it.next().toString();
                if (Query.length() == 0) {
                    Query.append("\n \" SELECT \"");
                    Query.append("\n + \" ");
                    Query.append(StrFields);
                    Query.append("\"");
                } else {
                    Query.append("\n + \",");
                    Query.append(StrFields);
                    Query.append("\"");
                }
            }

            Query.append("\n + \"");
            Query.append(" FROM ");
            Query.append(Table);
            Query.append(" \"");

            if (Condition.equals("") != true) {
                Query.append("\n + \" ");
                Query.append(Condition);
                Query.append(" \"");
            }

        } else if (Instruction.equals("INSERT")) {
            StringBuilder QueryValues = new StringBuilder();

            while (it.hasNext()) {
                StrFields = it.next().toString();
                if (Query.length() == 0) {
                    //*** Fields ***//
                    Query.append("\n \" INSERT INTO ");
                    Query.append(Table);
                    Query.append(" (\"");
                    Query.append("\n + \" ");
                    Query.append(StrFields);
                    Query.append("\"");
                    //*** Values ***//
                    QueryValues.append("\n + \" VALUES (\"");
                    QueryValues.append("\n + \" ");
                    QueryValues.append("param_");
                    QueryValues.append(StrFields);
                    QueryValues.append("\"");

                } else {
                    //*** Fields ***//
                    Query.append("\n + \",");
                    Query.append(StrFields);
                    Query.append("\"");
                    //*** Values ***//                    
                    QueryValues.append("\n + \",");
                    QueryValues.append("param_");
                    QueryValues.append(StrFields);
                    QueryValues.append("\"");
                }
            }

            //*** Fields ***//
            Query.append("\n + \")");
            Query.append("\"");
            //*** Values ***//                    
            QueryValues.append("\n + \");");
            QueryValues.append("\"");
            //*** Completamos sentencis SQL ***//
            Query.append(QueryValues);

        } else if (Instruction.equals("UPDATE")) {
            while (it.hasNext()) {
                StrFields = it.next().toString();
                if (Query.length() == 0) {
                    //*** Fields ***//
                    Query.append("\n \" UPDATE ");
                    Query.append(Table);
                    Query.append(" SET \"");
                    Query.append("\n + \"");
                    Query.append(StrFields);
                    Query.append(" = ");
                    Query.append("\'\" + ");
                    Query.append("param_");
                    Query.append(StrFields);
                    Query.append(" + \"\'\"");
                    //*** Values ***//
                    //+ " nombre = '" + nombre + "'"
                } else {
                    //*** Fields ***//
                    Query.append("\n + \",");
                    Query.append(StrFields);
                    Query.append(" = ");
                    Query.append("\'\" + ");
                    Query.append("param_");
                    Query.append(StrFields);
                    Query.append(" + \"\'\"");
                }
            }

            if (Condition.equals("") != true) {
                Query.append("\n + \" ");
                Query.append(Condition);
                Query.append(" \"");
            }

        } else if (Instruction.equals("DELETE")) {
        }

        return Query.toString();
    }

    public static void getDescribeTable(String Table) {
    }
}
