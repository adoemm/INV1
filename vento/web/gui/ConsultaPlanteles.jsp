<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaPlanteles");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Planteles</title>
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a> 
                                > Catálogo de Planteles
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
                            <fieldset>
                                <legend>Catálogo de Planteles</legend>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Centro de Trabajo</th>
                                            <th>Teléfono</th>
                                            <th>Email</th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            it = QUID.select_Planteles(session.getAttribute("FK_ID_Plantel").toString(),SystemUtil.haveAcess("VerTodo", userAccess)).iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td><%=listAux.get(1)%></td>
                                            <td><%=listAux.get(3)%></td>
                                            <td><%=listAux.get(4)%></td>
                                            <td><%=listAux.get(5)%></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertPlantelAlmacen.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Plantel=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Emblem-Package-64.png" title="Abastecedor" width="22" height="23" alt="Abastecedor"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPuntuacion.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Plantel=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/icon_star.png" title="Puntuación" width="22" height="23" alt="Puntuación"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdatePlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Plantel=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Actualizar plantel" width="22" height="23" alt="Actualizar plantel"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Plantel=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=borrarPlantel"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar Plantel" width="22" height="23" alt="Eliminar Plantel"></a></td>
                                        </tr>
                                        <%
                                            }
                                        %>  
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Centro de Trabajo</th>
                                            <th>Teléfono</th>
                                            <th>Email</th>
                                        </tr>
                                    </tfoot>
                                    <thead>                                        
                                        <tr>
                                           <th>Nombre</th>
                                            <th>Centro de Trabajo</th>
                                            <th>Teléfono</th>
                                            <th>Email</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Nuevo Plantel</button>
                    </a> 
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