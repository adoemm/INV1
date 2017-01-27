/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author marco
 */
public final class PatronUtil {

    //Esta es la version de esta clase
    private static final String version = "V0.1";

    public static String getVersion() {
        return version;
    }

    /**
     * <p>
     * Metodo - Confirma si un caracter forma parte del 0 al 9
     *
     * <p>
     *
     * @param char c - caracter
     * @return boolean - "true" si si, "false" si no
     */
    public static String convertir2CadenaSat(String cadena, int longitud) {
        cadena = cadena.replace("@", "");
        cadena = cadena.replace("@", "");
        cadena = cadena.replace("\'", "");
        cadena = cadena.replace("%", "");
        cadena = cadena.replace("!", "");
        cadena = cadena.replace("¡", "");
        cadena = cadena.replace(".", "");
        cadena = cadena.replace("$", "");
        cadena = cadena.replace("&", "");
        cadena = cadena.replace("¿", "");
        cadena = cadena.replace("?", "");
        cadena = cadena.replace("Ñ", "");
        cadena = cadena.replace("ñ", "");
        if (cadena.length() > longitud) {
            cadena = cadena.replace(" ", "");
            if (cadena.length() > longitud) {
                cadena = cadena.substring(0, longitud);
            }
        }
        return cadena;
    }

    public static boolean validaCadenaSat(String nombre) {
        boolean valido = true;
        for (int i = 0; i < nombre.length(); i++) {
            if (nombre.charAt(i) == '@' || nombre.charAt(i) == '\'' || nombre.charAt(i) == '%'
                    || nombre.charAt(i) == '!' || nombre.charAt(i) == '¡'
                    || nombre.charAt(i) == '.' || nombre.charAt(i) == '$'
                    || nombre.charAt(i) == '&' || nombre.charAt(i) == '¿'
                    || nombre.charAt(i) == '?' || nombre.charAt(i) == 'Ñ') {
                valido = false;
            }
        }
        return valido;
    }

    public static LinkedList calcPatronesMuestra(LinkedList serials, float porcetajePrecisionMuestra, int porcentajePrecicionHit) {
        int tamyMuestra = serials.size();
        int precisionHit = 1;
        //int precisionHit = (tamyMuestra * porcetajePrecisionMuestra) / 100;
        LinkedList hitsEstadistica = new LinkedList();
        Multimap<String, String> patronMap = ArrayListMultimap.create();
        Multimap<String, String> orderedSerialesPorLongitud = ArrayListMultimap.create();
        HashMap mapLongitudes = new HashMap();
        HashMap mapLongitudesSize = new HashMap();
        Iterator itAux = serials.iterator();
        String serial = "";
        LinkedList patrones = new LinkedList();
        while (itAux.hasNext()) {
            serial = itAux.next().toString();
            orderedSerialesPorLongitud.put(String.valueOf(serial.length()), serial);
        }
//        System.out.println("Precicion Hit: " + porcentajePrecicionHit);
//        System.out.println("Tamaño de la muestra: " + tamyMuestra);
//        System.out.println("Posibles longitudes" + orderedSerialesPorLongitud.keySet());

        HashMap<String, Integer> hits = new HashMap();
        Iterator it = null;
        Iterator it2 = null;
        String aux = "";
        String aux2 = "";
        String key = "";
        String longitudSerial = "";
        int maxLong = 1;

        Long init = UTime.getTimeMilis();

        Iterator itOrderedSerialesPorLongitud = orderedSerialesPorLongitud.keySet().iterator();
        //while para iterar las posibles longitudes
        while (itOrderedSerialesPorLongitud.hasNext()) {
            longitudSerial = itOrderedSerialesPorLongitud.next().toString();
            maxLong = Integer.parseInt(longitudSerial);
            Collection serialesPorLongitud = orderedSerialesPorLongitud.get(longitudSerial);
            precisionHit = (serialesPorLongitud.size() * porcentajePrecicionHit) / 100;
            mapLongitudesSize.put(maxLong, serialesPorLongitud.size());
            //for para iterar las posiciones de las columnas
            if (serialesPorLongitud.size() >= tamyMuestra * porcetajePrecisionMuestra) {
                for (int i = 0; i < maxLong; i++) {
                    hits = new HashMap();
                    it = serialesPorLongitud.iterator();
                    aux = "";
                    aux2 = "";
                    key = "";

                    //while para iterar todos los seriales
                    while (it.hasNext()) {
                        aux = it.next().toString();
                        key = new StringBuilder().append(String.valueOf(aux.charAt(i))).append(String.valueOf(maxLong)).toString();
                        if (!hits.keySet().contains(key)) {
                            hits.put(key, 0);
                        }
                        it2 = serialesPorLongitud.iterator();
                        //while para iterar serial x vs n
                        b:
                        while (it2.hasNext()) {
                            aux2 = it2.next().toString();
                            if (aux.charAt(i) == aux2.charAt(i)) {
                                hits.put(key, hits.get(key) + 1);
                                break b;
                            }
                        }
                    }
//        Long finals = UTime.getTimeMilis();
//        System.out.println("final"+finals);
//        System.out.println("tiempos" + (finals - init) / 1000);

                    //System.out.println("Longitud:" + maxLong + " Posicion: " + i + " cuenta:" + hits.size() + " Caracter|posicion:" + hits.keySet());
                    Iterator it3 = hits.keySet().iterator();
                    String aux3 = "";
                    while (it3.hasNext()) {
                        aux3 = it3.next().toString();
                        int nHits = hits.get(aux3);
                        if (nHits >= precisionHit) {
                            LinkedList row = new LinkedList();
                            row.add(aux3.substring(1));//longitud
                            row.add(i);//posicion
                            row.add(StringUtil.isTipoDatoChar("" + aux3.charAt(0)));//tipo de dato
                            row.add(aux3.charAt(0));//caracter
                            row.add(hits.get(aux3));//no de hits                                                            
                            hitsEstadistica.add(row);
                            if (!mapLongitudes.containsKey(aux3.substring(1))) {
                                mapLongitudes.put(aux3.substring(1), aux3.substring(1));
                            }
                            patronMap.put(aux3.substring(1) + "|" + i, "" + aux3.charAt(0));
                        }
                    }
                }
            }
        }

//        Iterator it4 = hitsEstadistica.iterator();
//        while (it4.hasNext()) {
//            LinkedList auxEstadisticas = (LinkedList) it4.next();
//            System.out.println("Longitud: " + auxEstadisticas.get(0) + " Posicion: " + auxEstadisticas.get(1) + " TD: " + auxEstadisticas.get(2) + " caracter: " + auxEstadisticas.get(3) + " noHits: " + auxEstadisticas.get(4));
//        }
//        Iterator itPatronMap = patronMap.keySet().iterator();
//        while (itPatronMap.hasNext()) {
//            System.out.println("" + patronMap.get(itPatronMap.next().toString()));
//        }
//        System.out.println("Longitudes tomadas para hacer patron: " + mapLongitudes.keySet());
        Iterator itMapLongitudes = mapLongitudes.keySet().iterator();
        while (itMapLongitudes.hasNext()) {
            int longitud = Integer.parseInt(itMapLongitudes.next().toString());
            String patronChar = "";
            String generica = "";
            int i = 0;
            long coeficientePrecision = 1;
            long coeficientePrecisionGenerico = 1;
            for (; i < longitud; i++) {
                String[] auxPatron = PatronUtil.tipoDatoPatron(Arrays.stream(patronMap.get(longitud + "|" + i).toArray()).toArray(String[]::new));
                if (patronMap.get(longitud + "|" + i).toArray().length >= 7) {
                    patronChar = patronChar + auxPatron[0];
                    coeficientePrecision = coeficientePrecision * Long.parseLong(auxPatron[1]);
                } else {
//                    if (patronMap.get(longitud + "|" + i).toString().equalsIgnoreCase("[O, 0]") || patronMap.get(longitud + "|" + i).toString().equalsIgnoreCase("[0, O]")) {
//                        patronChar = patronChar + "[0]";
//                        coeficientePrecision = coeficientePrecision * 1;
//                    } else {
                    if (patronMap.get(longitud + "|" + i).toArray().length > 0) {
                        patronChar = patronChar + patronMap.get(longitud + "|" + i).toString();
                        coeficientePrecision = coeficientePrecision * patronMap.get(longitud + "|" + i).toArray().length;
                    } else {
                        patronChar = patronChar + "[0-9a-z]";
                        coeficientePrecision = coeficientePrecision * 1;
                    }
//                    }
                }
                generica = generica + auxPatron[0];
                coeficientePrecisionGenerico = coeficientePrecisionGenerico + Long.parseLong(auxPatron[1]);
            }
            //System.out.println("" + mapLongitudesSize);     
//            System.out.println("" + coeficientePrecision);
            coeficientePrecision = coeficientePrecision / Integer.parseInt(mapLongitudesSize.get(longitud).toString());
//            System.out.println("" + coeficientePrecision);
            coeficientePrecisionGenerico = coeficientePrecisionGenerico * coeficientePrecision;

//            generica = coeficientePrecisionGenerico + "->+<- " + generica;
//            patronChar = coeficientePrecision + "->+<- " + patronChar;
//            System.out.println("Longitud: " + longitud + " Patron:" + patronChar);
//            System.out.println("Longitud: " + longitud + " Patron Generico:" + generica);
            LinkedList auxPatrones = new LinkedList();
            auxPatrones.add(patronChar);
            auxPatrones.add(longitud);
            auxPatrones.add("particular");
            auxPatrones.add(coeficientePrecision);
            patrones.add(auxPatrones);

            LinkedList auxPatronesGenerica = new LinkedList();
            auxPatronesGenerica.add(generica);
            auxPatronesGenerica.add(longitud);
            auxPatronesGenerica.add("generica");
            auxPatronesGenerica.add(coeficientePrecisionGenerico);
            patrones.add(auxPatronesGenerica);
        }
        return patrones;
    }

    public static String[] tipoDatoPatron(String[] chacteres) {
        String[] tipo = {"_", "60"};
        boolean az = false;
        boolean digit = false;
        String aux = "";

        for (int i = 0; i < chacteres.length; i++) {
            aux = aux + chacteres[i];
        }

        aux = aux.toUpperCase();
        //System.out.println("chars: " + aux);
        if (aux.matches("[0-9]+")) {
            tipo[0] = "[0-9]";
            tipo[1] = "10";
        } else if (aux.matches("[a-zA-Z]+")) {
            tipo[0] = "[a-z]";
            tipo[1] = "38";
        } else if (aux.matches("[a-zA-Z_0-9]+")) {
            if (aux.equalsIgnoreCase("o0") || aux.equalsIgnoreCase("0o")) {
                tipo[0] = "[0-9]";
                tipo[1] = "10";
            } else {
                tipo[0] = "[0-9a-z]";
                tipo[1] = "48";
            }
        }

        return tipo;
    }

    public static String limpiaPatron(String patron) {
        String patronLimpio = patron.replaceAll(",", "");
        patronLimpio = patronLimpio.replaceAll(" ", "");
        return patronLimpio;
    }

    public static HashMap calcHits5(LinkedList serials) {
//        for (int i = 0; i < serials.size(); i++) {
//            System.out.println("" + serials.get(i));
//        }
        HashMap<String, Integer> hits = new HashMap();
        Iterator it = null;
        Iterator it2 = null;
        String aux = "";
        String aux2 = "";
        String key = "";
        int longitud = 0;
        int maxLong = 1;
        Long init = UTime.getTimeMilis();
        //System.out.println("Init:"+init);
        for (int i = 0; i < maxLong; i++) {
            System.out.println("\n\n\n");
            it = serials.iterator();
            aux = "";
            aux2 = "";
            key = "";
            longitud = 0;

            while (it.hasNext()) {
                aux = it.next().toString();
                longitud = aux.length();
                key = new StringBuilder().append(String.valueOf(aux.charAt(i))).append(String.valueOf(longitud)).toString();
                if (!hits.keySet().contains(key)) {
                    hits.put(key, 0);
                }
                if (longitud > maxLong) {
                    maxLong = longitud;
                }
                it2 = serials.iterator();
                b:
                while (it2.hasNext()) {
                    aux2 = it2.next().toString();
                    //if (longitud == longitud2 && !aux.equalsIgnoreCase(aux2)) {
                    if (longitud == aux2.length()) {
                        if (aux.charAt(i) == aux2.charAt(0)) {
                            hits.put(key, hits.get(key) + 1);
                            //hits.put(key, Integer.parseInt(hits.get(key).toString()) + 1);
                            //System.out.println(aux.charAt(0) + ":" + hits.get(aux.charAt(0)));
                            break b;
                        }
                    }
                }
            }
//        Long finals = UTime.getTimeMilis();
//        System.out.println("final"+finals);
//        System.out.println("tiempos" + (finals - init) / 1000);

            System.out.println("" + hits.keySet());
            Iterator it3 = hits.keySet().iterator();
            String aux3 = "";
            while (it3.hasNext()) {
                aux3 = it3.next().toString();
                //System.out.println("aux3:"+aux3);
                System.out.println(aux3 + "=" + hits.get(aux3));
            }
        }
//        System.out.println("Diferencias: " + diferencias);
//        System.out.println("Total Opciones: " + serials.size() * serials.size());
//        System.out.println("Total muestra: " + serials.size());
//        System.out.println("Total diferencias: " + diferencias / serials.size());
//        System.out.println("Total iguales: " + Integer.parseInt("" + hits.get(aux3.charAt(0))) / serials.size());
        return hits;
    }

    public static HashMap calcHits4(LinkedList serials) {
        for (int i = 0; i < serials.size(); i++) {
            System.out.println("" + serials.get(i));
        }
        HashMap hits = new HashMap();
        Iterator it = serials.iterator();
        Iterator it2 = serials.iterator();
        String aux = "";
        String aux2 = "";
        int longitud = 0;
        int longitud2 = 0;
        int diferencias = 0;
        while (it.hasNext()) {
            aux = it.next().toString();
            longitud = aux.length();
            if (!hits.keySet().contains(aux.charAt(0))) {
                hits.put(aux.charAt(0), 0);
            }
            it2 = serials.iterator();
            b:
            while (it2.hasNext()) {
                aux2 = it2.next().toString();
                longitud2 = aux2.length();
                //if (longitud == longitud2 && !aux.equalsIgnoreCase(aux2)) {
                if (longitud == longitud2) {
                    if (aux.charAt(0) == aux2.charAt(0)) {
                        hits.put(aux.charAt(0), Integer.parseInt(hits.get(aux.charAt(0)).toString()) + 1);
                        //System.out.println(aux.charAt(0) + ":" + hits.get(aux.charAt(0)));
                        break b;
                    }
                }
                //} else {
                //    diferencias++;
                //}
            }
        }

        System.out.println("" + hits.keySet());
        Iterator it3 = hits.keySet().iterator();
        String aux3 = "";
        while (it3.hasNext()) {
            aux3 = it3.next().toString();
            System.out.println(aux3.charAt(0) + "=" + hits.get(aux3.charAt(0)));
        }
        System.out.println("Diferencias: " + diferencias);
        System.out.println("Total Opciones: " + serials.size() * serials.size());
        System.out.println("Total muestra: " + serials.size());
        System.out.println("Total diferencias: " + diferencias / serials.size());
        System.out.println("Total iguales: " + Integer.parseInt("" + hits.get(aux3.charAt(0))) / serials.size());

        return hits;
    }

    public static HashMap calcHits3(LinkedList serials) {
        for (int i = 0; i < serials.size(); i++) {
            System.out.println("" + serials.get(i));
        }
        HashMap hits = new HashMap();
        Iterator it = serials.iterator();
        Iterator it2 = serials.iterator();
        String aux = "";
        String aux2 = "";
        int longitud = 0;
        int longitud2 = 0;
        int diferencias = 0;
        while (it.hasNext()) {
            aux = it.next().toString();
            longitud = aux.length();
            if (!hits.keySet().contains(aux.charAt(0))) {
                hits.put(aux.charAt(0), 0);
            }
            it2 = serials.iterator();
            while (it2.hasNext()) {
                aux2 = it2.next().toString();
                longitud2 = aux2.length();
                if (longitud == longitud2 && !aux.equalsIgnoreCase(aux2)) {
                    if (aux.charAt(0) == aux2.charAt(0)) {
                        hits.put(aux.charAt(0), Integer.parseInt(hits.get(aux.charAt(0)).toString()) + 1);
                        System.out.println(aux.charAt(0) + ":" + hits.get(aux.charAt(0)));
                    }
                } else {
                    diferencias++;
                }
            }
        }

        System.out.println("" + hits.keySet());
        Iterator it3 = hits.keySet().iterator();
        String aux3 = "";
        while (it3.hasNext()) {
            aux3 = it3.next().toString();
            System.out.println(aux3.charAt(0) + "=" + hits.get(aux3.charAt(0)));
        }
        System.out.println("Diferencias: " + diferencias);
        System.out.println("Total Opciones: " + serials.size() * serials.size());
        System.out.println("Total muestra: " + serials.size());
        System.out.println("Total diferencias: " + diferencias / serials.size());
        System.out.println("Total iguales: " + Integer.parseInt("" + hits.get(aux3.charAt(0))) / serials.size());

        return hits;
    }

    public static HashMap calcHits2(LinkedList serials) {
        HashMap hits = new HashMap();
        for (int i = 0; i < serials.size(); i++) {
            PatronUtil.hitsPerSerial2(serials, serials.get(i).toString(), hits);
        }
        Iterator it = hits.keySet().iterator();
        String aux = "";
        while (it.hasNext()) {
            aux = it.next().toString();
            System.out.println(aux + "=" + hits.get(aux));
        }
        return hits;
    }

    public static void hitsPerSerial2(LinkedList serials, String serial, HashMap hits) {
        int longitud = serial.length();
        char base = '\0';
        char serialComparador = '\0';

        for (int i = 0; i < serial.length(); i++) {
            base = serial.charAt(i);
            serialComparador = '\0';
            if (!hits.keySet().contains(base)) {
                hits.put(base, 0);
            }
            for (int j = 0; j < serials.size(); j++) {
                if (serials.get(j).toString().length() == longitud) {
                    serialComparador = serials.get(j).toString().charAt(i);
                    if (base == serialComparador) {
                        hits.put(base, Integer.parseInt(hits.get(base).toString()) + 1);
                    }
                }
            }
        }
    }

    public static String getTipo(String cadena) {
        String result = "";
        if (cadena.contains("N") && cadena.contains("L") && cadena.contains("S")) {
            result = "";
        }
        return result;
    }

    public static boolean StringMatchWithPattern(String patron, String cadena) {
        boolean match = false;
        try {
            Pattern p = Pattern.compile(patron);
            Matcher m = p.matcher(cadena);
            if (m.matches()) {
                match = true;
            }
        } catch (Exception e) {
            match = false;
        }
        return match;
    }

}
