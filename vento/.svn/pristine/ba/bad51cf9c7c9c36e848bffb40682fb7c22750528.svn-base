package jspread.core.db.dbTest;


/**
 *
 * @author JeanPaul
 */

import java.sql.*;

class TestAccessConnection {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {
        try {
            System.out.println("Load Driver");
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
// set this to a MS Access DB you have on your machine
            //String filename = "d:/java/mdbTEST.mdb";
            //C:\Users\JeanPaul\Desktop\Access Repair\Damaged
            //String filename = "C:\\Users\\JeanPaul\\Desktop\\Access Repair\\Damaged\\CECYTEM.mdb";
            //String filename = "C:/Users/JeanPaul/Desktop/Access Repair/Damaged/CECYTEM.mdb";
            String filename = "C:\\CECYTEM.mdb";
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            //database += filename.trim() + ";DriverID=22;READONLY=true}"; // add on to the end 
            database += filename.trim();
// now we can get the connection from the DriverManager
            System.out.println("Make Connection to: " + database);
            Connection conn = DriverManager.getConnection(database, "admin", "7km!8thm@28$%");
            //System.out.println(""+conn.isClosed());
            //System.out.println("" + conn.isValid(0));
            Statement st;
            ResultSet rs;
            st = conn.createStatement();
            rs = st.executeQuery("select * from CALMODULO");
            //rs = st.executeQuery("select * from PLANTELES");
            int count = 0;
            while (rs.next()) {
                System.out.println("" + rs.getString(1));
                count++;
            }

            System.out.println("Count: " + count);

            System.out.println("Close connection");
            conn.close();

            System.out.println("Close connection");
            conn.close();





//        SystemSettings.ignition();
//        JSpreadConnectionPool cp = JSpreadConnectionPool.getSingleInstance();
//        Connection conn = cp.getConnectionFromPool();
//        Statement st;
//        ResultSet rs;
//        st = conn.createStatement();
//        rs = st.executeQuery("select * from ALUMNOSDGI");
//        //rs = st.executeQuery("select * from PLANTELES");
//        int count = 0;
//        while (rs.next()) {
//            System.out.println("" + rs.getString(1));
//            count++;
//        }
//        cp.returnConnectionToPool(conn);
//
//        System.out.println("Count: " + count);
//        System.out.println("CP: " + cp.getDBURL());


        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
}
