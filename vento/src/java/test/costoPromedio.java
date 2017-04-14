/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Scanner;

/**
 *
 * @author emmanuel
 */
public class costoPromedio {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {

            costoPromedio cp = new costoPromedio();
            System.out.println("Evaluacion de Costo Promedio por inventario");
            Scanner leer = new Scanner(System.in);
            System.out.println("Introducir cantidad en Inventario: ");
            int cantidadInventario = leer.nextInt();
            System.out.println("Introducir costo en Inventario: ");
            double costoUnidadInventario = leer.nextDouble();
            System.out.println("Introducir cantidad de Entrada: ");
            int cantidadEntrada = leer.nextInt();
            System.out.println("Introducir costo de Entrada: ");
            double costoUnidadEntrada = leer.nextDouble();

            System.out.println("El costo promedio del producto es de:" + cp.costoPromedioxInventario(cantidadInventario, costoUnidadInventario, cantidadEntrada, costoUnidadEntrada));
        }
    }

    public double costoPromedioxInventario(int cantidadInventario, double costoUnidadInventario, int cantidadEntrada, double costoUnidadEntrada) {

        double costoTotalInventario = 0;
        double costoTotalEntrada = 0;
        double costoPromedio = 0;
        costoTotalInventario = cantidadInventario * costoUnidadInventario;
        costoTotalEntrada = cantidadEntrada * costoUnidadEntrada;
        costoTotalInventario = costoTotalEntrada + costoTotalInventario;
        cantidadInventario = cantidadEntrada + cantidadInventario;
        costoPromedio = redondearDecimales(costoTotalInventario / cantidadInventario, 2);
        return costoPromedio;
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        return resultado;
    }
}
