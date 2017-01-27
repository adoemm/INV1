/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jspread.core.db.JSpreadConnectionPool;

/**
 *
 * @author JeanPaul
 */
public class JSpreadConcurrencyManager extends Thread {

    @Override
    public void run() {
        try {
            //System.out.println("Iniciando CM");
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            JSpreadConnectionPool jscp = null;
            jscp = JSpreadConnectionPool.getSingleInstance();
            int priority = jscp.getPriority();
            int priorityTimeOut = jscp.getPriorityTimeOut();
            int timeOut = 0;

            while (jscp.isIsTransInUse()) {
                //Thread.sleep(priority);
                timeOut = timeOut + priority;
                if (timeOut > priorityTimeOut) {
                    priority = 1;
                    //break;
                }
                //System.out.println("En espera: "+jscp.isIsTransInUse());
            }

            //System.out.println("Finalizado CM");
        } catch (Exception ex) {
            Logger.getLogger(JSpreadConcurrencyManager.class.getName()).log(Level.SEVERE, null, ex);
            //throw new Exception("My one time exception with some message!");
            //throw new MyCustomException( ... );
        }
    }
}
