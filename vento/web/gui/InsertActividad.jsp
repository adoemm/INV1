<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertActividad");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nueva Actividad</title>
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
            function seleccionarTodo(contenedor, valor) {
                var checks = document.getElementById(contenedor);
                for (i = 0; i < checks.getElementsByTagName('input').length; i++) {
                    if (checks.getElementsByTagName('input')[i].type === "checkbox") {
                        checks.getElementsByTagName('input')[i].checked = valor.checked;
                    }
                }
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaActividad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Actividades</a> 
                                > Nueva Actividad
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertActividad" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertActividad">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertActividad"/>
                        <fieldset>
                            <legend>Nueva Actividad</legend>
                            <div>
                                <label for="idTipoActividad">*Tipo de Actividad</label>
                                <%
                                Iterator t=QUID.select_TipoActividad().iterator();
                                %>
                                <select name="idTipoActividad"> 
                                    <option value=""></option>
                                    <%
                                    while(t.hasNext()){
                                        LinkedList datos=(LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session,datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%                                       
                                    }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="descripcion">*Descripción de la actividad</label>
                                <textarea  id="descripcion" name="descripcion" cols="30" rows="5"></textarea>
                            </div>
                            <div>
                                <label for="fechaInicio">*Fecha de Inicio (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaInicio"   name="fechaInicio" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="horaInicio">Hora de Inicio (HH:MM:SS)</label>    
                                <input type="time" name="horaInicio" id="horaInicio">    
                            </div>
                            <div>
                                <label for="fechaFin">*Fecha de Termino (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaFin"   name="fechaFin" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="horaFin">Hora de Termino (HH:MM:SS)</label>    
                                <input type="time" name="horaFin" id="horaFin">    
                            </div>
                            <div>
                                <label for="fechaLimite">Fecha Limite (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaLimite"   name="fechaLimite" value="" size="10" MAXLENGTH="10">
                            </div>     
                            <div>
                                <label for="horaLimite">Hora Limite (HH:MM:SS)</label>    
                                <input type="time" name="horaLimite" id="horaLimite">    
                            </div>
                            <div class="msginfo" align="center">
                                Seleccione los planteles a los que asignará la actividad.
                            </div>
                            <div>
                                <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divPlanteles', this);" />Seleccionar todo
                            </div>
                            <div id="divPlanteles" style="height: 300px; overflow-x: scroll;">
                                <%
                                     t = QUID.select_NombrePlantelID(session.getAttribute("FK_ID_Plantel").toString(),SystemUtil.haveAcess("VerTodo", userAccess)).iterator();
                                    while (t.hasNext()) {
                                        LinkedList datos = (LinkedList) t.next();
                                %>
                                <input type="checkbox" name="idPlantel" value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%><br>
                                <%
                                    }
                                %>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertActividad'));"/>
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