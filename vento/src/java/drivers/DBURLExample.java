/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drivers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author iox
 */
public final class DBURLExample {

    private static final String version = "V0.7";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {

        String Classforname;//Tambien llamado Driver
        String protocol;
        String DBMS;
        String host;
        int port;
        String schema;
        String user;
        String password;
        String extraParameters;
        String DBURL;
        String connectionType;



        Classforname = "org.apache.derby.jdbc.ClientDriver";//Tambien llamado Driver
        protocol = "jdbc";
        DBMS = "derby";
        host = "127.0.0.1";
        port = 1527;
        schema = "$h3rL0ck";
        user = "SherlockDB";
        password = "pass";
        extraParameters = "";


        DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        System.out.println("" + DBURL);





//        Classforname = "com.mysql.jdbc.Driver";
        String URL1 = "jdbc:mysql://";
        String URL2 = ":3306/Linx";
//        String URLFinal;
//        String host = "127.0.0.1";
//        String user = "root";
//        String pass = "mundolost";
        DBURL = URL1 + URL2;
        //System.out.println("" + DBURL);

        Classforname = "com.mysql.jdbc.Driver";
        protocol = "jdbc";
        DBMS = "mysql";
        host = "127.0.0.1";
        port = 3306;
        schema = "Linx";
        user = "root";
        password = "pass";
        extraParameters = "";

        //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + extraParameters + "";
        System.out.println("" + DBURL);



        Classforname = "com.mysql.jdbc.Driver";
        protocol = "jdbc";
        DBMS = "mysql";
        host = "127.0.0.1";
        port = 3306;
        schema = "Linx";
        user = "root";
        password = "pass";
        extraParameters = "";

        //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + extraParameters + "";
        System.out.println("" + DBURL);





//        //Para MSSQLServer
//    //"jdbc:sqlserver://172.16.2.52:1433;databaseName=prueba", "SQLDesarrollo", "sicecytem2012DB");
//    private String Classforname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//    //private String URL1 = "jdbc:sqlserver://172.16.2.52";
//    //private String URL1 = "jdbc:sqlserver://172.16.3.8";
//    private String URL1 = "jdbc:sqlserver://172.16.1.231";
//    private String URL2 = ":1433;databaseName=SEGPROY";
//    private String URLFinal;
//    private String host = "172.16.2.52";
//    private String user = "SQLDesarrollo";
//    private String pass = "sicecytem2012DB";

        Classforname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        protocol = "jdbc";
        DBMS = "sqlserver";
        host = "127.0.0.1";
        port = 1433;
        schema = "SEGPROY";
        user = "root";
        password = "pass";
        extraParameters = "";

        //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + ";databaseName=" + schema + extraParameters + "";
        System.out.println("" + DBURL);



        //ACCESS
        System.out.println("Para access hay que asegurarse que este hecha la conexion del ODBC");
        System.out.println("Tambien es importante mencionar que si estas usando la version de acces de 32 bits, la version de java tambien debe de ser de 32 bits, de lo contrario marcara error de driver");
        Classforname = "sun.jdbc.odbc.JdbcOdbcDriver";
        protocol = "jdbc:odbc";
        //Driver={Microsoft Access Driver (*.mdb)}; // Access de 32-bit URL
        //Driver={Microsoft Access Driver (*.mdb, *.accdb)}; // Access de 64-bit URL
        DBMS = "Driver={Microsoft Access Driver (*.mdb, *.accdb)};"; // Access de 64-bit URL
        host = "127.0.0.1";
        port = 1433;
        schema = "DBQ=C:\\C.mdb;";
        user = "root";
        password = "pass";
        extraParameters = "";

        DBURL = protocol + ":" + DBMS + schema + extraParameters + "";
        System.out.println("" + DBURL);



        //H2 - Embedded (local) connection http://www.h2database.com/html/features.html
        Classforname = "org.h2.Driver";
        protocol = "jdbc";
        DBMS = "h2";
        host = "127.0.0.1";
        port = 0;
        schema = "C:\\h2DBS\\myTesth2";
        user = "root";
        password = "pass";
        extraParameters = "";

        DBURL = protocol + ":" + DBMS + ":" + schema + extraParameters + "";
        System.out.println("H2 Embedded: " + DBURL);


        //H2 - tcp connection
        // Connection con = DriverManager.getConnection("jdbc:h2:tcp://127.0.0.1:8080/myTesth2",
        Classforname = "org.h2.Driver";
        protocol = "jdbc";
        DBMS = "h2";
        host = "127.0.0.1";
        port = 9091;
        schema = "myTesth2";
        user = "root";
        password = "pass";
        extraParameters = "";
        connectionType = "tcp";

        //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        DBURL = protocol + ":" + DBMS + ":" + connectionType + "://" + host + ":" + port + "/" + schema + extraParameters + "";
        System.out.println("H2 TCP: " + DBURL);

        //H2 SSL connection
        // Connection con = DriverManager.getConnection("jdbc:h2:ssl://127.0.0.1:8080/myTesth2",
        Classforname = "org.h2.Driver";
        protocol = "jdbc";
        DBMS = "h2";
        host = "127.0.0.1";
        port = 9091;
        schema = "myTesth2";
        user = "root";
        password = "pass";
        extraParameters = "";
        connectionType = "ssl";

        //DBURL = protocol + ":" + DBMS + "://" + host + ":" + port + "/" + schema + ";" + "user=" + user + ";" + "password=" + password + ";" + extraParameters + "";
        DBURL = protocol + ":" + DBMS + ":" + connectionType + "://" + host + ":" + port + "/" + schema + extraParameters + "";
        System.out.println("H2 SSL: " + DBURL);




        String str = "Hola Mundo";
        MessageDigest digester = null;

        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        System.out.println("" + hexString);
        //d501194c987486789bb01b50dc1a0adb




    }
}
