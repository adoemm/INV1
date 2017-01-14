/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JeanPaul
 */
public class CommAPI {

    private static final String version = "V0.03";

    public static URL prepareURL(String protocol, String address, String port, String context, String internalSitePath) throws MalformedURLException {
        return new URL(protocol + "://" + address + ":" + port + "/" + context + "/" + internalSitePath);
    }

    public static URL prepareURL(String protocol, String address, String port, String context) throws MalformedURLException {
        return new URL(protocol + "://" + address + ":" + port + "/" + context);
    }

    public static URL addInternalSitePath(URL url, String internalSitePath) throws MalformedURLException {
        url = new URL(url.toString() + "/" + internalSitePath);
        return url;
    }

    public static String sendNGet(URL url, String requestMethod, String uAgent, String parameters) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
            con.setRequestProperty("User-Agent", uAgent);

            con.setDoOutput(true);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                if (response.toString().equals("error")) {
                    return null;
                } else {
                    return response.toString();
                }
            } else {
                return "Error " + responseCode;
            }
        } catch (Exception ex) {
            Logger.getLogger(CommAPI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static LinkedList dataToLinkedList(String data) {
        try {
            LinkedList table = new LinkedList();
            String row[] = data.split("</tr->");
            String col[];
            for (int i = 1; i < row.length; i++) {
                LinkedList rowData = new LinkedList();
                col = row[i].split("</td->");
                for (int j = 1; j < col.length; j++) {
                    rowData.add(col[j]);
                }
                table.add(rowData);
            }
            //System.out.println("" + table.get(7));
            return table;
        } catch (Exception ex) {
            Logger.getLogger(CommAPI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static LinkedList sendNGetFull(
            URL url,
            String internalSitePath,
            String decodePassword,
            String requestMethod,
            String uAgent,
            String parameters) throws Exception {
        try {
            url = CommAPI.addInternalSitePath(url, internalSitePath);
            String data = CommAPI.sendNGet(url, requestMethod, uAgent, parameters);
            data = WebUtil.decode(decodePassword, data);
            if (data.equalsIgnoreCase("")) {
                return new LinkedList();
            } else {
                return CommAPI.dataToLinkedList(data);
            }
        } catch (Exception ex) {
            Logger.getLogger(CommAPI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String prepareParameters(HashMap<String, String> paramaters) {
        StringBuilder prepared = new StringBuilder();
        Collection<?> keys = paramaters.keySet();
        boolean first = true;
        for (Object key : keys) {
            //System.out.println("Key " + key);
            //System.out.println("Value " + paramaters.get(key));
            if (!first) {
                prepared.append("&");
                prepared.append(key);
                prepared.append("=");
                prepared.append(paramaters.get(key));
            } else {
                first = false;
                prepared.append(key.toString());
                prepared.append("=");
                prepared.append(paramaters.get(key));
            }
        }

        return prepared.toString();
    }
}
