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
        <title>Catálogos del Sistema</title>
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
                            > Catálogos del Sistema
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
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMarca.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoMarcas.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoModelos.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipo_Compra.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoTipoCompras.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipo_Garantia.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoGarantias.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaLicencia.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoLicencias.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipo_Software.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/tipoSoftware.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaCategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoCategorias.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSubCategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoSubCategorias.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaDepartamento.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoDepartamento.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipoProveedor.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/tipoProveedores.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaDetalle.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoDetalles.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPlanteles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoPlanteles.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaNombreSoftware.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/catalogoNombreSoftware.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaCheckList.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/checkList.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaIncidente.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/incidentes.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipoArchivo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/tipoDocumento.png" width="150" height="40"/></a></td>
                        </tr>
                        <tr>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMedida.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/presentacionConsumible.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTipoActividad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/tipoActividad.png" width="150" height="40"/></a></td>
                            <td><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaRubro.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/consultaRubros.png" width="150" height="40"/></a></td>
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
