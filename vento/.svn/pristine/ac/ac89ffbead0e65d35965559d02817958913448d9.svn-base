
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteResumenGeneral");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>

<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Etiquetas</title>

        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }

            function getSubcategoria(idCategoria) {
                
                if (idCategoria !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getSubcategoria4Categoria.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria + '&inCheck=<%=WebUtil.encode(session, "si")%>&getIDs=<%=WebUtil.encode(session, "si")%>'
                        , success: function(response) {
                            $('#wrapper').find('#divSubcategoria').html(response);
                        }});
                }
            }

            function getDepartamento(idPlantel) {
                if (idPlantel !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDepartamento4Plantel.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=' + idPlantel
                        , success: function(response) {
                            $('#wrapper').find('#divDepartamento').html(response);
                        }});
                }
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
    <body onload="getDepartamento(document.getElementById('idPlantel').value);">
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>               
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuReportes.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Reportes</a> 
                                > Etiquetas
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>                                   
                    <form id="report" name="report" method="post" enctype="application/x-www-form-urlencoded" target="_blank" action="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/BarCode4Bien.jsp">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <fieldset>
                            <legend>Etiquetas</legend>
                            <div>
                                <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                    <jsp:param name='plantelActual' value='<%=WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                                    <jsp:param name='onChange' value='getDepartamento' />
                                </jsp:include>
                            </div>
                            <div id="divDepartamento"></div>
                            <div>
                                <label for="idCategoria">*Categoria</label>
                                <%
                                    Iterator t = QUID.select_Categoria().iterator();
                                %>
                                <select name="idCategoria" onchange="getSubcategoria(this.value);">
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
<!--                            <div id="divSelectAll" style="display: none; border-bottom: 1px solid;border-color: #DDDDDD;">
                                <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divSubcategoria', this);" />Seleccionar todo
                            </div>-->
                            <div id="divSubcategoria"></div>
                        </fieldset>
                        <input type="submit" value="Imprimir(Avery Template 5160 -3X10-)" name="Imprimir" />
                    </form>
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