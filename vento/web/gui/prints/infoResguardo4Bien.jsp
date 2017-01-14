<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("verInfoResguardo");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Ordern de Servicio</title>
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
                                    <br><b><strong>INFORMACIÓN DE RESGUARDO</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                    <%
                        LinkedList bien = QUID.select_Bien(WebUtil.decode(session,request.getParameter("idBien")));
                    %>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                       <tr class="celdaRellenoCenter">
                            <td width="50%">Plantel</td>
                            <td>Fecha</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td ><%=bien.get(1)%></td>
                            <td><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                        <tr  class="celdaRellenoCenter">
                            <td>Categoría del Bien/Artículo</td>
                            <td>Marca</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td ><%=bien.get(5)%></td>
                            <td><%=bien.get(7)%></td>
                        </tr>
                        <tr class="celdaRellenoCenter">
                            <td>Modelo</td>
                            <td>No. Serie</td>
                        </tr>
                        <tr>
                            <td><%=bien.get(9)%></td>
                            <td><%=bien.get(12)%></td>
                        </tr>
                        <tr class="celdaRellenoCenter">
                            <td >No. Inventario</td>
                            <td>Estatus</td>
                        </tr>
                        <tr>
                            <td><%=bien.get(15)%></td>
                            <td><%=bien.get(18)%></td>
                        </tr>
                        <tr>
                            <td colspan="2"><b>Observaciones: </b><%=bien.get(19)%></td>
                        </tr>
                    </table>
                    <br><br>
                    <%
                     Iterator resguardos = QUID.select_Resguardo4Bien(WebUtil.decode(session, request.getParameter("idBien")), "Activo").iterator();
                                while (resguardos.hasNext()) {
                                    LinkedList datosResguardo = (LinkedList) resguardos.next();
                    %>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr class="celdaRellenoCenter">
                            <td colspan="2">Responsable del Resguardo</td>
                        </tr>
                        <tr>
                            <td><b>Nombre:&nbsp;</b><%=datosResguardo.get(2).toString().toString().toUpperCase()%></td>
                            <td><b>Cargo:&nbsp;</b><%=datosResguardo.get(4).toString().toUpperCase()%></td>
                        </tr>
                        <tr>
                            <td><b>No. de Tarjeta de Resguardo:&nbsp;</b><%=datosResguardo.get(10).toString().toUpperCase()%></td>
                            <td><b>Fecha de Asignación:&nbsp;</b><%=datosResguardo.get(7).toString().toUpperCase()%></td>
                        </tr>
                        <tr>
                            <td colspan="2"><b>Observaciones:&nbsp;</b><%=datosResguardo.get(6).toString().toUpperCase()%></td>
                        </tr>
                    </table>
                    <br>
                    <%
                                }
                    %>
                    <br><br><br><br>
                    <table width="92%" class="firma" style="text-align: center;">
                        <tr>
                            <td width="40%">_________________________________________</td>
                            <td width="0%"></td>
                            <td width="40%">_________________________________________</td>
                        </tr>
                        <tr>
                            <td>Responsable del Resguardo</td>
                            <td></td>
                            <td>Vo. Bo.</td>
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
