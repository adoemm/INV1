/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IOX
 */
public class Conexion {

    private String DBURL;
    private String DRIVER = "com.mysql.jdbc.Driver";
    private String protocol = "jdbc";
    private String DBMS = "mysql";
    private String host = "127.0.0.1";
    private String port = "3306";
    private String schema = "";
    private String extraParameters = "";
    private String user = "";
    private String password = "";
//    private static final String host = "127.0.0.1";
//    private static final String port = "3306";
//    private static final String schema = "matrix88_noticias";
//    private static final String extraParameters = "";
//    private static final String user = "root";
//    private static final String password = "mundolost";
    
    private static final String version = "V0.1";

    public String getDBURL() {
        return DBURL;
    }

    public void setDBURL(String DBURL) {
        this.DBURL = DBURL;
    }

    public String getDRIVER() {
        return DRIVER;
    }

    public void setDRIVER(String DRIVER) {
        this.DRIVER = DRIVER;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDBMS() {
        return DBMS;
    }

    public void setDBMS(String DBMS) {
        this.DBMS = DBMS;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getExtraParameters() {
        return extraParameters;
    }

    public void setExtraParameters(String extraParameters) {
        this.extraParameters = extraParameters;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        java.sql.Connection conexion = null;
        try {
            Class.forName(DRIVER);
            //conexion.setAutoCommit(false);
            //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + extraParameters + "";
            DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + extraParameters + "";
            conexion = DriverManager.getConnection(DBURL, user, password);
            return conexion;
        } catch (Exception ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }

    public java.sql.Connection getConnectionAccessDBMS(String dataBaseDir, String user, String password, String extraParameters) {
        DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
        protocol = "jdbc:odbc";
        //DBMS = "Driver={Microsoft Access Driver (*.mdb)};DBQ=";
        DBMS = "Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=";
        schema = dataBaseDir;
        host = "127.0.0.1";

        java.sql.Connection conexion = null;
        try {
            Class.forName(DRIVER);
            //conexion.setAutoCommit(false);
            DBURL = protocol + ":" + DBMS + schema + extraParameters + "";
            //System.out.println("DBURL: "+DBURL);
            //System.out.println("user: "+user);
            //System.out.println("password: "+password);
            conexion = DriverManager.getConnection(DBURL, user, password);
            return conexion;
        } catch (Exception ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }
}
