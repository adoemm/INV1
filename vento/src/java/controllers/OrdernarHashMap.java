/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Administrator
 */
public class OrdernarHashMap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        final boolean order = true;
        try {
            Map<String, Integer> map = new TreeMap(
                    new Comparator<Integer>() {
                @Override
                public int compare(Integer first, Integer second) {
                    if (order) {
                        return second.compareTo(first);
                    } else {
                        return first.compareTo(second);
                    }
                }
            });

            map.put("v",2);
            map.put("h",3);
            map.put("e",4);
            map.put("a",1);

            System.out.println(map.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
