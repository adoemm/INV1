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

public class leerArchivo extends HttpServlet {

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
            abrirArchivo(session, request, response, quid, out);
        } finally {
            quid = null;
            out.close();
        }
    }

    private void abrirArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        Iterator it = null;
        LinkedList listAux = null;
        String msg = "";
        try {
            
            if (request.getContentLength() > 52428800000L) {
                msg = ("ERROR - El tamaño maximo de archivo es de 50Mb");
                out.println(msg);
            } else if(request.getContentLength()<=0){
                 msg = ("ERROR - Seleccione un archivo");
                out.println(msg);
               
            }
            else {
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (!isMultipart) {
                    
                    msg = ("ERROR - Tipo de forma incorrecta");
                    out.println(msg);
                   
                } else {
                    String filePath = getServletContext().getRealPath("/") + "generatedFiles\\";
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    List items = null;
                    try {
                        items = upload.parseRequest(request);
//                        if(items.isEmpty() || items==null ){
//                            System.out.println("item vacio");
//                        }
                    } catch (FileUploadException ex) {
                        //System.out.println("EX en subirExcel: " + ex);
                        msg = ("ERROR - No se ha podido guardar el archivo");
                        ex.printStackTrace();
                    }
                    Iterator i = items.iterator();
                    while (i.hasNext()) {
                        FileItem item = (FileItem) i.next();
                        if (item.isFormField()) {
                            String name = item.getFieldName();
                            String value = item.getString();
                        } else {
                            String fieldName = item.getFieldName();
                            String fileName = item.getName();
                            boolean isInMemory = item.isInMemory();
                            long sizeInBytes = item.getSize();
                            String nombre = "layout" + UTime.getDateString(Calendar.getInstance());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=No se ha podido guardar la base de datos." + ex.getMessage() + "&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
        }
    }
private void validaRequest(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
    
    
}
//    private void guardarBaseDatos(String pathFile, String nameFile, HttpServletRequest request, HttpServletResponse response, HttpSession session, QUID quid) {
//        try {
//            Iterator it = null;
//            LinkedList listAux = null;
//            ReadExcel excel = new ReadExcel();
//            Calendar c = Calendar.getInstance();
//            it = null;
//            File f = new File(pathFile + nameFile + ".xlsx");
//            LinkedList datos = excel.readFileExcel(f);
//            if (this.validaArchivo(datos, request, response, session)) {
//                it = datos.iterator();
//                if (it.hasNext()) {
//                    for (int j = 0; j < 7; j++) {
//                        it.next();
//                    }
//                }
//                int filasAfectadas = 0;
//                Transporter tport = null;
//                LinkedList transacciones = new LinkedList();
//                while (it.hasNext()) {
//                    listAux = (LinkedList) it.next();
//                    if (listAux.size() > 18) {
//                        String nombreContribuidor = StringUtil.convertir2CadenaSat(listAux.get(1).toString().trim(), 40);
//                        String rfc = listAux.get(5).toString();
//
//                        //String campo8 = StringUtil.getRoudDobule2Int(listAux.get(10).toString().trim());
////                        String campo9 = StringUtil.getRoudDobule2Int(listAux.get(11).toString().trim());
////                        String campo13 = StringUtil.getRoudDobule2Int(listAux.get(12).toString().trim());
////                        String campo15 = StringUtil.getRoudDobule2Int(listAux.get(13).toString().trim());
//                        String campo8=!listAux.get(10).toString().trim().equals("")?StringUtil.getRoudDobule2Int(listAux.get(10).toString().trim()):"0";
//                        String campo9=!listAux.get(11).toString().trim().equals("")?StringUtil.getRoudDobule2Int(listAux.get(11).toString().trim()):"0";
//                        String campo13=!listAux.get(12).toString().trim().equals("")?StringUtil.getRoudDobule2Int(listAux.get(12).toString().trim()):"0";
//                        String campo15=!listAux.get(13).toString().trim().equals("")?StringUtil.getRoudDobule2Int(listAux.get(13).toString().trim()):"0";
//                        
//                        Transporter t = null;
////                        if (!quid.exist_Contribuidor(rfc, "", nombreContribuidor, "1", "743")) {
////                            t = quid.insert_Contribuidor(rfc.trim().toUpperCase(), "", nombreContribuidor.trim(), "1", "743");
////                        }
//                        String ID_Contribuidor = quid.select_ID_Contribuidor(rfc, "", nombreContribuidor, "1", "743");
//                        if (!ID_Contribuidor.equals("")) {
//                            Calendar fecha = Calendar.getInstance();
//                            //System.out.println("fecha:" + UTime.ddmmaaaa2aaaa_mm_dd(listAux.get(7).toString()));
//                            //fecha.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(listAux.get(7).toString()));
//                            //fecha.setTimeInMillis(Long.parseLong(StringUtil.double2Long(listAux.get(7).toString())));
//                            tport = quid.IDinsert_Transacion(
//                                    ID_Contribuidor,
//                                    "3",
//                                    //      UTime.ddmmaaaa2aaaa_mm_dd(listAux.get(7).toString()),
//                                    //  UTime.calendar2SQLDateFormat(fecha),
//                                    listAux.get(7).toString(),
//                                    campo8,
//                                    campo9,
//                                    "0",
//                                    "0",
//                                    "0",
//                                    campo13,
//                                    "0",
//                                    campo15,
//                                    "0",
//                                    "0",
//                                    "0",
//                                    "0",
//                                    "0",
//                                    "0",
//                                    "0");
//                            String idTransaccion = tport.getMsg();
//                            if (!idTransaccion.equals("")) {
//                                transacciones.add(idTransaccion);
//                            }
//                            if (tport.getCode() == 0) {
//                                filasAfectadas += 1;
//                            }
//                        }
//
////                        String lineaLayout = "04|85|" + listAux.get(5) + "||" + listAux.get(1) + "|||" + listAux.get(10) + "|";
////                        lineaLayout += listAux.get(11) + "||||" + listAux.get(12) + "||" + listAux.get(13) + "||||||||" + "fecha:" + listAux.get(7).toString();
////                        System.out.println(lineaLayout);
//                    }
//                }
//                if (WebUtil.existAtrribute(session.getAttributeNames(), "Transaccion")) {
//                    session.removeAttribute("Transaccion");
//                }
//                session.setAttribute("Transaccion", transacciones);
//                this.getServletConfig().getServletContext().getRequestDispatcher(
//                        "" + PageParameters.getParameter("msgUtil")
//                        + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Registros afectados " + filasAfectadas + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
//            }
//        } catch (Exception ex) {
//            try {
//                Logger.getLogger(leerArchivo.class.getName()).log(Level.SEVERE, null, ex);
//                this.getServletConfig().getServletContext().getRequestDispatcher(
//                        "" + PageParameters.getParameter("msgUtil")
//                        + "/msgNRedirectFull.jsp?title=Error &type=error&msg=Ha ocurrido un error.  " + ex.getMessage() + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/LAbrirArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "").forward(request, response);
//            } catch (ServletException ex1) {
//                Logger.getLogger(leerArchivo.class.getName()).log(Level.SEVERE, null, ex1);
//            } catch (IOException ex1) {
//                Logger.getLogger(leerArchivo.class.getName()).log(Level.SEVERE, null, ex1);
//            }
//        }
//    }



    private boolean validaArchivo(LinkedList datos, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        boolean valido = false;
        int error = 0;
        Iterator t = datos.iterator();
        int fila = 7;
        if (t.hasNext() && datos.size()> 7) {
            for (int j = 0; j < 6; j++) {
                t.next();
            }
            LinkedList headers=(LinkedList) t.next();
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
            if(headers.size()<19 && !(headers.get(0).toString().trim().equalsIgnoreCase("#")
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
                    && headers.get(18).toString().trim().equalsIgnoreCase("TOTAL PAGADO"))
                    ){
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
            Logger.getLogger(leerArchivo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(leerArchivo.class.getName()).log(Level.SEVERE, null, ex);
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
