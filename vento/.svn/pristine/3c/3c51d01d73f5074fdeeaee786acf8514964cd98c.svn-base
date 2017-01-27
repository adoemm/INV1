<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertEnlace");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Enlace</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function (response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
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
                <div class="errors">
                    <p>
                        <em>Los campos con  <strong>*</strong> son necesarios.</em>
                    </p>
                </div>
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a> >
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaEnlace.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Conectividad</a> 
                                > Nuevo Enlace
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertEnlace" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertEnlace">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertEnlace"/>
                        <fieldset>
                            <legend>Nuevo Enlace</legend>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                            </jsp:include>
                            <div>
                                <label for="tipo">*Tipo de Enlace</label>
                                <select name="tipo">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "SATELITAL")%>">SATELITAL</option>
                                    <option value="<%=WebUtil.encode(session, "CABLE")%>">CABLE</option>
                                    <option value="<%=WebUtil.encode(session, "ADSL")%>">ADSL</option>
                                    <option value="<%=WebUtil.encode(session, "PLC")%>">PLC</option>
                                </select>
                            </div>
                            <div>
                                <label for="velocidadSubida">*Velocidad de Subida(Mbps)</label>
                                <input type="text" name="velocidadSubida" >
                            </div>
                            <div>
                                <label for="velocidadBajada">*Velocidad de Bajada(Mbps)</label>
                                <input type="text" name="velocidadBajada" >
                            </div>

                            <div>
                                <label for="noAlumnos">*Total Alumnos Conectados</label>
                                <input type="text" name="noAlumnos">
                            </div>
                            <div>
                                <label for="noDocentes">*Total Docentes Conectados</label>
                                <input type="text" name="noDocentes">
                            </div>
                            <div>
                                <label for="noAdministrativos">*Total Administrativos Conectados</label>
                                <input type="text" name="noAdministrativos">
                            </div>
                            <div>
                                <label for="noDispositivos">*Total Dispositivos Conectados</label>
                                <input type="text" name="noDispositivos">
                            </div>
                            <div>
                                <label for="noNodos">*Total Nodos Conectados</label>
                                <input type="text" name="noNodos">
                            </div>
                            <div>
                                <label for="calidadServicio">*Calidad de Servicio(0 a 10)</label>
                                <input type="text" name="calidadServicio">
                            </div>
                            <div>
                                <%
                                    Iterator t = QUID.select_Proveedor().iterator();
                                %>
                                <label for="idProveedor">*Proveedor</label>          
                                <select name="idProveedor">
                                    <option value=""></option>
                                    <%
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertEnlace'));"/>
                        </div> 
                    </form>
                    <div id="divResult"> 
                    </div>
                </div>   
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
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