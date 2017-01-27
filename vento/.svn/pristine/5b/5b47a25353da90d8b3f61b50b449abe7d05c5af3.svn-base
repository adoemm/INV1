package jspread.core.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import jspread.core.db.QUID;

/**
 *
 * @author JeanPaul
 */
public class ReadZIP {

    public static void readZip(String filename, String carpeta, String destFileName) {
        try {
//Nuestro OutputStream
            BufferedOutputStream dest = null;
//InputStream a partir del archivo ZIP a leer
            FileInputStream fis = new FileInputStream(filename);
//Obtenemos el checksum
            CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
//Indicamos que será un archivo ZIP
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
            ZipEntry entry;
            //Configuramos el tamaño del buffer
            int BUFFER = 1 * 1024 * 1024;
//Iteramos por el contenido del ZIP
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.getName().equalsIgnoreCase("Thumbs.db")) {
                    System.out.println("Extrayendo del ZIP: " + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
// Escribimos los archivos en la ubicación deseada
                    FileOutputStream fos;
                    if (destFileName.equalsIgnoreCase("")) {
                        fos = new FileOutputStream(carpeta + entry.getName());
                    } else {
                        fos = new FileOutputStream(carpeta + destFileName + FileUtil.getExtension(entry.getName()));
                    }
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
            }
            zis.close();
            System.out.println("Checksum:" + checksum.getChecksum().getValue());
        } catch (Exception ex) {
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String filename = "C:/basesce/CECYTEMR.zip";
        String carpeta = "C:/basesce/";
        readZip(filename, carpeta, "203");
    }
}
