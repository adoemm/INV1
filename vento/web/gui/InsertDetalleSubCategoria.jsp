<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertDetalleSubCategoria");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Detalles de Subcategoría</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
        <%@ include file="/gui/pageComponents/dataTablesFullFunctionParameters.jsp"%>
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSubCategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Subcategorías</a>
                                > Detalles de Subcategoría
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <div id="loadingScreen" style="width:100%; height: 350px; font-size: 22px">                                
                    </div>
                    <script type="text/javascript" language="javascript" charset="utf-8">
            $("#loadingScreen").Loadingdotdotdot({
                "speed": 400,
                "maxDots": 6
            });
                    </script>
                    <div id="contenent_info" style="display:none;">
                        <form name="usuario" method="post" action="" enctype="application/x-www-form-urlencoded" id="form">
                            <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                            <input type="hidden" name="FormFrom" value="nuevoDetalleSubCategoria"/>
                            <input type="hidden" name="ID_SubCategoria" value="<%=request.getParameter("ID_SubCategoria")%>"/>
                            <fieldset>
                                <legend>Detalles de Subcategoría</legend>
                                <div>
                                    <%
                                        String nombreSubcategoria = QUID.select_SubCategoria(WebUtil.decode(session, request.getParameter("ID_SubCategoria"))).get(0).toString();
                                    %>
                                    <label>Subcategoria</label>
                                    <input type="text" value="<%=nombreSubcategoria%>" size="40" disabled>
                                </div>
                                <p align="right"><a href="javascript:location.reload()"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-View-Refresh-64.png"/></a></p>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Detalle</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int i = 0;
                                            String idSubcategoria = WebUtil.decode(session, request.getParameter("ID_SubCategoria"));
                                            it = QUID.select_AllDetalle4SubCategoria(WebUtil.decode(session, request.getParameter("ID_SubCategoria"))).iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                                i++;
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td id="rOnly"><%=i%></td> 
                                            <td id="rOnly"><%=listAux.get(1)%></td>
                                            <%
                                                if (listAux.get(2) != null && idSubcategoria.equalsIgnoreCase(listAux.get(2).toString())) {
                                            %>
                                            <td id="rOnly" class="rOnly"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Dialog-Apply-64.png" title="Detalle asignado" width="22" height="23" alt="Detalle asignado"></td>
                                                <%
                                                } else {
                                                %>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSubCategoria=<%=request.getParameter("ID_SubCategoria")%>&idDetalle=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=nuevoDetalleSubCategoria"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/add.png" title="Asignar detalle" width="22" height="23" alt="Asignar detalle"></a></td>
                                                    <%
                                                        }
                                                    %>
                                            <%
                                            if(listAux.get(4)!= null){
                                                %>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSubCategoria=<%=request.getParameter("ID_SubCategoria")%>&idDetalleSubcategoria=<%=WebUtil.encode(session, listAux.get(4))%>&FormFrom=deleteDetalleSubcategoria"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar" width="22" height="23" alt="Eliminar"></a></td>
                                            <%
                                            }else{
                                                %>
                                            <td id="rOnly" class="rOnly"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-gray.png" title="Eliminar" width="22" height="23" alt="Eliminar"></td>
                                            <%
                                            }
                                            %>
                                            
                                        </tr>
                                        <%
                                            }
                                        %>  
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>NP</th>
                                            <th>Detalle</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Detalle</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
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
%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
} else {
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
    }
%>