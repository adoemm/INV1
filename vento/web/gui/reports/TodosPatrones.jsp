
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("TodosPatrones");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>

<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Todos los Patrones</title>

        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
            function enviarInfo(form, action) {
                if (action === "exportar") {
                    form.action = '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("excelReports")%>/TodosPatrones.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>';
                            }
                            form.submit();
                        }
                        function getSubcategoria(idCategoria) {
                            if (idCategoria !== null) {
                                $.ajax({type: 'POST'
                                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                    , cache: false
                                    , async: false
                                    , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getSubcategoria4Categoria.jsp"
                                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idCategoria=' + idCategoria + '&inCheck=<%=WebUtil.encode(session, "si")%>&getIDs=<%=WebUtil.encode(session, "si")%>'
                                    , success: function (response) {
                                        $('#wrapper').find('#divSubcategoria').html(response);
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
                        function getModelo(idMarca) {
                            if (idMarca !== null) {
                                $.ajax({type: 'POST'
                                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                    , cache: false
                                    , async: false
                                    , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getModelo4Marca.jsp"
                                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMarca=' + idMarca+'&conTokyo=<%=WebUtil.encode(session,"Todos")%>'
                                    , success: function (response) {
                                        $('#wrapper').find('#divModelo').html(response);
                                    }});
                            }
                        }
                        function getDescripcionModelo(idModelo) {
                            if (idModelo !== null) {
                                $.ajax({type: 'POST'
                                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                    , cache: false
                                    , async: false
                                    , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDescripcion4Modelo.jsp"
                                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idModelo=' + idModelo
                                    , success: function (response) {
                                        $('#wrapper').find('#divDescModelo').html(response);
                                    }});
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
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuReportes.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Reportes</a> 
                                > Todos los Patrones
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>                                   
                    <form id="report" name="report" method="post" enctype="application/x-www-form-urlencoded" target="_blank" action="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/ResumenGeneral.jsp">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <fieldset>
                            <legend>Todos los Patrones</legend>
                            <div>
                                <label for="idMarca">*Marca</label>
                                <%
                                    Iterator t = QUID.select_Marca().iterator();
                                %>
                                <select name="idMarca" onchange="getModelo(this.value);">
                                    <option value=""></option>  
                                    <option value="<%=WebUtil.encode(session,"Todos")%>">Todas las Marcas</option>  
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
                            <div id="divModelo"></div>
                        </fieldset>
                        <input type="button" value="Exportar a excel" name="Exportar" onclick=" enviarInfo(document.getElementById('report'), 'exportar');"/>
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