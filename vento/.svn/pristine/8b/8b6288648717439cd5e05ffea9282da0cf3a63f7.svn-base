package jspread.core.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jspread.core.models.Transporter;

/**
 *
 * @author JeanPaul
 */
public final class DeveloperQUID {

    /**
     * Este objeto se encargara de utilizar una conexion disponible de la
     * piscina de conexiones
     */
    private JSpreadConnectionPool jscp = null;
    /**
     * Este objeto alamcenara cualquier consulta, insercion o actualizacion a la
     * base de datos.
     */
    private ResultSet rsQ = null;
    /**
     * its a connection to de database
     */
    private static Connection conn;
    /**
     * Execute de query to the database
     */
    private static Statement stmtQ;
    private static Statement stmtU;
    /**
     * String que contiene la sentencia SQL a ejecutar
     */
    private static String SQLSentence = null;

    public DeveloperQUID(String DBMS) throws ClassNotFoundException, SQLException {

        if (DBMS.equals("mysql")) {
            jscp = JSpreadConnectionPool.getSingleInstance();
            jscp.setClassforname("com.mysql.jdbc.Driver");
            jscp.setProtocol("jdbc");
            jscp.setDBMS("mysql");
            jscp.setHost("172.16.1.2");
            jscp.setPort(3306);
            jscp.setSchema("segpro");
            jscp.setUser("root");
            jscp.setPassword("mundolost");
            jscp.setMAX_POOL_SIZE(10);
            jscp.initialize();
            System.out.println("Direccion de la base de datos: " + jscp.getDBURL());
        } else if (DBMS.equals("sqlserver")) {

            jscp = JSpreadConnectionPool.getSingleInstance();
            jscp.setClassforname("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            jscp.setProtocol("jdbc");
            jscp.setDBMS("sqlserver");
            //jscp.setHost("127.0.0.1");
            jscp.setHost("172.16.2.215");
            //jscp.setHost("189.254.108.19");
            jscp.setPort(1433);
            jscp.setSchema("CECYTEMNG");
            jscp.setUser("root");
            jscp.setPassword("xyz9345%mlo");
            jscp.setMAX_POOL_SIZE(5);
            jscp.createDatabaseURL();
            jscp.initialize();
            System.out.println("Direccion de la base de datos: " + jscp.getDBURL());
        }
    }

    public LinkedList getFieldsTable(String Table, String dbms) {
        LinkedList listToSend = new LinkedList();
        try {
            SQLSentence = null;
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            stmtQ = conn.createStatement();

            SQLSentence = ""
                    + " Describe "
                    + Table
                    + " ;";

            rsQ = stmtQ.executeQuery(SQLSentence);

            while (rsQ.next()) {
                listToSend.add(rsQ.getString("field"));
            }
            jscp.returnConnectionToPool(conn);
        } catch (Exception ex) {
            jscp.returnConnectionToPool(conn);
            Logger.getLogger(DeveloperQUID.class.getName()).log(Level.SEVERE, null, ex);
            listToSend = null;
        }
        return listToSend;
    }

    public LinkedList getDescribeFieldsTable(String Table, String dbms) {
        LinkedList listToSend = new LinkedList();
        try {
            SQLSentence = null;
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            stmtQ = conn.createStatement();

            if (dbms.equals("mysql")) {
                SQLSentence = ""
                        + " Describe "
                        + Table
                        + " ;";

                rsQ = stmtQ.executeQuery(SQLSentence);

                while (rsQ.next()) {
                    LinkedList ListAux = new LinkedList();
                    ListAux.add(rsQ.getString("Field"));
                    ListAux.add(rsQ.getString("Type"));
                    ListAux.add(rsQ.getString("Key"));
                    listToSend.add(ListAux);
                }
            }
            else
            {

                String [] dato = Table.split("\\.");
                SQLSentence = ""
                        + " sp_columns "
                        + dato[2].toString()
                        + " ;";
                
                rsQ = stmtQ.executeQuery(SQLSentence);

                while (rsQ.next()) {
                    LinkedList ListAux = new LinkedList();
                    ListAux.add(rsQ.getString("COLUMN_NAME"));
                    ListAux.add(rsQ.getString("TYPE_NAME"));
                    ListAux.add(rsQ.getString("DATA_TYPE"));
                    listToSend.add(ListAux);
                }                
            }
            
            jscp.returnConnectionToPool(conn);
        } catch (Exception ex) {
            jscp.returnConnectionToPool(conn);
            Logger.getLogger(DeveloperQUID.class.getName()).log(Level.SEVERE, null, ex);
            listToSend = null;
        }
        return listToSend;
    }
}
