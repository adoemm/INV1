/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jspread.core.db.QUID;
import jspread.core.models.Transporter;
import jspread.core.util.FileUtil;
import jspread.core.util.PageParameters;
import jspread.core.util.PatronUtil;
import jspread.core.util.SessionUtil;
import jspread.core.util.StringUtil;
import jspread.core.util.SystemUtil;
import jspread.core.util.UTime;
import jspread.core.util.UserUtil;
import jspread.core.util.WebUtil;
import jspread.core.util.security.JHash;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import systemSettings.SystemSettings;

/**
 *
 * @author JeanPaul
 */
public final class controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        //response.addHeader("Cache-control", "no-store");
        //response.addHeader("Cache-control", "max-age=0");
        //response.setHeader("Expires", "0");
        //response.setHeader("Pragma", "No-cache");

        HttpSession session;
        PrintWriter out;
        QUID quid;
        LinkedList<String> userAccess;

        session = request.getSession(true);
        SessionUtil.addIfNotExistSession(session);
        out = response.getWriter();

        quid = new QUID();
        quid.setRequest(request);

        //this.showAllParametersConsole(session, request, response, quid, out);
        //this.showAllParametersConsoleDecode(session, request, response, quid, out);
        try {
            try {
                if (PageParameters.getParameter("SiteOnMaintenance").equals("true")) {
                    //System.out.println("Enviando a manto");
                    response.sendRedirect(PageParameters.getParameter("SiteOnMaintenanceURL").toString());
                } else // <editor-fold defaultstate="collapsed" desc="Realizando LogIn de usuario">
                //si proviene de la página  de login aqui se detectara y se validara al usuario
                 if (request.getParameter("LogInPage") != null) {
                        //aqui va la consulta a base de datos (quid) para que valide el usuario
                        if (request.getParameter("captcha").equals(session.getAttribute("captcha")) && request.getParameter("captcha").equalsIgnoreCase("") == false) {
                            if (request.getParameter("user").equalsIgnoreCase("") == false && request.getParameter("pass").equalsIgnoreCase("") == false) {
                                //System.out.println(""+JHash.getStringMessageDigest(""+request.getParameter("pass"), JHash.MD5));
                                LinkedList infoUser = null;
                                LinkedList<String> accessos = null;
                                String tipoRol = "";

                                infoUser = quid.select_idUsuario(request.getParameter("user"), request.getParameter("pass"), "1");
                                tipoRol = "otro";

                                if (infoUser != null) {
                                    session.removeAttribute("captcha");
                                    SessionUtil.clearNCloseSession(session);
                                    session = request.getSession(true);
                                    session.setAttribute("tipoRol", infoUser.get(3));
                                    session.setAttribute("userID", infoUser.get(0));
                                    session.setAttribute("userName", infoUser.get(1));
                                    session.setAttribute("FK_ID_Plantel", infoUser.get(2));
                                    //asignacion de permisos
                                    //LinkedList<String> accessos = new LinkedList();
                                    accessos = quid.select_permisosPorUsuarios(infoUser.get(0).toString());
                                    session.setMaxInactiveInterval(3 * 60 * 60); // 2hrs * 60 min * 60 seg
                                    accessos.add("LoggedUser");
                                    //accesos del usaurio y parametros del mismo
                                    session.setAttribute("userAccess", accessos);
                                    SessionUtil.addIfNotExistSession(session);
                                    request.getRequestDispatcher(PageParameters.getParameter("mainMenuServLet")).forward(request, response);
                                } else {
                                    this.getServletConfig().getServletContext().getRequestDispatcher(
                                            "" + PageParameters.getParameter("msgUtil")
                                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usuario o password incorrectos.&url=" + PageParameters.getParameter("mainMenu") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                                }
                            } else {
                                this.getServletConfig().getServletContext().getRequestDispatcher(
                                        "" + PageParameters.getParameter("msgUtil")
                                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El usuario y password no pueden estar vacíos.&url=" + PageParameters.getParameter("mainMenu") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                            }
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Código de verificación incorrecto.&url=" + PageParameters.getParameter("mainMenu") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                        }
                        // </editor-fold> 
                        // <editor-fold defaultstate="expanded" desc="Validando que sea un usuario logeado">
                    } else if (session.getAttribute("userAccess") == null) {
                        response.sendRedirect(PageParameters.getParameter("mainContext") + PageParameters.getParameter("LogInPage"));
                        // </editor-fold> 
                    } else // <editor-fold defaultstate="collapsed" desc="Cerrando sesion">
                     if (request.getParameter("exit") != null) {
                            //session.invalidate();
                            this.clearNCloseSession(session, request, response, quid, out);;
                            //quid.insertLog("SysLogOut", "exit", "", "", "", "");
                            response.sendRedirect(PageParameters.getParameter("mainContext") + PageParameters.getParameter("LogInPage").toString());
                            // </editor-fold>
                            // <editor-fold defaultstate="collapsed" desc="Revisando de que form viene">
                        } else if (request.getParameter("FormFrom") != null) {
                            //indice de metodos usados
                            switch (request.getParameter("FormFrom")) {
                                case "nuevoPermiso":
                                    this.insertarPermiso(session, request, response, quid, out);
                                    break;
                                case "actualizarPermiso":
                                    this.actualizarPermiso(session, request, response, quid, out);
                                    break;
                                case "deleteSoftware":
                                    this.deleteSoftware(session, request, response, quid, out);
                                    break;
                                case "updateSoftware":
                                    this.updateSoftware(session, request, response, quid, out);
                                    break;
                                case "nuevoUsuario":
                                    this.insertarUsuario(session, request, response, quid, out);
                                    break;
                                case "insertMarca":
                                    this.InsertMarca(session, request, response, quid, out);
                                    break;
                                case "deleteMarca":
                                    this.deleteMarca(session, request, response, quid, out);
                                    break;
                                case "actualizarMarca":
                                    this.actualizarMarca(session, request, response, quid, out);
                                    break;
                                case "insertModelo":
                                    this.insertModelo(session, request, response, quid, out);
                                    break;
                                case "deleteModelo":
                                    this.deleteModelo(session, request, response, quid, out);
                                    break;
                                case "updateModelo":
                                    this.updateModelo(session, request, response, quid, out);
                                    break;
                                case "insertTipo_Compra":
                                    this.insertTipo_Compra(session, request, response, quid, out);
                                    break;
                                case "deleteTipo_Compra":
                                    this.deleteTipo_Compra(session, request, response, quid, out);
                                    break;
                                case "updateTipo_Compra":
                                    this.updateTipo_Compra(session, request, response, quid, out);
                                    break;
                                case "insertTipo_Garantia":
                                    this.insertTipo_Garantia(session, request, response, quid, out);
                                    break;
                                case "deleteTipoGarantia":
                                    this.deleteTipoGarantia(session, request, response, quid, out);
                                    break;
                                case "updateTipo_Garantia":
                                    this.updateTipo_Garantia(session, request, response, quid, out);
                                    break;
                                case "insertLicencia":
                                    this.insertLicencia(session, request, response, quid, out);
                                    break;
                                case "updateLicencia":
                                    this.updateLicencia(session, request, response, quid, out);
                                    break;
                                case "deleteLicencia":
                                    this.deleteLicencia(session, request, response, quid, out);
                                    break;
                                case "insertTipo_Software":
                                    this.insertTipo_Software(session, request, response, quid, out);
                                    break;
                                case "updateTipo_Software":
                                    this.updateTipo_Software(session, request, response, quid, out);
                                    break;
                                case "deleteTipo_Software":
                                    this.deleteTipo_Software(session, request, response, quid, out);
                                    break;
                                case "insertCategoria":
                                    this.insertCategoria(session, request, response, quid, out);
                                    break;
                                case "deleteCategoria":
                                    this.deleteCategoria(session, request, response, quid, out);
                                    break;
                                case "updateCategoria":
                                    this.updateCategoria(session, request, response, quid, out);
                                    break;
                                case "insertSubCategoria":
                                    this.insertSubCategoria(session, request, response, quid, out);
                                    break;
                                case "updateDepatamento_Plantel":
                                    this.updateDepatamento_Plantel(session, request, response, quid, out);
                                    break;
                                case "insertGrupo":
                                    this.insertGrupo(session, request, response, quid, out);
                                    break;
                                case "deleteGrupo":
                                    this.deleteGrupo(session, request, response, quid, out);
                                    break;
                                case "updateGrupo":
                                    this.actualizarGrupo(session, request, response, quid, out);
                                    break;
                                case "insertSoftware":
                                    this.insertSoftware(session, request, response, quid, out);
                                    break;
                                case "deleteSubCategoria":
                                    this.deleteSubCategoria(session, request, response, quid, out);
                                    break;
                                case "updateSubCategoria":
                                    this.updateSubCategoria(session, request, response, quid, out);
                                    break;
                                case "borrarPermiso":
                                    this.borrarPermiso(session, request, response, quid, out);
                                    break;
                                case "insertDepartamento":
                                    this.insertDepartamento(session, request, response, quid, out);
                                    break;
                                case "deleteDepartamento":
                                    this.deleteDepartamento(session, request, response, quid, out);
                                    break;
                                case "deleteDepartamentoPlantel":
                                    this.deleteDepartamentoPlantel(session, request, response, quid, out);
                                    break;
                                case "updateDepartamento":
                                    this.updateDepartamento(session, request, response, quid, out);
                                    break;
                                case "nuevoRol":
                                    this.insertarNuevoRol(session, request, response, quid, out);
                                    break;
                                case "editarUsuario":
                                    this.actualizarUsuario(session, request, response, quid, out);
                                    break;
                                case "updatePassUsuario":
                                    this.actualizarPassUsuario(session, request, response, quid, out, true);
                                    break;
                                case "editarRol":
                                    this.editarRolUsuario(session, request, response, quid, out);
                                    break;
                                case "EliminaRol":
                                    this.borrarRolUsuario(session, request, response, quid, out);
                                    break;
                                case "nuevoPersonal":
                                    this.insertarPersonal(session, request, response, quid, out);
                                    break;
                                case "vincularPersonal":
                                    this.vincularPersonal(session, request, response, quid, out);
                                    break;
                                case "updateVincularPersonal":
                                    this.actualizarVincularPersonal(session, request, response, quid, out);
                                    break;
                                case "actualizarPersonal":
                                    this.actualizarPersonal(session, request, response, quid, out);
                                    break;
                                case "editarPassPersonal":
                                    this.actualizarPassUsuario(session, request, response, quid, out, false);
                                    break;
                                case "updatePlantel":
                                    this.updatePlantel(session, request, response, quid, out);
                                    break;
                                case "insertPlantel":
                                    this.insertPlantel(session, request, response, quid, out);
                                    break;
                                case "borrarPlantel":
                                    this.borrarPlantel(session, request, response, quid, out);
                                    break;
                                case "nuevoProveedor":
                                    this.insertarProveedor(session, request, response, quid, out);
                                    break;
                                case "borrarProveedor":
                                    this.eliminarProveedor(session, request, response, quid, out);
                                    break;
                                case "editarProveedor":
                                    this.editarProveedor(session, request, response, quid, out);
                                    break;
                                case "nuevoTipoProveedor":
                                    this.insertarTipoProveedor(session, request, response, quid, out);
                                    break;
                                case "borrarTipoProveedor":
                                    this.eliminarTipoProveedor(session, request, response, quid, out);
                                    break;
                                case "editarTipoProveedor":
                                    this.editarTipoProveedor(session, request, response, quid, out);
                                    break;
                                case "nuevoDetalle":
                                    this.nuevoDetalle(session, request, response, quid, out);
                                    break;
                                case "eliminarDetalle":
                                    this.eliminarDetalle(session, request, response, quid, out);
                                    break;
                                case "editarDetalle":
                                    this.editarDetalle(session, request, response, quid, out);
                                    break;
                                case "insertValor":
                                    this.insertarValor(session, request, response, quid, out);
                                    break;
                                case "eliminarValor":
                                    this.eliminarValor(session, request, response, quid, out);
                                    break;
                                case "nuevoDetalleSubCategoria":
                                    this.insertarDetalleSubcategoria(session, request, response, quid, out);
                                    break;
                                case "deleteDetalleSubcategoria":
                                    this.eliminarDetalleSubcategoria(session, request, response, quid, out);
                                    break;
                                case "insertBien":
                                    this.insertarBien(session, request, response, quid, out);
                                    break;
                                case "deleteBien":
                                    this.eliminarBien(session, request, response, quid, out);
                                    break;
                                case "updateBien":
                                    this.actualizarBien(session, request, response, quid, out);
                                    break;
                                case "insertNombreSoftware":
                                    this.insertNombreSoftware(session, request, response, quid, out);
                                    break;
                                case "deleteNombreSoftware":
                                    this.deleteNombreSoftware(session, request, response, quid, out);
                                    break;
                                case "updateNombreSoftware":
                                    this.updateNombreSoftware(session, request, response, quid, out);
                                    break;
                                case "deleteBds":
                                    this.eliminarBDS(session, request, response, quid, out);
                                    break;
                                case "insertBDS":
                                    this.insertarBDS(session, request, response, quid, out);
                                    break;
                                case "deleteGrupoBien":
                                    this.eliminarGrupoBien(session, request, response, quid, out);
                                    break;
                                case "insertarGrupoBien":
                                    this.insertarGrupoBien(session, request, response, quid, out);
                                    break;
                                case "insertPBT":
                                    this.insertarPBT(session, request, response, quid, out);
                                    break;
                                case "deletePBT":
                                    this.eliminarPBT(session, request, response, quid, out);
                                    break;
                                case "insertPBTFull":
                                    this.insertarPBTFull(session, request, response, quid, out);
                                    break;
                                case "deleteGarantia":
                                    this.eliminarGarantia(session, request, response, quid, out);
                                    break;
                                case "insertGarantia":
                                    this.insertarGarantia(session, request, response, quid, out);
                                    break;
                                case "updateGarantia":
                                    this.actualizarGarantia(session, request, response, quid, out);
                                    break;
                                case "insertGarantiaFull":
                                    this.insertarGarantiaFull(session, request, response, quid, out);
                                    break;
                                case "insertBaja":
                                    this.insertarBaja(session, request, response, quid, out);
                                    break;
                                case "insertSolicitud":
                                    this.insertarSolicitud(session, request, response, quid, out);
                                    break;
                                case "deleteSolicitud":
                                    this.eliminarSolicitud(session, request, response, quid, out);
                                    break;
                                case "updateSolicitud":
                                    this.actualizarSolicitud(session, request, response, quid, out);
                                    break;
                                case "insertSolicitudBaja":
                                    this.insertarSolicitudBaja(session, request, response, quid, out);
                                    break;
                                case "deleteSolicitudBaja":
                                    this.eliminarSolicitudBaja(session, request, response, quid, out);
                                    break;
                                case "updateBaja":
                                    this.actualizarBaja(session, request, response, quid, out);
                                    break;
                                case "deleteCheckList":
                                    this.eliminarCheckList(session, request, response, quid, out);
                                    break;
                                case "insertChekList":
                                    this.insertarCheckList(session, request, response, quid, out);
                                    break;
                                case "insertRubro":
                                    this.insertarRubro(session, request, response, quid, out);
                                    break;
                                case "eliminarCheckListRubro":
                                    this.eliminarCheckListRubro(session, request, response, quid, out);
                                    break;
                                case "updateCRB":
                                    this.actualizarCRB(session, request, response, quid, out);
                                    break;
                                case "insertCRB":
                                    this.insertarCRB(session, request, response, quid, out);
                                    break;
                                case "insertIncidente":
                                    this.insertarIncidente(session, request, response, quid, out);
                                    break;
                                case "deleteIncidente":
                                    this.eliminarIncidente(session, request, response, quid, out);
                                    break;
                                case "insertBitacoraIncidente":
                                    this.insertarBitacoraIncidente(session, request, response, quid, out);
                                    break;
                                case "deleteBitacoraIncidente":
                                    this.eliminarBitacoraIncidente(session, request, response, quid, out);
                                    break;
                                case "updateBitacoraIncidente":
                                    this.actualizarBitacoraIncidente(session, request, response, quid, out);
                                    break;
                                case "deleteResguardo":
                                    this.eliminarResguardo(session, request, response, quid, out);
                                    break;
                                case "updateResguardo":
                                    this.actualizarResguardo(session, request, response, quid, out);
                                    break;
                                case "deleteCRB":
                                    this.eliminarCRB(session, request, response, quid, out);
                                    break;
                                case "deleteResguardoPersonal":
                                    this.eliminarResguardoPersonal(session, request, response, quid, out);
                                    break;
                                case "nuevoMovimiento":
                                    this.insertTraspaso(session, request, response, quid, out);
                                    break;
                                case "insertTraspasoBien":
                                    this.insertTraspasoBien(session, request, response, quid, out);
                                    break;
                                case "insertDocumentos":
                                    this.insertarDocumentos(session, request, response, quid, out);
                                    break;
                                case "deleteTipoArchivo":
                                    this.eliminarDocumentos(session, request, response, quid, out);
                                    break;
                                case "file4Software":
                                    this.eliminarArchivo(session, request, response, quid, out);
                                    break;
                                case "file4Bien":
                                    this.eliminarArchivo(session, request, response, quid, out);
                                    break;
                                case "insertMovimientoEntrada":
                                    this.insertMovimientoEntrada(session, request, response, quid, out);
                                    break;
                                case "insertConsumible":
                                    this.insertConsumible(session, request, response, quid, out);
                                    break;
                                case "insertEntradaConsumible":
                                    this.insertEntradaConsumible(session, request, response, quid, out);
                                    break;
                                case "deleteConsumibleMovimiento":
                                    this.deleteConsumibleMovimiento(session, request, response, quid, out);
                                    break;
                                case "updateMovimientoEntrada":
                                    this.updateMovimientoEntrada(session, request, response, quid, out);
                                    break;
                                case "insertMovimientoSalida":
                                    this.insertMovimientoSalida(session, request, response, quid, out);
                                    break;
                                case "insertSalidaConsumible":
                                    this.insertSalidaConsumible(session, request, response, quid, out);
                                    break;
                                case "deleteConsumibleSalida":
                                    this.deleteConsumibleSalida(session, request, response, quid, out);
                                    break;
                                case "updateMovimientoSalida":
                                    this.updateMovimientoSalida(session, request, response, quid, out);
                                    break;
                                case "updateConsumible":
                                    this.updateConsumible(session, request, response, quid, out);
                                    break;
                                case "deleteMedida":
                                    this.deleteMedida(session, request, response, quid, out);
                                    break;
                                case "insertMedida":
                                    this.insertarMedida(session, request, response, quid, out);
                                    break;
                                case "updatePDG":
                                    this.updatePDG(session, request, response, quid, out);
                                    break;
                                case "insertConteoFisico":
                                    this.insertConteoFisico(session, request, response, quid, out);
                                    break;
                                case "insertConteoFisicoConsumible":
                                    this.insertConteoFisicoConsumible(session, request, response, quid, out);
                                    break;
                                case "terminarInventario":
                                    this.terminarInventario(session, request, response, quid, out);
                                    break;
                                case "insertTipoActividad":
                                    this.insertTipoActividad(session, request, response, quid, out);
                                    break;
                                case "deleteTipoActividad":
                                    this.deleteTipoActividad(session, request, response, quid, out);
                                    break;
                                case "updateTipoActividad":
                                    this.updateTipoActividad(session, request, response, quid, out);
                                    break;
                                case "insertActividad":
                                    this.insertActividad(session, request, response, quid, out);
                                    break;
                                case "updateActividadPlantel":
                                    this.updateActividadPlantel(session, request, response, quid, out);
                                    break;
                                case "updateEstatusActividadPlantel":
                                    this.updateEstatusActividadPlantel(session, request, response, quid, out);
                                    break;
                                case "deleteActividadPlantel":
                                    this.deleteActividadPlantel(session, request, response, quid, out);
                                    break;
                                case "updateActividad":
                                    this.updateActividad(session, request, response, quid, out);
                                    break;
                                case "insertSolicitudPlantel":
                                    this.insertSolicitudPlantel(session, request, response, quid, out);
                                    break;
                                case "updateSolicitudPlantel":
                                    this.updateSolicitudPlantel(session, request, response, quid, out);
                                    break;
                                case "deleteSolicitudPlantel":
                                    this.deleteSolicitudPlantel(session, request, response, quid, out);
                                    break;
                                case "insertEnlace":
                                    this.insertEnlace(session, request, response, quid, out);
                                    break;
                                case "updateEnlace":
                                    this.updateEnlace(session, request, response, quid, out);
                                    break;
                                case "deleteEnlace":
                                    this.deleteEnlace(session, request, response, quid, out);
                                    break;
                                case "updateEstatusBien":
                                    this.updateEstatusBien(session, request, response, quid, out);
                                    break;
                                case "deleteObjetoArchivo":
                                    this.deleteObjetoArchivo(session, request, response, quid, out);
                                    break;
                                case "updateArchivo":
                                    this.updateArchivo(session, request, response, quid, out);
                                    break;
                                case "deleteRubro":
                                    this.deleteRubro(session, request, response, quid, out);
                                    break;
                                case "insertSimpleRubro":
                                    this.insertSimpleRubro(session, request, response, quid, out);
                                    break;
                                case "updateRubro":
                                    this.updateRubro(session, request, response, quid, out);
                                    break;
                                case "deletePuntuacion":
                                    this.deletePuntuacion(session, request, response, quid, out);
                                    break;
                                case "insertPuntuacion":
                                    this.insertPuntuacion(session, request, response, quid, out);
                                    break;
                                case "updatePuntuacion":
                                    this.updatePuntuacion(session, request, response, quid, out);
                                    break;
                                case "insertPlantelAlmacen":
                                    this.insertPlantelAlmacen(session, request, response, quid, out);
                                    break;
                                case "updatePlantelAlmacen":
                                    this.updatePlantelAlmacen(session, request, response, quid, out);
                                    break;
                                case "checkExistencia4Consumible":
                                    this.checkExistencia4Consumible(session, request, response, quid, out);
                                    break;
                                case "insertOrdenSurtimiento":
                                    this.insertOrdenSurtimiento(session, request, response, quid, out);
                                    break;
                                case "deleteOrdenConsumible":
                                    this.deleteOrdenConsumible(session, request, response, quid, out);
                                    break;
                                case "updateOrdenSurtimiento":
                                    this.updateOrdenSurtimiento(session, request, response, quid, out);
                                    break;
                                case "deleteOrdenSurtimiento":
                                    this.deleteOrdenSurtimiento(session, request, response, quid, out);
                                    break;
                                case "updateOrdenSurtimientoPendiente":
                                    this.updateOrdenSurtimientoPendiente(session, request, response, quid, out);
                                    break;
                                case "getSesiones":
                                    this.getSesionsList(session, request, response, quid, out);
                                    break;
                                case "terminarSesion":
                                    this.closeSession(session, request, response, quid, out);
                                    break;
                                case "terminarAllSesions":
                                    this.closeAllSession(session, request, response, quid, out);
                                    break;
                                case "terminarAllSessionsTime":
                                    this.closeAllSessionTime(session, request, response, quid, out);
                                    break;
                                case "calcPatronSerialNumber":
                                    this.calcPatronSerialNumber(session, request, response, quid, out);
                                    break;
                                case "actualizarPatronSerie":
                                    this.actualizarPatronSerie(session, request, response, quid, out);
                                    break;
                                case "deletePatronSerie":
                                    this.deletePatronSerie(session, request, response, quid, out);
                                    break;
                                case "resetFoliosMovimiento":
                                    this.resetFoliosMovimiento(session, request, response, quid, out);
                                    break;
                                case "validaSerial":
                                    this.validaSerial(session, request, response, quid, out);
                                    break;

                            }
                        } else if (ServletFileUpload.isMultipartContent(new ServletRequestContext(request))) {
                            this.subirArchivo(session, request, response, quid, out);
                        } else {
                            out.println("UPS.... Algo malo ha pasado");
                        }
            } catch (Exception ex) {
                Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            quid = null;
            out.close();
        }
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

    private void showAllParametersConsoleDecode(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        //--------------------------------------------------------------
        Enumeration enu = request.getParameterNames();
        String param = "";
        System.out.println("\nshowAllParametersConsoleDecode");
        while (enu.hasMoreElements()) {
            param = enu.nextElement().toString();
            System.out.println(param + " = " + request.getParameter(param));
            System.out.println("Desc: " + param + " = " + WebUtil.decode(session, request.getParameter(param)));
            System.out.println("");
        }
        //--------------------------------------------------------------
    }

    private void showAllParametersConsole(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        //--------------------------------------------------------------
        Enumeration enu = request.getParameterNames();
        String param = "";
        System.out.println("\nshowAllParametersConsole");
        while (enu.hasMoreElements()) {
            param = enu.nextElement().toString();
            System.out.println(param + " = " + request.getParameter(param));
            System.out.println("");
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
        session = null;
    }

    private boolean validarFormTraspasoBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        if (request.getParameter("idBien").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un bien/artículo.").forward(request, response);
        } else if (request.getParameter("estatusTraspaso").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el estatus del movimiento.").forward(request, response);
        } else if (request.getParameter("FK_ID_Plantel").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("FK_ID_Personal_Recibe").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el nombre del resposanble del plantel.").forward(request, response);
        } else if (request.getParameter("observaciones").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba las observaciones del bien.").forward(request, response);
        } else {
            todoBien = true;
        }

        return todoBien;
    }

    private void insertTraspasoBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertSoftware");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormTraspasoBien(session, request, response, quid, out)) {
                Calendar cal = Calendar.getInstance();
                Transporter tport = quid.insert_Transpaso_Bien(WebUtil.decode(session, request.getParameter("idBien")),
                        WebUtil.decode(session, request.getParameter("idNuevoMovimiento")),
                        WebUtil.decode(session, request.getParameter("estatusTraspaso")),
                        WebUtil.decode(session, request.getParameter("FK_ID_Plantel")),
                        WebUtil.decode(session, request.getParameter("FK_ID_Personal_Recibe")),
                        request.getParameter("observaciones"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El bien se agrego correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudBaja.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrio un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario NO válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void actualizarPermiso(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdatePermiso");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("0")) {
                if ((request.getParameter("value").length() < 40)) {
                    int nombrePermiso = quid.select_rowsPermiso(request.getParameter("value"));
                    if (nombrePermiso < 1) {
                        Transporter tport = quid.update_Permiso(
                                request.getParameter("value").trim(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error. " + tport.getMsg()).forward(request, response);
                        }
                    } else {
                        out.println("Este permiso ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre del permiso no es correcto o excede de 40 caracteres");
                }
            } else if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    Transporter tport = quid.update_PermisoXTipo(
                            request.getParameter("value").trim(),
                            Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                    if (tport.getCode() == 0) {
                        out.print(request.getParameter("value"));
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error. " + tport.getMsg()).forward(request, response);
                    }
                } else {
                    out.println("El registro no se puede actualizar, el tipo de permiso no es correcto o excede de 40 caracteres");
                }
            } else if (request.getParameter("column").equalsIgnoreCase("2")) {
                if ((request.getParameter("value").length() < 100)) {
                    Transporter tport = quid.update_PermisoXDescripcion(
                            request.getParameter("value").trim(),
                            Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                    if (tport.getCode() == 0) {
                        out.print(request.getParameter("value"));
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error. " + tport.getMsg()).forward(request, response);
                    }
                } else {
                    out.println("El registro no se puede actualizar, la descripción del permiso no es correcto o excede de 100 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void deleteSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteSoftware");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.trans_delete_SoftwarePlantel(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Software_Plantel"))),
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Software"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=La información se ha eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error al intentar eliminar la información.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);
            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página ha tenido un error :(").forward(request, response);
        }
    }

    private boolean validarFormMovimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        String Serial = null;

        if (request.getParameter("motivoTranspaso").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el motivo del movimiento por favor.").forward(request, response);
        } else if (request.getParameter("FK_ID_Personal").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el responsable.").forward(request, response);

        } else {
            todoBien = true;
        }

        return todoBien;
    }

    private void insertTraspaso(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertMovimiento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        String observaciones = " ";
        String idNuevoMovimiento = null;

        int noActualizacionesPermitidas = 0;
        observaciones = request.getParameter("observaciones").equalsIgnoreCase("") ? " " : request.getParameter("observaciones");

        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormMovimiento(session, request, response, quid, out)) {
                if (this.validarFacturaProveedor(session, request, response, quid, out)) {
                    Calendar cal = Calendar.getInstance();
                    idNuevoMovimiento = quid.trans_insert_traspaso(
                            UTime.calendar2SQLDateFormat(cal),
                            WebUtil.decode(session, request.getParameter("motivoTranspaso")),
                            "Capturado",
                            WebUtil.decode(session, request.getParameter("FK_ID_Personal")),
                            session.getAttribute("userID").toString(),
                            request.getParameter("observaciones"),
                            session.getAttribute("FK_ID_Plantel").toString()
                    );
                    if (idNuevoMovimiento != null) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La operación se realizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertMovimientoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "_param_" + "idNuevoMovimiento" + "=" + WebUtil.encode(session, idNuevoMovimiento)).forward(request, response);

                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Error&type=error&msg=Ocurrió un error al realizar el proceso.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario NO válido para realizar esta tarea.").forward(request, response);
        }
    }

    private boolean validarFormSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        if (request.getParameter("FK_ID_Proveedor").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione un proveedor por favor.").forward(request, response);

        } else if (!StringUtil.isValidStringLength(request.getParameter("noFactura"), 1, 50)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de factura debe de contener entre 1 y 50 caracteres.").forward(request, response);

        } else if (request.getParameter("FK_ID_NombreSoftware").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el nombre de software.").forward(request, response);

        } else if (!StringUtil.isValidStringLength(request.getParameter("version"), 1, 50)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La versión debe contener entre 1 y 50 caracteres.").forward(request, response);

        } else if (!UTime.validaFecha(request.getParameter("fechaAdquisicion"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de adquisición valida.").forward(request, response);

        } else if (!StringUtil.isValidInt(request.getParameter("noLicencias"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Ingrese el número de licencias correctamente.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noLicenciasAsignadas"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese el número de licencias asignadas.").forward(request, response);
        } else if (Integer.parseInt(request.getParameter("noLicenciasAsignadas")) > Integer.parseInt(request.getParameter("noLicencias"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de licencias asignadas debe ser menor al número total de licencias").forward(request, response);

        } else if (request.getParameter("SoporteTecnico").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Debe seleccionar si la licencia cuenta con soporte técnico o no.").forward(request, response);
        } else if (request.getParameter("FK_ID_Tipo_Software").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Debe seleccionar el tipo de software").forward(request, response);

        } else if (request.getParameter("FK_ID_Licencia").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Debe seleccionar el tipo de licencia").forward(request, response);
        } else if (request.getParameter("FK_ID_Tipo_Compra").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Debe seleccionar el tipo de compra").forward(request, response);

        } else if (request.getParameter("FK_ID_Plantel").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!StringUtil.isValidStringLength(request.getParameter("nombreResponsable"), 1, 120)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Ingrese el nombre del responsable.").forward(request, response);

        } else if (request.getParameter("status").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el estatus.").forward(request, response);

        } else if (WebUtil.decode(session, request.getParameter("SoporteTecnico")).equalsIgnoreCase("1")
                && !StringUtil.validaEmail(request.getParameter("emailSoporteTecnico"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un correo de soporte válido.").forward(request, response);

        } else if (WebUtil.decode(session, request.getParameter("SoporteTecnico")).equalsIgnoreCase("1")
                && !StringUtil.isValidStringLength(request.getParameter("telefonoSoporte").trim(), 1, 10)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de teléfono debe contener 10 digitos").forward(request, response);
        } else if (!request.getParameter("aniosLicencia").equalsIgnoreCase("")
                && !StringUtil.isPositiveInt(request.getParameter("aniosLicencia").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tiempo de duración de la licencia no es valido.").forward(request, response);
        } else if (!request.getParameter("hddRequerido").trim().equalsIgnoreCase("")
                && !StringUtil.isValidInt(request.getParameter("hddRequerido").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El Disco duro requerido debe ser un número entero.").forward(request, response);
        } else if (!request.getParameter("ramRequerida").trim().equalsIgnoreCase("")
                && !StringUtil.isValidInt(request.getParameter("ramRequerida").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La Memoria RAM requerida debe ser un número entero.").forward(request, response);
        } else {
            todoBien = true;
        }

        return todoBien;
    }

    private boolean validarFacturaProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        int rowsSoftware = quid.select_rowsSoftwareID(request.getParameter("noFactura"),
                WebUtil.decode(session, request.getParameter("FK_ID_Proveedor")),
                WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                request.getParameter("version"),
                WebUtil.decode(session, request.getParameter("FK_ID_Plantel")));
        if (rowsSoftware > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El software YA se encuentra registrado.").forward(request, response);
        } else {
            todoBien = true;
        }
        return todoBien;
    }

    private void insertSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertSoftware");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        String observaciones = " ";
        String Serial = " ";
        String fechaIstalacion = " ";
        String fechaVencimiento = " ";
        String noDictamen = " ";
        int hddRequerido = 0;
        int ramRequerida = 0;
        String soRequerido = " ";
        String noContrato = " ";
        String noAutorizacion = " ";
        String fechaInstalacion = " ";
        int aniosLicencia = 0;
        int upgrade = 0;
        int degrade = 0;

        int noActualizacionesPermitidas = 0;

        if (request.getParameter("upgrade") != null) {
            upgrade = 1;
        } else {
            upgrade = 0;
        }
        if (request.getParameter("degrade") != null) {
            degrade = 1;
        } else {
            degrade = 0;
        }
        String idNuevoSoftware = null;
        String emailSoporteTecnico = null;
        String telefonoSoporte = null;

        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormSoftware(session, request, response, quid, out)) {
                if (this.validarFacturaProveedor(session, request, response, quid, out)) {
                    if (WebUtil.decode(session, request.getParameter("SoporteTecnico")).equalsIgnoreCase("1")) {
                        emailSoporteTecnico = request.getParameter("emailSoporteTecnico");
                        telefonoSoporte = request.getParameter("telefonoSoporte");
                    } else {
                        emailSoporteTecnico = " ";
                        telefonoSoporte = " ";
                    }
                    observaciones = request.getParameter("observaciones").equalsIgnoreCase("") ? " " : request.getParameter("observaciones");
                    Serial = request.getParameter("serial").equalsIgnoreCase("") ? " " : request.getParameter("serial");
                    fechaInstalacion = request.getParameter("fechaInstalacion").equalsIgnoreCase("") ? " " : request.getParameter("fechaInstalacion");
                    fechaVencimiento = request.getParameter("fechaVencimiento").equalsIgnoreCase("") ? " " : request.getParameter("fechaVencimiento");
                    noDictamen = request.getParameter("noDictamen").equalsIgnoreCase("") ? " " : request.getParameter("noDictamen");
                    hddRequerido = request.getParameter("hddRequerido").trim().equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("hddRequerido").trim());
                    ramRequerida = request.getParameter("ramRequerida").trim().equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("ramRequerida").trim());
                    soRequerido = request.getParameter("soRequerido").equalsIgnoreCase("") ? " " : request.getParameter("soRequerido");
                    noContrato = request.getParameter("noContrato").equalsIgnoreCase("") ? " " : request.getParameter("noContrato");
                    noAutorizacion = request.getParameter("noAutorizacion").equalsIgnoreCase("") ? " " : request.getParameter("noAutorizacion");
                    fechaInstalacion = request.getParameter("fechaInstalacion").equalsIgnoreCase("") ? " " : request.getParameter("fechaInstalacion");
                    aniosLicencia = request.getParameter("aniosLicencia").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("aniosLicencia"));
                    noActualizacionesPermitidas = request.getParameter("noActualizacionesPermitidas").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("noActualizacionesPermitidas"));
                    Calendar cal = Calendar.getInstance();

                    idNuevoSoftware = quid.trans_insert_Software_Plantel(
                            WebUtil.decode(session, request.getParameter("FK_ID_Plantel")),
                            WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                            request.getParameter("version"),
                            Serial,
                            fechaVencimiento,
                            request.getParameter("fechaAdquisicion"),
                            noDictamen,
                            request.getParameter("noLicencias"),
                            request.getParameter("noLicenciasAsignadas"),
                            hddRequerido,
                            ramRequerida,
                            soRequerido,
                            WebUtil.decode(session, request.getParameter("SoporteTecnico")),
                            emailSoporteTecnico,
                            telefonoSoporte,
                            WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Software")),
                            WebUtil.decode(session, request.getParameter("FK_ID_Licencia")),
                            WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Compra")),
                            request.getParameter("nombreResponsable"),
                            observaciones,
                            "Sin documento",
                            WebUtil.decode(session, request.getParameter("status")),
                            WebUtil.decode(session, request.getParameter("FK_ID_Proveedor")),
                            fechaIstalacion,
                            noContrato,
                            noAutorizacion,
                            aniosLicencia,
                            request.getParameter("noFactura"),
                            upgrade,
                            degrade,
                            noActualizacionesPermitidas
                    );
                    if (idNuevoSoftware.equals("")) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al realizar el proceso.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);

                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La operación se realizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis())).forward(request, response);
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario NO válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void updateSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateSoftware");
        String emailSoporteTecnico = null;
        String telefonoSoporte = null;
        String observaciones = " ";
        String serial = " ";
        String fechaVencimiento = " ";
        String noDictamen = " ";
        int hddRequerido = 0;
        int ramRequerida = 0;
        String soRequerido = " ";
        String noContrato = " ";
        String noAutorizacion = " ";
        String fechaInstalacion = " ";
        int aniosLicencia = 0;
        int upgrade = 0;
        int degrade = 0;
        int noActualizacionesPermitidas = 0;
        observaciones = request.getParameter("observaciones").equalsIgnoreCase("") ? " " : request.getParameter("observaciones");
        serial = request.getParameter("serial").equalsIgnoreCase("") ? " " : request.getParameter("serial");
        fechaVencimiento = request.getParameter("fechaVencimiento").equalsIgnoreCase("") ? " " : request.getParameter("fechaVencimiento");
        noDictamen = request.getParameter("noDictamen").equalsIgnoreCase("") ? " " : request.getParameter("noDictamen");
        hddRequerido = request.getParameter("hddRequerido").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("hddRequerido"));
        ramRequerida = request.getParameter("ramRequerida").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("ramRequerida"));
        soRequerido = request.getParameter("soRequerido").equalsIgnoreCase("") ? " " : request.getParameter("soRequerido");
        noContrato = request.getParameter("noContrato").equalsIgnoreCase("") ? " " : request.getParameter("noContrato");
        noAutorizacion = request.getParameter("noAutorizacion").equalsIgnoreCase("") ? " " : request.getParameter("noAutorizacion");
        fechaInstalacion = request.getParameter("fechaInstalacion").equalsIgnoreCase("") ? " " : request.getParameter("fechaInstalacion");
        aniosLicencia = request.getParameter("aniosLicencia").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("aniosLicencia"));
        noActualizacionesPermitidas = request.getParameter("noActualizacionesPermitidas").equalsIgnoreCase("") ? 0 : Integer.parseInt(request.getParameter("noActualizacionesPermitidas"));
        if (request.getParameter("upgrade") != null) {
            upgrade = 1;
        } else {
            upgrade = 0;
        }
        if (request.getParameter("degrade") != null) {
            degrade = 1;
        } else {
            degrade = 0;
        }
        String idSoftwarePlantel = !request.getParameter("idSoftwarePlantel").equalsIgnoreCase("") ? WebUtil.decode(session, request.getParameter("idSoftwarePlantel")) : "";
        String ID_Software = WebUtil.decode(session, request.getParameter("ID_Software"));
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int getRowsSoftware = quid.select_rowsSoftware(ID_Software,
                request.getParameter("noFactura"),
                WebUtil.decode(session, request.getParameter("FK_ID_Proveedor")),
                WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                request.getParameter("version"));
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormSoftware(session, request, response, quid, out)) {
                if (WebUtil.decode(session, request.getParameter("SoporteTecnico")).equalsIgnoreCase("1")) {
                    emailSoporteTecnico = request.getParameter("emailSoporteTecnico");
                    telefonoSoporte = request.getParameter("telefonoSoporte");
                } else if (WebUtil.decode(session, request.getParameter("SoporteTecnico")).equalsIgnoreCase("0")) {
                    emailSoporteTecnico = " ";
                    telefonoSoporte = " ";
                }
                if (getRowsSoftware > 0) {
                    Transporter tport = quid.trans_update_Software(
                            WebUtil.decode(session, request.getParameter("FK_ID_Plantel")),
                            WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                            request.getParameter("version"),
                            serial,
                            fechaVencimiento,
                            request.getParameter("fechaAdquisicion"),
                            noDictamen,
                            request.getParameter("noLicencias"),
                            request.getParameter("noLicenciasAsignadas"),
                            hddRequerido,
                            ramRequerida,
                            soRequerido,
                            WebUtil.decode(session, request.getParameter("SoporteTecnico")),
                            emailSoporteTecnico,
                            telefonoSoporte,
                            WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Software")),
                            WebUtil.decode(session, request.getParameter("FK_ID_Licencia")),
                            WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Compra")),
                            request.getParameter("nombreResponsable"),
                            observaciones,
                            WebUtil.decode(session, request.getParameter("status")),
                            Integer.parseInt(WebUtil.decode(session, request.getParameter("FK_ID_Proveedor"))),
                            noContrato,
                            noAutorizacion,
                            fechaInstalacion,
                            aniosLicencia,
                            request.getParameter("noFactura"),
                            upgrade,
                            degrade,
                            noActualizacionesPermitidas,
                            Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Software"))),
                            idSoftwarePlantel);
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Información Actualizada&type=info&msg=La información fue actualizada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=Lo sentimos no se ha podido guardar :(").forward(request, response);
                    }
                } else {
                    int rowsSoftware = quid.select_rowsSoftwareID(request.getParameter("noFactura"),
                            WebUtil.decode(session, request.getParameter("FK_ID_Proveedor")),
                            WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                            request.getParameter("version"),
                            WebUtil.decode(session, request.getParameter("FK_ID_Plantel")));
                    if (rowsSoftware > 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=Este registro YA existe en la base de datos.").forward(request, response);
                    } else {
                        Transporter tport = quid.trans_update_Software(
                                WebUtil.decode(session, request.getParameter("FK_ID_Plantel")),
                                WebUtil.decode(session, request.getParameter("FK_ID_NombreSoftware")),
                                request.getParameter("version"),
                                serial,
                                fechaVencimiento,
                                request.getParameter("fechaAdquisicion"),
                                noDictamen,
                                request.getParameter("noLicencias"),
                                request.getParameter("noLicenciasAsignadas"),
                                hddRequerido,
                                ramRequerida,
                                soRequerido,
                                WebUtil.decode(session, request.getParameter("SoporteTecnico")),
                                emailSoporteTecnico,
                                telefonoSoporte,
                                WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Software")),
                                WebUtil.decode(session, request.getParameter("FK_ID_Licencia")),
                                WebUtil.decode(session, request.getParameter("FK_ID_Tipo_Compra")),
                                request.getParameter("nombreResponsable"),
                                observaciones,
                                WebUtil.decode(session, request.getParameter("status")),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("FK_ID_Proveedor"))),
                                noContrato,
                                noAutorizacion,
                                fechaInstalacion,
                                aniosLicencia,
                                request.getParameter("noFactura"),
                                upgrade,
                                degrade,
                                noActualizacionesPermitidas,
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Software"))),
                                idSoftwarePlantel);
                        if (tport.getCode() == 0) {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msgNRedirect.jsp?title=Información Actualizada&type=info&msg=La información fue actualizada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=error&msg=Lo sentimos no se ha podido guardar :(").forward(request, response);
                        }
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void updateDepatamento_Plantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("UpdateDepartamento_Plantel");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                if (request.getParameter("idPlantel").equals("")) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
                } else {
                    boolean error = false;
                    int noparametros = Integer.parseInt(request.getParameter("numparam"));
                    for (int i = 1; i <= noparametros; i++) {
                        String paramPermiso = WebUtil.decode(session, request.getParameter("option" + i));
                        if (paramPermiso != null && paramPermiso.equals("") != true) {
                            Transporter tport = quid.insert_DeptoPlantel(Integer.parseInt(WebUtil.decode(session, request.getParameter("idPlantel"))), Integer.parseInt(paramPermiso));
                            if (tport.getCode() != 0) {
                                error = true;
                            }
                        }
                    }
                    if (error == false) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNBackFull.jsp?title=Guardado Exitoso&type=info&msg=El proceso se realizó con éxito.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento_Plantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNBackFull.jsp?title=Error&type=error&msg=Error inesperado, no fue posible asignar los registros correspondientes.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento_Plantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

                    }

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void updateDepartamento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateDepartamento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 149)) {
                    int nombreDepartamento = quid.select_rowsDepartamento(request.getParameter("value").toUpperCase());
                    if (nombreDepartamento < 1) {
                        Transporter tport = quid.update_Departamento(
                                request.getParameter("value").toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error. " + tport.getMsg()).forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre del departamento no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void updateNombreSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateNombreSoftware");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int nombreNombreSoftware = quid.select_rowsNombreSoftware(request.getParameter("value").toUpperCase());
                    if (nombreNombreSoftware < 1) {
                        Transporter tport = quid.update_NombreSoftware(
                                request.getParameter("value").toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error. " + tport.getMsg()).forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre del software no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void deleteDepartamento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteDepartamento");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                if (quid.getRowsDepartamentoinDepartamentoDepartamentoPlantel(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Departamento")))) == 0) {
                    Transporter tport = quid.delete_Departamento(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Departamento"))));
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=La información fue eliminada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    }
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se puede borrar porque contiene asociaciones&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }

            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteNombreSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteNombreSoftware");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_NombreSoftware(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Nombre_Software"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=La información fue eliminada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaNombreSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaNombreSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaNombreSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteDepartamentoPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteDepartamentoPlantel");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_DepartamentoPlantel(WebUtil.decode(session, request.getParameter("ID_Departamento_Plantel")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El departamento del plantel fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento_Plantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=" + tport.getMsg() + "&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento_Plantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento_Plantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void insertDepartamento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertDepartamento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreDepartamento = quid.select_rowsDepartamento(request.getParameter("nombreDepartamento").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreDepartamento"), 1, 149)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del departamento debe contener entre 1 y 50 caracteres").forward(request, response);
            } else if (nombreDepartamento > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del departamento YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_Departamento(
                        request.getParameter("nombreDepartamento").toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=La información se guardó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDepartamento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private boolean validarFormSubCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        if (request.getParameter("FK_ID_Categoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una categoría.").forward(request, response);
        } else if (!StringUtil.isValidStringLength(request.getParameter("nombreSubCategoria"), 1, 90)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre de subcategoría válido.").forward(request, response);
        } else {
            todoBien = true;
        }
        return todoBien;
    }

    private void borrarPermiso(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeletePermiso");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Permiso(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Permiso"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El permiso fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void updateSubCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateSubCategoria");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int SubCategoria = quid.select_rowsSubCategoria(request.getParameter("nombreSubCategoria").toUpperCase(),
                WebUtil.decode(session, request.getParameter("FK_ID_Categoria")));
        if (SubCategoria > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La subcategoría YA se encuentra registrada.").forward(request, response);
        } else if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormSubCategoria(session, request, response, quid, out)) {
                Transporter tport = quid.update_SubCategoria(
                        WebUtil.decode(session, request.getParameter("FK_ID_Categoria")),
                        request.getParameter("nombreSubCategoria").toUpperCase(),
                        WebUtil.decode(session, request.getParameter("ID_SubCategoria")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La información se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error").forward(request, response);
                }
            }

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void insertarPermiso(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertPermiso");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            int nombrePermiso = quid.select_rowsPermiso(request.getParameter("nombrePermiso").toUpperCase());
            if (!StringUtil.isValidStringLength(request.getParameter("nombrePermiso"), 3, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de permiso debe tener entre 3 y 100 caracteres").forward(request, response);
            } else if (!StringUtil.isValidStringLength(request.getParameter("tipoPermiso"), 3, 20)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El tipo de permiso debe tener entre 3 y 20 caracteres").forward(request, response);
            } else if (!StringUtil.isValidStringLength(request.getParameter("descripcion"), 3, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=La descripción debe tener entre 3 y 100 caracteres").forward(request, response);
            } else if (nombrePermiso > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de permiso YA se encuentra registrado.").forward(request, response);
            } else {
                Transporter tport = quid.insert_Permiso(
                        request.getParameter("nombrePermiso").trim(),
                        request.getParameter("tipoPermiso"),
                        request.getParameter("descripcion").trim());
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El permiso se ha creado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El permiso no se pudo crear.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPermisos.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        }
    }

    private void deleteMarca(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteMarca");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Marca(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Marca"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El nombre de la marca fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMarca.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMarca.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMarca.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteTipoGarantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteTipo_Garantia");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Tipo_Garantia(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Tipo_Garantia"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El tipo de garantía fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Garantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Garantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Garantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteTipo_Software(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteTipo_Software");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Tipo_Software(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Tipo_Software"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El tipo de software fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Software.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Software.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Software.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteTipo_Compra(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteTipoCompra");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Tipo_Compra(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Tipo_Compra"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El tipo de compra fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Compra.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Compra.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Compra.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteModelo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteModelo");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Modelo(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Modelo"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El modelo fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteLicencia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteLicencia");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Licencia(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Licencia"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El registro fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaLicencia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaLicencia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteCategoria");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Categoria(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Categoria"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=La categoría fue eliminada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void borrarPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeletePlantel");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_Plantel(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Plantel"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=Los datos del plantel fueron eliminados correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPlanteles.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPlanteles.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPlanteles.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteSubCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("DeleteSubCategoria");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Transporter tport = quid.delete_SubCategoria(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_SubCategoria"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=La subcategoría fue eliminada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);

            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void deleteGrupo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteGrupo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (quid.getRowsGrupoinGrupo_Bien(WebUtil.decode(session, request.getParameter("idGrupo"))) == 0) {
                Transporter tport = tport = quid.delete_RelacionPdg(WebUtil.decode(session, request.getParameter("idPdg")));
                if (tport.getCode() == 0) {
                    tport = quid.delete_Grupo(WebUtil.decode(session, request.getParameter("idGrupo")));
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El grupo fue eliminado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error al eliminar el grupo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                    }
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error al eliminar el grupo del departamento.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El grupo no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }

    }

    private void updateModelo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateModelo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormModelo(session, request, response, quid, out)) {
                String descripcion = request.getParameter("descripcion").trim().equals("") ? " " : request.getParameter("descripcion");
                Transporter tport = quid.update_Modelo(
                        request.getParameter("modelo").trim().toUpperCase(),
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("FK_ID_Marca"))),
                        descripcion,
                        WebUtil.decode(session, request.getParameter("ID_Modelo")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La información del modelo fue actualizada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Lo sentimos no se ha podido guardar :(").forward(request, response);
                }

            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void updateCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateCategoria");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int nombreMarca = quid.select_rowsCategoria(request.getParameter("value").toUpperCase());
                    if (nombreMarca < 1) {
                        Transporter tport = quid.update_Categoria(
                                request.getParameter("value").trim().toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre de la categoría no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void actualizarGrupo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList grupoInfo = quid.select_Grupo(WebUtil.decode(session, request.getParameter("idGrupo")));
        if (this.validFormGrupo(session, request, response, quid, out)) {
            if (!grupoInfo.get(6).toString().equalsIgnoreCase(WebUtil.decode(session, request.getParameter("idDepartamento")))) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdatePDG");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    LinkedList bienGrupo = quid.select_Bien4Grupo(
                            WebUtil.decode(session, request.getParameter("idGrupo")),
                            "Baja",
                            false,
                            true);
                    Transporter tport = quid.trans_update_Grupo(request.getParameter("nombreGrupo").trim().toUpperCase(),
                            WebUtil.decode(session, request.getParameter("idGrupo")),
                            WebUtil.decode(session, request.getParameter("idPdg")),
                            WebUtil.decode(session, request.getParameter("idDepartamento")),
                            WebUtil.decode(session, request.getParameter("idPlantel")),
                            bienGrupo);

                    if (tport.getCode() == 0) {

                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El grupo se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al actualizar la información.").forward(request, response);
                    }

                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Usted no tiene el permiso para realizar esta acción.").forward(request, response);
                }
            } else {
                Transporter tport = quid.update_Grupo(request.getParameter("nombreGrupo").trim().toUpperCase(),
                        WebUtil.decode(session, request.getParameter("idGrupo")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El grupo se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al actualizar el grupo.").forward(request, response);
                }
            }
        }
    }

    private void actualizarMarca(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateMarca");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int nombreMarca = quid.select_rowsMarca(request.getParameter("value").toUpperCase());
                    if (nombreMarca < 1) {
                        Transporter tport = quid.update_Marca(
                                request.getParameter("value").trim().toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=error&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre de la marca no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void updateTipo_Software(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateTipo_Software");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int tipo = quid.select_filasTipo_Software(request.getParameter("value").toUpperCase());
                    if (tipo < 1) {
                        Transporter tport = quid.update_Tipo_Software(
                                request.getParameter("value").trim().toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el tipo de software no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea").forward(request, response);
        }
    }

    private void updateLicencia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateLicencia");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int nombreLicencia = quid.select_rowsLicencia(request.getParameter("value").toUpperCase());
                    if (nombreLicencia < 1) {
                        Transporter tport = quid.update_Licencia(
                                request.getParameter("value").trim().toUpperCase(),
                                Integer.parseInt(WebUtil.decode(session, request.getParameter("row_id"))));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre de la licencia no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void updateTipo_Garantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateTipo_Garantia");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int garantia = quid.select_rowsTipoGarantia(request.getParameter("value").toUpperCase());
                    if (garantia < 1) {
                        Transporter tport = quid.update_Tipo_Garantia(
                                request.getParameter("value").trim().toUpperCase(),
                                WebUtil.decode(session, request.getParameter("row_id")));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el tipo de garantía no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void updateTipo_Compra(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateTipo_Compra");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if ((request.getParameter("value").length() < 40)) {
                    int tipoCompra = quid.select_rowsTipo_Compra(request.getParameter("value").toUpperCase());
                    if (tipoCompra < 1) {
                        Transporter tport = quid.update_Tipo_Compra(
                                request.getParameter("value").trim().toUpperCase(),
                                WebUtil.decode(session, request.getParameter("row_id")));
                        if (tport.getCode() == 0) {
                            out.print(request.getParameter("value"));
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msg.jsp?title=Error&type=info&msg=Ha ocurrido un error.").forward(request, response);
                        }
                    } else {
                        out.println("Este registro ya existe.");
                    }
                } else {
                    out.println("El registro no se puede actualizar, el nombre de la marca no es correcto o excede de 40 caracteres");
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void actualizarVincularPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String ID_PersonalPlantel = WebUtil.decode(session, request.getParameter("ID_PersonalPlantel"));
        String idPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
        String idPersonal = WebUtil.decode(session, request.getParameter("idPersonal"));
        if (this.validarFormDatosLaborales(session, request, response, quid, out)) {
            if (request.getParameter("situacionActual").trim().equalsIgnoreCase("activo")) {
                quid.update_SituacionPersonalPlantel("Inactivo", idPlantel, idPersonal);
            }
            Transporter tport = quid.update_PersonalPlantel(
                    request.getParameter("cargo"),
                    request.getParameter("situacionActual"),
                    request.getParameter("fechaCargo"),
                    ID_PersonalPlantel);
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El empleado fue actualizado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
            }
        }
    }

    private void vincularPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String idPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
        String idPersonal = WebUtil.decode(session, request.getParameter("idPersonal"));
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaCargo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una fecha valida.").forward(request, response);
        } else if (quid.getRowPersonalPlantel(idPersonal, idPlantel, request.getParameter("cargo")) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Esta persona ya se encuentra registrada en este plantel con ese cargo.").forward(request, response);
        } else if (!quid.select_Resguardo4Personal(idPersonal, "Activo").isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se puede asignar el cargo porque el personal tiene a resguardo bienes/artículos pendientes.").forward(request, response);
        } else if (this.validarFormDatosLaborales(session, request, response, quid, out)) {

            if (request.getParameter("situacionActual").equalsIgnoreCase("Activo")) {
                quid.update_SituacionPersonalPlantel("Inactivo", idPlantel, idPersonal);
            }

            Transporter tport = quid.insert_PersonalPlantel(
                    Integer.parseInt(idPersonal),
                    Integer.parseInt(idPlantel),
                    request.getParameter("situacionActual"),
                    request.getParameter("cargo"),
                    request.getParameter("fechaCargo"));

            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNCloseFull.jsp?title=Guardado Exitoso&type=info&msg=El empleado fue registrado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void actualizarPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdatePersonal");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormPersonal(session, request, response, quid, out)) {
                String aMaterno = (request.getParameter("aMaterno").equals("")) ? " " : request.getParameter("aMaterno").trim().toUpperCase();
                Transporter tport = quid.update_Personal(
                        request.getParameter("nombre").toUpperCase(),
                        request.getParameter("aPaterno").toUpperCase(),
                        aMaterno,
                        request.getParameter("telefono").trim(),
                        request.getParameter("preparacionProfesional").toUpperCase().trim(),
                        request.getParameter("correo").trim(),
                        request.getParameter("siglasTitulo").trim(),
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("idPersonal"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El personal fue actualizado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted No tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

    private boolean validarFormDireccion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("calle").equals("") || request.getParameter("calle").length() < 3) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una calle valida.").forward(request, response);
        } else if (request.getParameter("noExt").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un numero exterior valido o especifique S/N.").forward(request, response);
        } else if (!request.getParameter("colonia").equals("") && !StringUtil.validaColonia(request.getParameter("colonia"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una colonia valida.").forward(request, response);
        } //        else if (request.getParameter("cp").equals("") || !StringUtil.validaCodigoPostal(request.getParameter("cp"))) {
        //            this.getServletConfig().getServletContext().getRequestDispatcher(
        //                    "" + PageParameters.getParameter("msgUtil")
        //                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un codigo postal valido.").forward(request, response);
        //        }
        else {
            valido = true;
        }
        return valido;
    }

    private boolean validaFormProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (!StringUtil.validaNombre(request.getParameter("nombre"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        }// else if (!StringUtil.validaTelefono(request.getParameter("telefono"))) {
        //            this.getServletConfig().getServletContext().getRequestDispatcher(
        //                    "" + PageParameters.getParameter("msgUtil")
        //                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un telefono valido.").forward(request, response);
        //        }//
        else if (!request.getParameter("correo").trim().equals("")
                && !StringUtil.validaEmail(request.getParameter("correo").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un correo valido.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertarProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormProveedor(session, request, response, quid, out)
                && this.validarFormDireccion(session, request, response, quid, out)) {
            String clave = request.getParameter("clave").trim().equals("") ? "NA" : request.getParameter("clave").trim();
            String telefono = request.getParameter("telefono").trim().equals("") ? "NA" : request.getParameter("telefono").trim();
            String cp = request.getParameter("cp").trim().equals("") ? "NA" : request.getParameter("cp").trim();
            String correo = request.getParameter("correo").trim().equals("") ? "NA" : request.getParameter("correo").trim();
            Transporter tport = quid.insert_Proveedor(request.getParameter("nombre").trim(),
                    clave,
                    telefono,
                    correo,
                    request.getParameter("calle").trim(),
                    request.getParameter("localidad").trim(),
                    request.getParameter("colonia").trim(),
                    cp,
                    request.getParameter("noExt").trim());
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El proveedor fue registrado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void editarProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateProveedor");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormProveedor(session, request, response, quid, out)
                    && this.validarFormDireccion(session, request, response, quid, out)) {
                Transporter tport = quid.update_Proveedor(request.getParameter("nombre").trim(),
                        request.getParameter("clave").trim(),
                        request.getParameter("telefono").trim(),
                        request.getParameter("correo").trim(),
                        request.getParameter("calle").trim(),
                        request.getParameter("localidad").trim(),
                        request.getParameter("colonia").trim(),
                        request.getParameter("cp").trim(),
                        request.getParameter("noExt").trim(),
                        WebUtil.decode(session, request.getParameter("idProveedor")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El proveedor se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para relizar esta acción.").forward(request, response);
        }
    }

    private void eliminarProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("eliminarProveedor");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Proveedor(WebUtil.decode(session, request.getParameter("idProveedor")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El proveedor se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El proveedor no pudo ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        }
    }

    private void editarTipoProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateTipoProveedor");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!request.getParameter("nombreTipo").trim().equals("")) {
                Transporter tport = quid.update_TipoProveedor(request.getParameter("nombreTipo").trim().toUpperCase(),
                        WebUtil.decode(session, request.getParameter("idTipoProveedor")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El registro se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Escriba una descripción valida.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para relizar esta acción.").forward(request, response);
        }
    }

    private void eliminarTipoProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("eliminarTipoProveedor");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_TipoProveedor(WebUtil.decode(session, request.getParameter("idTipoProveedor")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no pudo ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        }
    }

    private void insertarTipoProveedor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("nombreTipo").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Escriba una descripción valida.").forward(request, response);
        } else {
            Transporter tport = quid.insert_Tipo_Proveedor(request.getParameter("nombreTipo").trim().toUpperCase());
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El tipo de proveedor fue registrado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoProveedor.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void insertarPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validarFormPersonal(session, request, response, quid, out)
                && this.validarFormDatosLaborales(session, request, response, quid, out)) {
            if (quid.getRowCurpPersonal(request.getParameter("curp").trim()) > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Información&type=info&msg=Esta persona ya se encuentra registrada, vaya a la página Vincular Personal.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                String aMaterno = (request.getParameter("aMaterno").equals("")) ? " " : request.getParameter("aMaterno").trim().toUpperCase();
                Transporter tport = quid.insert_Personal(
                        request.getParameter("curp").trim().toUpperCase(),
                        request.getParameter("nombre").trim().toUpperCase(),
                        request.getParameter("aPaterno").trim().toUpperCase(),
                        aMaterno,
                        request.getParameter("telefono").trim(),
                        request.getParameter("preparacionProfesional").toUpperCase().trim(),
                        request.getParameter("correo").trim(),
                        request.getParameter("siglasTitulo"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                if (tport.getCode() == 0) {
                    String idPersonal = tport.getMsg();
                    tport = quid.insert_PersonalPlantel(
                            Integer.parseInt(idPersonal),
                            Integer.parseInt(WebUtil.decode(session, request.getParameter("idPlantel"))),
                            request.getParameter("situacionActual"),
                            request.getParameter("cargo"),
                            request.getParameter("fechaCargo"));
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=El personal fue registrado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Información&type=info&msg=El personal fue registrado correctamente. Sin embargo no se ligó a su plantel&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPersonal.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    }
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        }
    }

    private boolean validarFormDatosLaborales(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("cargo").equals("")
                || request.getParameter("cargo").length() < 4) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un cargo valido.").forward(request, response);
        } else if (request.getParameter("situacionActual").equals("")
                || request.getParameter("situacionActual").length() < 5) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba la situación actual.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaCargo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateVincularPersonal")
                && request.getParameter("situacionActual").equalsIgnoreCase("Inactivo")
                && !quid.select_Resguardo(WebUtil.decode(session, request.getParameter("ID_PersonalPlantel")), "Activo").isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La situación actual no puede cambiar porque el personal tiene bienes/artículos a su resguardo.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private boolean validarFormPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("actualizarPersonal")
                && !StringUtil.validaCurp(request.getParameter("curp"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un CURP valido.").forward(request, response);
        } else if (!StringUtil.validaNombre(request.getParameter("nombre"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else if (!StringUtil.validaNombre(request.getParameter("aPaterno"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un apellido paterno valido.").forward(request, response);
        } else if (!request.getParameter("aMaterno").equals("") && !StringUtil.validaNombre(request.getParameter("aMaterno"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un apellido materno valido.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("actualizarPersonal")
                && !UTime.validaFecha(request.getParameter("fechaNacimiento"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de nacimiento valida.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("actualizarPersonal")
                && request.getParameter("estadoNacimiento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione la entidad de nacimiento valida.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("actualizarPersonal")
                && request.getParameter("sexo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el sexo.").forward(request, response);
        } else if (request.getParameter("preparacionProfesional").equals("")
                || request.getParameter("preparacionProfesional").length() < 5) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique la preparación profesional.").forward(request, response);
        } else if (!StringUtil.validaTelefono(request.getParameter("telefono"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un teléfono valido.").forward(request, response);
        } else if (!StringUtil.validaEmail(request.getParameter("correo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un correo valido.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("actualizarPersonal")
                && !StringUtil.matchCurp(request.getParameter("curp"),
                        request.getParameter("nombre"),
                        request.getParameter("aPaterno"),
                        request.getParameter("aMaterno"),
                        request.getParameter("fechaNacimiento"),
                        request.getParameter("sexo"),
                        WebUtil.decode(session, request.getParameter("estadoNacimiento")))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor verifique el CURP. Debe verificar el Nombre"
                    + ", Apellidos,Sexo, Fecha y Estado de nacimiento").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void updatePlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdatePlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormPlantel(session, request, response, quid, out)) {
                Transporter tport = quid.update_Plantel(
                        request.getParameter("nombre").trim(),
                        request.getParameter("direccion").trim().toUpperCase(),
                        request.getParameter("claveCentroTrabajo").trim().toUpperCase(),
                        request.getParameter("telefono"),
                        request.getParameter("correo").toLowerCase(),
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Plantel"))));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La información del plantel fue actualizada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPlanteles.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Lo sentimos no se ha podido guardar la información :(").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertPlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validarFormPlantel(session, request, response, quid, out)) {
                LinkedList idPlantel = quid.select_idPlantel(request.getParameter("nombre"));
                LinkedList cctExistente = quid.select_idPlantelXCCT(request.getParameter("claveCentroTrabajo").trim().toUpperCase());
                if (idPlantel.size() > 0 || cctExistente.size() > 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Este plantel ya se encuentra registrado.").forward(request, response);
                } else {
                    Calendar cal = Calendar.getInstance();
                    Transporter tport = quid.insert_Plantel(
                            request.getParameter("nombre").trim(),
                            request.getParameter("direccion").trim().toUpperCase(),
                            request.getParameter("claveCentroTrabajo").trim().toUpperCase(),
                            request.getParameter("telefono"),
                            request.getParameter("correo"));
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=Guardado exitoso.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPlanteles.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirect.jsp?title=Error&type=error&msg=Lo sentimos no se ha podido guardar la información :(&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("mainMenuServLet")).forward(request, response);
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msgUsted NO cuenta con el permiso para realizar esta tarea&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("mainMenuServLet")).forward(request, response);
        }
    }

    private boolean validarFormModelo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        if (!StringUtil.isValidStringLength(request.getParameter("modelo"), 1, 90)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre de modelo válido.").forward(request, response);
        } else if (request.getParameter("FK_ID_Marca").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una marca.").forward(request, response);
        } else if (quid.select_rowsModelo(WebUtil.decode(session, request.getParameter("FK_ID_Marca")),
                WebUtil.decode(session, request.getParameter("ID_Modelo")),
                request.getParameter("modelo").trim().toUpperCase()) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Este modelo YA se encuentra registrado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        } else {
            todoBien = true;
        }
        return todoBien;
    }

    private boolean validarFormPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean todoBien = false;
        if (!StringUtil.isValidStringLength(request.getParameter("nombre"), 1, 90)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre válido.").forward(request, response);
        } else if (!StringUtil.isValidStringLength(request.getParameter("direccion"), 1, 340)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese una direccion válida.").forward(request, response);
        } else if (!StringUtil.isValidStringLength(request.getParameter("claveCentroTrabajo"), 1, 50)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese una clave del centro de trabajo válida.").forward(request, response);
        } else if (!StringUtil.isValidStringLength(request.getParameter("telefono"), 1, 20)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese un número telefónico válido.").forward(request, response);
        } else if (!StringUtil.validaEmail(request.getParameter("correo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La cuenta de correo electrónico no es válida.").forward(request, response);
        } else {
            todoBien = true;
        }
        return todoBien;
    }

    private boolean validaFormUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("plantel").equalsIgnoreCase("")) {

            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if ((request.getParameter("password").trim().equals("")
                || request.getParameter("password") == null)
                || !request.getParameter("password").equals(request.getParameter("password2"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña no coincide o está en blanco.").forward(request, response);
        } else if (!StringUtil.validaNombre(request.getParameter("nombreCompleto"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else if (!StringUtil.validaEmail(request.getParameter("correo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor escriba un correo valido.").forward(request, response);
        } else if (request.getParameter("rol").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione un Rol.").forward(request, response);
        } else if (!StringUtil.validaPassword(request.getParameter("password"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña no cumple con los requisitos.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void actualizarPassUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out, boolean revisarPassActual) throws Exception {
        String tipoRol = session.getAttribute("tipoRol").toString();
        String passwordActual = "";
        if (revisarPassActual) {
            passwordActual = quid.select_PasswordUsuario(session.getAttribute("userID").toString());
        }

        if (revisarPassActual && request.getParameter("passwordAnterior").equals("")
                || (revisarPassActual && !passwordActual.equals(
                        JHash.getStringMessageDigest(
                                request.getParameter("passwordAnterior"), JHash.MD5)))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña actual no es correcta.").forward(request, response);
        } else if ((request.getParameter("password").trim().equals("")
                || request.getParameter("password") == null)
                || !request.getParameter("password").equals(
                request.getParameter("password2"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña no coincide o esta en blanco.").forward(request, response);
        } else if (!StringUtil.validaPassword(request.getParameter("password"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña no cumple con los requisitos.").forward(request, response);
        } else {

            Transporter tport = null;
            if (revisarPassActual && request.getParameter("idPersonal") == null) {
                tport = quid.update_PasswordUsuario(
                        request.getParameter("password"),
                        session.getAttribute("userID").toString());
            }
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El password se ha guardado correctamente.&url=" + PageParameters.getParameter("mainMenu") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void actualizarUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("plantel").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!StringUtil.validaNombre(request.getParameter("nombreCompleto"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else if (!StringUtil.validaEmail(request.getParameter("correo"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor escriba un correo valido.").forward(request, response);
        } else if (request.getParameter("rol").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione un Rol.").forward(request, response);
        } else if (!request.getParameter("password").trim().equals("")) {
            if (!request.getParameter("password").equals(request.getParameter("password2"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Las contraseñas no coinciden o estan en blanco.").forward(request, response);
            } else if (!StringUtil.validaPassword(request.getParameter("password"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=La contraseña no cumple con los requisitos.").forward(request, response);
            } else {
                actualizarUsuario(true, session, request, response, quid, out);
            }
        } else {
            actualizarUsuario(false, session, request, response, quid, out);
        }
    }

    private void actualizarUsuario(boolean changePassword, HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        Transporter tport = null;
        if (changePassword) {
            tport = quid.update_Usuario(
                    request.getParameter("usuario").trim(),
                    request.getParameter("password"),
                    request.getParameter("correo").trim(),
                    request.getParameter("nombreCompleto").trim(),
                    Short.parseShort(WebUtil.decode(session, request.getParameter("estatus"))),
                    WebUtil.decode(session, request.getParameter("plantel")),
                    WebUtil.decode(session, request.getParameter("idUsuario")));
        } else {
            tport = quid.update_UsuarioIgnorePassword(
                    request.getParameter("usuario").trim(),
                    request.getParameter("correo").trim(),
                    request.getParameter("nombreCompleto").trim(),
                    Short.parseShort(WebUtil.decode(session, request.getParameter("estatus"))),
                    WebUtil.decode(session, request.getParameter("plantel")),
                    WebUtil.decode(session, request.getParameter("idUsuario")));
        }
        if (tport.getCode() == 0) {
            tport = quid.update_RolUsuario(
                    WebUtil.decode(session, request.getParameter("rol")),
                    WebUtil.decode(session, request.getParameter("idRolUsuario")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El usuario se ha guardado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaUsuarios.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El Rol no se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaUsuarios.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNBackFull.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
        }
    }

    //Validar para que no se puedan borrar registros que esten en otra tabla...
    private void InsertMarca(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertMarca");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreMarca = quid.select_rowsMarca(request.getParameter("nombreMarca").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreMarca"), 1, 20)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la marca debe contener entre 1 y 20 caracteres").forward(request, response);
            } else if (nombreMarca > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la marca YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_Marca(
                        request.getParameter("nombreMarca").trim().toUpperCase());
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente el nombre de la marca.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMarca.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private boolean validFormGrupo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("idDepartamento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un departamento.").forward(request, response);
        } else if (request.getParameter("nombreGrupo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertGrupo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validFormGrupo(session, request, response, quid, out)) {
            Transporter tport = quid.insert_Grupo(
                    request.getParameter("nombreGrupo").trim().toUpperCase(),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()));
            if (tport.getCode() == 0) {
                tport = quid.insert_RelacionPDG(tport.getMsg(), WebUtil.decode(session, request.getParameter("idDepartamento")));
                if (tport.getCode() == 0) {
                    request.getRequestDispatcher("/ajaxFunctions/getGrupo4Plantel.jsp").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al asociar el grupo.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al guardar el grupo.").forward(request, response);
            }
        }
    }

    private void insertCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertCategoria");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreMarca = quid.select_rowsCategoria(request.getParameter("nombreCategoria").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreCategoria"), 1, 50)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre categoría debe contener entre 1 y 50 caracteres").forward(request, response);
            } else if (nombreMarca > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la categoría YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_Categoria(
                        request.getParameter("nombreCategoria").trim().toUpperCase());
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente la nueva categoría.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertTipo_Software(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertTipo_Software");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int tipo = quid.select_rowsTipo_Software(request.getParameter("tipo").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("tipo"), 1, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El tipo de software debe contener entre 1 y 100 caracteres").forward(request, response);
            } else if (tipo > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la marca YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_Tipo_Software(
                        request.getParameter("tipo").trim().toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente el tipo de software.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Software.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertLicencia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertLicencia");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreSoftware = quid.select_rowsLicencia(request.getParameter("nombreLicencia").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreLicencia"), 1, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la licencia debe contener entre 1 y 100 caracteres").forward(request, response);
            } else if (nombreSoftware > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre de la licencia YA se encuentra registrado.").forward(request, response);
            } else {
                Transporter tport = quid.insert_Licencia(
                        request.getParameter("nombreLicencia").toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente el tipo de licencia.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaLicencia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertNombreSoftware(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertNombreSoftware");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreNombreSoftware = quid.select_rowsNombreSoftware(request.getParameter("nombreSoftware").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreSoftware"), 1, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del software debe contener entre 1 y 100 caracteres").forward(request, response);
            } else if (nombreNombreSoftware > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del software YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_NombreSoftware(
                        request.getParameter("nombreSoftware").toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente el nombre del software.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaNombreSoftware.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario NO válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertTipo_Compra(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertTipoCompra");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int nombreCompra = quid.select_rowsTipo_Compra(request.getParameter("compra").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("compra"), 1, 20)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del tipo de compra debe contener entre 1 y 20 caracteres").forward(request, response);
            } else if (nombreCompra > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El tipo de compra YA se encuentra registrado.").forward(request, response);
            } else {
                Transporter tport = quid.insert_Tipo_Compra(
                        request.getParameter("compra").trim().toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El tipo de compra se ha guardado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Compra.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario No válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertTipo_Garantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertTipo_Garantia");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int garantia = quid.select_rowsTipo_Garantia(request.getParameter("garantia").toUpperCase());
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("garantia"), 1, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El tipo de garantía debe contener entre 1 y 100 caracteres").forward(request, response);
            } else if (garantia > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del tipo de compra YA se encuentra registrado.").forward(request, response);

            } else {
                Transporter tport = quid.insert_Tipo_Garantia(
                        request.getParameter("garantia").trim().toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente el tipo de garantía.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipo_Garantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertModelo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertModelo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("modelo"), 1, 100)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del modelo debe contener entre 1 y 20 caracteres").forward(request, response);
            } else if (request.getParameter("FK_ID_Marca").equalsIgnoreCase("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione una marca.").forward(request, response);
//            } else if (!StringUtil.isValidStringLength(request.getParameter("descripcion"), 1, 100)) {
//                this.getServletConfig().getServletContext().getRequestDispatcher(
//                        "" + PageParameters.getParameter("msgUtil")
//                        + "/msgNBackFull.jsp?title=Error&type=error&msg=La descripción debe contener entre 1 y 100 caracteres").forward(request, response);
//
//            }
            } else if (quid.select_rowsModelo(WebUtil.decode(session, request.getParameter("FK_ID_Marca")), request.getParameter("modelo").trim().toUpperCase()) > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=El nombre del modelo YA se encuentra registrado.").forward(request, response);

            } else {
                String desc = request.getParameter("descripcion").equals("") ? " " : request.getParameter("descripcion").trim();
                Transporter tport = quid.insert_Modelo(
                        request.getParameter("modelo").trim().toUpperCase(),
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("FK_ID_Marca"))),
                        desc);
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El modelo se ha guardado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModelo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertSubCategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertSubCategoria");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        int SubCategoria = quid.select_rowsSubCategoria(request.getParameter("nombreSubCategoria").toUpperCase(),
                WebUtil.decode(session, request.getParameter("FK_ID_Categoria")));
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isValidStringLength(request.getParameter("nombreSubCategoria"), 1, 50)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=La subcategoría debe contener entre 1 y 50 caracteres").forward(request, response);
            } else if (request.getParameter("FK_ID_Categoria").equalsIgnoreCase("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor seleccione una categoría.").forward(request, response);
            } else if (SubCategoria > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=La subcategoría YA se encuentra registrada.").forward(request, response);
            } else {
                Transporter tport = quid.insert_SubCategoria(
                        Integer.parseInt(WebUtil.decode(session, request.getParameter("FK_ID_Categoria"))),
                        request.getParameter("nombreSubCategoria").toUpperCase());
                if (tport.getCode() == 0) {

                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Se ha guardado correctamente la subcategoría.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usuario no válido para realizar esta tarea.").forward(request, response);
        }
    }

    private void insertarUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormUsuario(session, request, response, quid, out)) {
            if (!StringUtil.isValidStringLength(request.getParameter("usuario"), 3, 20)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=EL usuario debe tener entre 3 y 20 caracteres").forward(request, response);
            } else {
                Transporter tport = quid.insert_Usuario(
                        request.getParameter("usuario").trim(),
                        request.getParameter("password"),
                        request.getParameter("correo").trim(),
                        request.getParameter("nombreCompleto").trim(),
                        Short.parseShort("0"),
                        WebUtil.decode(session, request.getParameter("plantel")));
                if (tport.getCode() == 0) {
                    tport = quid.insert_RolUsuario(tport.getMsg(), WebUtil.decode(session, request.getParameter("rol")));
                    if (tport.getCode() == 0) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El usuario se ha creado correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaUsuarios.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El Rol no se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaUsuarios.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    }
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=" + tport.getMsg() + "").forward(request, response);
                }
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    private void insertarNuevoRol(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            if (!StringUtil.isValidStringLength(request.getParameter("rol"), 3, 60)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor introduzca un nombre para el nuevo rol.").forward(request, response);
            } else if (!StringUtil.isValidStringLength(request.getParameter("dscrol"), 3, 199)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor introduzca una descripción para el nuevo rol.").forward(request, response);
            } else if (this.validaParametersPermisos(request) == false) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Debe de seleccionar por lo menos un permiso.").forward(request, response);
            } else {

                boolean error = false;
                int noparametros = Integer.parseInt(request.getParameter("numparam"));
                String ID_Rol = quid.insert_newRol(request.getParameter("rol"), request.getParameter("dscrol"));

                if (ID_Rol != null) {
                    for (int i = 1; i <= noparametros; i++) {
                        String paramPermiso = WebUtil.decode(session, request.getParameter("option" + i));
                        if (paramPermiso != null && paramPermiso.equals("") != true) {
                            Transporter tport = quid.insert_RolPermiso(Integer.parseInt(ID_Rol), Integer.parseInt(paramPermiso));
                            if (tport.getCode() != 0) {
                                error = true;
                            }
                        }
                    }

                    if (error == false) {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El rol se ha dado de alta exitosamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRol.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Error inesperado, no fue posible asignar permisos.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRol.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                    }

                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Error al dar de alta el nuevo rol.").forward(request, response);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void editarRolUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {
            if (!StringUtil.isValidStringLength(request.getParameter("rol"), 3, 60)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor introduzca un nombre para el nuevo rol.").forward(request, response);
            } else if (!StringUtil.isValidStringLength(request.getParameter("dscrol"), 3, 199)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Por favor introduzca una descripción para el nuevo rol.").forward(request, response);
            } else if (this.validaParametersPermisos(request) == false) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=Debe de seleccionar por lo menos un permiso.").forward(request, response);
            } else {
                boolean error = false;
                int noparametros = Integer.parseInt(request.getParameter("numparam"));
                Transporter tpor1 = quid.update_RolInfo(WebUtil.decode(session, request.getParameter("id_rol")), request.getParameter("rol"), request.getParameter("dscrol"));

                if (tpor1.getCode() == 0) {
                    //*** Elimina los permisos y los vuelve a dar de alta ***//
                    Transporter tpor2 = quid.delete_rolPermisos(WebUtil.decode(session, request.getParameter("id_rol")));
                    if (tpor2.getCode() == 0) {

                        for (int i = 1; i <= noparametros; i++) {
                            String paramPermiso = WebUtil.decode(session, request.getParameter("option" + i));
                            if (paramPermiso != null && paramPermiso.equals("") != true) {
                                Transporter tport = quid.insert_RolPermiso(Integer.parseInt(WebUtil.decode(session, request.getParameter("id_rol"))), Integer.parseInt(paramPermiso));
                                if (tport.getCode() != 0) {
                                    error = true;
                                }
                            }
                        }
                        LinkedList accessos = quid.select_permisosPorUsuarios(session.getAttribute("userID").toString());
                        accessos.add("LoggedUser");
                        session.removeAttribute("userAccess");
                        session.setAttribute("userAccess", accessos);
                        if (error == false) {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=Los datos del rol se actualizaron correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRol.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                        } else {
                            this.getServletConfig().getServletContext().getRequestDispatcher(
                                    "" + PageParameters.getParameter("msgUtil")
                                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Error inesperado, no fue posible asignar permisos.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRol.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                        }
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msgNBackFull.jsp?title=Error&type=error&msg=Error inesperado, no se pudieron actualizar los permisos del rol.").forward(request, response);
                    }

                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Error al editar el rol de usuario.").forward(request, response);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private void borrarRolUsuario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        try {

            if (quid.select_rolOcupado(WebUtil.decode(session, request.getParameter("id_rol"))) == false) {
                //*** Elimina el rol y sus permisos de ROL_PERMISOS ***//
                Transporter tpor2 = quid.delete_rolUsuario(WebUtil.decode(session, request.getParameter("id_rol")));
                if (tpor2.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Guardado Exitoso&type=info&msg=El Rol se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRol.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Error inesperado, no se pudo eliminar el rol de usuario.").forward(request, response);
                }

            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBackFull.jsp?title=Error&type=error&msg=No es posible eliminar este rol, actualmente esta en uso.").forward(request, response);

            }

        } catch (Exception ex) {
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);

            this.getServletConfig()
                    .getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNBackFull.jsp?title=Error&type=error&msg=Lo sentimos la página  ha tenido un error :(").forward(request, response);
        }
    }

    private Boolean validaParametersPermisos(HttpServletRequest request) {
        Boolean valido = false;
        int noparametros = Integer.parseInt(request.getParameter("numparam"));

        for (int i = 1; i <= noparametros; i++) {
            if (request.getParameter("option" + i) != null) {
                valido = true;
            }
        }

        return valido;

    }

    private boolean validaFormDetalle(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("nombreDetalle").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese una descripción válida.").forward(request, response);
            //} else if (request.getParameter("valor").equalsIgnoreCase("")) {
//            this.getServletConfig().getServletContext().getRequestDispatcher(
//                    "" + PageParameters.getParameter("msgUtil")
//                    + "/msg.jsp?title=Error&type=error&msg=Por favor ingrese un valor válido.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void nuevoDetalle(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormDetalle(session, request, response, quid, out)) {
            Transporter tport = quid.insert_Detalle(request.getParameter("nombreDetalle").trim().toUpperCase());
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La información se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarDetalle(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteDetalle");

        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Detalle(WebUtil.decode(session, request.getParameter("idDetalle")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Borrado Exitoso&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no pudo ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted no tiene permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        }
    }

    private void editarDetalle(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateDetalle");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormDetalle(session, request, response, quid, out)) {
                Transporter tport = quid.update_Detalle(request.getParameter("nombreDetalle").trim().toUpperCase(),
                        WebUtil.decode(session, request.getParameter("idDetalle")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Guardado Exitoso&type=info&msg=La información fue actualizada correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted no tiene los permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void insertarValor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (!request.getParameter("valor").trim().equalsIgnoreCase("")) {
            Transporter tport = quid.insert_Valor(WebUtil.decode(session, request.getParameter("idDetalle")), request.getParameter("valor").trim().toUpperCase());
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
            request.getRequestDispatcher("/ajaxFunctions/getValores4Detalle.jsp").forward(request, response);
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El valor no es valido.").forward(request, response);
        }
    }

    private void eliminarValor(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        Transporter tport = quid.delete_Valor(WebUtil.decode(session, request.getParameter("idValor")));
        if (tport.getCode() == 0) {
            request.getRequestDispatcher("/ajaxFunctions/getValores4Detalle.jsp").forward(request, response);
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaDetalle.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "").forward(request, response);
        }

    }

    private void insertarDetalleSubcategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (quid.select_Detalle_SubCategoria(WebUtil.decode(session, request.getParameter("idSubCategoria")),
                WebUtil.decode(session, request.getParameter("idDetalle"))).isEmpty()) {
            Transporter tport = quid.insert_DetalleSubcategoria(WebUtil.decode(session, request.getParameter("idDetalle")),
                    WebUtil.decode(session, request.getParameter("idSubCategoria")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El detalle se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertDetalleSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_SubCategoria=" + request.getParameter("idSubCategoria")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertDetalleSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_SubCategoria=" + request.getParameter("idSubCategoria")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El detalle ya existe.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertDetalleSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_SubCategoria=" + request.getParameter("idSubCategoria")).forward(request, response);
        }
    }

    private void eliminarDetalleSubcategoria(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        Transporter tport = quid.delete_DetalleSubcategoria(WebUtil.decode(session, request.getParameter("idDetalleSubcategoria")));
        if (tport.getCode() == 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El detalle se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertDetalleSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_SubCategoria=" + request.getParameter("idSubCategoria")).forward(request, response);
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertDetalleSubCategoria.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_SubCategoria=" + request.getParameter("idSubCategoria")).forward(request, response);
        }
    }

    private boolean validaFormBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        String noInventario = "";
        String noSerie = "";
        String idBien = WebUtil.decode(session, request.getParameter("idBien"));
        if (request.getParameter("FormFrom").equalsIgnoreCase("updateBien")) {
            noSerie = quid.select_Bien4Campo("NOSERIE", idBien);
            noInventario = quid.select_Bien4Campo("NOINVENTARIO", idBien);
        }

        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("idDepartamento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un departamento.").forward(request, response);
        } else if (request.getParameter("idCategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una categoria.").forward(request, response);
        } else if (request.getParameter("idSubcategoria") == null
                || request.getParameter("idSubcategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una subcategoria.").forward(request, response);
        } else if (request.getParameter("idMarca").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una marca.").forward(request, response);
        } else if (request.getParameter("idModelo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un modelo.").forward(request, response);
        } else if (request.getParameter("idTipoCompra").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de compra.").forward(request, response);
        } else if (request.getParameter("noSerie").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de serie.").forward(request, response);
        } else if (!SystemUtil.haveAcess("evitarPatronSerie", (LinkedList<String>) session.getAttribute("userAccess"))
                && !this.validaSerialXModelo(session, request, response, quid, out)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de serie no coincide con el patron del modelo seleccionado.").forward(request, response);
        } else if (!request.getParameter("fechaCompra").equals("") && !UTime.validaFecha(request.getParameter("fechaCompra"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        } else if (request.getParameter("status").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && quid.select_CountBien4Campo("noSerie", request.getParameter("noSerie").trim()) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de serie ya existe.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && !request.getParameter("noInventario").equalsIgnoreCase("00000")
                && quid.select_CountBien4Campo("noInventario", request.getParameter("noInventario").trim()) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de inventario ya existe.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && !noInventario.equalsIgnoreCase(request.getParameter("noInventario"))
                && !request.getParameter("noInventario").equalsIgnoreCase("00000")
                && quid.select_CountBien4Campo("noInventario", request.getParameter("noInventario")) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de inventario ya existe.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && !noSerie.equalsIgnoreCase(request.getParameter("noSerie"))
                && quid.select_CountBien4Campo("noSerie", request.getParameter("noSerie")) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de serie ya existe.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && quid.select_Bien4Campo("STATUS", WebUtil.decode(session, request.getParameter("idBien"))).equalsIgnoreCase("Baja")
                && !SystemUtil.haveAcess("eliminarBaja", (LinkedList<String>) session.getAttribute("userAccess"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El bien/articulo no puede ser actualizado porque ya esta dado de baja.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateBien")
                && !quid.select_Bien4Campo("FK_ID_SubCategoria", WebUtil.decode(session, request.getParameter("idBien"))).equalsIgnoreCase(WebUtil.decode(session, request.getParameter("idSubcategoria")))
                && !quid.select_Detalle4Bien(WebUtil.decode(session, request.getParameter("idBien"))).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se puede actualizar el bien/articulo porque tiene especificaciones de otra subcategoria.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertarBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormBien(session, request, response, quid, out)) {
            String noDictamen = request.getParameter("noDictamen").equals("") ? " " : request.getParameter("noDictamen").trim().toUpperCase();
            String noFactura = request.getParameter("noFactura").equals("") ? " " : request.getParameter("noFactura").trim().toUpperCase();
            String fechaCompra = request.getParameter("fechaCompra").equals("") ? " " : request.getParameter("fechaCompra").trim();
            String obs = request.getParameter("observaciones").equals("") ? " " : request.getParameter("observaciones").trim().toUpperCase();
            String desc = request.getParameter("descripcion").equals("") ? " " : request.getParameter("descripcion").trim().toUpperCase();
            String noInventario = request.getParameter("noInventario").equals("") ? "00000" : request.getParameter("noInventario").trim().toUpperCase();
            int sucess = 0;
            Transporter tport = quid.insert_Bien(
                    WebUtil.decode(session, request.getParameter("idSubcategoria")),
                    WebUtil.decode(session, request.getParameter("idModelo")),
                    desc,
                    request.getParameter("noSerie").trim().toUpperCase(),
                    noDictamen,
                    noFactura,
                    noInventario,
                    WebUtil.decode(session, request.getParameter("idTipoCompra")),
                    fechaCompra,
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    WebUtil.decode(session, request.getParameter("idDepartamento")),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("status")).toUpperCase(),
                    obs,
                    " ");
            if (tport.getCode() == 0) {
                String idBien = tport.getMsg();
                Iterator t = quid.select_Detalle4Subcategoria(WebUtil.decode(session, request.getParameter("idSubcategoria"))).iterator();
                while (t.hasNext()) {
                    LinkedList dat = (LinkedList) t.next();
                    if (request.getParameter(WebUtil.encode(session, dat.get(5))) != null
                            && !request.getParameter(WebUtil.encode(session, dat.get(5))).equalsIgnoreCase("")) {
                        tport = quid.insert_RelacionBDS(idBien,
                                dat.get(5).toString(),
                                WebUtil.decode(session, request.getParameter(WebUtil.encode(session, dat.get(5)))));
                        if (tport.getCode() == 0) {
                            sucess += 1;
                        }
                    }
                }
                if (!request.getParameter("idGrupo").equals("")) {
                    tport = quid.insert_GrupoBien(idBien, WebUtil.decode(session, request.getParameter("idGrupo")));
                }
                String msg = "";
                if (tport.getCode() != 0) {
                    msg = "Ocurrió un error al ligar el grupo.";
                }
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Guardado Exitoso&type=info&msg=El bien/artículo se guardo correctamente. Se guardaron " + sucess + " detalles. " + msg).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void actualizarBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateBien");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormBien(session, request, response, quid, out)) {
                String noDictamen = request.getParameter("noDictamen").equals("") ? " " : request.getParameter("noDictamen").trim().toUpperCase();
                String noFactura = request.getParameter("noFactura").equals("") ? " " : request.getParameter("noFactura").trim().toUpperCase();
                String fechaCompra = request.getParameter("fechaCompra").equals("") ? " " : request.getParameter("fechaCompra").trim();
                String obs = request.getParameter("observaciones").equals("") ? " " : request.getParameter("observaciones").trim().toUpperCase();
                String desc = request.getParameter("descripcion").equals("") ? " " : request.getParameter("descripcion").trim().toUpperCase();
                String idGrupoBien = !request.getParameter("idGrupoBien").equalsIgnoreCase("") ? WebUtil.decode(session, request.getParameter("idGrupoBien")) : "";
                String noInventario = request.getParameter("noInventario").equals("") ? "00000" : request.getParameter("noInventario").trim().toUpperCase();
                String descError = "";
                String groupOperation = "none";
                if (!request.getParameter("idGrupoActual").equalsIgnoreCase("")
                        && !request.getParameter("idGrupo").equalsIgnoreCase("")
                        && !WebUtil.decode(session, request.getParameter("idGrupoActual")).equalsIgnoreCase(WebUtil.decode(session, request.getParameter("idGrupo")))) {
                    groupOperation = "update";
                } else if (request.getParameter("idGrupoActual").equalsIgnoreCase("")
                        && request.getParameter("idGrupo") != null
                        && !request.getParameter("idGrupo").equalsIgnoreCase("")) {
                    groupOperation = "insert";
                } else if (!request.getParameter("idGrupoActual").equalsIgnoreCase("")
                        && request.getParameter("idGrupo").equalsIgnoreCase("")) {
                    groupOperation = "delete";
                }
                String msg = request.getParameter("idGrupoActual").equalsIgnoreCase("") ? " Utilize la sección de grupos para asignar el bien a un grupo." : "";
                boolean deleteBaja = false;
                LinkedList idBaja = null;
                if (quid.select_Bien4Campo("STATUS", WebUtil.decode(session, request.getParameter("idBien"))).equalsIgnoreCase("Baja")
                        && !WebUtil.decode(session, request.getParameter("status")).equalsIgnoreCase("Baja")) {
                    deleteBaja = true;
                    idBaja = quid.select_ID_Baja4Bien(WebUtil.decode(session, request.getParameter("idBien")));
                }
                Transporter tport = quid.trans_update_Bien(
                        WebUtil.decode(session, request.getParameter("idSubcategoria")),
                        WebUtil.decode(session, request.getParameter("idModelo")),
                        desc,
                        request.getParameter("noSerie").toUpperCase(),
                        noDictamen,
                        noFactura,
                        noInventario,
                        WebUtil.decode(session, request.getParameter("idTipoCompra")),
                        fechaCompra,
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("idDepartamento")),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        WebUtil.decode(session, request.getParameter("status")).toUpperCase(),
                        obs,
                        " ",
                        WebUtil.decode(session, request.getParameter("idGrupo")),
                        idGrupoBien,
                        WebUtil.decode(session, request.getParameter("idBien")),
                        groupOperation,
                        deleteBaja,
                        idBaja);
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Completada&type=info&msg=La información fue actualizada correctamente." + msg + "&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted no tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void eliminarBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteBien");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Bien(WebUtil.decode(session, request.getParameter("idBien")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El bien se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El bien/artículo no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permios para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void eliminarBDS(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteBDS");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_BDS(WebUtil.decode(session, request.getParameter("idBds")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El detalle se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBDS.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBDS.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permios para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBDS.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }

    }

    private void insertarBDS(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        int sucess = 0;
        Iterator t = quid.select_Detalle4Subcategoria(WebUtil.decode(session, request.getParameter("idSubcategoria"))).iterator();
        String idBien = WebUtil.decode(session, request.getParameter("idBien"));
        while (t.hasNext()) {
            LinkedList dat = (LinkedList) t.next();
            if (request.getParameter(WebUtil.encode(session, dat.get(5))) != null
                    && !request.getParameter(WebUtil.encode(session, dat.get(5))).equalsIgnoreCase("")
                    && quid.select_RelacionBDS(idBien, dat.get(5).toString()).isEmpty()) {
                Transporter tport = quid.insert_RelacionBDS(idBien,
                        dat.get(5).toString(),
                        WebUtil.decode(session, request.getParameter(WebUtil.encode(session, dat.get(5)))));
                if (tport.getCode() == 0) {
                    sucess += 1;
                }
            }
        }
        this.getServletConfig().getServletContext().getRequestDispatcher(
                "" + PageParameters.getParameter("msgUtil")
                + "/msgNRedirect.jsp?title=Operación terminada&type=info&msg=Se guardaron " + sucess + " detalles.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBDS.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
    }

    private void eliminarGrupoBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteGrupoBien");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_GrupoBien(WebUtil.decode(session, request.getParameter("idGrupoBien")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=Se eliminó correctamente el bien del grupo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void insertarGrupoBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (quid.select_GrupoBien4Bien(WebUtil.decode(session, request.getParameter("idBien"))).isEmpty()) {
            LinkedList pdg = quid.select_PDG(WebUtil.decode(session, request.getParameter("idPdg")));
            if (!pdg.isEmpty()) {
                Long idInserted = quid.trans_insert_GrupoBien(
                        WebUtil.decode(session, request.getParameter("idBien")),
                        WebUtil.decode(session, request.getParameter("idGrupo")),
                        pdg.get(1).toString(),
                        WebUtil.decode(session, request.getParameter("idPlantel")));
                if (idInserted >= 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El bien/artículo se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El bien/artículo no se pudo asignar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=No se pudo obtener la información del bien/artículo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El bien ya esta registrado en un grupo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void insertarPBT(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idTipo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione el tipo de proveedor.").forward(request, response);
        } else if (request.getParameter("idProveedor").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione el proveedor.").forward(request, response);
        } else if (!quid.select_PBT(WebUtil.decode(session, request.getParameter("idBien")),
                WebUtil.decode(session, request.getParameter("idTipo"))).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tipo de proveedor ya esta registrado.").forward(request, response);
        } else {
            Transporter tport = quid.insert_RelacionPBT(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    WebUtil.decode(session, request.getParameter("idTipo")),
                    WebUtil.decode(session, request.getParameter("idProveedor")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El proveedor se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPBD.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarPBT(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeletePBT");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_PBT(WebUtil.decode(session, request.getParameter("idPBT")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=Se eliminó correctamente el proveedor del bien.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPBD.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPBD.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPBD.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void insertarPBTFull(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idTipo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione el tipo de proveedor.").forward(request, response);
        } else if (request.getParameter("idProveedor").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione el proveedor.").forward(request, response);
        } else {
            String idGrupo = WebUtil.decode(session, request.getParameter("idGrupo"));
            String idTipo = WebUtil.decode(session, request.getParameter("idTipo"));
            String idProveedor = WebUtil.decode(session, request.getParameter("idProveedor"));
            boolean error = false;
            Iterator bienesGrupo = quid.select_Bien4Grupo(idGrupo, "Baja", false, true).iterator();
            while (bienesGrupo.hasNext()) {
                LinkedList bien = (LinkedList) bienesGrupo.next();
                if (quid.select_PBT(bien.get(0).toString(), idTipo).isEmpty()) {
                    if (quid.insert_RelacionPBT(bien.get(0).toString(), idTipo, idProveedor).getCode() != 0) {
                        error = true;
                    }
                }
            }
            if (!error) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El proveedor se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error durante la operación.").forward(request, response);
            }
        }
    }

    private void eliminarGarantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteGarantia");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Garantia(WebUtil.decode(session, request.getParameter("idGarantia")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=Se eliminó correctamente la garantía del bien.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGarantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGarantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGarantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private boolean validaFormGarantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("tipoGarantia").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione el tipo de garantía.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaInicio"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione una fecha de inicio valida.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaFin"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione una fecha de termino valida.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("insertGarantia")
                && quid.select_CountGarantia(WebUtil.decode(session, request.getParameter("idBien")),
                        WebUtil.decode(session, request.getParameter("tipoGarantia"))) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tipo de garantía ya esta registrado.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateGarantia")
                && !WebUtil.decode(session, request.getParameter("tipoActual")).equalsIgnoreCase(WebUtil.decode(session, request.getParameter("tipoGarantia")))
                && quid.select_CountGarantia(WebUtil.decode(session, request.getParameter("idBien")),
                        WebUtil.decode(session, request.getParameter("tipoGarantia"))) > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tipo de garantía ya esta registrado.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertarGarantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormGarantia(session, request, response, quid, out)) {
            Transporter tport = quid.insert_Garantia(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin"),
                    WebUtil.decode(session, request.getParameter("tipoGarantia")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La garantía se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGarantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }

        }

    }

    private void actualizarGarantia(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormGarantia(session, request, response, quid, out)) {
            Transporter tport = quid.update_Garantia(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin"),
                    WebUtil.decode(session, request.getParameter("tipoGarantia")),
                    WebUtil.decode(session, request.getParameter("idGarantia")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La garantía se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGarantia.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void insertarGarantiaFull(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormGarantia(session, request, response, quid, out)) {
            String idGrupo = WebUtil.decode(session, request.getParameter("idGrupo"));
            String idTipoGarantia = WebUtil.decode(session, request.getParameter("tipoGarantia"));
            Iterator bienesGrupo = quid.select_Bien4Grupo(idGrupo, "Baja", false, true).iterator();
            boolean error = false;
            while (bienesGrupo.hasNext()) {
                LinkedList bien = (LinkedList) bienesGrupo.next();
                if (quid.select_CountGarantia(bien.get(0).toString(), idTipoGarantia) <= 0) {
                    if (quid.insert_Garantia(bien.get(0).toString(), request.getParameter("fechaInicio"), request.getParameter("fechaFin"),
                            idTipoGarantia).getCode() != 0) {
                        error = true;
                    }
                }
            }
            if (!error) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La garantía se asigno correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaGrupoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idGrupo=" + request.getParameter("idGrupo") + "_param_idPdg=" + request.getParameter("idPdg") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error durante la operación.").forward(request, response);
            }

        }

    }

    private boolean validaFormBaja(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (!UTime.validaFecha(request.getParameter("fechaBaja"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione una fecha valida.").forward(request, response);
        } else if (request.getParameter("noOficio").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de oficio.").forward(request, response);
        } else if (request.getParameter("motivoBaja").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el motivo de baja.").forward(request, response);
        } else if (quid.select_Bien4Campo("STATUS", WebUtil.decode(session, request.getParameter("idBien"))).equalsIgnoreCase("Funcional")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El estatus del bien/articulo es Funcional.").forward(request, response);
        } else if (!quid.select_Baja4Bien(WebUtil.decode(session, request.getParameter("idBien"))).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El bien seleccionado ya esta dado de baja, Por favor actualize el estatus.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertarBaja(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormBaja(session, request, response, quid, out)) {
            String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
            Transporter tport = quid.insert_Baja(
                    request.getParameter("fechaBaja"),
                    request.getParameter("noOficio").trim(),
                    obs,
                    WebUtil.decode(session, request.getParameter("motivoBaja")),
                    WebUtil.decode(session, request.getParameter("idBien")));
            if (tport.getCode() == 0) {
                tport = quid.update_Status4Bien("BAJA", WebUtil.decode(session, request.getParameter("idBien")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La baja se registro correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/UpdateBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error al actualizar el estatus del bien.").forward(request, response);
                }
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La baja se registro correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/UpdateBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private boolean validaFormSolicitud(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaSolicitud"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor selecione una fecha valida.").forward(request, response);
        } else if (request.getParameter("numeroOficio").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de oficio.").forward(request, response);
        } else if (request.getParameter("nombreSolicitante").equals("") || !StringUtil.validaNombre(request.getParameter("nombreSolicitante"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else if (request.getParameter("nombreResponsable").equals("") || !StringUtil.validaNombre(request.getParameter("nombreResponsable"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un nombre valido.").forward(request, response);
        } else {
            valido = true;
        }

        return valido;
    }

    private void insertarSolicitud(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormSolicitud(session, request, response, quid, out)) {
            String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
            Transporter tport = quid.insert_Solicitud(
                    request.getParameter("numeroOficio").trim(),
                    request.getParameter("nombreSolicitante").trim(),
                    request.getParameter("fechaSolicitud").trim(),
                    request.getParameter("nombreResponsable").trim(),
                    "En espera",
                    obs,
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    "BAJA");
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La solicitud se registro correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarSolicitud(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteSolicitud");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Solicitud(WebUtil.decode(session, request.getParameter("idSolicitud")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=Se eliminó correctamente la solicitud.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void actualizarSolicitud(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateSolicitud");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormSolicitud(session, request, response, quid, out)) {
                String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
                Transporter tport = quid.update_Solicitud(
                        request.getParameter("numeroOficio").trim(),
                        request.getParameter("nombreSolicitante").trim(),
                        request.getParameter("fechaSolicitud"),
                        request.getParameter("nombreResponsable").trim(),
                        WebUtil.decode(session, request.getParameter("status")),
                        obs,
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("idSolicitud")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La solicitud se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void insertarSolicitudBaja(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idBien").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un bien/articulo.").forward(request, response);
        } else if (request.getParameter("motivoBaja").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique el motivo de baja.").forward(request, response);
        } else if (quid.select_Bien4Campo("STATUS", WebUtil.decode(session, request.getParameter("idBien"))).equalsIgnoreCase("Funcional")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El estatus del bien/articulo es Funcional.").forward(request, response);
        } else if (!quid.select_Baja4Bien(WebUtil.decode(session, request.getParameter("idBien"))).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El bien seleccionado ya esta dado de baja, Por favor actualize el estatus.").forward(request, response);
        } else {
            String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
            Transporter tport = quid.trans_insert_Baja_SolicitudBaja(WebUtil.decode(session, request.getParameter("idBien")),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    "sin especificar",
                    obs,
                    WebUtil.decode(session, request.getParameter("motivoBaja")),
                    WebUtil.decode(session, request.getParameter("idSolicitud")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El bien se agrego correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudBaja.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarSolicitudBaja(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteSolicitudBaja");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.trans_delete_SolicitudBaja(
                    WebUtil.decode(session, request.getParameter("idSolicitudBaja")),
                    WebUtil.decode(session, request.getParameter("idBaja")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El bien se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudBaja.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudBaja.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudBaja.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void actualizarBaja(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {

        if (request.getParameter("noOficio").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de oficio.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaBaja"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una fecha valida.").forward(request, response);
        } else {
            LinkedList bienes = quid.select_SolicitudBaja(WebUtil.decode(session, request.getParameter("idSolicitud")));
            Transporter tport = quid.trans_update_Baja_Bien_Solicitud(bienes,
                    "Atendida",
                    request.getParameter("fechaBaja"),
                    request.getParameter("noOficio"),
                    "BAJA",
                    WebUtil.decode(session, request.getParameter("idSolicitud")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La solicitud se confirmo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/UpdateSolicitud.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idSolicitud=" + request.getParameter("idSolicitud")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarCheckList(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteCheckList");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.trans_delete_CheckList(
                    WebUtil.decode(session, request.getParameter("idCheckList")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCheckList.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCheckList.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCheckList.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void insertarCheckList(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("descripcion").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una descripción.").forward(request, response);
        } else {
            Transporter tport = quid.insert_CheckList(request.getParameter("descripcion").trim());
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La operación se confirmo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCheckList.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void insertarRubro(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("categoria").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una categoria.").forward(request, response);
        } else if (request.getParameter("rubro").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor describa el rubro.").forward(request, response);
        } else {
            Transporter tport = quid.trans_insert_Rubro_ChecklistBrubro(
                    WebUtil.decode(session, request.getParameter("idCheckList")),
                    request.getParameter("rubro").trim().toUpperCase(),
                    WebUtil.decode(session, request.getParameter("categoria")));
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
            request.getRequestDispatcher("/ajaxFunctions/getRubros4CheckList.jsp").forward(request, response);
        }
    }

    private void eliminarCheckListRubro(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteCheckListRubro");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_CheckListRubro(WebUtil.decode(session, request.getParameter("idCheckListRubro")));
            if (tport.getCode() == 0) {
                request.getRequestDispatcher("/ajaxFunctions/getRubros4CheckList.jsp").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void actualizarCRB(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String[] idsRubros = request.getParameterValues("idRubro");
        if (idsRubros != null && idsRubros.length > 0) {
            LinkedList rubrosCheckList = quid.select_CheckListRubro4CheckLIist(WebUtil.decode(session, request.getParameter("idCheckList")));
            Transporter tport = quid.trans_insert_RelacionCRB(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    idsRubros,
                    rubrosCheckList, session);
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La operación se completo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCRB.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Si desea eliminar la lista vaya a la tabla de consulta.").forward(request, response);
        }
    }

    private void insertarCRB(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String[] idsRubros = request.getParameterValues("idRubro");
        if (idsRubros.length > 0) {
            LinkedList rubrosCheckList = quid.select_CheckListRubro4CheckLIist(WebUtil.decode(session, request.getParameter("idCheckList")));
            Transporter tport = quid.trans_insert_RelacionCRB(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    idsRubros,
                    rubrosCheckList, session);
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La operación se completo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCRB.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarCRB(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteCRB");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            LinkedList rubrosCheckList = quid.select_CheckListRubro4CheckLIist(WebUtil.decode(session, request.getParameter("idCheckList")));
            Transporter tport = quid.trans_delete_CRB(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    rubrosCheckList);
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=La operación se completo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCRB.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=info&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCRB.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaCRB.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void insertarIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("descripcion").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una descripción.").forward(request, response);
        } else {
            Transporter tport = quid.insert_Incidente(request.getParameter("descripcion").trim().toUpperCase());
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La operación se confirmo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteIncidente");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Incidente(
                    WebUtil.decode(session, request.getParameter("idIncidente")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private boolean validaFormBitacoraIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idIncidente").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un incidente.").forward(request, response);
        } else if (request.getParameter("prioridad").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione la prioridad del incidente.").forward(request, response);
        } else if (request.getParameter("status").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el estatus del reporte.").forward(request, response);
        } else if ((!request.getParameter("FormFrom").equalsIgnoreCase("updateBitacoraIncidente")
                && !request.getParameter("fechaAtencion").equalsIgnoreCase("") && !UTime.validaFecha(request.getParameter("fechaAtencion")))
                || (request.getParameter("FormFrom").equalsIgnoreCase("updateBitacoraIncidente")
                && !UTime.validaFecha(request.getParameter("fechaAtencion")))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        } else if (!request.getParameter("FormFrom").equalsIgnoreCase("updateBitacoraIncidente")
                && !quid.select_Bitacora_Incidente4Bien(WebUtil.decode(session, request.getParameter("idBien")),
                        "Pendiente",
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance())).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Ya se ha registrado un reporte el dia de hoy.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateBitacoraIncidente")
                && request.getParameter("accion").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique la acción aplicada.").forward(request, response);
        } else {
            valido = true;
        }

        return valido;
    }

    private void insertarBitacoraIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormBitacoraIncidente(session, request, response, quid, out)) {
            String fechaAtencion = !request.getParameter("fechaAtencion").equalsIgnoreCase("") ? request.getParameter("fechaAtencion") : "-";
            String accion = !request.getParameter("accion").equalsIgnoreCase("") ? request.getParameter("accion") : "Ninguna";
            String observaciones = !request.getParameter("observaciones").equalsIgnoreCase("") ? request.getParameter("observaciones") : " ";
            Transporter tport = quid.insert_Bitacora_Incidente(
                    SystemUtil.getNumeroReporte(WebUtil.decode(session, request.getParameter("idBien")), quid),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    fechaAtencion,
                    observaciones,
                    accion,
                    WebUtil.decode(session, request.getParameter("idIncidente")),
                    WebUtil.decode(session, request.getParameter("status")),
                    WebUtil.decode(session, request.getParameter("idBien")),
                    WebUtil.decode(session, request.getParameter("prioridad")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El reporte se registro correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/UpdateBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idBien=" + request.getParameter("idBien") + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarBitacoraIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteBitacoraIncidente");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_BitacoraIncidente(
                    WebUtil.decode(session, request.getParameter("idBitacoraIncidente")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBitacoraIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBitacoraIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBitacoraIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void actualizarBitacoraIncidente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormBitacoraIncidente(session, request, response, quid, out)) {
            String observaciones = !request.getParameter("observaciones").trim().equalsIgnoreCase("") ? request.getParameter("observaciones") : " ";
            Transporter tport = quid.update_Bitacora_Incidente(
                    request.getParameter("noReporte"),
                    request.getParameter("fechaCreacion"),
                    request.getParameter("fechaAtencion"),
                    observaciones,
                    request.getParameter("accion"),
                    WebUtil.decode(session, request.getParameter("idIncidente")),
                    WebUtil.decode(session, request.getParameter("status")),
                    WebUtil.decode(session, request.getParameter("idBien")),
                    WebUtil.decode(session, request.getParameter("prioridad")),
                    WebUtil.decode(session, request.getParameter("idBitacoraIncidente")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La bitácora se actualizó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaBitacoraIncidente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
        }
    }

    private void eliminarResguardo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteResguardo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.update_StatusResguardo(
                    "Inactivo",
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("idResguardo")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se eliminó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaResguardo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ocurrió un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaResguardo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaResguardo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private boolean validaFormResguardo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (!UTime.validaFecha(request.getParameter("fechaAsignacion"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una fecha valida.").forward(request, response);
        } else if (!quid.select_Resguardo4Bien(WebUtil.decode(session, request.getParameter("idBien")), "Activo").isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El bien ya se encuentra en resguardo por otra persona.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void actualizarResguardo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormResguardo(session, request, response, quid, out)) {
            String noTarjeta = !request.getParameter("noTarjetaResguardo").trim().equals("") ? request.getParameter("noTarjetaResguardo").trim() : "NA";
            String obs = !request.getParameter("observaciones").trim().equals("") ? request.getParameter("observaciones").trim() : " ";
            Transporter tport = quid.insert_Resguardo(
                    WebUtil.decode(session, request.getParameter("idBien")),
                    obs,
                    request.getParameter("fechaAsignacion"),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    "Activo",
                    noTarjeta,
                    WebUtil.decode(session, request.getParameter("idPersonalPlantel")));
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
            request.getRequestDispatcher("/ajaxFunctions/getResguado4Personal.jsp").forward(request, response);
        }
    }

    private void eliminarResguardoPersonal(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteResguardo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.update_StatusResguardo(
                    "Inactivo",
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("idResguardo")));
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Ocurrió un error.").forward(request, response);
            }
            request.getRequestDispatcher("/ajaxFunctions/getResguado4Personal.jsp").forward(request, response);

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con los permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void subirArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String FormFrom = "";
        if (ServletFileUpload.isMultipartContent(new ServletRequestContext(request))) {
            System.out.println("Tamaño del archivo: " + request.getContentLength());
            if (PageParameters.getParameter("fileSizeLimited").equals("1")
                    && (request.getContentLength() == -1//Este valor aparece cuando se desconoce el tamano
                    || request.getContentLength() > Integer.parseInt(PageParameters.getParameter("maxSizeToUpload")))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNBack.jsp?title=Error&type=error&msg=El tamaño máximo del archivo es de " + StringUtil.formatDouble1Decimals(Double.parseDouble(PageParameters.getParameter("maxSizeToUpload")) / 1048576) + " MBytes.").forward(request, response);
            } else {    
                
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                List items = upload.parseRequest(request);
                Iterator i = items.iterator();
                LinkedList filesToUpload = new LinkedList();
                HashMap parameters = new HashMap();
                while (i.hasNext()) {
                    FileItem item = (FileItem) i.next();
                    if (item.isFormField()) {
                        if (item.getFieldName().equalsIgnoreCase("FormFrom")) {
                            FormFrom = item.getString();
                        } else {
                            parameters.put(item.getFieldName(), item.getString());
                        }
                    } else {
                        filesToUpload.add(item);
                    }
                }
                switch (FormFrom) {
                    case "file4Software":
                        this.insertarArchivo(session, request, response, quid, out, parameters, filesToUpload, FormFrom);
                        break;
                    case "insertObjetoArchivo":
                        this.insertarObjetoArchivo(session, request, response, quid, out, parameters, filesToUpload, FormFrom);
                        break;
                }
            }
        }
    }

    private void insertarArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out, HashMap parameters, LinkedList filesToUpload, String FormFrom) throws Exception {
        if (parameters.get("idTipoArchivo") == null || parameters.get("idTipoArchivo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Seleccione el tipo de archivo.").forward(request, response);
        } else if (filesToUpload.isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha seleccionado ningún archivo.").forward(request, response);
        } else if (!filesToUpload.isEmpty()) {
            String id = WebUtil.decode(session, parameters.get("mainID").toString());
            String fechaUpload = UTime.calendar2SQLDateFormat(Calendar.getInstance());
            String obs = WebUtil.decode(session, parameters.get("descripcion").toString()).trim().equals("") ? " " : WebUtil.decode(session, parameters.get("descripcion").toString());
            String filePath = PageParameters.getParameter("folderDocs");
            String idTipo = WebUtil.decode(session, parameters.get("idTipoArchivo").toString());
            File verifyFolder = new File(PageParameters.getParameter("folderDocs"));
            if (!verifyFolder.exists()) {
                verifyFolder.mkdirs();
            }
            int sucess = 0;
            for (int i = 0; i < filesToUpload.size(); i++) {
                FileItem itemToUpload = null;
                itemToUpload = (FileItem) filesToUpload.get(i);
                String nombreArchivo = id + "_" + UTime.getSimpleTimeStampString() + FileUtil.getExtension(itemToUpload.getName());
                String extension = FileUtil.getExtension(itemToUpload.getName());
                if (this.validarDocumentExtension(session, request, response, quid, out, extension)) {
                    File fileToWrite = new File(filePath, nombreArchivo);
                    Transporter tport = quid.trans_insert_Archivo4Software(id, idTipo, nombreArchivo, obs, filePath, extension, fechaUpload, FormFrom);
                    if (tport.getCode() == 0) {
                        itemToUpload.write(fileToWrite);
                        sucess += 1;
//                            try {
//                                FileOutputStream fileToWrite;
//                                File copia = new File(filePath, "copia-" + nombreArchivo);
//                                ReadableByteChannel readable = Channels.newChannel(itemToUpload.getInputStream());
//                                ByteBuffer buffer = ByteBuffer.allocate(1024);
//                                fileToWrite = new FileOutputStream(copia);
//
//                                while (readable.read(buffer) != -1) {
//                                    fileToWrite.write(buffer.array());
//                                    buffer.clear();
//                                }
//                                fileToWrite.flush();
//                                fileToWrite.close();
//                                readable.close();
//                                sucess += 1;
//                            } catch (IOException e) {
//
//                            }
                    }
                } else {
                    sucess = -1;
                }
            }
            if (sucess != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Operación Exitosa&type=info&msg=Se han guardado " + sucess + " de " + filesToUpload.size() + " archivos.").forward(request, response);
            }

        }

    }

    private void insertarObjetoArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out, HashMap parameters, LinkedList filesToUpload, String FormFrom) throws Exception {
        if (parameters.get("idTipoArchivo") == null || parameters.get("idTipoArchivo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Seleccione el tipo de archivo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
        } else if (parameters.get("nombreArchivo") == null || parameters.get("nombreArchivo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Escriba el nombre del archivo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
        } else if (parameters.get("descripcion") == null
                || parameters.get("descripcion").toString().trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Escriba una descripción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
        } else if (parameters.get("tipoAcceso").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Seleccione el tipo de acceso para el archivo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
        } else if (filesToUpload.isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=No ha seleccionado ningún archivo.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
        } else if (!filesToUpload.isEmpty()) {
            String idObjeto = WebUtil.decode(session, parameters.get("idObjeto").toString());
            String fechaActualizacion = UTime.calendar2SQLDateFormat(Calendar.getInstance());
            String descripcion = WebUtil.decode(session, parameters.get("descripcion").toString());
            String ubicacionFisica = PageParameters.getParameter("folderDocs");
            String idTipoArchivo = WebUtil.decode(session, parameters.get("idTipoArchivo").toString());
            String nombreObjeto = WebUtil.decode(session, parameters.get("nombreObjeto").toString());
            String keyWords = parameters.get("keywords").toString();
            String nombreArchivo = parameters.get("nombreArchivo").toString();
            String FK_ID_Plantel = session.getAttribute("FK_ID_Plantel").toString();
            //Colocar en otro folder los docs de movimientos
            if (nombreObjeto.equalsIgnoreCase("MOVIMIENTO")) {
                ubicacionFisica = PageParameters.getParameter("folderMovs");
            }

            //File verifyFolder = new File(PageParameters.getParameter("folderDocs"));
            File verifyFolder = new File(ubicacionFisica);
            if (!verifyFolder.exists()) {
                verifyFolder.mkdirs();
            }
            int sucess = 0;
            for (int i = 0; i < filesToUpload.size(); i++) {
                FileItem itemToUpload = null;
                itemToUpload = (FileItem) filesToUpload.get(i);

                String extension = FileUtil.getExtension(itemToUpload.getName());
                String hashName = JHash.getFileDigest(itemToUpload.get(), "MD5") + extension;

                long tamanio = itemToUpload.getSize();

                if (this.validarDocumentExtension(session, request, response, quid, out, extension)) {
                    File fileToWrite = new File(ubicacionFisica, hashName);
                    Transporter tport = quid.trans_insert_Archivo4Objeto(
                            idObjeto,
                            nombreObjeto,
                            idTipoArchivo,
                            nombreArchivo,
                            descripcion,
                            ubicacionFisica,
                            extension,
                            fechaActualizacion,
                            tamanio,
                            WebUtil.decode(session, parameters.get("tipoAcceso").toString()),
                            keyWords,
                            hashName,
                            FK_ID_Plantel);
                    if (tport.getCode() == 0) {
                        if (!fileToWrite.exists()) {
                            itemToUpload.write(fileToWrite);
                        }
                        sucess += 1;
                    }
                } else {
                    sucess = -1;
                }
            }
            if (sucess != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Se han guardado " + sucess + " de " + filesToUpload.size() + " archivos.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                        + "_param_nombreObjeto=" + parameters.get("nombreObjeto") + "_param_idObjeto=" + parameters.get("idObjeto")).forward(request, response);
            }

        }

    }

    private boolean validarDocumentExtension(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out, String extension) throws Exception {
        boolean valido = false;
        if (!SystemUtil.isSystemAllowedExtension(extension)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirect.jsp?title=Error&type=error&msg=Tipo de archivo no valido.&url=" + PageParameters.getParameter("mainController") + "?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_exit=1").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertarDocumentos(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertTipoDocumentos");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("nombreDocumento").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor escriba el nombre del documento.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                Transporter tport = quid.insert_TipoArchivo(request.getParameter("nombreDocumento").trim().toUpperCase());
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se guardó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no pudo ser guardado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                }
            }

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void eliminarDocumentos(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("EliminarTipoDocumento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_TipoArchivo(WebUtil.decode(session, request.getParameter("idDocumento")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no pudo ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void eliminarArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteArchivo");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            LinkedList documento = quid.select_Archivo(WebUtil.decode(session, request.getParameter("idArchivo")));
            Transporter tport = quid.trans_delete_Archivo(
                    WebUtil.decode(session, request.getParameter("idTableArchivo")),
                    WebUtil.decode(session, request.getParameter("idArchivo")),
                    request.getParameter("FormFrom"));
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Acceso Ilegal&type=error&msg=El archivo no pudo ser eliminado.").forward(request, response);
            } else {
                if (PageParameters.getParameter("deleteFileOnModifyBD").equals("1")) {
                    borrarDocumentoDeDisco(documento.get(2).toString(), documento.get(0).toString());
                }
                request.getRequestDispatcher("/ajaxFunctions/getArchivos.jsp").forward(request, response);

            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private int borrarDocumentoDeDisco(String path, String fileName) {
        int result = 1;
        File archivo = new File(path + fileName);
        if (archivo.delete()) {
            result = 0;
        }
        return result;
    }

    private boolean validaFormMovimientoEntrada(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        }  else if (request.getParameter("idProveedor").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un proveedor.").forward(request, response);
        } else if (request.getParameter("idTipoCompra").equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de compra.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaMovimiento"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de Registro valida.").forward(request, response);
        } else if (request.getParameter("noFactura").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de factura.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaFactura"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de factura valida.").forward(request, response);
        } else if (request.getParameter("noReferencia").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de referencia.").forward(request, response);
        } else if (!StringUtil.isValidDouble(request.getParameter("iva"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un monto para el IVA valido.").forward(request, response);
        } else if (request.getParameter("estatus").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void updateMovimientoEntrada(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateMovimientoEntrada");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormMovimientoEntrada(session, request, response, quid, out)) {
                String motivo = request.getParameter("motivoMovimiento").trim().equals("") ? " " : request.getParameter("motivoMovimiento").trim();
                String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
                Transporter tport = quid.trans_update_Movimiento(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("folio")).trim().toUpperCase(),
                        request.getParameter("fechaMovimiento"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        request.getParameter("noFactura"),
                        request.getParameter("noReferencia"),
                        WebUtil.decode(session, request.getParameter("estatus")),
                        obs,
                        request.getParameter("iva"),
                        WebUtil.decode(session, request.getParameter("idTipoMovimiento")),
                        session.getAttribute("userID").toString(),
                        request.getParameter("motivoMovimiento"),
                        WebUtil.decode(session, request.getParameter("idProveedor")),
                        WebUtil.decode(session, request.getParameter("idTipoCompra")),
                        WebUtil.decode(session, request.getParameter("idMovimientoProveedor")),
                        request.getParameter("fechaFactura"),
                        WebUtil.decode(session, request.getParameter("idMovimiento")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Los datos se actualizaron corretcamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMovimientoEntrada.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al actualizar los datos.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

   private void insertMovimientoEntrada(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (!WebUtil.decode(session, request.getParameter("idTipoMovimiento")).equalsIgnoreCase("-1")) {
            if (this.validaFormMovimientoEntrada(session, request, response, quid, out)) {
                 int folio=quid.select_siguienteFolioXTipoMovimiento("ENTRADA");
                String motivo = request.getParameter("motivoMovimiento").trim().equals("") ? " " : request.getParameter("motivoMovimiento").trim();
                String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
                Long idEntrada = quid.trans_insert_MovimientoEntrada(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        String.valueOf(folio),
                        request.getParameter("fechaMovimiento"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        request.getParameter("noFactura"),
                        request.getParameter("noReferencia"),
                        WebUtil.decode(session, request.getParameter("estatus")),
                        obs,
                        request.getParameter("iva"),
                        WebUtil.decode(session, request.getParameter("idTipoMovimiento")),
                        session.getAttribute("userID").toString(),
                        motivo,
                        WebUtil.decode(session, request.getParameter("idProveedor")),
                        WebUtil.decode(session, request.getParameter("idTipoCompra")),
                        "-2",
                        request.getParameter("fechaFactura"));
                if (idEntrada != -1) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Por favor proceda a capturar los consumibles.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertMovimientoConsumible.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idMovimiento=" + WebUtil.encode(session, idEntrada) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tipo de movimiento no es premitido.").forward(request, response);
        }
    }
    private boolean validaFormConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        String claveActual = "";
        if (request.getParameter("FormFrom").equalsIgnoreCase("updateConsumible")) {
            claveActual = quid.select_Consumible4Campo("clave", WebUtil.decode(session, request.getParameter("idConsumible")));
        }
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("idSubcategoria") == null || request.getParameter("idSubcategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una subcategoria.").forward(request, response);
        } else if (request.getParameter("idSubcategoria") == null || request.getParameter("idSubcategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una subcategoria.").forward(request, response);
        } else if (request.getParameter("clave").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una clave.").forward(request, response);
        } else if (request.getParameter("descripcion").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una descripción.").forward(request, response);
        } else if (request.getParameter("idMedida").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione la medida.").forward(request, response);
        } else if (!StringUtil.isValidDoubleMayorCero(request.getParameter("precioActual").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique un precio valido.").forward(request, response);
        } else if (!StringUtil.isValidDoubleMayorIgualCero(request.getParameter("total").trim())) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique un total de existencias valido.").forward(request, response);
        } else if (request.getParameter("status").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("insertConsumible")
                && !quid.select_Consumible4Clave(request.getParameter("clave").trim()).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La clave ya existe.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateConsumible")
                && !claveActual.equalsIgnoreCase(request.getParameter("clave").trim())
                && !quid.select_Consumible4Clave(request.getParameter("clave").trim()).isEmpty()) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La clave ya existe.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormConsumible(session, request, response, quid, out)) {
            String noReferencia = request.getParameter("noReferencia").trim().equals("") ? " " : request.getParameter("noReferencia").trim().toUpperCase();
            String obs = StringUtil.removeEnter(request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim().toUpperCase());
            String desc = StringUtil.removeEnter(request.getParameter("descripcion").trim().toUpperCase());
            Long idInserted = quid.insert_Consumible(
                    request.getParameter("clave").trim().toUpperCase(),
                    desc,
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    request.getParameter("total"),
                    WebUtil.decode(session, request.getParameter("idMedida")),
                    WebUtil.decode(session, request.getParameter("idSubcategoria")),
                    request.getParameter("precioActual"),
                    noReferencia,
                    UTime.calendar2aaaamd(Calendar.getInstance()),
                    UTime.calendar2aaaamd(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("status")),
                    obs);
            if (idInserted != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Operación Correcta&type=info&msg=La operación se ha completado correctamente.").forward(request, response);
            }
        }
    }

    private void updateConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateConsumible");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormConsumible(session, request, response, quid, out)) {
                String noReferencia = request.getParameter("noReferencia").trim().equals("") ? " " : request.getParameter("noReferencia").trim().toUpperCase();
                String obs = StringUtil.removeEnter(request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim().toUpperCase());
                String desc = StringUtil.removeEnter(request.getParameter("descripcion").trim().toUpperCase());
                Transporter tport = quid.update_Consumible(
                        request.getParameter("clave").trim().toUpperCase(),
                        desc,
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        request.getParameter("total"),
                        WebUtil.decode(session, request.getParameter("idMedida")),
                        WebUtil.decode(session, request.getParameter("idSubcategoria")),
                        request.getParameter("precioActual"),
                        noReferencia,
                        UTime.calendar2aaaamd(Calendar.getInstance()),
                        WebUtil.decode(session, request.getParameter("status")),
                        obs,
                        WebUtil.decode(session, request.getParameter("idConsumible")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Los datos se actualizaron corretcamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaConsumible.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al actualizar los datos.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

    private boolean validaFormEntradaConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idMovimiento") == null || request.getParameter("idMovimiento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La información del movimiento se ha perdido.").forward(request, response);
        } else if (request.getParameter("idConsumible").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha seleccionado ningun consumible.").forward(request, response);
        } else if (!StringUtil.isValidDoubleMayorCero(request.getParameter("cantidad"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una cantidad valida.").forward(request, response);
        } else if (!StringUtil.isValidDoubleMayorCero(request.getParameter("precioUnitario"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un precio valido.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertEntradaConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormEntradaConsumible(session, request, response, quid, out)) {
            String idConsumible = WebUtil.decode(session, request.getParameter("idConsumible"));
            String strCantidadAcual = quid.select_Consumible4Campo("total", idConsumible);
            String strPrecioActual = quid.select_Consumible4Campo("precioActual", idConsumible);
            if (!strCantidadAcual.equals("")
                    && !strPrecioActual.equals("")
                    && StringUtil.isValidDouble(strCantidadAcual)
                    && StringUtil.isValidDouble(strPrecioActual)) {
//                Double nuevoPrecio = Double.parseDouble(request.getParameter("precioUnitario")) != Double.parseDouble(strPrecioActual) ? Double.parseDouble(request.getParameter("precioUnitario")) : Double.parseDouble(strPrecioActual);
                Double nuevaCantidad = Double.parseDouble(strCantidadAcual) + Double.parseDouble(request.getParameter("cantidad"));
                Double nuevoPrecio = this.calculaCostoPromedio(session, request, response, quid, out);
                Transporter tport = quid.trans_insert_ConsumibleMovimiento(
                        idConsumible,
                        WebUtil.decode(session, request.getParameter("idMovimiento")),
                        request.getParameter("cantidad"),
                        request.getParameter("precioUnitario"),
                        nuevoPrecio,
                        nuevaCantidad,
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                if (tport.getCode() == 0) {
                    if (nuevaCantidad > 0) {
                        quid.update_EstatusConsumible("Disponible", idConsumible);
                    }
                    request.getRequestDispatcher("/ajaxFunctions/getConsumible4Movimiento.jsp").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al realizar la operación.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se ha encontrado información del consumible.").forward(request, response);
            }
        }
    }

    private Double calculaCostoPromedio(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String strPrecioActual = quid.select_Consumible4Campo("precioActual",
                WebUtil.decode(session, request.getParameter("idConsumible")));
        Double costoPromedio = Double.parseDouble(strPrecioActual);//0.0;
        LinkedList sumas = quid.select_SumaActualCostos4Consumible(
                WebUtil.decode(session, request.getParameter("idPlantel")),
                WebUtil.decode(session, request.getParameter("idConsumible")),
                PageParameters.getParameter("inicioAnioContable"),
                PageParameters.getParameter("finAnioContable"));
        if (!sumas.isEmpty()) {
            costoPromedio = Double.parseDouble(sumas.get(0).toString());
            costoPromedio += (Double.parseDouble(request.getParameter("cantidad")) * Double.parseDouble(request.getParameter("precioUnitario")));
            costoPromedio = costoPromedio / (Double.parseDouble(sumas.get(1).toString()) + Double.parseDouble(request.getParameter("cantidad")));
        }
        return StringUtil.formatDoubleTwoDecimals(costoPromedio);
    }

    private void deleteConsumibleMovimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteConsumibleMovimiento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            LinkedList movimiento = quid.select_Movimiento_Consumible(WebUtil.decode(session, request.getParameter("idMovimientoConsumible")));
            if (!movimiento.isEmpty()) {
                String strCantidadActual = quid.select_Consumible4Campo("total", movimiento.get(1).toString());
//                String strPrecioActual = quid.select_Consumible4Campo("precioActual", movimiento.get(1).toString());
                LinkedList sumas = quid.select_SumaActualCostos4Consumible(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        movimiento.get(1).toString(),
                        PageParameters.getParameter("inicioAnioContable"),
                        PageParameters.getParameter("finAnioContable"));
                String precioActual = quid.select_Consumible4Campo("precioActual",
                        movimiento.get(1).toString());
                Double costoPromedio = Double.parseDouble(precioActual);//0.0;
//                Double costoPromedio = 0.0;
                if (!sumas.isEmpty()
                        && (Double.parseDouble(sumas.get(1).toString()) - Double.parseDouble(movimiento.get(2).toString())) > 0) {
                    costoPromedio = Double.parseDouble(sumas.get(0).toString()) - (Double.parseDouble(movimiento.get(2).toString()) * Double.parseDouble(movimiento.get(3).toString()));
                    costoPromedio = costoPromedio / (Double.parseDouble(sumas.get(1).toString()) - Double.parseDouble(movimiento.get(2).toString()));
                }
                String strPrecioActual = StringUtil.sformatDoubleTwoDecimals(costoPromedio);
                Double cantidadActual = Double.parseDouble(strCantidadActual) - Double.parseDouble(movimiento.get(2).toString());
                Transporter tport = quid.trans_delete_MovimientoConsumible(
                        WebUtil.decode(session, request.getParameter("idMovimientoConsumible")),
                        movimiento.get(1).toString(),
                        cantidadActual,
                        Double.parseDouble(strPrecioActual),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                if (tport.getCode() == 0) {
                    if (cantidadActual <= 0) {
                        quid.update_EstatusConsumible("Agotado", movimiento.get(1).toString());
                    }
                    request.getRequestDispatcher("/ajaxFunctions/getConsumible4Movimiento.jsp").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al realizar la operación.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se ha encontrado información del consumible.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

     private boolean validaFormMovimientoSalida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noTurno"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de turno valido.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaMovimiento"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        }else if (request.getParameter("idDepartamento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un departamento.").forward(request, response);
        } else if (request.getParameter("idPersonalPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleciione un responsable.").forward(request, response);
        } else if (request.getParameter("estatus").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertMovimientoSalida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (!WebUtil.decode(session, request.getParameter("idTipoMovimiento")).equalsIgnoreCase("-1")) {
            if (this.validaFormMovimientoSalida(session, request, response, quid, out)) {
                String motivo = request.getParameter("motivoMovimiento").trim().equals("") ? " " : request.getParameter("motivoMovimiento").trim();
                String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
                 int folio = quid.select_siguienteFolioXTipoMovimiento("SALIDA");           
                Long idEntrada = quid.trans_insert_MovimientoSalida(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        String.valueOf(folio),
                        request.getParameter("fechaMovimiento"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        "NA",
                        "NA",
                        WebUtil.decode(session, request.getParameter("estatus")),
                        obs,
                        "0",
                        WebUtil.decode(session, request.getParameter("idTipoMovimiento")),
                        session.getAttribute("userID").toString(),
                        motivo,
                        WebUtil.decode(session, request.getParameter("idDepartamento")),
                        WebUtil.decode(session, request.getParameter("idPersonalPlantel")),
                        request.getParameter("noTurno"));
                if (idEntrada != -1) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Por favor proceda a capturar los consumibles.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertMovimientoConsumibleSalida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idMovimiento=" + WebUtil.encode(session, idEntrada) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El tipo de movimiento no es premitido.").forward(request, response);
        }
    }
    private boolean validaFormSalidaConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idMovimiento") == null || request.getParameter("idMovimiento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La información del movimiento se ha perdido.").forward(request, response);
        } else if (request.getParameter("idConsumible").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha seleccionado ningun consumible.").forward(request, response);
        } else if (!StringUtil.isValidDoubleMayorCero(request.getParameter("cantidad"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una cantidad valida.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertSalidaConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormSalidaConsumible(session, request, response, quid, out)) {
            String idConsumible = WebUtil.decode(session, request.getParameter("idConsumible"));
            String strCantidadAcual = quid.select_Consumible4Campo("total", idConsumible);
            String strPrecioActual = quid.select_Consumible4Campo("precioActual", idConsumible);
            if (!strCantidadAcual.equals("")
                    && !strPrecioActual.equals("")
                    && StringUtil.isValidDouble(strCantidadAcual)
                    && StringUtil.isValidDouble(strPrecioActual)) {
                Double nuevoPrecio = Double.parseDouble(strPrecioActual);
                Double nuevaCantidad = Double.parseDouble(strCantidadAcual) - Double.parseDouble(request.getParameter("cantidad"));
                if (nuevaCantidad >= 0) {
                    Transporter tport = quid.trans_insert_ConsumibleMovimiento(
                            idConsumible,
                            WebUtil.decode(session, request.getParameter("idMovimiento")),
                            request.getParameter("cantidad"),
                            strPrecioActual,
                            nuevaCantidad,
                            nuevoPrecio,
                            UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                    if (tport.getCode() == 0) {
                        if (nuevaCantidad == 0) {
                            quid.update_EstatusConsumible("Agotado", idConsumible);
                        }
                        request.getRequestDispatcher("/ajaxFunctions/getConsumible4Movimiento.jsp").forward(request, response);
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al realizar la operación.").forward(request, response);
                    }
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=El sistema ha detectado cantidades negativas, solo tiene disponibles " + strCantidadAcual + " unidades. Por favor revise el inventario físico.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se ha encontrado información del consumible.").forward(request, response);
            }
        }
    }

    private void deleteConsumibleSalida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteConsumibleMovimientoSalida");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            LinkedList movimiento = quid.select_Movimiento_Consumible(WebUtil.decode(session, request.getParameter("idMovimientoConsumible")));
            if (!movimiento.isEmpty()) {
                String strCantidadActual = quid.select_Consumible4Campo("total", movimiento.get(1).toString());
                String strPrecioActual = quid.select_Consumible4Campo("precioActual", movimiento.get(1).toString());
                Double cantidadActual = Double.parseDouble(strCantidadActual) + Double.parseDouble(movimiento.get(2).toString());
                Transporter tport = quid.trans_delete_MovimientoConsumible(
                        WebUtil.decode(session, request.getParameter("idMovimientoConsumible")),
                        movimiento.get(1).toString(),
                        cantidadActual,
                        Double.parseDouble(strPrecioActual),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()));
                if (tport.getCode() == 0) {
                    if (cantidadActual > 0) {
                        quid.update_EstatusConsumible("Disponible", movimiento.get(1).toString());
                    }
                    request.getRequestDispatcher("/ajaxFunctions/getConsumible4Movimiento.jsp").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al realizar la operación.").forward(request, response);
                }
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se ha encontrado información del consumible.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void updateMovimientoSalida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateMovimientoSalida");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormMovimientoSalida(session, request, response, quid, out)) {
                String motivo = request.getParameter("motivoMovimiento").trim().equals("") ? " " : request.getParameter("motivoMovimiento").trim();
                String obs = request.getParameter("observaciones").trim().equals("") ? " " : request.getParameter("observaciones").trim();
                Transporter tport = quid.trans_update_MovimientoSalida(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("folio")).trim().toUpperCase(),
                        request.getParameter("fechaMovimiento"),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        "NA",
                        "NA",
                        WebUtil.decode(session, request.getParameter("estatus")),
                        obs,
                        "0",
                        WebUtil.decode(session, request.getParameter("idTipoMovimiento")),
                        session.getAttribute("userID").toString(),
                        motivo,
                        WebUtil.decode(session, request.getParameter("idDepartamento")),
                        WebUtil.decode(session, request.getParameter("idPersonalPlantel")),
                        WebUtil.decode(session, request.getParameter("idTraslado")),
                        request.getParameter("noTurno"),
                        WebUtil.decode(session, request.getParameter("idMovimiento")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Los datos se actualizaron corretcamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMovimientoSalida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ocurrio un error al actualizar los datos.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO tiene permisos para realizar esta acción.").forward(request, response);
        }
    }

    private void deleteMedida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteMedida");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Medida(WebUtil.decode(session, request.getParameter("idMedida")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void insertarMedida(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("InsertMedida");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("medida").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor escriba la descripción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else if (!quid.select_Medida(request.getParameter("medida").trim().toUpperCase()).isEmpty()) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=La presentación ya existe.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                Transporter tport = quid.insert_Medida(request.getParameter("medida").trim().toUpperCase());
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se guardó correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no pudo ser guardado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                }
            }

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaMedida.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void updatePDG(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idPlantel").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("idDepartamento").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un departamento.").forward(request, response);
        } else if (request.getParameter("idPDG") == null) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se selecciono ningún grupo.").forward(request, response);
        } else {
            String[] idPDG = request.getParameterValues("idPDG");
            String idNuevoDeptoPlantel = WebUtil.decode(session, request.getParameter("idDepartamento"));
            int success = 0;
            for (int i = 0; i < idPDG.length; i++) {
                LinkedList infoPDG = quid.select_PDG(WebUtil.decode(session, idPDG[i]));
                if (!infoPDG.isEmpty()) {
                    LinkedList bienGrupo = quid.select_Bien4Grupo(
                            infoPDG.get(0).toString(),
                            "Baja",
                            false,
                            true);
                    Transporter tport = quid.trans_update_RelacionPdg4DeptoPlanetl(
                            WebUtil.decode(session, idPDG[i]),
                            idNuevoDeptoPlantel,
                            WebUtil.decode(session, request.getParameter("idPlantel")),
                            bienGrupo);
                    if (tport.getCode() == 0) {
                        success += 1;
                    }
                }

            }
            if (success == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No fue posible reasignar los grupos.").forward(request, response);
            } else {
                request.getRequestDispatcher("/ajaxFunctions/getGrupo4Plantel.jsp").forward(request, response);
            }
        }
    }

    private void insertConteoFisico(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (!UTime.validaFecha(request.getParameter("fechaRegistro"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione la fecha.").forward(request, response);
        } else if (quid.select_CountConteoFisico4Estatus(
                WebUtil.decode(session, request.getParameter("idPlantel")), "pendiente") > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Existen operaciones pendientes.").forward(request, response);

        } else {
            int idIserted = quid.insert_ConteoFisico(
                    request.getParameter("fechaRegistro"), "pendiente", WebUtil.decode(session, request.getParameter("idPlantel")), UTime.calendar2SQLDateFormat(Calendar.getInstance()), "");
            if (idIserted != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=Por favor proceda a capturar el conteo de los consumibles.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertConteoFisicoCosumible.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idConteoFisico=" + WebUtil.encode(session, idIserted) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se pudo crear el corte de inventario.").forward(request, response);
            }
        }
    }

    private void insertConteoFisicoConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String idConteoFisico = WebUtil.decode(session, request.getParameter("idConteoFisico"));
        if (quid.select_ConteoFisico4Campo("estatus", idConteoFisico).equalsIgnoreCase("terminado")
                || quid.select_ConteoFisico4Campo("estatus", idConteoFisico).equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El inventario ya se ha cerrado").forward(request, response);
        } else if (request.getParameterValues("idConsumible") == null) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha registrado ningún consumible").forward(request, response);
        } else {
            String[] idConsumible = request.getParameterValues("idConsumible");
            int success = 0;
            quid.update_fechaModificacioConteoFisico(UTime.calendar2SQLDateFormat(Calendar.getInstance()), idConteoFisico);
            for (int i = 0; i < idConsumible.length; i++) {
                String conteoFisico = request.getParameter(idConsumible[i]);
                if (StringUtil.isValidDouble(conteoFisico)) {
                    int coutCosumibleYARegistrado = quid.select_CountConumible4ConteoFisico(WebUtil.decode(session, idConsumible[i]), idConteoFisico);
                    if (coutCosumibleYARegistrado > 0) {
//                    if (request.getParameter("idConteoFisicoConsumible_" + idConsumible[i]) != null) {
                        Transporter tport = quid.update_ConteoFisico_Consumible(
                                idConteoFisico,
                                WebUtil.decode(session, idConsumible[i]),
                                conteoFisico,
                                request.getParameter("conteoLogico_" + idConsumible[i]),
                                UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                                request.getParameter("precioHistorico_" + idConsumible[i]),
                                WebUtil.decode(session, request.getParameter("idConteoFisicoConsumible_" + idConsumible[i])));
                        if (tport.getCode() == 0) {
                            success += 1;
                        }
                    } else if (coutCosumibleYARegistrado == 0) {
                        Long idInserted = quid.insert_ConteoFisicoConsumible(
                                idConteoFisico,
                                WebUtil.decode(session, idConsumible[i]),
                                conteoFisico,
                                request.getParameter("conteoLogico_" + idConsumible[i]),
                                UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                                request.getParameter("precioHistorico_" + idConsumible[i]));
                        if (idInserted != -1) {
                            success += 1;
                        }
                    }
                }
            }
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Información&type=info&msg=Se guardaron " + success + " de " + idConsumible.length + " registros.").forward(request, response);
        }
    }

    private void terminarInventario(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String idConteoFisico = WebUtil.decode(session, request.getParameter("idConteoFisico"));
        int faltantes = quid.select_CountConsumiblesSinConteoFisico(
                idConteoFisico,
                WebUtil.decode(session, request.getParameter("idPlantel")));
        if (faltantes > 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Exiten " + faltantes + " regitros sin conteo fisico.").forward(request, response);

        } else if (quid.select_ConteoFisico4Campo("estatus", idConteoFisico).equalsIgnoreCase("terminado")
                || quid.select_ConteoFisico4Campo("estatus", idConteoFisico).equalsIgnoreCase("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El inventario ya se ha cerrado").forward(request, response);
        } else {
            LinkedList conteoFisico = quid.select_ConteoFisico_Conumible(idConteoFisico);
            if (conteoFisico.isEmpty()) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se ha registrado ningún conteo fisico").forward(request, response);
            } else {
                Transporter tport = quid.trans_update_ExitenciasConsumible(
                        conteoFisico,
                        idConteoFisico);
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Información&type=info&msg=El cierre de inventario se realizo correctamente.").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=No se pudo realizar el cierre de inventario.").forward(request, response);
                }
            }
        }
    }

    private void insertTipoActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("insertTipoActividad");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("tipoActividad").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor escriba una descripción valida.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                int idInserted = quid.insert_TipoActividad(request.getParameter("tipoActividad").trim().toUpperCase());
                if (idInserted != -1) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Correcta&type=info&msg=El registro se inserto correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo guardar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }

    }

    private void deleteTipoActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("deleteTipoActividad");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_TipoActividad(WebUtil.decode(session, request.getParameter("idTipoActividad")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTipoActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void updateTipoActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("updateTipoActividad");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if (request.getParameter("value").trim().equals("")) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Escriba una descripción valida. Actualice la página para continuar.").forward(request, response);

                } else {
                    Transporter tport = quid.update_TipoActividad(
                            request.getParameter("value").trim().toUpperCase(),
                            WebUtil.decode(session, request.getParameter("idTipoActividad")));
                    if (tport.getCode() == 0) {
                        out.print(request.getParameter("value"));
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=No se pudo actualizar el registro. Actualice la página para continuar.").forward(request, response);
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea. Actualice la página para continuar.").forward(request, response);
        }
    }

    private boolean validaFormActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idTipoActividad").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de actividad.").forward(request, response);
        } else if (request.getParameter("descripcion").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una descripción valida.").forward(request, response);
        } else if (!UTime.isMySQLFormat(request.getParameter("fechaInicio"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de inicio valida.").forward(request, response);
        } else if (!UTime.isMySQLFormat(request.getParameter("fechaFin"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha de termino valida.").forward(request, response);
        } else if (!request.getParameter("fechaLimite").equals("")
                && !request.getParameter("fechaLimite").equals("0000-00-00")
                && !UTime.isMySQLFormat(request.getParameter("fechaLimite"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha limite valida.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("insertActividad")
                && request.getParameterValues("idPlantel") == null) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba seleccione al menos un plantel.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormActividad(session, request, response, quid, out)) {
            String horaCreacion = UTime.getHourHHMMSS(Calendar.getInstance());
            String horaInicio = request.getParameter("horaInicio").equals("") ? "00:00:00" : request.getParameter("horaInicio");
            String horaFin = request.getParameter("horaFin").equals("") ? "00:00:00" : request.getParameter("horaFin");
            String fechaLimite = request.getParameter("fechaLimite") == null ? "0000-00-00" : request.getParameter("fechaLimite");
            String horaLimite = request.getParameter("horaLimite").equals("") ? "00:00:00" : request.getParameter("horaLimite");
            LinkedList ID_Plantel = new LinkedList();
            String[] idPlantel = request.getParameterValues("idPlantel");
            for (int i = 0; i < idPlantel.length; i++) {
                ID_Plantel.add(WebUtil.decode(session, idPlantel[i]));
            }
            Long idInserted = quid.trans_insert_Actividad(
                    request.getParameter("descripcion").trim().replaceAll("\n", " ").replaceAll("\r", " "),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin"),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    horaCreacion,
                    horaInicio,
                    horaFin,
                    horaCreacion,
                    session.getAttribute("userID").toString(),
                    session.getAttribute("userID").toString(),
                    WebUtil.decode(session, request.getParameter("idTipoActividad")),
                    fechaLimite,
                    horaLimite,
                    "PENDIENTE",
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    horaCreacion,
                    "",
                    session.getAttribute("userID").toString(),
                    "0",
                    ID_Plantel);
            if (idInserted == -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se pudo guardar la actividad.").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La actividad se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        }
    }

    private void updateActividadPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String fechaLimite = quid.select_ActividadPlantel4Campo("A.fechaLimite", WebUtil.decode(session, request.getParameter("idActividadPlantel")));
        String fechaTermino = quid.select_ActividadPlantel4Campo("A.fechaFin", WebUtil.decode(session, request.getParameter("idActividadPlantel")));
        if (!StringUtil.isValidInt(request.getParameter("porcentajeCompleto"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Escriba un porcentaje de avance valido.").forward(request, response);
        } else if (Integer.parseInt(request.getParameter("porcentajeCompleto")) < 0
                || Integer.parseInt(request.getParameter("porcentajeCompleto")) > 100) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Escriba un porcentaje de avance entre 0 y 100.").forward(request, response);
        } else if (!UTime.comparaStringFechas(UTime.calendar2SQLDateFormat(Calendar.getInstance()), fechaTermino)
                && (fechaLimite.equals("") || fechaLimite.equalsIgnoreCase("0000-00-00"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La actividad ya supero la fecha de termino.").forward(request, response);
        } else if ((!fechaLimite.equals("") && !fechaLimite.equals("0000-00-00"))
                && !UTime.comparaStringFechas(UTime.calendar2SQLDateFormat(Calendar.getInstance()), fechaLimite)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=La actividad ya supero la fecha limite.").forward(request, response);
        } else {
            String estatus = Integer.parseInt(request.getParameter("porcentajeCompleto")) == 100 ? "COMPLETADA" : "PENDIENTE";

            Transporter tport = quid.update_ActividadPlantel(
                    estatus,
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    request.getParameter("observaciones").trim(),
                    UTime.getHourHHMMSS(Calendar.getInstance()),
                    session.getAttribute("userID").toString(),
                    request.getParameter("porcentajeCompleto"),
                    WebUtil.decode(session, request.getParameter("idActividadPlantel")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La actividad se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=La actividad no se pudo guardar.").forward(request, response);
            }
        }
    }

    private void updateEstatusActividadPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateActividadPlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            String fechaLimite = quid.select_ActividadPlantel4Campo("A.fechaLimite", WebUtil.decode(session, request.getParameter("idActividadPlantel")));
            String fechaTermino = quid.select_ActividadPlantel4Campo("A.fechaFin", WebUtil.decode(session, request.getParameter("idActividadPlantel")));
            if (quid.select_ActividadPlantel4Campo("AP.estatus", WebUtil.decode(session, request.getParameter("idActividadPlantel"))).equalsIgnoreCase("COMPLETADA")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=La actividad ya se ha completado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else if (!UTime.comparaStringFechas(UTime.calendar2SQLDateFormat(Calendar.getInstance()), fechaTermino)
                    && (fechaLimite.equals("") || fechaLimite.equalsIgnoreCase("0000-00-00"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=La actividad ya supero la fecha de termino.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else if ((!fechaLimite.equals("") && !fechaLimite.equals("0000-00-00"))
                    && !UTime.comparaStringFechas(UTime.calendar2SQLDateFormat(Calendar.getInstance()), fechaLimite)) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=La actividad ya supero la fecha limite.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                Transporter tport = quid.update_ActividadPlantel(
                        "COMPLETADA",
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        "",
                        UTime.getHourHHMMSS(Calendar.getInstance()),
                        session.getAttribute("userID").toString(),
                        "100",
                        WebUtil.decode(session, request.getParameter("idActividadPlantel")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=La actividad se marco como completada.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo guardar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void deleteActividadPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteActividadPlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.trans_delete_ActividadPlantel(
                    WebUtil.decode(session, request.getParameter("idActividadPlantel")),
                    WebUtil.decode(session, request.getParameter("idActividad")),
                    quid.select_CountActividad(WebUtil.decode(session, request.getParameter("idActividad"))) - 1 == 0);
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=La actividad se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo borrar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void updateActividad(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormActividad(session, request, response, quid, out)) {
            String horaInicio = request.getParameter("horaInicio").equals("") ? "00:00:00" : request.getParameter("horaInicio");
            String horaFin = request.getParameter("horaFin").equals("") ? "00:00:00" : request.getParameter("horaFin");
            String fechaLimite = request.getParameter("fechaLimite") == null ? "0000-00-00" : request.getParameter("fechaLimite");
            String horaLimite = request.getParameter("horaLimite").equals("") ? "00:00:00" : request.getParameter("horaLimite");
            Transporter tport = quid.update_Actividad(
                    request.getParameter("descripcion").trim().replaceAll("\n", " ").replaceAll("\n", " "),
                    request.getParameter("fechaInicio"),
                    request.getParameter("fechaFin"),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    horaInicio,
                    horaFin,
                    UTime.getHourHHMMSS(Calendar.getInstance()),
                    session.getAttribute("userID").toString(),
                    WebUtil.decode(session, request.getParameter("idTipoActividad")),
                    fechaLimite,
                    horaLimite,
                    WebUtil.decode(session, request.getParameter("idActividad")));
            if (tport.getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se pudo guardar la actividad.").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La actividad se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaActividad.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        }
    }

    private boolean validaFormSolicitudPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("numeroOficio").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de oficio.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaSolicitud"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        } else if (request.getParameter("asunto").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un asunto.").forward(request, response);
        } else if (request.getParameter("solicitud").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor describa la solicitud.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateSolicitudPlantel")
                && request.getParameter("observaciones").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba las observaciones.").forward(request, response);
        } else if (request.getParameter("FormFrom").equalsIgnoreCase("updateSolicitudPlantel")
                && request.getParameter("estatus").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertSolicitudPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormSolicitudPlantel(session, request, response, quid, out)) {
            int idInserted = quid.insert_SolicitudPlantel(
                    request.getParameter("numeroOficio").trim(),
                    "",
                    request.getParameter("fechaSolicitud"),
                    "",
                    "SIN VER",
                    "",
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    request.getParameter("asunto").trim(),
                    request.getParameter("solicitud").trim(),
                    request.getParameter("justificacion").trim(),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    session.getAttribute("userID").toString(),
                    session.getAttribute("userID").toString(),
                    session.getAttribute("userID").toString(),
                    UTime.getHourHHMMSS(Calendar.getInstance()),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    "OTRA");
            if (idInserted != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La solicitud se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudPlantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=No se pudo guardar la solicitud.").forward(request, response);
            }
        }
    }

    private void updateSolicitudPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateSolicitudPlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormSolicitudPlantel(session, request, response, quid, out)) {
                Transporter tport = quid.update_SolicitudPlantel(
                        request.getParameter("numeroOficio").trim(),
                        "",
                        request.getParameter("fechaSolicitud"),
                        "",
                        WebUtil.decode(session, request.getParameter("estatus")),
                        request.getParameter("observaciones").trim(),
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        request.getParameter("asunto").trim(),
                        request.getParameter("solicitud").trim(),
                        request.getParameter("justificacion").trim(),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        session.getAttribute("userID").toString(),
                        session.getAttribute("userID").toString(),
                        WebUtil.decode(session, request.getParameter("idSolicitud")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=La solicitud se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudPlantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=No se pudo guardar la solicitud.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.").forward(request, response);
        }
    }

    private void deleteSolicitudPlantel(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteSolicitudPlantel");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_SolicitudPlantel(
                    WebUtil.decode(session, request.getParameter("idSolicitud")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=La solicitud se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudPlantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo borrar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudPlantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaSolicitudPlantel.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private boolean validaFormEnlace(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idPlantel").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("tipo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de enlace.").forward(request, response);
        } else if (!StringUtil.isValidFloat(request.getParameter("velocidadSubida"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una velocidad de subida valida.").forward(request, response);
        } else if (!StringUtil.isValidFloat(request.getParameter("velocidadBajada"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una velocidad de bajada valida.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noAlumnos"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de alumnos conectados.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noDocentes"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de docente conectados.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noAdministrativos"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de administrativos conectados.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noDispositivos"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de dispositivos conectados.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("noNodos"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de nodos conectados.").forward(request, response);
        } else if (!StringUtil.isValidInt(request.getParameter("calidadServicio"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique la calidad del servicio.").forward(request, response);
        } else if (request.getParameter("idProveedor").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un proveedor.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertEnlace(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormEnlace(session, request, response, quid, out)) {
            int idInserted = quid.insert_Enlace(
                    WebUtil.decode(session, request.getParameter("tipo")),
                    request.getParameter("velocidadSubida"),
                    request.getParameter("velocidadBajada"),
                    request.getParameter("noAlumnos"),
                    request.getParameter("noDocentes"),
                    request.getParameter("noAdministrativos"),
                    request.getParameter("noDispositivos"),
                    request.getParameter("noNodos"),
                    request.getParameter("calidadServicio"),
                    WebUtil.decode(session, request.getParameter("idProveedor")),
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()));
            if (idInserted != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El registro se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaEnlace.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=El registro no se pudo guardar.").forward(request, response);
            }
        }
    }

    private void updateEnlace(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormEnlace(session, request, response, quid, out)) {
            Transporter tport = quid.update_Enlace(
                    WebUtil.decode(session, request.getParameter("tipo")),
                    request.getParameter("velocidadSubida"),
                    request.getParameter("velocidadBajada"),
                    request.getParameter("noAlumnos"),
                    request.getParameter("noDocentes"),
                    request.getParameter("noAdministrativos"),
                    request.getParameter("noDispositivos"),
                    request.getParameter("noNodos"),
                    request.getParameter("calidadServicio"),
                    WebUtil.decode(session, request.getParameter("idProveedor")),
                    WebUtil.decode(session, request.getParameter("idPlantel")),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("idEnlace")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El registro se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaEnlace.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=El registro no se pudo guardar.").forward(request, response);
            }
        }
    }

    private void deleteEnlace(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteEnlace");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Enlace(
                    WebUtil.decode(session, request.getParameter("idEnlace")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaEnlace.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo borrar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaEnlace.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaEnlace.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private void updateEstatusBien(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idBien") == null || request.getParameter("idBien").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha indicado el artículo/bien.").forward(request, response);
        } else if (request.getParameter("status") == null || request.getParameter("status").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No se ha indicado el estatus.").forward(request, response);
        } else {
            Transporter tport = new Transporter(-1, "No se ha efectuado ningúna operación");
            if (!WebUtil.decode(session, request.getParameter("status")).equalsIgnoreCase("Baja")) {
                tport = quid.update_Status4Bien(WebUtil.decode(session, request.getParameter("status")).toUpperCase(),
                        WebUtil.decode(session, request.getParameter("idBien")));
            } else {
                tport = quid.trans_update_SetBajaBienDeleteFromAnyGroup(WebUtil.decode(session, request.getParameter("idBien")));
            }

            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Información&type=info&msg=El cambio del estatus se realizo correctamente.").forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=La operación no se pudo realizar.").forward(request, response);
            }
        }
    }

    private void deleteObjetoArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeleteObjetoArchivo");
        LinkedList documento = quid.select_Archivo(WebUtil.decode(session, request.getParameter("idArchivo")));
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)
                || documento.get(12).equals(session.getAttribute("FK_ID_Plantel"))) {

            boolean deleteArchivo = quid.select_CountArchivo4Objeto(
                    WebUtil.decode(session, request.getParameter("idArchivo")),
                    WebUtil.decode(session, request.getParameter("idObjeto"))) == 0;

            Transporter tport = quid.trans_delete_ObjetoArchivo_Archivo(
                    WebUtil.decode(session, request.getParameter("idObjetoArchivo")),
                    WebUtil.decode(session, request.getParameter("idArchivo")),
                    deleteArchivo);
            if (tport.getCode() == 0) {
                if (PageParameters.getParameter("deleteFileOnModifyBD").equals("1")) {
                    borrarDocumentoDeDisco(documento.get(2).toString(), documento.get(8).toString());
                }
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                        + "_param_nombreObjeto=" + request.getParameter("nombreObjeto")
                        + "_param_idObjeto=" + request.getParameter("idObjeto")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo borrar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                        + "_param_nombreObjeto=" + request.getParameter("nombreObjeto")
                        + "_param_idObjeto=" + request.getParameter("idObjeto")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/Insert_ObjetoArchivo.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())
                    + "_param_nombreObjeto=" + request.getParameter("nombreObjeto")
                    + "_param_idObjeto=" + request.getParameter("idObjeto")).forward(request, response);
        }
    }

    private void updateArchivo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdateArchivo");
        LinkedList documento = quid.select_Archivo(WebUtil.decode(session, request.getParameter("idArchivo")));
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)
                || documento.get(12).equals(session.getAttribute("FK_ID_Plantel"))) {
            if (request.getParameter("idTipoArchivo").equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de archivo.").forward(request, response);
            } else if (request.getParameter("nombreArchivo").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el nombre del archivo.").forward(request, response);
            } else if (request.getParameter("descripcion").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una descripción para el archivo.").forward(request, response);
            } else if (request.getParameter("tipoAcceso").equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione el tipo de acceso.").forward(request, response);
            } else if (request.getParameter("keywords").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba las keywords para el archivo.").forward(request, response);
            } else if (request.getParameter("extension").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba la extension del archivo.").forward(request, response);
            } else if (!StringUtil.isValidDouble(request.getParameter("tamanio").trim())) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un valor para tamaño valido.").forward(request, response);
            } else {
                Transporter tport = quid.update_Archivo(
                        request.getParameter("nombreArchivo").trim(),
                        request.getParameter("descripcion").trim(),
                        request.getParameter("extension").trim(),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        WebUtil.decode(session, request.getParameter("idTipoArchivo")),
                        Long.parseLong(request.getParameter("tamanio").trim()) * 1048576,
                        WebUtil.decode(session, request.getParameter("tipoAcceso")),
                        request.getParameter("keywords").trim(),
                        WebUtil.decode(session, request.getParameter("idArchivo")));
                if (tport.getCode() != 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=La información no se puedo actualizar.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.").forward(request, response);
        }
    }

    private void deleteRubro(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("deleteRubro");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Rubro(WebUtil.decode(session, request.getParameter("idRubro")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void insertSimpleRubro(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("insertSimpleRubro");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("rubro").trim().equals("")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor escriba una descripción valida.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                int idInserted = quid.insert_Rubro(request.getParameter("rubro").trim().toUpperCase());
                if (idInserted != -1) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Correcta&type=info&msg=El registro se inserto correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo guardar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaRubro.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }

    }

    private void updateRubro(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("updateRubro");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                if (request.getParameter("value").trim().equals("")) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Escriba una descripción valida. Actualice la página para continuar.").forward(request, response);

                } else {
                    Transporter tport = quid.update_Rubro(
                            request.getParameter("value").trim().toUpperCase(),
                            WebUtil.decode(session, request.getParameter("idRubro")));
                    if (tport.getCode() == 0) {
                        out.print(request.getParameter("value"));
                    } else {
                        this.getServletConfig().getServletContext().getRequestDispatcher(
                                "" + PageParameters.getParameter("msgUtil")
                                + "/msg.jsp?title=Error&type=error&msg=No se pudo actualizar el registro. Actualice la página para continuar.").forward(request, response);
                    }
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea. Actualice la página para continuar.").forward(request, response);
        }
    }

    private void deletePuntuacion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("deletePuntuacion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            Transporter tport = quid.delete_Puntuacion(WebUtil.decode(session, request.getParameter("idPuntuacion")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se elimino correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser eliminado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
        }
    }

    private boolean validaFormPuntuacion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idRubro").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor seleccione un rubro.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
        } else if (!StringUtil.isValidDouble(request.getParameter("puntuacion"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Por favor escriba una puntuación valida.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void insertPuntuacion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("insertPuntuacion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormPuntuacion(session, request, response, quid, out)) {
                Long idInserted = quid.trans_insert_Puntuacion(
                        WebUtil.decode(session, request.getParameter("ID_Plantel")),
                        WebUtil.decode(session, request.getParameter("idRubro")),
                        request.getParameter("puntuacion"),
                        request.getParameter("observaciones").replaceAll("\n", " ").replaceAll("\r", " "),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        "1",
                        session.getAttribute("userID").toString());
                if (idInserted != -1) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Operación Exitosa&type=info&msg=El registro se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no se pudo guardar.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaPuntuacion.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Plantel=" + request.getParameter("ID_Plantel")).forward(request, response);
        }
    }

    private boolean validaFormUpdatePuntuacion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("idRubro").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un rubro.").forward(request, response);
        } else if (!StringUtil.isValidDouble(request.getParameter("puntuacion"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgl.jsp?title=Error&type=error&msg=Por favor escriba una puntuación valida.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void updatePuntuacion(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("insertPuntuacion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (this.validaFormUpdatePuntuacion(session, request, response, quid, out)) {
                Transporter tport = quid.update_Puntuacion(
                        WebUtil.decode(session, request.getParameter("ID_Plantel")),
                        WebUtil.decode(session, request.getParameter("idRubro")),
                        request.getParameter("puntuacion"),
                        request.getParameter("observaciones").replaceAll("\n", " ").replaceAll("\r", " "),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        "1",
                        session.getAttribute("userID").toString(),
                        WebUtil.decode(session, request.getParameter("idPuntuacion")));
                if (tport.getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Operación Exitosa&type=info&msg=El registro se actualizo correctamente.").forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=El registro no se pudo actualizar.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.").forward(request, response);
        }

    }

    private void insertPlantelAlmacen(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameterValues("idPlantelSolicita") == null || request.getParameterValues("idPlantelSolicita").length == 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No ha realizado ninguna selección.").forward(request, response);
        } else {
            String[] idPlantelSolicita = request.getParameterValues("idPlantelSolicita");
            String idPlantelSurte = WebUtil.decode(session, request.getParameter("idPlantelSurte"));
            int success = 0;
            for (int i = 0; i < idPlantelSolicita.length; i++) {
                LinkedList registroPlantelAlmacen = quid.select_PlantelAlmacen4PlantelSurte(
                        idPlantelSurte,
                        WebUtil.decode(session, idPlantelSolicita[i]));
                if (registroPlantelAlmacen.size() > 0) {
                    if (quid.update_PlanteAlmacen(
                            WebUtil.decode(session, idPlantelSolicita[i]),
                            idPlantelSurte,
                            "1",
                            registroPlantelAlmacen.get(0).toString()).getCode() == 0) {

                        success += 1;
                    }
                } else if (quid.insert_PlabtelAlmacen(
                        WebUtil.decode(session, idPlantelSolicita[i]),
                        idPlantelSurte, "1") != -1) {
                    success += 1;
                }
            }
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Información&type=info&msg=Se guardaron " + success + " de " + idPlantelSolicita.length + " registros.").forward(request, response);
        }
    }

    private void updatePlantelAlmacen(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {

        LinkedList listIdPlantelSolicita = new LinkedList();
        if (request.getParameter("idPlantelSolicita") != null) {
            String[] idPlantelSolicita = request.getParameterValues("idPlantelSolicita");
            for (int i = 0; i < idPlantelSolicita.length; i++) {
                listIdPlantelSolicita.add(WebUtil.decode(session, idPlantelSolicita[i]));
            }
        }
        if (quid.trans_update_estatusPlantelAlmacen(
                listIdPlantelSolicita,
                WebUtil.decode(session, request.getParameter("idPlantelSurte")),
                "1").getCode() == 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Información&type=info&msg=Los datos se actualizaron correctamente.").forward(request, response);
        }

    }

    private void checkExistencia4Consumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        String existencia = quid.select_Consumible4Campo("total", WebUtil.decode(session, request.getParameter("idConsumible")));
        if (Double.parseDouble(existencia) <= 0) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Este artículo no se puede surtir en este momento.").forward(request, response);
        }

    }

    private boolean validaFormOrdenSurtido(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (request.getParameter("FormFrom").equals("insertOrdenSurtimiento")
                && request.getParameter("idPlantelSurte").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un plantel.").forward(request, response);
        } else if (request.getParameter("asuntoGeneral").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique un asunto descriptivo.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaRequerida"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor indique una fecha valida.").forward(request, response);
        } else if (request.getParameter("idConsumible") == null) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=No ha agregado ningún material.").forward(request, response);
        } else {

            String[] idConsumibles = request.getParameterValues("idConsumible");

            boolean todoBien = true;
            LinkedList checkDuplicates = new LinkedList();
            String msg = "";
            for (int i = 0; i < idConsumibles.length && todoBien; i++) {
                if (checkDuplicates.indexOf(idConsumibles[i]) == -1) {
                    checkDuplicates.add(idConsumibles[i]);
                    String cantidadSolicitada = request.getParameter(idConsumibles[i]);
                    if (!StringUtil.isValidDoubleMayorCero(cantidadSolicitada)) {
                        todoBien = false;
                        msg = "Escriba una cantidad valida para " + quid.select_Consumible4Campo("descripcion", WebUtil.decode(session, idConsumibles[i]));
                    } else if (Double.parseDouble(cantidadSolicitada) > Double.parseDouble(quid.select_Consumible4Campo("total", WebUtil.decode(session, idConsumibles[i])))) {
                        todoBien = false;
                        msg = "El producto no puede ser surtido en la cantidad solicitada. " + quid.select_Consumible4Campo("descripcion", WebUtil.decode(session, idConsumibles[i]));
                    }
                } else {
                    todoBien = false;
                    msg = "El elemento esta duplicado. " + quid.select_Consumible4Campo("descripcion", WebUtil.decode(session, idConsumibles[i]));
                }
            }

            if (!todoBien) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=" + msg).forward(request, response);
            } else {
                valido = true;
            }

        }
        return valido;
    }

    private void insertOrdenSurtimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormOrdenSurtido(session, request, response, quid, out)) {
            String[] idConsumibles = request.getParameterValues("idConsumible");
            Long folio = quid.select_nextID4Table("ORDEN_SURTIMIENTO");
            LinkedList consumibles = new LinkedList();
            for (int i = 0; i < idConsumibles.length; i++) {
                LinkedList aux = new LinkedList();
                aux.add(WebUtil.decode(session, idConsumibles[i]));
                aux.add(request.getParameter(idConsumibles[i]));
                consumibles.add(aux);

            }
            String justificacion = request.getParameter("justificacion").trim().equals("") ? "" : request.getParameter("justificacion").trim();
            String obs = request.getParameter("observaciones").trim().equals("") ? "" : request.getParameter("observaciones").trim();

            if (quid.trans_insert_OrdenSurtimiento(
                    WebUtil.decode(session, request.getParameter("idPlantelSurte")),
                    request.getParameter("asuntoGeneral").trim(),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    folio.toString(),
                    request.getParameter("fechaRequerida"),
                    justificacion,
                    obs,
                    WebUtil.decode(session, request.getParameter("estatus")),
                    session.getAttribute("userID").toString(),
                    session.getAttribute("userID").toString(),
                    "0000-00-00",
                    consumibles) != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El registro se guardo correctamente. Su folio es: " + folio + ".&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantelSolicita")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=La operación no se pudo completar").forward(request, response);
            }
        }

    }

    private void deleteOrdenConsumible(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("deleteOrdenConsumible");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!quid.select_OrdenSurtimiento4Campo("estatus", WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).equalsIgnoreCase("Pendiente")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=El registro ya no se puede modificar.").forward(request, response);
            } else if (quid.delete_OrdenSurtimiento_Consumible(WebUtil.decode(session, request.getParameter("idOrdenConsumible"))).getCode() != 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=El registro no se pudo eliminar.").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.").forward(request, response);
        }
    }

    private void updateOrdenSurtimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormOrdenSurtido(session, request, response, quid, out)) {
            if (!quid.select_OrdenSurtimiento4Campo("estatus", WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).equalsIgnoreCase("Pendiente")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=El registro ya no se puede modificar.").forward(request, response);
            } else {
                String[] idConsumibles = request.getParameterValues("idConsumible");
                LinkedList consumibles = new LinkedList();
                for (int i = 0; i < idConsumibles.length; i++) {
                    LinkedList aux = new LinkedList();
                    aux.add(WebUtil.decode(session, idConsumibles[i]));
                    aux.add(request.getParameter(idConsumibles[i]));
                    consumibles.add(aux);
                }
                String justificacion = request.getParameter("justificacion").trim().equals("") ? "" : request.getParameter("justificacion").trim();
                String obs = request.getParameter("observaciones").trim().equals("") ? "" : request.getParameter("observaciones").trim();
                if (quid.trans_update_OrdenSurtimiento(
                        request.getParameter("asuntoGeneral"),
                        request.getParameter("fechaRequerida"),
                        justificacion,
                        obs,
                        WebUtil.decode(session, request.getParameter("estatus")),
                        "0000-00-00",
                        consumibles,
                        WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).getCode() == 0) {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El registro se actualizo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantelSolicita")).forward(request, response);
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=El registro no se pudo actualizar.").forward(request, response);
                }
            }
        }
    }

    private void deleteOrdenSurtimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("deleteOrdenSurtimiento");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!quid.select_OrdenSurtimiento4Campo("estatus", WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).equalsIgnoreCase("Pendiente")) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro ya no puede ser borrado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else if (quid.select_CountMovimiento_OrdenSurtimiento(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))) > 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser borrado porque contiene asociaciones.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else if (quid.trans_delete_OrdenSurtimiento(WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Información&type=info&msg=El registro fue borrado exitosamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=El registro no puede ser borrado.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimiento.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantel")).forward(request, response);
        }
    }

    private boolean validaFormOrdenSurtidoPendiente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        if (!StringUtil.isValidInt(request.getParameter("noTurno"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número de turno valido.").forward(request, response);
        } else if (!UTime.validaFecha(request.getParameter("fechaMovimiento"))) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba una fecha valida.").forward(request, response);
        } else if (request.getParameter("estatus").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un estatus.").forward(request, response);
        } else if (!quid.select_OrdenSurtimiento4Campo("estatus", WebUtil.decode(session, request.getParameter("idOrdenSurtimiento"))).equalsIgnoreCase("Proceso")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El registro ya no se puede modificar.").forward(request, response);
        } else {
            valido = true;
        }
        return valido;
    }

    private void updateOrdenSurtimientoPendiente(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (this.validaFormOrdenSurtidoPendiente(session, request, response, quid, out)) {
            String[] consumibles = request.getParameterValues("idConsumible");
            LinkedList listaConsumibles = new LinkedList();
            boolean error = false;
            String msg = "";
            for (int i = 0; i < consumibles.length && !error; i++) {
                String idConsumible = quid.select_OrdenSurtimientoConsumible4Campo("FK_ID_Consumible", WebUtil.decode(session, consumibles[i]));
                if (!StringUtil.isValidDouble(request.getParameter(consumibles[i]))) {
                    error = true;
                    msg = "Por favor escriba una cantidad validad para el artículo número " + (i + 1) + ".";
                } else if (Double.parseDouble(quid.select_Consumible4Campo("total", idConsumible))
                        < Double.parseDouble(request.getParameter(consumibles[i]))) {
                    error = true;
                    msg = "No se puede surtir el artículo número " + (i + 1) + " por falta de inventario.";
                } else {
                    String strCantidadActual = quid.select_Consumible4Campo("total", idConsumible);
                    Double nuevaCantidad = Double.parseDouble(strCantidadActual) - Double.parseDouble(request.getParameter(consumibles[i]));
                    String estatus = "Disponible";
                    if (nuevaCantidad <= 0) {
                        estatus = "Agotado";
                    }
                    LinkedList aux = new LinkedList();
                    aux.add(WebUtil.decode(session, consumibles[i]));
                    aux.add(request.getParameter(consumibles[i]));
                    aux.add("");
                    aux.add(idConsumible);
                    aux.add(nuevaCantidad.toString());
                    aux.add(estatus);
                    listaConsumibles.add(aux);
                }
            }
            if (error) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=" + msg).forward(request, response);
            } else if (quid.trans_insert_OrdenSurtimientoMovimientoSalida(
                    WebUtil.decode(session, request.getParameter("idPlantelSurte")),
                    WebUtil.decode(session, request.getParameter("idPlantelSolicita")),
                    WebUtil.decode(session, request.getParameter("folio")),
                    request.getParameter("fechaMovimiento"),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    "NA",
                    "NA",
                    WebUtil.decode(session, request.getParameter("estatus")),
                    request.getParameter("observacionesMovimiento"),
                    "0.0",
                    WebUtil.decode(session, request.getParameter("idTipoMovimiento")),
                    session.getAttribute("userID").toString(),
                    "",
                    request.getParameter("noTurno"),
                    request.getParameter("observaciones"),
                    "Completado",
                    session.getAttribute("userID").toString(),
                    UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                    WebUtil.decode(session, request.getParameter("idOrdenSurtimiento")),
                    listaConsumibles) != -1) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirect.jsp?title=Operación Exitosa&type=info&msg=El registro se guardo correctamente.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaOrdenSurtimientoPendiente.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_idPlantel=" + request.getParameter("idPlantelSurte")).forward(request, response);

            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=La operación no se pudo realizar").forward(request, response);
            }
        }
    }

    private void getSesionsList(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList sessionsData = new LinkedList();
        ConcurrentHashMap<String, HttpSession> sessions = SessionUtil.getAllSessions();
        Collection<?> keys = sessions.keySet();
        for (Object key : keys) {
            HttpSession s = sessions.get(key.toString());
            try {
                LinkedList aux = new LinkedList();
                aux.add(s.getId());
                aux.add(quid.select_PlantelXCampo("nombre", s.getAttribute("FK_ID_Plantel").toString()));
                aux.add(s.getAttribute("userName"));
                aux.add(s.getAttribute("tipoRol"));
                aux.add(UTime.getStringDateLongFormat(s.getCreationTime()));
                aux.add(UTime.getStringDateLongFormat(s.getLastAccessedTime()));
                aux.add(UTime.getDiferenciaEnHoras(Calendar.getInstance().getTimeInMillis(), s.getLastAccessedTime()));
                aux.add(s.getAttribute("userID"));
                aux.add(UTime.getDiferenciaEnHoras(s.getLastAccessedTime(), s.getCreationTime()));
                aux.add(UTime.milisToHours(s.getMaxInactiveInterval() * 1000 - (Calendar.getInstance().getTimeInMillis() - s.getLastAccessedTime())));
                aux.add(UTime.milisToHours(s.getMaxInactiveInterval() * 1000));
                aux.add("1");
                sessionsData.add(aux);
            } catch (Exception ex) {
                try {
                    LinkedList aux = new LinkedList();
                    aux.add(s.getId());
                    aux.add("Anónimo");
                    aux.add("Anónimo");
                    aux.add("Anónimo");
                    aux.add(UTime.getStringDateLongFormat(s.getCreationTime()));
                    aux.add(UTime.getStringDateLongFormat(s.getLastAccessedTime()));
                    aux.add(UTime.getDiferenciaEnHoras(Calendar.getInstance().getTimeInMillis(), s.getLastAccessedTime()));
                    aux.add("Anónimo");
                    aux.add(UTime.getDiferenciaEnHoras(s.getLastAccessedTime(), s.getCreationTime()));
                    aux.add(UTime.getDiferenciaEnHoras(new Date(s.getLastAccessedTime() + s.getMaxInactiveInterval() * 1000).getTime(), Calendar.getInstance().getTimeInMillis()));
                    aux.add(UTime.milisToHours(s.getMaxInactiveInterval() * 1000));
                    aux.add("0");
                    sessionsData.add(aux);
                } catch (Exception eX2) {

                }
            }
        }
        request.setAttribute("sesiones", sessionsData);
        request.getRequestDispatcher("/ajaxFunctions/getSessions.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
    }

    private void closeSession(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("terminaSesion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!session.getId().equalsIgnoreCase(WebUtil.decode(session, request.getParameter("idSesion")))) {
                SessionUtil.clearNCloseSession(WebUtil.decode(session, request.getParameter("idSesion")));
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Información&type=info&msg=La sesión fue terminada.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=No puede terminar su propia sesión.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void closeAllSession(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("terminaSesion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            SessionUtil.destroyAllSessions(session.getId());
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Información&type=info&msg=Todas las sesiónes fueron terminadas.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void closeAllSessionTime(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("terminaSesion");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("sessionTime") == null || !StringUtil.isPositiveInt(request.getParameter("sessionTime"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Información&type=info&msg=Por favor escriba un número valido en minutos.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            } else {
                int count = SessionUtil.destroyAllSessionsIdleTime(session.getId(), Integer.parseInt(request.getParameter("sessionTime")));
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Información&type=info&msg=Se cerraron " + count + " sesiones&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.&url=" + PageParameters.getParameter("mainContext") + "/jspread/admin/SessionManager.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis())).forward(request, response);
        }
    }

    private void calcPatronSerialNumber(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("calcularPatronNumeroSerie");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            int patronesInsertados = this.calcularPatronSerialNumber(session, request, response, quid, out);
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Información&type=info&msg=Se guardaron " + patronesInsertados + " patron(es)").forward(request, response);
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta acción.").forward(request, response);
        }
    }

    private int calcularPatronSerialNumber(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList idModelos = new LinkedList();
//            String ID_Usuario = "-1";
//            if (WebUtil.decode(session, request.getParameter("ID_Usuario")) != null && !WebUtil.decode(session, request.getParameter("ID_Usuario")).equalsIgnoreCase("")) {
//                ID_Usuario = WebUtil.decode(session, request.getParameter("ID_Usuario"));
//            }
        if (request.getParameter("idModelo") != null) {
            idModelos.add(WebUtil.decode(session, request.getParameter("idModelo")));
        } else {
            idModelos = quid.select_ID_Modelos();
        }
        int patronesInsertados = 0;

        for (int i = 0; i < idModelos.size(); i++) {
//                if (quid.select_Patron_SerieDeUsuario(idModelos.get(i).toString(), "-1").isEmpty()) {
//                    ID_Usuario = "-1";
            Long countSeriales = quid.select_countSerialByModelo(idModelos.get(i).toString());
            if (countSeriales >= 1) {
                int muestra = 0;
                if (countSeriales >= 1 && countSeriales <= 5000) {
                    muestra = Integer.parseInt(countSeriales.toString());
                } else if (countSeriales >= 5001 && countSeriales <= 10000) {
                    muestra = Integer.parseInt("" + (countSeriales * 0.5));
                } else if (countSeriales >= 10001 && countSeriales <= 100000) {
                    muestra = Integer.parseInt("" + (countSeriales * 0.3));
                } else if (countSeriales >= 100001 && countSeriales <= 100000) {
                    muestra = Integer.parseInt("" + (countSeriales * 0.1));
                } else if (countSeriales >= 1000001 && countSeriales <= 10000000) {
                    muestra = Integer.parseInt("" + (countSeriales * 0.05));
                } else {
                    muestra = Integer.parseInt("" + (countSeriales * 0.01));
                }
                LinkedList serialNumbers = quid.select_RandomSerialNumbers(idModelos.get(i).toString(), muestra);
//                        System.out.println("\n\n\n");
//                        System.out.println("ID_Modelo:" + WebUtil.decode(session, request.getParameter("ID_Modelo")));
                Iterator itPatrones = PatronUtil.calcPatronesMuestra(serialNumbers, 0.07f, 7).iterator();
                while (itPatrones.hasNext()) {
                    LinkedList aux = (LinkedList) itPatrones.next();
                    Iterator aux2;
                    if (request.getParameter("ID_Usuario") != null) {
                        aux2 = quid.select_Patron_SerieXId(aux.get(1).toString(), aux.get(2).toString(), idModelos.get(i).toString(), "").iterator();
                    } else {
                        aux2 = quid.select_Patron_SerieXId(aux.get(1).toString(), aux.get(2).toString(), idModelos.get(i).toString(), "-1").iterator();
                    }

                    while (aux2.hasNext()) {
                        //LinkedList datos = (LinkedList) aux2.next();
                        quid.delete_Patron_Serie(aux2.next().toString());
                    }
                    if (quid.insert_Patron_Serie(
                            aux.get(0).toString(),
                            UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                            aux.get(1).toString(),
                            aux.get(2).toString(),
                            aux.get(3).toString(),
                            "-1",
                            idModelos.get(i).toString()) != -1) {
                        patronesInsertados++;
                    }
                }
            }
//                    ID_Usuario = request.getParameter("ID_Usuario");
//                }
        }
        return patronesInsertados;
    }

    private void actualizarPatronSerie(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("UpdatePatronSerie");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (request.getParameter("column").equalsIgnoreCase("1")) {
                Transporter tport = quid.update_PatronSerie(
                        request.getParameter("value").trim().toUpperCase(),
                        WebUtil.decode(session, request.getParameter("row_id")),
                        UTime.calendar2SQLDateFormat(Calendar.getInstance()),
                        session.getAttribute("userID").toString());
                if (tport.getCode() == 0) {
                    out.print(request.getParameter("value"));
                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(
                            "" + PageParameters.getParameter("msgUtil")
                            + "/msg.jsp?title=Error&type=error&msg=Ha ocurrido un error.").forward(request, response);
                }
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void deletePatronSerie(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("DeletePatronSerie");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

            Transporter tport = quid.delete_Patron_Serie(WebUtil.decode(session, request.getParameter("ID_Patron_Serie")));
            if (tport.getCode() == 0) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Información&type=info&msg=El borrado ha sido exitoso.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModeloPatron.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Modelo=" + request.getParameter("ID_Modelo")).forward(request, response);
            } else {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Ha ocurrido un error.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModeloPatron.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Modelo=" + request.getParameter("ID_Modelo")).forward(request, response);
            }

        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msgNRedirectFull.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.&url=" + PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaModeloPatron.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, UTime.getTimeMilis()) + "_param_ID_Modelo=" + request.getParameter("ID_Modelo")).forward(request, response);
        }
    }

    private void resetFoliosMovimiento(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("ResetFolios");
        LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
        if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
            if (!StringUtil.isPositiveInt(request.getParameter("cuentaTotalEntrada"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número valido para los folios de entrada.").forward(request, response);
            } else if (!StringUtil.isPositiveInt(request.getParameter("cuentaTotalSalida"))) {
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Error&type=error&msg=Por favor escriba un número valido para los folios de salida.").forward(request, response);
            } else {
                int sucess = 0;
                if (quid.update_FoliosTipoMovimiento(quid.select_TipoMovimiento("ENTRADA") + "", request.getParameter("cuentaTotalEntrada")).getCode() == 0) {
                    sucess += 1;
                }
                if (quid.update_FoliosTipoMovimiento(quid.select_TipoMovimiento("SALIDA") + "", request.getParameter("cuentaTotalSalida")).getCode() == 0) {
                    sucess += 1;
                }
                this.getServletConfig().getServletContext().getRequestDispatcher(
                        "" + PageParameters.getParameter("msgUtil")
                        + "/msg.jsp?title=Información&type=info&msg=Se restablecieron los folios de " + sucess + " tipos de movimiento").forward(request, response);
            }
        } else {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Usted NO cuenta con el permiso para realizar esta tarea.").forward(request, response);
        }
    }

    private void validaSerial(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        if (request.getParameter("idCategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una categoria.").forward(request, response);
        } else if (request.getParameter("idSubcategoria") == null
                || request.getParameter("idSubcategoria").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una subcategoria.").forward(request, response);
        } else if (request.getParameter("idMarca").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione una marca.").forward(request, response);
        } else if (request.getParameter("idModelo").equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor seleccione un modelo.").forward(request, response);
        } else if (request.getParameter("noSerie").trim().equals("")) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=Por favor escriba el número de serie.").forward(request, response);
        } else if (!SystemUtil.haveAcess("evitarPatronSerie", (LinkedList<String>) session.getAttribute("userAccess"))
                && !this.validaSerialXModelo(session, request, response, quid, out)) {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    "" + PageParameters.getParameter("msgUtil")
                    + "/msg.jsp?title=Error&type=error&msg=El número de serie no coincide con el patron del modelo seleccionado.").forward(request, response);
        }
    }

    private boolean validaSerialXModelo(HttpSession session, HttpServletRequest request, HttpServletResponse response, QUID quid, PrintWriter out) throws Exception {
        boolean valido = false;
        String ID_Modelo = WebUtil.decode(session, request.getParameter("idModelo"));
        String noSerie = request.getParameter("noSerie").trim().toUpperCase();

        LinkedList patrones = quid.select_PatronXModelo(ID_Modelo, false);
        if (patrones.size() > 0) {
            valido = this.matchSerialXModelo(quid, patrones, ID_Modelo, noSerie);
        } else if (quid.select_CountBien4Campo("FK_ID_Modelo", ID_Modelo) >= 20) {
            if (this.calcularPatronSerialNumber(session, request, response, quid, out) > 0) {
                valido = this.validaSerialXModelo(session, request, response, quid, out);
            }
        } else {
            valido = true;
        }
        return valido;
    }

    private boolean matchSerialXModelo(QUID quid, LinkedList patrones, String ID_Modelo, String noSerie) throws Exception {
        boolean match = false;
        for (int i = 0; i < patrones.size(); i++) {
            if (PatronUtil.StringMatchWithPattern(patrones.get(i).toString().toUpperCase(), noSerie)) {
                match = true;
                break;
            }
        }
        return match;
    }

//    private boolean validaSerialXModelo(QUID quid, String ID_Modelo, String noSerie) throws Exception {
//        LinkedList patrones = quid.select_PatronXModelo(ID_Modelo, false);
//        boolean valido = false;
//        if (patrones.size() > 0) {
//            for (int i = 0; i < patrones.size(); i++) {
//                if (PatronUtil.StringMatchWithPattern(patrones.get(i).toString().toUpperCase(), noSerie)) {
//                    valido = true;
//                    break;
//                }
//            }
//        } else {
//            valido = true;
//        }
//        return valido;
//    }
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
            Logger.getLogger(controller.class
                    .getName()).log(Level.SEVERE, null, ex);
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
