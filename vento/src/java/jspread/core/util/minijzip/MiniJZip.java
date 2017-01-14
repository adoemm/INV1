/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minijzip;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author JeanPaul
 */
public class MiniJZip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Los siguientes comandos son para ser ejecutados desde la linea de comandos del sistema operativo CMD o Terminal
        //El sistema al decomprimir no borra ni archivos ni carpetas al descomprimir
        //*EL sistema no adminte caracteres epeciales como la letra "Ñ" "ñ", pero si adminite acentos
        //java -jar miniJZip.jar StringFileTarget StringFileDirectory
        //java -jar "C:\Users\JeanPaul\Documents\NetBeansProjects\miniJZip\dist\miniJZip.jar" "C:/Users/JeanPaul/Downloads/dirzip.zip" "C:/Users/JeanPaul/Downloads/dir1/"

//File theFile = new File("C:/Users/JeanPaul/Downloads/dirzip.zip"), targetDir = new File("C:/Users/JeanPaul/Downloads/dir1/");
        File theFile = new File(args[0]), targetDir = new File(args[1]);
        UtilZip.unpackArchive(theFile, targetDir);
    }

}
