<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteGarantia");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Garantias</title>
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
                    <%
                        String tipoGarantia = QUID.select_Tipo_Garantia(WebUtil.decode(session, request.getParameter("tipoGarantia")));
                        LinkedList subcategorias = new LinkedList();
                        String[] code = request.getParameterValues("idSubcategoria");
                        for (int i = 0; i < request.getParameterValues("idSubcategoria").length; i++) {
                            subcategorias.add(WebUtil.decode(session, code[i]));
                        }

                        Iterator t = QUID.select_Garantia4TipoGarantia(
                                WebUtil.decode(session, request.getParameter("tipoGarantia")),
                                subcategorias,
                                "Baja",
                                false,
                                WebUtil.decode(session, request.getParameter("idPlantel"))).iterator();
                    %>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" style="text-align: right;">
                        <tr>
                            <td width="85%" >FECHA:</td>
                            <td width="15%" ><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr class="encabezadoColuma"><td colspan="8">RELACIÓN DE EQUIPO DE COMPUTO QUE CUENTA CON GARANTÍA <%=tipoGarantia%></td></tr>
                        <tr class="encabezadoColuma">
                            <td>NO.</td>
                            <td>MARCA</td>
                            <td>MODELO</td>
                            <td>CANTIDAD</td>
                            <td>FECHA INICIO</td>
                            <td>FECHA FIN</td>
                            <td>FACTURA</td>
                            <td>PROVEEDOR</td>
                        </tr>
                        <%
                            int i = 1;
                            while (t.hasNext()) {
                                LinkedList datos = (LinkedList) t.next();
                                if (i % 15 == 0) {
                        %>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                _______________________________________________
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre Responsable">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value="Escriba aqui el cargo">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                     <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" style="text-align: right;">
                        <tr>
                            <td width="85%" >FECHA:</td>
                            <td width="15%" ><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr class="encabezadoColuma"><td colspan="8">RELACIÓN DE EQUIPO DE COMPUTO QUE CUENTA CON GARANTÍA <%=tipoGarantia%></td></tr>
                        <tr class="encabezadoColuma">
                            <td>NO.</td>
                            <td>MARCA</td>
                            <td>MODELO</td>
                            <td>CANTIDAD</td>
                            <td>FECHA INICIO</td>
                            <td>FECHA FIN</td>
                            <td>FACTURA</td>
                            <td>PROVEEDOR</td>
                        </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td><%=i%></td>
                        <td><%=datos.get(0)%></td>
                        <td><%=datos.get(1)%></td>
                        <td><%=datos.get(8)%></td>
                        <td><%=datos.get(2)%></td>
                        <td><%=datos.get(3)%></td>
                        <td><%=datos.get(4)%></td>
                        <td><%=datos.get(5)%></td>
                    </tr>
                    <%
                            i += 1;
                        }
                    %>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" style="text-align: center;">
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                _______________________________________________
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="50%">
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value="Nombre Responsable">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" size="80" style="border: 0px; text-align: center;font-size: 12px;" value="Escriba aqui el cargo">
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                </div>
            </div>
        </div>
    </body>
</html>
<%
} else {
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
