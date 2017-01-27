<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaTraspaso");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Movimientos</title>
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
                                > Catálogo de Movimientos</td>
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
                                <legend>Catálogo de Movimientos</legend>
                                <p align="right"><a href="javascript:location.reload()"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-View-Refresh-64.png"/></a></p>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>Plantel</th>
                                            <th>Motivo</th>
                                            <th>Estatus</th>
                                            <th>Fecha</th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int i = 0;
                                            it = QUID.select_Traspaso().iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                                i++;
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td><%=listAux.get(4)%></td>
                                            <td><%=listAux.get(2)%></td>
                                            <td><%=listAux.get(3)%></td>
                                            <td><%=listAux.get(1)%></td>

                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertMovimientoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "&idNuevoMovimiento=" + WebUtil.encode(session, listAux.get(0).toString())%>" target="_blank">
                                                    <img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-List-Add-64.png" title="Agregar bien" width="22" height="23" alt="Agregar bien"></a></td>
                                            
                                                    <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/ConsultaTraspasoBien.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "&idPlantel=" + WebUtil.encode(session, listAux.get(5).toString())%>" target="_blank">
                                                    <img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Computer-64.png" title="Catálogo de Bienes de un Traspaso" width="22" height="23" alt="Catálogo de Bienes de un Traspaso"></a></td>
                                            
                                                    <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/InsertBienATraspaso.jsp?" + WebUtil.encode(session, "imix") + "=" + WebUtil.encode(session, "" + UTime.getTimeMilis()) + "&ID_Traspaso=" + WebUtil.encode(session, listAux.get(0).toString())%>" target="_blank">
                                                    <img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Actualizar Traspaso" width="22" height="23" alt="Actualizar Traspaso"></a></td>
                                           
                                                    <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Traspaso=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=deleteTraspaso"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar Traspaso" width="22" height="23" alt="Eliminar Traspaso"></a></td>
                                        </tr>
                                        <%
                                            }
                                        %>  
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>Plantel</th>
                                            <th>Motivo</th>
                                            <th>Estatus</th>
                                            <th>Fecha</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>Plantel</th>
                                            <th>Motivo</th>
                                            <th>Estatus</th>
                                            <th>Fecha</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <div id="divResult"> 
                    </div> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertMovimiento.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Nuevo Movimiento</button>
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