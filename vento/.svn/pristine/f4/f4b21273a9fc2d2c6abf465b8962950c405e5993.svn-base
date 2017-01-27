<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("checkList4Bien");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Instrumento de Validación</title>
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
                                    <br><b><strong>Instrumento de Validación</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <%
                        LinkedList checkList = QUID.select_CheckList(WebUtil.decode(session, request.getParameter("idCheckList")));
                        LinkedList bien = QUID.select_Bien(WebUtil.decode(session, request.getParameter("idBien")));
                    %>
                    <table width="92%" border="0" align="center" >
                        <tr>
                            <td colspan="4"><div class="borde"><b>&nbsp;Descripción</b></div></td>
                        </tr>
                        <tr>
                            <td colspan="4"><div class="borde">&nbsp;<%=checkList.get(0)%></div></td>
                        </tr>
                        <tr>
                            <td colspan="2" width="50%"><div class="borde"><b>&nbsp;Marca:</b>&nbsp;<%=bien.get(7)%></div></td>
                            <td colspan="2"><div class="borde"><b>&nbsp;Modelo:</b>&nbsp;<%=bien.get(9)%></div></td>

                        </tr>
                        <tr>
                            <td><div class="borde" width="37%"><b>&nbsp;No. Serie:</b>&nbsp;<%=bien.get(12)%></div></td>
                            <td><div class="borde" width="37%"><b>&nbsp;No.Inventario:</b>&nbsp;<%=bien.get(15)%></div></td>
                            <td colspan="2"><div class="borde" width="26%"><b>&nbsp;Fecha:</b>&nbsp;<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></div></td>
                        </tr>
                    </table>
                    <p></p>
                    <table width="92%" class="onlyBordeOddChild" align="center" rules="all" style="">
                        <tr>
                            <td class="celdaRellenoCenter" width="10%">NP</td>
                            <td class="celdaRellenoCenter" width="20%">Categoría</td>
                            <td class="celdaRellenoCenter">Rubro</td>
                            <td class="celdaRellenoCenter" style="text-align: center; width:10%; ">&nbsp;</td>
                        </tr>
                        <%
                            Iterator rubros = QUID.select_Rubro4Bien(WebUtil.decode(session, request.getParameter("idBien")),
                                    WebUtil.decode(session, request.getParameter("idCheckList"))).iterator();
                            int i = 1;
                            while (rubros.hasNext()) {
                                LinkedList datos = (LinkedList) rubros.next();
                                if (i % 20 == 0) {
                        %>
                    </table>
                    <p>&nbsp;</p><p>&nbsp;</p>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>_____________________________________</td>
                            <td>
                                &nbsp;
                            </td>
                            <td>_____________________________________</td>
                        </tr>
                        <tr>
                            <td><input type="text" size="60" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre del Usuario"></td>
                            <td >
                            </td>
                            <td><input type="text" size="60" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre del Encargado"></td>
                        </tr>
                        <tr>
                            <td>Usuario.</td>
                            <td></td>
                            <td>Revisó.</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center" >
                        <tr>
                            <td colspan="4"><div class="borde"><b>&nbsp;Descripción</b></div></td>
                        </tr>
                        <tr>
                            <td colspan="4"><div class="borde">&nbsp;<%=checkList.get(0)%></div></td>
                        </tr>
                        <tr>
                            <td colspan="2" width="50%"><div class="borde"><b>&nbsp;Marca:</b>&nbsp;<%=bien.get(7)%></div></td>
                            <td colspan="2"><div class="borde"><b>&nbsp;Modelo:</b>&nbsp;<%=bien.get(9)%></div></td>

                        </tr>
                        <tr>
                            <td><div class="borde" width="37%"><b>&nbsp;No. Serie:</b>&nbsp;<%=bien.get(12)%></div></td>
                            <td><div class="borde" width="37%"><b>&nbsp;No.Inventario:</b>&nbsp;<%=bien.get(15)%></div></td>
                            <td colspan="2"><div class="borde" width="26%"><b>&nbsp;Fecha:</b>&nbsp;<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></div></td>
                        </tr>
                    </table>
                    <p></p>
                    <table width="92%" class="onlyBordeOddChild" align="center" rules="all" style="">
                        <tr>
                            <td class="celdaRellenoCenter" width="10%">NP</td>
                            <td class="celdaRellenoCenter" width="20%">Categoría</td>
                            <td class="celdaRellenoCenter">Rubro</td>
                            <td class="celdaRellenoCenter" style="text-align: center; width:10%; ">&nbsp;</td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td style="text-align: center;"><%=i%></td>
                            <td style="text-align: left;"><%=datos.get(3)%></td>
                            <td><%=datos.get(0)%></td>
                            <td style="text-align: center;">
                                <%
                                    if (datos.get(1) != null) {
                                %>
                                <img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Dialog-Apply-64.png" width="24" height="24"/>
                                <%
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                                i += 1;
                            }
                        %>
                    </table>
                    <p>&nbsp;</p><p>&nbsp;</p>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>_____________________________________</td>
                            <td>
                                &nbsp;
                            </td>
                            <td>_____________________________________</td>
                        </tr>
                        <tr>
                            <td><input type="text" size="60" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre del Usuario"></td>
                            <td >
                            </td>
                            <td><input type="text" size="60" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre del Encargado"></td>
                        </tr>
                        <tr>
                            <td>Usuario.</td>
                            <td></td>
                            <td>Revisó.</td>
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
