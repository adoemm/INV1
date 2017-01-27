<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertBitacoraincidente");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Reportar Incidente</title>
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
                    , success: function(response) {
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Bienes</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Editar Bien</a> 
                                > Reportar Incidente
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertBitacoraIncidente" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertBitacoraIncidente">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertBitacoraIncidente"/>
                        <input type="hidden" id='idBien' name="idBien" value="<%=request.getParameter("idBien")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Reportar Incidente</legend>
                            <%
                                Iterator incidentes = QUID.select_Incidente().iterator();
                            %>
                            <div>
                                <label for="idIncidente">*Incidente</label>
                                <select name="idIncidente">
                                    <option value=""></option>
                                    <%
                                        while (incidentes.hasNext()) {
                                            LinkedList datos = (LinkedList) incidentes.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="prioridad">*Prioridad</label>
                                <select name="prioridad">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "Alta")%>">Alta</option>
                                    <option value="<%=WebUtil.encode(session, "Normal")%>" selected>Normal</option>
                                    <option value="<%=WebUtil.encode(session, "Baja")%>">Baja</option>
                                </select>
                            </div>
                            <div>
                                <label for="status">*Estatus</label>
                                <select name="status">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "Pendiente")%>">Pendiente</option>
                                    <option value="<%=WebUtil.encode(session, "Atendida")%>">Atendida</option>
                                </select>
                            </div>
                            <div>
                                <label for="fechaAtencion">Fecha de atención (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaAtencion"   name="fechaAtencion" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="accion">Acción aplicada</label>
                                <textarea name="accion" cols="35" rows="5"></textarea>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" id="observaciones" cols="35" rows="5"></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertBitacoraIncidente'));"/>
                            </div>  
                        </fieldset>
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