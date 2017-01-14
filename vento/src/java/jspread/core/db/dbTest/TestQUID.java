package jspread.core.db.dbTest;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import jspread.core.db.DeveloperQUID;
import jspread.core.db.QUID;
import jspread.core.db.util.PreparedStatementSQL;
import jspread.core.models.Transporter;
import jspread.core.util.SystemUtil;
import jspread.core.util.UTime;
import systemSettings.SystemSettings;

/**
 *
 * @author Hewlet
 */
public class TestQUID {

    // private static final String URL = "jdbc:odbc:TestDB";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "7km!8thm@28$%";
    private static final String DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
    //private static final String URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=D:\\Database\\testdb.mdb;}";
    //private static final String URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Users\\JeanPaul\\Desktop\\Access Repair\\Damaged\\CECYTEM.mdb;}";
    //private static final String URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\C.mdb;}";
    private static final String URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\C.mdb;";
    //String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+filename;  // 32-bit URL  
    //String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+filename // 64-bit URL
    //C:\Users\JeanPaul\Desktop\Access Repair\Damaged

    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {

        SystemSettings.ignition();
        QUID q=new QUID();
//        Transporter t=null;
//        DecimalFormat df = new DecimalFormat("#.##"); 
//        Double f = 7.6;
//        t=q.insert_Alumno(
//           "Juancho"
//           ,"Pedraza"
//           ,"Ramirez"
//           ,(byte)16
//           ,"PERJ010196"
//           ,"1996-01-01"
//           ,"calle las girnaldas"
//           ,"7126514725"
//           ,"7221234789"
//           ,"CNV-009"
//           ,"50740"
//           ,1
//           ,1
//           ,10
//           ,"juancho@mail.com"
//           ,"Ramiro Juarez"
//           ,true
//           ,108
//           ,f
//           ,(byte)4
//           ,"2013-07-24"
//           ,"2012-07-01"
//           ,"2013-2014"
//           ,(byte)5
//           ,1
//           ,true
//           ,20
//           ,"123456"
//           ,"S/N"
//           ,"9"
//           ,"Las animas"
//           ,"Tejeringo el chico"
//           ,"calle las animas"
//           ,"y calle de las sirenas"
//           ,"frente al deposito de chelas"
//           ,"20130701-666"
//           ,"Rabia");
//        System.out.println("msg:"+t.getMsg());
//        System.out.println("float:"+f);
//        LinkedList listAux=null;
//        Iterator it=q.select_infoEstado().iterator();
//        while(it.hasNext()){
//            listAux = (LinkedList) it.next();
//            System.out.println("\n"+listAux.get(0)+"|"+listAux.get(1)+"|");
//            
//        }
        //q.insert_Grupo("grupoTest2",cupo, "2012/2013","matutino", "2013-07-22", semestre,"vigente");
//        DeveloperQUID quid = new DeveloperQUID();
////        LinkedList ListFields = null;
////        LinkedList listAux = null;
////        Transporter Trans = null;
//        //String StrTest = TranslatorSQL.getSQLTranslated("INSERT", "Xend.tabletest", "WHERE ID_Cecyte = 1 AND ID_Cecyte = 3");
//        PreparedStatementSQL ps = new PreparedStatementSQL();
//        String StrTest = ps.getSQLPreparedStatement(quid, "mysql", "DELETE", "segpro.estrategias", "where id = ?");        
//        System.out.println(StrTest);
//        byte b = 20;
        // Trans = quid.inserttabletest(3, "Jelipe2", "Perez", 900, null, "Gelipe", 20);
        //System.out.println("Rsult: " + Trans.getMsg());

        /*
         * ListFields = quid.getDescribeFieldsTable("cecyte"); Iterator it =
         * ListFields.iterator();
         *
         * while (it.hasNext()) { listAux = (LinkedList) it.next(); //String Aux
         * = it.next().toString(); System.out.println("Field: " +
         * listAux.get(0)); System.out.println("Type: " + listAux.get(1)); }
         *
         * String StrTest2 =
         * PreparedStatementSQL.getTranslatorTypeData("nvarchar(85)");
         * System.out.println("Type converted: " + StrTest2);
         */
//        System.out.println("Load Driver");
//        Class.forName(DRIVER);
//        System.out.println("Make Connection");
//        System.out.println("URL: "+URL);
//        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        Statement st;
//        ResultSet rs;
//        st = connection.createStatement();
//        rs = st.executeQuery("select * from ALUMNOSDGI");
//        //rs = st.executeQuery("select * from PLANTELES");
//        int count = 0;
//        while (rs.next()) {
//            System.out.println("" + rs.getString(1));
//            count++;
//        }
//
//        System.out.println("Count: " + count);
//
//
//        System.out.println("Close connection");
//        connection.close();
//        System.out.println("noCtrl:"+q.getIdAlumno("13415082640991", "copf860412hmcrxd09"));
        //Calendar c1 = GregorianCalendar.getInstance();             
        //System.out.println("Fecha actual: "+c1.getTime());
        //c1.set(2000, Calendar.AUGUST, 31);
        //c1.set(2013, Calendar.AUGUST, 31, 7, 50);
        //System.out.println("Fecha 31 Agosto 2013: "+c1.getTime());              
        //c1.roll(Calendar.MINUTE, 50); /* Añadir 50 minutos */
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
//            Date fecha = null;
//            fecha = UTime.stringDateTime("2013-09-03 07:50");
//            System.out.println("Fecha Tecleada: " + fecha);
//            Calendar calendarDate = Calendar.getInstance();
//            calendarDate.setTime(fecha);
//            calendarDate.add(Calendar.MINUTE, 50);
//            System.out.println("Fecha mas 50 minutos: " + sdf.format(calendarDate.getTime()));
//        } catch (Exception ex) {
//            System.out.println("Error inesperado: " + ex.getMessage());
//        }
//        System.out.println("Fecha mas 50 minutos: " + UTime.calculaHoraClase("2013-09-03 07:50", 50));
//        System.out.println("Fecha mas 50 minutos: " + UTime.calculaHoraClase("2013-09-03 08:40", 50));
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:MARCOBD", "DESTINO", "inv56%jdyr");
//
//        Statement stmt = conn.createStatement();
//        ResultSet rset
//                = stmt.executeQuery("select * from DESTINO.USUARIO");
//        while (rset.next()) {
//            System.out.println(rset.getString(1)+" | "+rset.getString(2));   // Print col 1
//        }
//        stmt.close();
        //System.out.println("ID: "+ SystemUtil.getNumeroReporte("1", q));
       
    }
}
