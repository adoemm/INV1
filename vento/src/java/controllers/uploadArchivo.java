/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import jspread.core.db.QUID;
import jspread.core.models.Transporter;
import jspread.core.util.FileUtil;
import jspread.core.util.PageParameters;
import jspread.core.util.StringUtil;
import jspread.core.util.UTime;
import jspread.core.util.WebUtil;
import jspread.core.util.security.JHash;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import systemSettings.SystemSettings;

public class uploadArchivo extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        SystemSettings.ignition();
        request.setCharacterEncoding(PageParameters.getParameter("charset").toString());
        response.setCharacterEncoding(PageParameters.getParameter("charset").toString());
        response.setContentType(PageParameters.getParameter("servletSetContentType").toString());
        response.setHeader("Cache-Control", "no-cache");
        HttpSession session;
        PrintWriter out;
        QUID quid;
        session = request.getSession(true);
        out = response.getWriter();
        quid = new QUID();
        quid.setRequest(request);
        this.showAllParametersConsole(session, request, response, quid, out);
        try {
            uploadFile(session, request, response, quid, out);
        } finally {
            quid = null;
            out.close();
        }
    }

    private void uploadFile(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        Iterator it = null;
        LinkedList listAux = null;
        String msg = "";
        try {

            if (request.getContentLength() > 52428800000L) {
                msg = ("ERROR - El tamaño maximo de archivo es de 50Mb");
                out.println(msg);
            } else if (request.getContentLength() <= 0) {
                msg = ("ERROR - Seleccione un archivo");
                out.println(msg);
                //System.out.println("archivo vacio");
            } else {
                //boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (!ServletFileUpload.isMultipartContent(request)) {

                    msg = ("ERROR - Tipo de forma incorrecta");
                    out.println(msg);
                    //System.out.println("no es multipart");
                } else {
                    String filePath = getServletContext().getRealPath("/") + "//scanedFiles\\";
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    List items = null;
                    try {
                        items = upload.parseRequest(request);

                        Iterator i = items.iterator();
                        String idSoftware = "";
                        FileItem itemToUpload = null;
                        while (i.hasNext()) {
                            FileItem item = (FileItem) i.next();
                            if (item.isFormField()) {
                                idSoftware = WebUtil.decode(session, item.getString());
                            } else {
                                String fieldName = item.getFieldName();
                                String fileName = item.getName();
                                boolean isInMemory = item.isInMemory();
                                long sizeInBytes = item.getSize();
                                itemToUpload = item;
                            }
                        }
                        if (itemToUpload != null) {
                            String fechaUpload = UTime.getDateString(Calendar.getInstance());
                            String nombreSoftware=idSoftware + "_" + fechaUpload + FileUtil.getExtension(itemToUpload.getName());
                            File fileToUpload = new File(filePath,nombreSoftware );
                            itemToUpload.write(fileToUpload);
                            quid.update_SoftwareFile( nombreSoftware,idSoftware);
                            msg = ("El archivo se guardo correctamente");
                            out.print(msg);
                        }
                    } catch (FileUploadException ex) {
                        msg = ("ERROR - No se ha podido guardar el archivo");
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=No se ha podido guardar el archivo." + ex.getMessage() + "&url=" + PageParameters.getParameter("mainMenu") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        }
    }

    private void validaRequest(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
    }

   

    private boolean validaArchivo(LinkedList datos, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        boolean valido = false;
        int error = 0;
        Iterator t = datos.iterator();
        int fila = 7;
        if (t.hasNext() && datos.size() > 7) {
            for (int j = 0; j < 6; j++) {
                t.next();
            }
            LinkedList headers = (LinkedList) t.next();
//            System.out.println("validando encabezados:");
//            System.out.println(headers.get(0).toString().trim()+" |"
//                    +  headers.get(1).toString().trim()+" |"
//                    +  headers.get(2).toString().trim()+" |"
//                    +  headers.get(3).toString().trim()+" |"
//                    +  headers.get(4).toString().trim()+" |"
//                    +  headers.get(5).toString().trim()+" |"
//                    +  headers.get(6).toString().trim()+" |"
//                    +  headers.get(7).toString().trim()+" |"
//                    +  headers.get(8).toString().trim()+" |"
//                    +  headers.get(9).toString().trim()+" |"
//                    +  headers.get(10).toString().trim()+" |"
//                    +  headers.get(11).toString().trim()+" |"
//                    +  headers.get(12).toString().trim()+" |"
//                    +  headers.get(13).toString().trim()+" |"
//                    +  headers.get(14).toString().trim()+" |"
//                    +  headers.get(15).toString().trim()+" |"
//                    +  headers.get(16).toString().trim()+" |"
//                    +  headers.get(17).toString().trim()+" |"
//                    +  headers.get(18).toString().trim());
            if (headers.size() < 19 && !(headers.get(0).toString().trim().equalsIgnoreCase("#")
                    && headers.get(1).toString().trim().equalsIgnoreCase("NOMBRE DEL PROVEEDOR DE BIENES O SERVICIOS")
                    && headers.get(2).toString().trim().equalsIgnoreCase("CONCEPTO DEL BIEN O SERVICIO PAGADO.")
                    && headers.get(3).toString().trim().equalsIgnoreCase("CURP")
                    && headers.get(4).toString().trim().equalsIgnoreCase("DOMICILIO")
                    && headers.get(5).toString().trim().equalsIgnoreCase("R.F.C")
                    && headers.get(6).toString().trim().equalsIgnoreCase("NUMERO DE CHEQUE")
                    && headers.get(7).toString().trim().equalsIgnoreCase("FECHA DEL CHEQUE")
                    && headers.get(8).toString().trim().equalsIgnoreCase("FECHA DE COMPROBACION")
                    && headers.get(9).toString().trim().equalsIgnoreCase("NUMERO DE POLIZA")
                    && headers.get(10).toString().trim().equalsIgnoreCase("MONTO DE LA OPERACIÓN PAGADA (Sin incluir IVA)")
                    && headers.get(11).toString().trim().equalsIgnoreCase("VALOR DE LOS ACTOS O ACTIVIDADES PAGADOS (16%)")
                    && headers.get(12).toString().trim().equalsIgnoreCase("VALOR DE LOS ACTOS O ACTIVIDADES PAGADOS (EXENTOS)")
                    && headers.get(13).toString().trim().equalsIgnoreCase("TOTAL IVA PAGADO POR EL CONTRIBUYENTE")
                    && headers.get(14).toString().trim().equalsIgnoreCase("10% ISR")
                    && headers.get(15).toString().trim().equalsIgnoreCase("0.005")
                    && headers.get(16).toString().trim().equalsIgnoreCase("0.002")
                    && headers.get(17).toString().trim().equalsIgnoreCase("0.02")
                    && headers.get(18).toString().trim().equalsIgnoreCase("TOTAL PAGADO"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El formato del archivo no es correcto.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
                return false;
            }
        }
        while (t.hasNext()) {
            LinkedList aux = (LinkedList) t.next();
            fila += 1;
            //System.out.println("fila: "+fila+ " aux sixe:"+aux.size());
            if (aux.size() > 18) {
                //System.out.println("validando fila:"+fila);
//                System.out.println( aux.get(5).toString().trim()+  StringUtil.convertir2CadenaSat(aux.get(1).toString().trim(),  40)+ 
//                        aux.get(10).toString().trim()+ 
//                        aux.get(11).toString().trim()+ 
//                        aux.get(12).toString().trim()+ 
//                        aux.get(13).toString().trim() );
//                
                if (!this.validarDatos(
                        aux.get(5).toString().trim(), StringUtil.convertir2CadenaSat(aux.get(1).toString().trim(), 40),
                        aux.get(10).toString().trim(),
                        aux.get(11).toString().trim(),
                        aux.get(12).toString().trim(),
                        aux.get(13).toString().trim(),
                        fila, request, response, session)) {
                    error += 1;
                }
            }
        }
        if (error > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Se detectaron  " + error + " errores" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private boolean validarDatos(String rfc, String nombre,
            String campo8, String campo9,
            String campo13, String campo15, int fila,
            HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        boolean valido = false;
        if (rfc.equalsIgnoreCase("") || !StringUtil.validaRFCSat(rfc.trim())) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=RFC invalido. " + rfc + " Fila:" + fila + " Columna:6" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);

        } else if (nombre.equalsIgnoreCase("") || !StringUtil.validaCadenaSat(nombre) || nombre.length() > 40) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Nombre invalido. " + nombre + " Fila:" + fila + " Columna: 2" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);

        } else if (campo8.equals("") || !StringUtil.isValidDouble(campo8)) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Dato invalido. Campo 8:" + campo8 + " Fila:" + fila + " Columna:11" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);

        } else if (campo9.equals("") || !StringUtil.isValidDouble(campo9)) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Dato invalido. Campo 9:" + campo9 + " Fila:" + fila + " Columna:12" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        } else if (campo13.equals("") || !StringUtil.isValidDouble(campo13)) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Dato invalido. Campo 13:" + campo13 + " Fila:" + fila + " Columna:13" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        } else if (campo15.equals("") || !StringUtil.isValidDouble(campo15)) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Dato invalido. Campo 15:" + campo15 + " Fila:" + fila + " Columna:14" + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void showAllParameters(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        //--------------------------------------------------------------
        //herramienta para diagnostico y visualizacion de parametros
        out.println("<br><br>Parametros:");
        Enumeration enu = request.getParameterNames();
        String param = "";
        while (enu.hasMoreElements()) {
            param = enu.nextElement().toString();

            out.println("<br>" + param + " = " + request.getParameter(param));
            System.out.println(param + " = " + request.getParameter(param));
        }
        out.println("<br><br>");
        //--------------------------------------------------------------
    }

    private void showAllParametersConsole(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        //--------------------------------------------------------------
        Enumeration enu = request.getParameterNames();
        String param = "";
        while (enu.hasMoreElements()) {
            param = enu.nextElement().toString();
            System.out.println(param + " = " + request.getParameter(param));
        }
        //--------------------------------------------------------------
    }

    private void clearNCloseSession(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String param = "";
        Enumeration enu = session.getAttributeNames();
        while (enu.hasMoreElements()) {
            param = enu.nextElement().toString();
            session.setAttribute(param, null);
            session.removeAttribute(param);
        }
        session.invalidate();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(uploadArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(uploadArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
