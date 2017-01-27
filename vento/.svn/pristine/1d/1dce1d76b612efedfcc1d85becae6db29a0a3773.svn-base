<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null && request.getParameter("idSubcategoria") != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteConteoBien");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Conteo de Bienes</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 28cm; height: 25cm; margin: 0 auto;">
            <div>
                <div align="center">
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Conteo de Equipo de Cómputo Existente en CECyTEM</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center"  style="text-align: center;">
                        <tr >
                            <td width="85%" ></td>
                            <td width="15%" class="borde">FECHA:&nbsp;<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <%
                        String[] subcategorias = request.getParameterValues("idSubcategoria");
                        Iterator t = QUID.select_Totales4Subcategoria(WebUtil.decode(session, request.getParameter("idPlantel")),
                                "Baja", subcategorias, false).iterator();
                    %>
                    <table width="100%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 8px;">
                        <tr class="celdaRellenoCenter">
                            <td>Plantel</td>
                            <%
                                for (int i = 0; i < subcategorias.length; i++) {
                            %>
                            <td><%=subcategorias[i]%></td>
                            <%
                                }
                            %>
                            <td>Total</td>
                        </tr>
                        <%
                            int fila = 1;
                            while (t.hasNext()) {
                                LinkedList datos = (LinkedList) t.next();
                                if (fila % 14 == 0) {
                        %>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 13px;">
                        <tr>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td ><strong>Colegio de Estudios Científicos y Tecnologicos del Estado de México</strong>
                                <br><strong>Unidad de Informática</strong>
                                <br><strong>Conteo de Equipo de Cómputo Existente en CECyTEM</strong>
                            </td>
                            <td width="15%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center"  style="text-align: center;">
                        <tr >
                            <td width="85%" ></td>
                            <td width="15%" class="borde">FECHA:&nbsp;<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                        </tr>
                    </table>
                    <table width="100%" border="0" align="center" class="borde" rules="all" style="text-align: center;font-size: 8px;">
                        <tr class="celdaRellenoCenter">
                            <td>Plantel</td>
                            <%
                                for (int i = 0; i < subcategorias.length; i++) {
                            %>
                            <td><%=subcategorias[i]%></td>
                            <%
                                }
                            %>
                            <td>Total</td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td><%=datos.get(0)%></td>
                            <%
                                for (int i = 0; i < subcategorias.length; i++) {
                            %>
                            <td><%=datos.get(i + 2)%></td>
                            <%
                                }
                            %>
                            <td><%=datos.get(1)%></td>
                        </tr>
                        <%
                                fila += 1;
                            }
                        %>
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
