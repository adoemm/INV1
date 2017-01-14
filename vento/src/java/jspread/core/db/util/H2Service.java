package jspread.core.db.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.tools.Server;

/**
 *
 * @author JeanPaul
 */
public final class H2Service {

    public static boolean reStartService(String host, String port, String BaseDir, String schema, String password) {
        boolean started = false;
        try {
//            Server logServer = Server.createTcpServer("-tcp", "-tcpPassword", password, "-tcpPort", port, "-baseDir", logBaseDir + schema);
//            //Server logServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPassword", "mundolost", "-tcpPort", "9092", "-baseDir", logBaseDir + schema);
//            System.out.println("Shutdown h2 service....");
//            logServer.stop();
//            logServer.shutdown();
//            Server.shutdownTcpServer("tcp://" + ip + ":" + port, password, true, true);
//            System.out.println("Shutdown Complete");
//            System.out.println("Start h2 service");
//            logServer.start();
//            started = true;
//            System.out.println("Start Successful");
            stopService(host, port, BaseDir, schema, password);
            //Thread.sleep(5000);
            startService(host, port, BaseDir, schema, password);
            started = true;
        } catch (Exception ex) {
            //Logger.getLogger(H2Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return started;
    }

    public static boolean stopService(String host, String port, String BaseDir, String schema, String password) {
        boolean stoped = false;
        try {
            Server logServer = Server.createTcpServer("-tcp", "-tcpPassword", password, "-tcpPort", port, "-baseDir", BaseDir + schema);
            System.out.println("\n\n");
            System.out.println("Shutdown h2 service....");
            logServer.stop();
            logServer.shutdown();
            Server.shutdownTcpServer("tcp://" + host + ":" + port, password, true, false);
            System.out.println("Shutdown Complete");
            System.out.println("\n\n");
            stoped = true;
        } catch (Exception ex) {
            //Logger.getLogger(H2Service.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("ex: "+ex);
        }
        return stoped;
    }

    public static boolean startService(String host, String port, String BaseDir, String schema, String password) {
        boolean started = false;
        try {
            Server logServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPassword", password, "-tcpPort", port, "-baseDir", BaseDir + schema);
            System.out.println("\n\n");
            System.out.println("Start h2 service");
            logServer.start();
            started = true;
            System.out.println("Start Successful");
            System.out.println("\n\n");
        } catch (Exception ex) {
            //Logger.getLogger/(H2Service.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("ex: "+ex);
        }
        return started;
    }
}
