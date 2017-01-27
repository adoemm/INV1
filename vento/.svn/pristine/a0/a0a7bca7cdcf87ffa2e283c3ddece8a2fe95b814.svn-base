package jspread.core.db.util;

import jspread.core.db.DeveloperQUID;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Hewlet
 */
public final class PreparedStatementSQL {

    public String getSQLPreparedStatement(DeveloperQUID dquid, String dbms, String Instruction, String Table, String Condition) throws ClassNotFoundException, SQLException {
        TablesNFieldsUtil tfUtil = new TablesNFieldsUtil();
        StringBuilder BuildPstmt = new StringBuilder();
        LinkedList ListFields = null;
        LinkedList ListAux = null;
        int CountFields = 0;

        if (Instruction.equals("INSERT")) {

            ListFields = dquid.getDescribeFieldsTable(Table, dbms);
            Iterator it = ListFields.iterator();
            StringBuilder BuildQueryFields = new StringBuilder();
            StringBuilder BuildQueryParameters = new StringBuilder();
            StringBuilder BuildHead = new StringBuilder();
            StringBuilder BuildHeadParameters = new StringBuilder();
            StringBuilder BuildPstmtSetValues = new StringBuilder();
            StringBuilder BuildFoot = new StringBuilder();
            StringBuilder BuildBody = new StringBuilder();
            //*** Construimos el encabezado de la funcion ***//            
            BuildHead.append("\n\n");
            BuildHead.append("public final Transporter ");
            BuildHead.append("insert_");
            BuildHead.append(Table.replace('.', '_'));
            BuildHead.append("(");


            while (it.hasNext()) {
                ListAux = (LinkedList) it.next();
                CountFields += 1;

                if (BuildQueryFields.length() == 0) {
                    //*** Fields ***//
                    BuildQueryFields.append("\n + \" INSERT INTO ");
                    BuildQueryFields.append(Table);
                    BuildQueryFields.append(" (\" ");
                    BuildQueryFields.append("\n + \" ");
                    BuildQueryFields.append(ListAux.get(0).toString());
                    BuildQueryFields.append("\"");
                    //*** Construir param_etros Query***//
                    BuildQueryParameters.append("\n + \" VALUES (\"");
                    BuildQueryParameters.append("\n + \" ");
                    BuildQueryParameters.append(" ? ");
                    BuildQueryParameters.append("\"");
                    //*** Construir parametros para el Metodo ***//                    
                    BuildHeadParameters.append(tfUtil.fieldTypeIdentifier(ListAux.get(1).toString(), dbms));
                    BuildHeadParameters.append(" ");
                    BuildHeadParameters.append(ListAux.get(0).toString());
                    //*** Construir valores para el PreparedStatement***//
                    //BuildPstmtSetValues.append("\n\n\n");
                    //BuildPstmtSetValues.append("pstmt = conn.prepareStatement(SQLSentence);");
                    BuildPstmtSetValues.append("\n");
                    BuildPstmtSetValues.append(getValueSet(ListAux.get(1).toString()));
                    BuildPstmtSetValues.append(CountFields);
                    BuildPstmtSetValues.append(",");
                    BuildPstmtSetValues.append(ListAux.get(0).toString());
                    BuildPstmtSetValues.append(");");
                } else {
                    //*** Fields ***//
                    BuildQueryFields.append("\n + \" ,");
                    BuildQueryFields.append(ListAux.get(0).toString());
                    BuildQueryFields.append("\" ");
                    //*** Construir param_etros Query***//
                    BuildQueryParameters.append("\n + \" ,? \"");
                    //*** Construir parametros para el Metodo ***//                    
                    BuildHeadParameters.append(" ,");
                    BuildHeadParameters.append(tfUtil.fieldTypeIdentifier(ListAux.get(1).toString(), dbms));
                    BuildHeadParameters.append(" ");
                    BuildHeadParameters.append(ListAux.get(0).toString());
                    //*** Construir valores para el PreparedStatement***//
                    BuildPstmtSetValues.append("\n");
                    BuildPstmtSetValues.append(getValueSet(ListAux.get(1).toString()));
                    BuildPstmtSetValues.append(CountFields);
                    BuildPstmtSetValues.append(",");
                    BuildPstmtSetValues.append(ListAux.get(0).toString());
                    BuildPstmtSetValues.append(");");

                }
            }

            //*** Terminamos QUERY ***//
            BuildQueryFields.append("\n + \")\" ");
            BuildQueryParameters.append("\n + \");\";");
            //*** Construimos el encabezado del metodo ***//
            BuildHead.append(BuildHeadParameters);
            BuildHead.append(") {");
            //*** Agregamos variables ***//
            BuildHead.append("\n Transporter tport = null;");
            BuildHead.append("\n JSpreadConnectionPool jscp = null;");
            BuildHead.append("\n Connection conn = null;");
            BuildHead.append("\n String SQLSentence = null;");
            BuildHead.append("\n PreparedStatement pstmt = null;");
            //BuildHead.append("\n BigDecimal id = null;");
            BuildHead.append("\n ResultSet rs = null;");
            BuildHead.append("\n String id_inserted = null;");
            //*** Agregamos Try ***//
            BuildHead.append("\n\n try {");
            BuildHead.append("\n SQLSentence = \"\"");
            //*** Construimos la parte del cuerpo del metodo ***//  
            BuildBody.append("\n\n jscp = JSpreadConnectionPool.getSingleInstance();");
            BuildBody.append("\n conn = jscp.getConnectionFromPool();");
            BuildBody.append("\n pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);");
            //*** Agregamos la ejecucion de la Query ***//
            BuildFoot.append("\n\n int rowCount = pstmt.executeUpdate();");
            BuildFoot.append("\n rs = pstmt.getGeneratedKeys();");
            BuildFoot.append("\n\n if (rs.next()) {");
            BuildFoot.append("\n id_inserted = rs.getString(1);");
            BuildFoot.append("\n }");
            //BuildFoot.append("\n\n id = this.insertLog(\"INSERT\", \"insert_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"\");");
            BuildFoot.append("\n\n endConnection(jscp, conn, pstmt, rs);");
            BuildFoot.append("\n tport = new Transporter(0, \"ID Generado: \" + id_inserted);");
            BuildFoot.append("\n } catch (Exception ex) {");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt, rs);");
            BuildFoot.append("\n Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);");
            //BuildFoot.append("\n BigDecimal secondLog = this.insertLog(\"INSERT\", \"insert_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"logID:\" + id + \" \" + ex);");
            //BuildFoot.append("\n tport = new Transporter(1, \"Error inesperado... Verifique log \" + secondLog);");
            BuildFoot.append("\n tport = new Transporter(1, \"Error inesperado... Verifique log \");");
            BuildFoot.append("\n }");
            BuildFoot.append("\n return tport;");
            BuildFoot.append("\n }");
            //*** Unimos todo para crear el metodo ***//                     
            //*** Agregamos encabezado, declaramos variables y parte del Try ***//
            BuildPstmt.append(BuildHead);
            //*** Agregamos campos de la sentencia SQL ***//
            BuildPstmt.append(BuildQueryFields);
            //*** Agregamos los parametros de la sentencia SQL ***//
            BuildPstmt.append(BuildQueryParameters);
            //*** Agregamos conexion y prepareStatement ***//
            BuildPstmt.append(BuildBody);
            //*** Definimos y establecemos los valores para Pstmt ***//
            BuildPstmt.append(BuildPstmtSetValues);
            //*** Agregamos el pie del metodo ***//
            BuildPstmt.append(BuildFoot);

        } else if (Instruction.equals("UPDATE")) {

            ListFields = dquid.getDescribeFieldsTable(Table, dbms);
            Iterator it = ListFields.iterator();
            StringBuilder BuildQuery = new StringBuilder();
            StringBuilder BuildHead = new StringBuilder();
            StringBuilder BuildHeadParameters = new StringBuilder();
            StringBuilder BuildPstmtSetValues = new StringBuilder();
            StringBuilder BuildFoot = new StringBuilder();
            StringBuilder BuildBody = new StringBuilder();
            //*** Construimos el encabezado de la funcion ***//            
            BuildHead.append("\n\n");
            BuildHead.append("public final Transporter ");
            BuildHead.append("update_");
            BuildHead.append(Table.replace('.', '_'));
            BuildHead.append("(");

            while (it.hasNext()) {
                ListAux = (LinkedList) it.next();
                CountFields += 1;

                if (BuildQuery.length() == 0) {
                    //*** Construye la query con campos y parametros para el primer campo***//
                    BuildQuery.append("\n + \" UPDATE ");
                    BuildQuery.append(Table);
                    BuildQuery.append(" SET \" ");
                    BuildQuery.append("\n + \" ");
                    BuildQuery.append(ListAux.get(0).toString());
                    //*** Agregar parametro del campo ***//
                    BuildQuery.append(" = ? \"");
                    //*** Construir parametros para el Metodo ***//                    
                    BuildHeadParameters.append(tfUtil.fieldTypeIdentifier(ListAux.get(1).toString(), dbms));
                    BuildHeadParameters.append(" param_");
                    BuildHeadParameters.append(ListAux.get(0).toString());
                    //*** Construir valores para el PreparedStatement***//
                    BuildPstmtSetValues.append("\n");
                    BuildPstmtSetValues.append("pstmt = conn.prepareStatement(SQLSentence);");
                    BuildPstmtSetValues.append("\n");
                    BuildPstmtSetValues.append(getValueSet(ListAux.get(1).toString()));
                    BuildPstmtSetValues.append(CountFields);
                    BuildPstmtSetValues.append(",param_");
                    BuildPstmtSetValues.append(ListAux.get(0).toString());
                    BuildPstmtSetValues.append(");");
                } else {
                    //*** Construye la query con campos y parametros para el resto de los campos***//
                    BuildQuery.append("\n + \" ,");
                    BuildQuery.append(ListAux.get(0).toString());
                    //*** Agregar parametro del campo ***//
                    BuildQuery.append(" = ? \"");
                    //*** Construir parametros para el Metodo ***//                    
                    BuildHeadParameters.append(" ,");
                    BuildHeadParameters.append(tfUtil.fieldTypeIdentifier(ListAux.get(1).toString(), dbms));
                    BuildHeadParameters.append(" param_");
                    BuildHeadParameters.append(ListAux.get(0).toString());
                    //*** Construir valores para el PreparedStatement***//
                    BuildPstmtSetValues.append("\n");
                    BuildPstmtSetValues.append(getValueSet(ListAux.get(1).toString()));
                    BuildPstmtSetValues.append(CountFields);
                    BuildPstmtSetValues.append(",param_");
                    BuildPstmtSetValues.append(ListAux.get(0).toString());
                    BuildPstmtSetValues.append(");");

                }
            }

            //*** Terminamos QUERY ***//
            if (Condition.equals("") != true) {
                BuildQuery.append("\n + \" ");
                BuildQuery.append(Condition);
                BuildQuery.append("\"");
                BuildQuery.append("\n + \";\";");
                //*** Agregamos parametro de la condicion ***//
                BuildHeadParameters.append(",int param_ID");
                //***Contruimos parametro para el Where ***//
                BuildPstmtSetValues.append("\n");
                BuildPstmtSetValues.append("pstmt.setInt(");
                BuildPstmtSetValues.append((CountFields + 1));
                BuildPstmtSetValues.append(",param_ID");
                BuildPstmtSetValues.append(");");
            } else {
                BuildQuery.append("\n + \";\";");
            }
            //*** Construimos el encabezado del metodo ***//
            BuildHead.append(BuildHeadParameters);
            BuildHead.append(") {");
            //*** Agregamos variables ***//
            BuildHead.append("\n Transporter tport = null;");
            BuildHead.append("\n JSpreadConnectionPool jscp = null;");
            BuildHead.append("\n Connection conn = null;");
            BuildHead.append("\n String SQLSentence = null;");
            BuildHead.append("\n PreparedStatement pstmt = null;");
            //Se quita pq no se usa la linea
            //BuildHead.append("\n BigDecimal id = null;");
            //*** Agregamos Try ***//
            BuildHead.append("\n\n try {");
            BuildHead.append("\n SQLSentence = \"\"");
            //*** Construimos la parte del cuerpo del metodo ***//  
            BuildBody.append("\n\n jscp = JSpreadConnectionPool.getSingleInstance();");
            BuildBody.append("\n conn = jscp.getConnectionFromPool();");            
            //*** Construimos la parte inferior del metodo ***//                     
            BuildFoot.append("\n\n int rowCount = pstmt.executeUpdate();");
            //Se quita pq no se usa la linea
            //BuildFoot.append("\n\n id = this.insertLog(\"UPDATE\", \"update_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"\");");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt);");
            BuildFoot.append("\n tport = new Transporter(0, \"Filas afectadas: \" + rowCount);");
            BuildFoot.append("\n } catch (Exception ex) {");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt);");
            BuildFoot.append("\n Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);");            
            //BuildFoot.append("\n BigDecimal secondLog = this.insertLog(\"UPDATE\", \"update_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"logID:\" + id + \" \" + ex);");
            //BuildFoot.append("\n tport = new Transporter(0, \"Error inesperado... Verifique log \" + secondLog);");
            BuildFoot.append("\n tport = new Transporter(1, \"Error inesperado... Verifique log \");");
            BuildFoot.append("\n }");
            BuildFoot.append("\n return tport;");
            BuildFoot.append("\n }");
            //*** ***//
            //*** Unimos todo para crear el metodo ***//                     
            //*** Agregamos encabezado, declaramos variables y parte del Try ***//
            BuildPstmt.append(BuildHead);
            //*** Agregamos sentencia SQL ***//
            BuildPstmt.append(BuildQuery);
            //*** Agregamos conexion y prepareStatement ***//
            BuildPstmt.append(BuildBody);            
            //*** Definimos y establecemos los valores para PStmt ***//
            BuildPstmt.append(BuildPstmtSetValues);
            //*** Agregamos el pie del metodo ***//
            BuildPstmt.append(BuildFoot);

        } else if (Instruction.equals("DELETE")) {

            StringBuilder BuildQuery = new StringBuilder();
            StringBuilder BuildHead = new StringBuilder();
            StringBuilder BuildPstmtSetValues = new StringBuilder();
            StringBuilder BuildFoot = new StringBuilder();
            StringBuilder BuildBody = new StringBuilder();
            //*** Construimos el encabezado de la funcion ***//            
            BuildHead.append("\n\n");
            BuildHead.append("public final Transporter ");
            BuildHead.append("delete_");
            BuildHead.append(Table.replace('.', '_'));
            BuildHead.append("(");


            //*** Construye query DELETE***//
            BuildQuery.append("\n + \" DELETE FROM ");
            BuildQuery.append(Table);
            BuildQuery.append(" \"");

            //*** Construye Condicion ***//
            if (Condition.equals("") != true) {
                BuildQuery.append("\n + \" ");
                BuildQuery.append(Condition);
                BuildQuery.append("\"");
                BuildQuery.append("\n + \";\";");
                //*** Finalizamos parametros del metodo ***//
                BuildHead.append("int param_ID");
                BuildHead.append(") {");
                //*** Construir valores para el PreparedStatement***//
                BuildPstmtSetValues.append("\n");
                BuildPstmtSetValues.append("pstmt = conn.prepareStatement(SQLSentence);");
                BuildPstmtSetValues.append("\n");
                BuildPstmtSetValues.append("pstmt.setInt(");
                BuildPstmtSetValues.append("1");
                BuildPstmtSetValues.append(",param_ID");
                BuildPstmtSetValues.append(");");
                BuildFoot.append("\n\n");
            } else {
                BuildQuery.append("\n + \";\";");
                //*** Finalizamos el metodo sin parametros ***//
                BuildHead.append(") {");
                //*** Construir PreparedStatement***//
                BuildPstmtSetValues.append("\n");
                BuildPstmtSetValues.append("pstmt = conn.prepareStatement(SQLSentence);");
            }

            //*** Agregamos variables ***//
            BuildHead.append("\n Transporter tport = null;");
            BuildHead.append("\n JSpreadConnectionPool jscp = null;");
            BuildHead.append("\n Connection conn = null;");            
            BuildHead.append("\n String SQLSentence = null;");
            BuildHead.append("\n PreparedStatement pstmt = null;");
            //Se quita pq no se usa la linea
            //BuildHead.append("\n BigDecimal id = null;");
            //*** Agregamos Try ***//
            BuildHead.append("\n\n try {");
            BuildHead.append("\n SQLSentence = \"\"");
            //*** Definimos parte del Metodo ***//
            BuildBody.append("\n\n jscp = JSpreadConnectionPool.getSingleInstance();");
            BuildBody.append("\n conn = jscp.getConnectionFromPool();");            
            //*** Construimos la parte inferior del metodo ***//  
            BuildFoot.append("\n int rowCount = pstmt.executeUpdate();");
            //Se quita pq no se usa la linea
            //BuildFoot.append("\n\n id = this.insertLog(\"DELETE\", \"delete_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"Comment\");");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt);");
            BuildFoot.append("\n tport = new Transporter(0, \"Filas afectadas: \" + rowCount);");
            BuildFoot.append("\n } catch (Exception ex) {");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt);");
            //Se quita pq no se usa la linea
            //BuildFoot.append("\n BigDecimal secondLog = this.insertLog(\"DELETE\", \"delete_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"logID:\" + id + \" \" + ex);");
            BuildFoot.append("\n Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);");
            //BuildFoot.append("\n tport = new Transporter(0, \"Error inesperado... Verifique log \" + secondLog);");
            BuildFoot.append("\n tport = new Transporter(1, \"Error inesperado... Verifique log \");");
            BuildFoot.append("\n }");
            BuildFoot.append("\n return tport;");
            BuildFoot.append("\n }");
            //*** ***//
            //*** Unimos todo para crear el metodo ***//                     
            //*** Agregamos encabezado, declaramos variables y parte del Try ***//
            BuildPstmt.append(BuildHead);
            //*** Agregamos sentencia SQL ***//
            BuildPstmt.append(BuildQuery);
            //*** Agregamos conexion y prepareStatement ***//
            BuildPstmt.append(BuildBody);            
            //*** Definimos y establecemos los valores para PStmt ***//
            BuildPstmt.append(BuildPstmtSetValues);
            //*** Agregamos el pie del metodo ***//
            BuildPstmt.append(BuildFoot);

        } else if (Instruction.equals("SELECT")) {

            ListFields = dquid.getDescribeFieldsTable(Table, dbms);
            Iterator it = ListFields.iterator();
            StringBuilder BuildQuery = new StringBuilder();
            StringBuilder BuildHead = new StringBuilder();
            StringBuilder BuildPstmtGetField = new StringBuilder();
            StringBuilder BuildPstmtGetHead = new StringBuilder();
            StringBuilder BuildFoot = new StringBuilder();
            StringBuilder BuildBody = new StringBuilder();
            //*** Construimos el encabezado de la funcion ***//            
            BuildHead.append("\n\n");
            BuildHead.append("public final LinkedList ");
            BuildHead.append("select_");
            BuildHead.append(Table.replace('.', '_'));
            BuildHead.append("(");

            while (it.hasNext()) {
                ListAux = (LinkedList) it.next();
                CountFields += 1;

                if (BuildQuery.length() == 0) {
                    //*** Construye el Select ***//
                    BuildQuery.append("\n + \" SELECT ");
                    //*** Construye campos del select ***//
                    BuildQuery.append("\"");
                    BuildQuery.append("\n + \" ");
                    BuildQuery.append(ListAux.get(0).toString());
                    BuildQuery.append("\"");
                    //*** Construir PreparedStatement***//
                    BuildPstmtGetHead.append("\n");
                    BuildPstmtGetHead.append("pstmt = conn.prepareStatement(SQLSentence);");
                    //*** Recupera los datos de los campos del select ***//
                    BuildPstmtGetField.append("\n while (rs.next()) { ");
                    BuildPstmtGetField.append("\n LinkedList llaux = new LinkedList(); ");
                    BuildPstmtGetField.append("\n llaux.add(");
                    BuildPstmtGetField.append(getTypeRS(ListAux.get(1).toString()));
                    BuildPstmtGetField.append(CountFields);
                    BuildPstmtGetField.append("));");
                } else {
                    //*** Construye campos del select ***//
                    BuildQuery.append("\n + \" ,");
                    BuildQuery.append(ListAux.get(0).toString());
                    BuildQuery.append("\"");
                    //*** Recupera los datos de los campos del select ***//
                    BuildPstmtGetField.append("\n llaux.add(");
                    BuildPstmtGetField.append(getTypeRS(ListAux.get(1).toString()));
                    BuildPstmtGetField.append(CountFields);
                    BuildPstmtGetField.append("));");
                }
            }

            //*** Terminamos while que recupera los datos del select ***//
            BuildPstmtGetField.append("\n listToSend.add(llaux);");
            BuildPstmtGetField.append("\n }");

            //*** Terminamos QUERY ***//
            if (Condition.equals("") != true) {
                BuildQuery.append("\n + \" FROM ");
                BuildQuery.append(Table);
                BuildQuery.append("\"");
                BuildQuery.append("\n + \" ");
                BuildQuery.append(Condition);
                BuildQuery.append("\"");
                BuildQuery.append("\n + \";\";");
                //*** Finalizamos parametros del metodo ***//
                BuildHead.append("int param_ID");
                BuildHead.append(") {");
                //*** Agregamos parametros al PrepareStatement ***//
                BuildPstmtGetHead.append("\n pstmt.setInt(");
                BuildPstmtGetHead.append("1");
                BuildPstmtGetHead.append(",param_ID);");
                BuildPstmtGetHead.append("\n rs = pstmt.executeQuery();");
                //BuildPstmtGetHead.append("\n\n id = this.insertLog(\"SELECT\", \"select_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"Comment\");");

            } else {
                BuildQuery.append("\n + \" FROM ");
                BuildQuery.append(Table);
                BuildQuery.append("\"");
                BuildQuery.append("\n + \";\";");
                //*** Finalizamos el metodo sin parametros ***//
                BuildHead.append(") {");
                //*** Finalizamos PreparedStatement***//
                BuildPstmtGetHead.append("\n rs = pstmt.executeQuery();");
                //BuildPstmtGetHead.append("\n\n id = this.insertLog(\"SELECT\", \"select_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"Comment\");");
            }

            //*** Agregamos variables ***//
            BuildHead.append("\n LinkedList listToSend = new LinkedList();");
            BuildHead.append("\n JSpreadConnectionPool jscp = null;");
            BuildHead.append("\n Connection conn = null;");            
            BuildHead.append("\n String SQLSentence = null;");
            BuildHead.append("\n PreparedStatement pstmt = null;");
            BuildHead.append("\n ResultSet rs = null;");
            //BuildHead.append("\n BigDecimal id = null;");                      
            //*** Agregamos Try ***//
            BuildHead.append("\n\n try {");
            BuildHead.append("\n SQLSentence = \"\"");
            //*** Agregamos parte del cuerpo ***//
            BuildBody.append("\n\n jscp = JSpreadConnectionPool.getSingleInstance();");
            BuildBody.append("\n conn = jscp.getConnectionFromPool();");            
            //*** Construimos la parte inferior del metodo ***//                     
            BuildFoot.append("\n endConnection(jscp, conn, pstmt, rs);");
            BuildFoot.append("\n } catch (Exception ex) {");
            BuildFoot.append("\n listToSend = null;");
            BuildFoot.append("\n endConnection(jscp, conn, pstmt, rs);");            
            //BuildFoot.append("\n BigDecimal secondLog = this.insertLog(\"SELECT\", \"select_").append(Table.replace('.', '_')).append("\", \"TestDatabase\", \"").append(Table).append("\", \"TestId\", \"logID:\" + id + \" \" + ex);");                        
            //BuildFoot.append("\n Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, \" Verifique log: \" + secondLog + \" \" + ex);");
            BuildFoot.append("\n Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, \" Verifique log: \" + \" \" + ex);");
            BuildFoot.append("\n }");
            BuildFoot.append("\n return listToSend;");
            BuildFoot.append("\n }");

            //***       Unimos todo para crear el metodo        ***//                     
            //*** Agregamos encabezado, declaramos variables y parte del Try ***//
            BuildPstmt.append(BuildHead);
            //*** Agregamos sentencia SQL ***//
            BuildPstmt.append(BuildQuery);
            //*** Agregamos el PreparedStatement ***//
            BuildPstmt.append(BuildBody);
            //*** Construimos el PStmt ***//
            BuildPstmt.append(BuildPstmtGetHead);
            //*** Construimos el While para recuperar los datos ***//
            BuildPstmt.append(BuildPstmtGetField);
            //*** Agregamos el pie del metodo ***//
            BuildPstmt.append(BuildFoot);

        }

        return BuildPstmt.toString();
    }

    public String getValueSet(String StrType) {

        String TypeSetData = "TypeSetUndefined";

        if (StrType.indexOf("varchar") != -1) {
            TypeSetData = "pstmt.setString(";
        } else if (StrType.indexOf("text") != -1) {
            TypeSetData = "pstmt.setString(";
        } else if (StrType.indexOf("tinyint") != -1) {
            TypeSetData = "pstmt.setByte(";
        } else if (StrType.indexOf("int") != -1) {
            TypeSetData = "pstmt.setInt(";
        } else if (StrType.indexOf("double") != -1) {
            TypeSetData = "pstmt.setDouble(";
        } else if (StrType.indexOf("float") != -1) {
            TypeSetData = "pstmt.setFloat(";
        } else if (StrType.indexOf("timestamp") != -1) {
            TypeSetData = "pstmt.setTimestamp(";
        } else if (StrType.indexOf("datetime") != -1) {
            TypeSetData = "pstmt.setDate(";
        } else if (StrType.indexOf("date") != -1) {
            TypeSetData = "pstmt.setDate(";
        } else if (StrType.indexOf("bit") != -1) {
            TypeSetData = "pstmt.setBoolean(";
        }

        return TypeSetData;
    }

    public String getTypeRS(String StrType) {

        String TypeSetData = "TypeSetUndefined";

        if (StrType.indexOf("varchar") != -1) {
            TypeSetData = "rs.getString(";
        } else if (StrType.indexOf("text") != -1) {
            TypeSetData = "rs.getString(";
        } else if (StrType.indexOf("tinyint") != -1) {
            TypeSetData = "rs.getByte(";
        } else if (StrType.indexOf("int") != -1) {
            TypeSetData = "rs.getInt(";
        } else if (StrType.indexOf("double") != -1) {
            TypeSetData = "rs.getDouble(";
        } else if (StrType.indexOf("float") != -1) {
            TypeSetData = "rs.getFloat(";
        } else if (StrType.indexOf("timestamp") != -1) {
            TypeSetData = "rs.getTimestamp(";
        } else if (StrType.indexOf("datetime") != -1) {
            TypeSetData = "rs.getDate(";
        } else if (StrType.indexOf("date") != -1) {
            TypeSetData = "rs.getDate(";
        } else if (StrType.indexOf("bit") != -1) {
            TypeSetData = "rs.getBoolean(";
        }

        return TypeSetData;
    }
}
