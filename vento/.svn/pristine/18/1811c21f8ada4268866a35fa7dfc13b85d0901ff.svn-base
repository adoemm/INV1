<%@page import="java.util.Calendar"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {

            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Reportes</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        

        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>

                <p></p>
                <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="64%" height="25" align="left" valign="top">
                            <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                            > Reportes
                        </td>
                        <td width="36" align="right" valign="top">
                            <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                        </td>
                    </tr>                        
                </table>
                <br>
                <br>
                <br>
                <div>
                    <table width="200" border="0" align="center"  >
                        <tr>

                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/Garantia.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/reporteGarantia.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/ResumenBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/resumenGeneral.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/Totales4Subcategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/conteoBienes.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/Totales4Modelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/conteoModelo.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/ResumenGeneral.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/resumenGeneral2.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/Baja4Status.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/baja4Status.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/BarCode4Depto.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/codigoBarras.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/Totales4Departamento.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/conteoDepartamentos.png" width="150" height="40"/></a></td>                        
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/SinNoInventario.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/sinNoInventario.png" width="150" height="40"/></a></td>                        
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/ResumenAgrupaciones.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/resumenAgruaciones.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr><td colspan="3" style="text-align: center;">Patrones de Números de Serie</td></tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/MismoModeloDifPatron.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/mismoModeloDifPatron.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/DifModeloMismoPatron.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/DifModeloMismoPatron.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/TodosPatrones.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/todosPatrones.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("reports")%>/ConsultaXPatron.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/buscarXPatron.png" width="150" height="40"/></a></td>
                        </tr>
                    </table>
                </div>
                <br>
                <br>
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
            </div>            
        </div>
    </body>
</html>
<%
} else {

%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<%@ include file="/gui/pageComponents/handleUnExpectedError.jsp"%>
</body>
</html>
<%
    }
%>
