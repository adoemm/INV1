/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jspread.core.db.QUID;
import jspread.core.util.security.JHash;
//import org.apache.catalina.Session;

/**
 *
 * @author Marco
 */
public class Test {

    public static void main(String[] args) {

        QUID q = new QUID();
//        String obtenerIdReporte = "";
//        String idBien = "1";
//        String idPlantel = q.select_Bien4Campo("FK_ID_Plantel", idBien);
//        int totalRegistrosBitacora = q.select_CountID_BitacoraIncidente() +1;
//        Calendar c = new GregorianCalendar();
//        String annio;
//        annio = Integer.toString(c.get(Calendar.YEAR));
//        obtenerIdReporte = annio + idPlantel + idBien + "-" + totalRegistrosBitacora;
//        System.out.println("obtenerIdReporte" + obtenerIdReporte) ;

        //System.out.println("años:"+UTime.calculaAnios("1997-10-17"));
//     String curp="LOOM861017HMCPCR08";
//     if(curp.contains(StringUtil.generaCurp("Marco Antonio","Lopez","Octaviano","1986-10-17","HOMBRE","Estado de México"))){
//         System.out.println("valido:");
//     }else{
//         System.out.println("no valido:");
//     }
        // String s=" de la Rosa ";
        //System.out.println("año:"+UTime.getAnio("1986-10-17"));
        //System.out.println("curp:"+StringUtil.generaCurp("diana lizet","archundia","perez","1997-10-10","femenino","Estado de México"));
        //System.out.println("pass:"+StringUtil.generaPassword(6));
        //System.out.println("vocal:"+StringUtil.getConsonat("Lopez",2));
        //System.out.println("nombre:"+StringUtil.getNombrePila("christian misael"));
        // System.out.println("apellido:"+StringUtil.getNombreSinArticulos("de la cruz"));
        //q.select_Nacionalidad();
        // System.out.println(s.trim());
//       System.out.println("match:"+StringUtil.matchCurp("loom861017hmcpcrO8","marco antonio",
//               "lopez","octaviano","1986-10-17","Masculino","estado de méxico"));     
        //System.out.println("fecha: "+UTime.formatCalendar2dMMaa(Calendar.getInstance().getTime()));
//     System.out.println("periodo base: 12:45-13:30");
//     System.out.println("12:46-13:31 "+UTime.isTimeOverLap("12:45","13:30","12:46","13:31"));
//     System.out.println("12:44-13:29 "+UTime.isTimeOverLap("12:44","13:30","12:44","13:29"));
//     System.out.println("12:50-13:25 "+UTime.isTimeOverLap("12:50","13:30","12:50","13:25"));
//     System.out.println("12:51-13:15 "+UTime.isTimeOverLap("12:51","13:30","12:51","13:15"));
//     System.out.println("12:43-13:00 "+UTime.isTimeOverLap("12:43","13:30","12:43","13:00"));
//     System.out.println("10:46-12:31 "+UTime.isTimeOverLap("12:43","13:30","10:46","12:31"));
//     System.out.println("13:30-14:00 "+UTime.isTimeOverLap("12:43","13:30","13:30","14:00"));
//     System.out.println("limites:"+UTime.HourWithinUpClosePeriod("13:30", "12:43", "13:30"));
        //System.out.println("len:"+"11:00 - 12:00 16-LECTURA, EXPRESIÓN ORAL Y ESCRITA I PROF(A). MARIA INES BRUMILDA JARAMILLO DOMINGUEZ".length());
//     System.out.println(String.format("%15s - %s","10","quimica 2"));
//     System.out.println(String.format("%15s - %s","10","algebra 2"));
//     System.out.println(String.format("%15s - %s","101548979","matematicas 2"));
        //System.out.println(SystemUtil.getPeriodoSiguiente("2014-2015"));
        // System.out.println(JHash.getStringMessageDigest("51813n", JHash.MD5));
        String aux = "Hóla 7-!?/&üÑñ#|";
        for (int i = 0; i < aux.length(); i++) {
            System.out.println("\n\n" + aux.charAt(i));
            if (Character.isAlphabetic(aux.charAt(i))) {
                System.out.println("Alfbetico");
            }
            if (Character.isDigit(aux.charAt(i))) {
                System.out.println("Digito");
            }
            if (Character.isLetter(aux.charAt(i))) {
                System.out.println("letra");
            }
            if (!Character.isLetterOrDigit(aux.charAt(i))) {
                System.out.println("Simbolo");//el espacio es considerado como simbolo
            }
        }

        HashMap hm = new HashMap();
        //hm.keySet().co
        hm.put(9, 3);

//        Pattern p = Pattern.compile("(A|Z)");
//        Matcher m = p.matcher("3689ADNPQXW3");
//        while (m.find()) {
//            String matched = m.group(1);
//            System.out.println("Entro: " + matched);
//        }
//
//        boolean az = false;
//        boolean digit = false;
//        boolean simbol = false;
//
//        if (" ".matches(".[0-9]+")) {
//            digit = true;
//            System.out.println("Numerica");
//        } else if (" ".matches(".[A-Z]+")) {
//            digit = true;
//            System.out.println("Alfabetica");
//        } else if (" ".matches(".[a-zA-Z_0-9]+")) {
//            az = true;
//            System.out.println("Alfanumerica");
//        } else {
//            System.out.println("Simbolo");
//        }
        
        aux = "0"; 
        if (aux.matches("[0-9]+")) {
            System.out.println("Numerica");
        } else if (aux.matches("[a-zA-Z]+")) {
            System.out.println("Alfabetica");
        } else if (aux.matches("[a-zA-Z_0-9]+")) {
            System.out.println("Alfanumerica");
        }else{
            System.out.println("nada");
        }

    }

}
