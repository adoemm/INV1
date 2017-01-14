<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("OrdenServicio");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Orden de Servicio</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 25cm; height: 28cm; margin: 0 auto;">
            <div>
                <div align="center">
                    <table width="92%" border="0" align="center">
                        <tr>
                            <td align="left" width="7%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td class="estilo2" width="80%">
                                <p align="center">
                                    <strong>COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</strong>
                                    <br><b><strong>UNIDAD DE INFORMÁTICA</strong></b>
                                    <br><b><strong>ORDEN DE SERVICIO</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                    <%
                        LinkedList incidente = QUID.select_Bitacora_Incidente(WebUtil.decode(session, request.getParameter("idBitacoraIncidente")));
                    %>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr  class="celdaRellenoCenter">
                            <td>Número de Orden</td>
                            <td>Fecha</td>
                            <td colspan="2">Plantel</td>
                            
                        </tr>
                        
                        <tr style="text-align: center;">
                            <td><%=incidente.get(7)%></td>
                            <td><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                            <td colspan="2"><%=incidente.get(0)%></td>
                            
                        </tr>
                        <tr  class="celdaRellenoCenter">
                            <td colspan="4">Departamento</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td colspan="4"><%=incidente.get(1)%></td>
                        </tr>
                        <tr  class="celdaRellenoCenter">
                            <td >Categoría del Equipo</td>
                            <td >Marca</td>
                            <td colspan="2" >Modelo</td>
                            
                        </tr>
                        <tr style="text-align: center;">
                            <td><%=incidente.get(19)%></td>
                            <td><%=incidente.get(5)%></td>
                            <td><%=incidente.get(6)%></td>
                        </tr>
                        <tr  class="celdaRellenoCenter">
                            <td colspan="2" width="50%">No. Serie</td>
                            <td colspan="2">No. Inventario</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td colspan="2"><%=incidente.get(3)%></td>
                            <td colspan="2"><%=incidente.get(4)%></td>
                        </tr>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr class="celdaRellenoCenter">
                            <td width="50%">Falla</td>
                            <td width="50%">Solución</td>
                        </tr>
                        <tr style="text-align: left;" height="100px">
                            <td><%=incidente.get(15)%></td>
                            <td><%=incidente.get(11)%></td>
                        </tr>
                        <tr>
                            <td colspan="2"><b>Técnico:</b>&nbsp;<input type="text" style="border: 0px; text-align: left;font-size: 12px; width: 80%" value="Nombre del Técnico"></td>
                        </tr>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr class="celdaRellenoCenter">
                            <td width="50%">Entrega Informática</td>
                            <td width="50%">Recibe Usuario/Plantel</td>
                        </tr>
                        <tr>
                            <td><b>Nombre:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value=""></td>
                            <td><b>Nombre:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value=""></td>
                        </tr>
                        <tr>
                            <td><b>Cargo:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value=""></td>
                            <td><b>Cargo:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value=""></td>
                        </tr>
                        <tr>
                            <td><b>Firma:&nbsp;</b></td>
                            <td><b>Firma:&nbsp;</b></td>
                        </tr>
                        <tr>
                            <td><b>Fecha:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value="<%=incidente.get(8)%>"></td>
                            <td><b>Fecha:&nbsp;</b><input type="text"  style="border: 0px; text-align: left;font-size: 12px; width: 80%" value=""></td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" >
                        <tr>
                            <td class="nota">
                                NOTA:Los C.P.U.'s quedan sellados con una etiqueta de seguridad de la Unidad de Informática y el equipo entregado se encuentra reparado y funcionando correctamente.
                            </td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr class="celdaRellenoCenter">
                            <td>Observaciones</td>
                        </tr>
                        <tr height="100px">
                            <td><%=incidente.get(10)%></td>
                        </tr>
                    </table> 
                    <p class="break"></p>
                </div>
            </div>
        </div>
    </body>
</html>
<%} else {
    //System.out.println("Usuario No valido para esta pagina");
%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
} else {
    //System.out.println("No se ha encontrado a imix");
%>
<%@ include file="/gui/pageComponents/invalidParameter.jsp"%>
<%        }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<%@ include file="/gui/pageComponents/handleUnExpectedError.jsp"%>
</body>
</html>
<%
        //response.sendRedirect(redirectURL);
    }
%>
