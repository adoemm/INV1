package jspread.core.util;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import jspread.core.db.DeveloperQUID;
import jspread.core.db.util.PreparedStatementSQL;
import jspread.core.db.util.TablesNFieldsUtil;

/**
 *
 * @author JeanPaul
 */
public final class HTMLFromGenerator {

    private LinkedList ListFields = null;
    private LinkedList ListAux = null;
    private String encryptionKey = null;
    private String dbms = "";
    private TablesNFieldsUtil tfUtil;
    private String validateForm = "";
    private int isThisTheFirstField = 0;
    private DeveloperQUID dquid = null;
    private static final String version = "V0.6";

    public LinkedList geterateInsertForm(DeveloperQUID dquid, String dbms, String Table, String whereClause, String formName, String action, String encryptionKey) throws ClassNotFoundException, SQLException {
        this.dbms = dbms;
        tfUtil = new TablesNFieldsUtil();
        this.encryptionKey = encryptionKey;
        String form = "";
        this.dquid = dquid;
        ListFields = dquid.getDescribeFieldsTable(Table, this.dbms);

        validateForm = validateForm + "\n" + "else if (request.getParameter(\"FormName\").equals(\"" + formName + "\")) {";
        form = form + "\n" + "<div class=\"form-container\">";
        form = form + "\n" + "<fieldset>";
        form = form + "\n" + "<legend>" + formName + "</legend>";
        form = form + "\n" + "<form name=\"" + formName + "\" method=\"post\" action=\"" + action + "\" enctype=\"application/x-www-form-urlencoded\" id=\"" + formName + "\">";
        form = form + "\n" + "<input type=\"hidden\" name=\"FormName\" value=\"" + formName + "\"/>";

        Iterator it = ListFields.iterator();
        while (it.hasNext()) {
            ListAux = null;
            ListAux = (LinkedList) it.next();

            form = form + "\n" + addInsertField(ListAux.get(0).toString(), ListAux.get(1).toString(), ListAux.get(2).toString());
            System.out.println("" + ListAux.get(1).toString());
        }

        form = form + "\n" + "<div class=\"buttonrow\">\n"
                + "                    <input type=\"submit\" value=\"Enviar\" class=\"button\">\n"
                + "                </div>";

        form = form + "\n" + "</form>";
        form = form + "\n" + "</fieldset>";
        form = form + "\n" + "</div>";
        validateForm = validateForm + "\n" + "else {";
        validateForm = validateForm + "\n" + "//TODO when everything is rigth";
        validateForm = validateForm + "\n" + "}";
        validateForm = validateForm + "\n" + "}";
        System.out.println("\n\n\n");
        System.out.println("" + validateForm);

        LinkedList all = new LinkedList();
        PreparedStatementSQL ps = new PreparedStatementSQL();
        String pss = ps.getSQLPreparedStatement(dquid, dbms, "INSERT", Table, whereClause);
        all.add(pss);
        all.add(form);
        all.add(validateForm);
        return all;
    }

    public LinkedList geterateUpDateForm(DeveloperQUID dquid, String dbms, String Table, String whereClause, String formName, String action, String encryptionKey) throws ClassNotFoundException, SQLException {
        this.dbms = dbms;
        tfUtil = new TablesNFieldsUtil();
        this.encryptionKey = encryptionKey;
        String form = "";
        this.dquid = dquid;
        ListFields = dquid.getDescribeFieldsTable(Table, this.dbms);

        validateForm = validateForm + "\n" + "else if (request.getParameter(\"FormName\").equals(\"" + formName + "\")) {";
        form = form + "\n" + "<%LinkedList values = (LinkedList) QUID.getMethotd(request.getParameter(\"IDX\")).get(0);%>";
        form = form + "\n" + "<div class=\"form-container\">";
        form = form + "\n" + "<fieldset>";
        form = form + "\n" + "<legend>" + formName + "</legend>";
        form = form + "\n" + "<form name=\"" + formName + "\" method=\"post\" action=\"" + action + "\" enctype=\"application/x-www-form-urlencoded\" id=\"" + formName + "\">";
        form = form + "\n" + "<input type=\"hidden\" name=\"FormName\" value=\"" + formName + "\"/>";
        form = form + "\n" + "<input type=\"hidden\" name=\"IDX\" value=\"<%request.getParameter(\"IDX\");%>\"/>";

        Iterator it = ListFields.iterator();
        while (it.hasNext()) {
            ListAux = null;
            ListAux = (LinkedList) it.next();

            form = form + "\n" + addUpDateField(ListAux.get(0).toString(), ListAux.get(1).toString(), ListAux.get(2).toString());
            System.out.println("" + ListAux.get(1).toString());
        }

        form = form + "\n" + "<div class=\"buttonrow\">\n"
                + "                    <input type=\"submit\" value=\"Enviar\" class=\"button\">\n"
                + "                </div>";

        form = form + "\n" + "</form>";
        form = form + "\n" + "</fieldset>";
        form = form + "\n" + "</div>";
        validateForm = validateForm + "\n" + "else {";
        validateForm = validateForm + "\n" + "//TODO when everything is rigth";
        validateForm = validateForm + "\n" + "}";
        validateForm = validateForm + "\n" + "}";
        System.out.println("\n\n\n");
        System.out.println("" + validateForm);
        LinkedList all = new LinkedList();
        PreparedStatementSQL ps = new PreparedStatementSQL();
        String pss = ps.getSQLPreparedStatement(dquid, dbms, "UPDATE", Table, whereClause);
        all.add(pss);
        all.add(form);
        all.add(validateForm);
        return all;
    }

    public LinkedList geterateSelectForm(DeveloperQUID dquid, String dbms, String Table, String whereClause, String formName, String action, String encryptionKey) throws ClassNotFoundException, SQLException {
        this.dbms = dbms;
        tfUtil = new TablesNFieldsUtil();
        this.encryptionKey = encryptionKey;
        String form = "";
        this.dquid = dquid;
        ListFields = dquid.getDescribeFieldsTable(Table, this.dbms);

        validateForm = validateForm + "\n" + "else if (request.getParameter(\"FormName\").equals(\"" + formName + "\")) {";

        form = form + "\n" + "<%LinkedList values = (LinkedList) QUID.getMethotd(request.getParameter(\"PARAMETER\")).get(0);%>";
        form = form + "\n" + "<div class=\"form-container\">";
        form = form + "\n" + "<fieldset>";
        form = form + "\n" + "<legend>" + formName + "</legend>";
        form = form + "\n" + "<form name=\"" + formName + "\" method=\"post\" action=\"" + action + "\" enctype=\"application/x-www-form-urlencoded\" id=\"" + formName + "\">";
        form = form + "\n" + "<input type=\"hidden\" name=\"FormName\" value=\"" + formName + "\"/>";
        form = form + "\n" + "<div>";
        form = form + "\n" + "<table width=\"100%\" border =\"1\" align=\"center\">";
        form = form + "\n" + "<tr>";
        form = form + "\n" + "<th></th>";
        form = form + "\n" + "<th></th>";
        Iterator it = ListFields.iterator();
        while (it.hasNext()) {
            ListAux = null;
            ListAux = (LinkedList) it.next();
            form = form + "\n" + "<th>" + ListAux.get(0).toString() + "</th>";
        }
        form = form + "\n" + "</tr>";
        form = form + "\n" + "<%";
        form = form + "\n" + "it = null;";
        form = form + "\n" + "it = values.iterator();";
        form = form + "\n" + "while (it.hasNext()) {";
        form = form + "\n" + "listAux = (LinkedList) it.next();";
        form = form + "\n" + "%>";
        form = form + "\n" + "<tr>";
        form = form + "\n" + "<td name =\"Ver Detalles\" clas =\"link\"><a href=\"" + action + ".jsp?<%out.print(\"IDX=\" + listAux.get(0));%>\">Ver Detalles</td>";
        form = form + "\n" + "<td name =\"eliminar\" clas =\"link\"><a href=\"" + action + ".jsp?<%out.print(\"IDX=\" + listAux.get(0));%>\">Eliminar</td>";
        it = null;
        it = ListFields.iterator();
        int cont = 0;
        while (it.hasNext()) {
            ListAux = null;
            ListAux = (LinkedList) it.next();
            form = form + "\n" + "<td name =\"" + ListAux.get(0) + "\"><%out.print(listAux.get(" + cont + "));%></td>";
            cont++;
        }
        form = form + "\n" + "</tr>";
        form = form + "\n" + "<%";
        form = form + "\n" + "}";
        form = form + "\n" + "%>";
        form = form + "\n" + "</table>";
        form = form + "\n" + "</div>";
        form = form + "\n" + "</form>";
        form = form + "\n" + "</div>";

        validateForm = validateForm + "\n" + " }";

        LinkedList all = new LinkedList();
        PreparedStatementSQL ps = new PreparedStatementSQL();
        String pss = ps.getSQLPreparedStatement(dquid, dbms, "SELECT", Table, whereClause);
        all.add(pss);
        all.add(form);
        all.add(validateForm);
        return all;
        //return form;
    }

    private String addInsertField(String fieldName, String type, String key) {
        String encryptedFieldName = fieldName;
        String fieldSyntax = null;
        String fieldType = tfUtil.fieldTypeIdentifier(type, dbms);
        int limitSize = tfUtil.fieldSizeLimit(type, dbms);
        System.out.println("\n");
        System.out.println("fieldType: " + fieldType);
        System.out.println("limitSize: " + limitSize);

        if (isThisTheFirstField == 0) {
            validateForm = validateForm + "\n" + "if";
            isThisTheFirstField++;
        } else {
            validateForm = validateForm + "\n" + "else if";
        }

        if (encryptedFieldName.equals("") == false) {
            //TODO codigo para encriptacion del nombre del campo
        }
        fieldSyntax = "<div>";
        fieldSyntax = fieldSyntax + "\n" + "<label for=\"" + encryptedFieldName + "\">" + fieldName + "</label>";


        if (key.equalsIgnoreCase("mul")) {
            fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"50\" MAXLENGTH=\"" + limitSize + "\">";
            fieldSyntax = fieldSyntax + "\n" + "<select name=\"" + encryptedFieldName + "\" id=\"" + encryptedFieldName + "\">";
            fieldSyntax = fieldSyntax + "\n" + "<option selected=\"selected\"></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "it = null;";
            fieldSyntax = fieldSyntax + "\n" + "it = QUID.getcatalog().iterator();";
            fieldSyntax = fieldSyntax + "\n" + "while (it.hasNext()) {";
            fieldSyntax = fieldSyntax + "\n" + "llaux = null;";
            fieldSyntax = fieldSyntax + "\n" + "llaux = (LinkedList) it.next();";
            fieldSyntax = fieldSyntax + "\n" + "if (llaux.get(0).equals(values.get(21))) {";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "<option selected=\"selected\" value=\"<%out.print(llaux.get(0));%>\"><%out.print(llaux.get(1));%></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "} else {";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "<option value=\"<%out.print(llaux.get(0));%>\"><%out.print(llaux.get(1));%></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "}";
            fieldSyntax = fieldSyntax + "\n" + "}";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "</select>";

            validateForm = validateForm + "\n" + " (request.getParameter(\"" + encryptedFieldName + "\").equals(\"\")) {";
            validateForm = validateForm + "\n" + " //todo accion for this field error";
            validateForm = validateForm + "\n" + " }";
        } else {
            if (fieldType.equals("String") && limitSize != -1 && limitSize < 75) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"50\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidStringLength(request.getParameter(\"" + encryptedFieldName + "\"),0," + limitSize + ")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("String") && limitSize != -1 && limitSize > 75) {
                fieldSyntax = fieldSyntax + "\n" + "<textarea id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" cols=\"40\" MAXLENGTH=\"" + limitSize + "\" rows=\"5\"></textarea>";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidStringLength(request.getParameter(\"" + encryptedFieldName + "\"),0," + limitSize + ")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("String")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"50\">";
                validateForm = validateForm + "\n" + " (request.getParameter(\"" + encryptedFieldName + "\").equals(\"\")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("int") && limitSize != -1) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"8\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidInt(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("int")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"5\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidInt(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("float") && limitSize != -1) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"8\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidFloat(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("float")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"8\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidFloat(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            }
        }
        fieldSyntax = fieldSyntax + "\n" + "</div>";
        return fieldSyntax;
    }

    private String addUpDateField(String fieldName, String type, String key) {
        String encryptedFieldName = fieldName;
        String fieldSyntax = null;
        String fieldType = tfUtil.fieldTypeIdentifier(type, dbms);
        int limitSize = tfUtil.fieldSizeLimit(type, dbms);
        System.out.println("\n");
        System.out.println("fieldType: " + fieldType);
        System.out.println("limitSize: " + limitSize);

        if (isThisTheFirstField == 0) {
            validateForm = validateForm + "\n" + "if";
            isThisTheFirstField++;
        } else {
            validateForm = validateForm + "\n" + "else if";
        }

        if (encryptedFieldName.equals("") == false) {
            //TODO codigo para encriptacion del nombre del campo
        }
        fieldSyntax = "<div>";
        fieldSyntax = fieldSyntax + "\n" + "<label for=\"" + encryptedFieldName + "\">" + fieldName + "</label>";

        if (key.equalsIgnoreCase("mul")) {
            fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"\" autocomplete=\"off\" size=\"50\" MAXLENGTH=\"" + limitSize + "\">";
            fieldSyntax = fieldSyntax + "\n" + "<select name=\"" + encryptedFieldName + "\" id=\"" + encryptedFieldName + "\">";
            fieldSyntax = fieldSyntax + "\n" + "<option selected=\"selected\"></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "it = null;";
            fieldSyntax = fieldSyntax + "\n" + "it = QUID.getcatalog().iterator();";
            fieldSyntax = fieldSyntax + "\n" + "while (it.hasNext()) {";
            fieldSyntax = fieldSyntax + "\n" + "llaux = null;";
            fieldSyntax = fieldSyntax + "\n" + "llaux = (LinkedList) it.next();";
            fieldSyntax = fieldSyntax + "\n" + "if (llaux.get(0).equals(values.get(21))) {";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "<option selected=\"selected\" value=\"<%out.print(llaux.get(0));%>\"><%out.print(llaux.get(1));%></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "} else {";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "<option value=\"<%out.print(llaux.get(0));%>\"><%out.print(llaux.get(1));%></option>";
            fieldSyntax = fieldSyntax + "\n" + "<%";
            fieldSyntax = fieldSyntax + "\n" + "}";
            fieldSyntax = fieldSyntax + "\n" + "}";
            fieldSyntax = fieldSyntax + "\n" + "%>";
            fieldSyntax = fieldSyntax + "\n" + "</select>";

            validateForm = validateForm + "\n" + " (request.getParameter(\"" + encryptedFieldName + "\").equals(\"\")) {";
            validateForm = validateForm + "\n" + " //todo accion for this field error";
            validateForm = validateForm + "\n" + " }";
        } else {
            if (fieldType.equals("String") && limitSize != -1 && limitSize < 75) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"50\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidStringLength(request.getParameter(\"" + encryptedFieldName + "\"),0," + limitSize + ")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("String") && limitSize != -1 && limitSize > 75) {
                fieldSyntax = fieldSyntax + "\n" + "<textarea id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" cols=\"40\" MAXLENGTH=\"" + limitSize + "\" rows=\"5\">"
                        + "<%out.print(values.get());%>"
                        + "</textarea>";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidStringLength(request.getParameter(\"" + encryptedFieldName + "\"),0," + limitSize + ")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("String")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"50\">";
                validateForm = validateForm + "\n" + " (request.getParameter(\"" + encryptedFieldName + "\").equals(\"\")) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("int") && limitSize != -1) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"8\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidInt(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("int")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"5\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidInt(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("float") && limitSize != -1) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"8\" MAXLENGTH=\"" + limitSize + "\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidFloat(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            } else if (fieldType.equals("float")) {
                fieldSyntax = fieldSyntax + "\n" + "<input type=\"text\" id=\"" + encryptedFieldName + "\" name=\"" + fieldName + "\" value=\"<%out.print(values.get());%>\" autocomplete=\"off\" size=\"8\">";
                validateForm = validateForm + "\n" + " (!StringUtil.isValidFloat(request.getParameter(\"" + encryptedFieldName + "\"))) {";
                validateForm = validateForm + "\n" + " //todo accion for this field error";
                validateForm = validateForm + "\n" + " }";
            }
        }
        fieldSyntax = fieldSyntax + "\n" + "</div>";
        return fieldSyntax;
    }

    public static void main(String args[]) throws Exception {
        HTMLFromGenerator fg = new HTMLFromGenerator();
//        //String form = fg.geterateInsertForm("xend.tabletest", "nombreDelForm", "clio", "fieldSecure123456789asdfg");
//
//        PreparedStatementSQL ps = new PreparedStatementSQL();
//        //String StrTest = ps.getSQLPreparedStatement("mysql", "INSERT", "psa.ctrl_inasistencias", "");
//        System.out.println("SQL Insert: " + StrTest);
//        //String form = fg.geterateInsertForm("mysql", "psa.ctrl_inasistencias", "nombreDelForm", "clio", "fieldSecure123456789asdfg");
        DeveloperQUID dquid = new DeveloperQUID("sqlserver");
        //LinkedList form = fg.geterateUpDateForm(dquid, "mysql", "psa.cat_departamentos", "nombreDelForm", "clio", "fieldSecure123456789asdfg");
        System.out.println(fg.geterateSelectForm(dquid, "mysql", "psa.cat_departamentos", "", "nombreDelForm", "clio", "fieldSecure123456789asdfg"));
//        //System.out.println("\n\n\n" + form);
    }
}
