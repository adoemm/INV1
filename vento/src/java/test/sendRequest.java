/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import jspread.core.util.CommAPI;
import jspread.core.util.WebUtil;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author JeanPaul
 */
public class sendRequest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        URL url = CommAPI.prepareURL("http", "127.0.0.1", "8084", "deo/CommAPI");
        HashMap<String, String> paramaters = new HashMap();
        paramaters.put("userSystem", "SIAPSystem");
        paramaters.put("nomEscuela", "benito");
                
        String prepared = CommAPI.prepareParameters(paramaters);
        LinkedList ll = CommAPI.sendNGetFull(url, "/testCOM.jsp", "SuperPasswordForSiapSystem8579", "POST", "Mozilla/5.0", prepared);

        System.out.println("" + ll.size());
        System.out.println("" + ll.get(20));

        /*
         URL url = CommAPI.prepareURL("http", "127.0.0.1", "8084", "deo/CommAPI", "testCOM.jsp");
         //System.out.println("" + CommAPI.sendNGet(url, "POST", "Mozilla/5.0", ""));
         String data = CommAPI.sendNGet(url, "POST", "Mozilla/5.0", "");
         data = WebUtil.decode("SuperPasswordForSiapSystem8579", data);
         //System.out.println("" + data);
         CommAPI.dataToLinkedList(data);
         */

        //System.out.println("" + new String(Base64.decodeBase64(CommAPI.sendNGet(url, "POST", "Mozilla/5.0", "").getBytes()), "UTF8"));
        //LinkedList response = new LinkedList(Arrays.asList());
        //LinkedList<String> listAux = (LinkedList<String>) response.get(0);
        //System.out.println("" + response.get(0));
    }
}
