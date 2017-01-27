<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertPBT");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Asignar Proveedor</title>
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
    <body onload="getDepartamento(document.getElementById('idPlantel').value);">
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaGrupo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Grupos</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateGrupo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Editar Grupo</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaGrupoBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Bienes del Grupo</a>
                                > Agregar Proveedor
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertPBTFull" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertPBTFull">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertPBTFull"/>
                        <input type="hidden" name="idGrupo" value="<%=request.getParameter("idGrupo")%>"/>
                        <input type="hidden" name="idPdg" value="<%=request.getParameter("idPdg")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Asignar Proveedor</legend>
                            <div>
                                <%
                                    Iterator t = QUID.select_TipoProveedor().iterator();
                                %>
                                <label for="idTipo">*Tipo de proveedor</label>         
                                <select name="idTipo">
                                    <option value=""></option>
                                    <%
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session,datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <%
                                    t = QUID.select_Proveedor().iterator();
                                %>
                                <label for="idProveedor">*Proveedor</label>          
                                <select name="idProveedor">
                                    <option value=""></option>
                                    <%
                                        while (t.hasNext()) {
                                            LinkedList datos = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session,datos.get(0))%>"><%=datos.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertPBTFull'));"/>
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