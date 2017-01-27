<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaPersonal");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Personal</title>
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> > 
                                Catálogo de Personal
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
                                <legend>Catálogo de Personal</legend>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>Plantel</th>
                                            <th>Curp</th>
                                            <th>Nombre</th>
                                            <th>A. Paterno</th>
                                            <th>A. Materno</th>
                                            <th>Correo</th>
                                            <th>Cargo</th>
                                            <th>Situación Actual</th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            it = QUID.select_Personal(session.getAttribute("FK_ID_Plantel").toString(), SystemUtil.haveAcess("VerTodo", userAccess)).iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">
                                            <td><%=listAux.get(6)%></td>
                                            <td><%=listAux.get(13)%></td>
                                            <td><%=listAux.get(1)%></td>
                                            <td><%=listAux.get(10)%></td>
                                            <td><%=listAux.get(11)%></td>
                                            <td><%=listAux.get(4)%></td>
                                            <td><%=listAux.get(7)%></td>
                                            <td><%=listAux.get(8)%></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdatePersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPersonal=<%=WebUtil.encode(session, listAux.get(0))%>&idPlantel=<%=WebUtil.encode(session, listAux.get(5))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Ver informacion" width="22" height="23" alt="Ver informacion"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateRelacionPersonalPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_PersonalPlantel=<%=WebUtil.encode(session, listAux.get(9))%>&nombrePlantel=<%=listAux.get(6)%>&FK_ID_Plantel=<%=WebUtil.encode(session, listAux.get(5))%>&idPersonal=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/worker.png" title="Modificar Relación Laboral" width="25" height="25" alt="Modificar Relación Laboral"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/RelacionPersonalPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPersonal=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Edit-Redo-64.png" title="Nuevo Cargo" width="22" height="23" alt="Nuevo Cargo"></a></td>
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
                                            <th>Curp</th>
                                            <th>Nombre</th>
                                            <th>A. Paterno</th>
                                            <th>A. Materno</th>
                                            <th>Correo</th>
                                            <th>Cargo</th>
                                            <th>Situación Actual</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>Plantel</th>
                                            <th>Curp</th>
                                            <th>Nombre</th>
                                            <th>A. Paterno</th>
                                            <th>A. Materno</th>
                                            <th>Correo</th>
                                            <th>Cargo</th>
                                            <th>Situación Actual</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Nuevo Personal</button>
                    </a>         
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/CentroOpcionesPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Buscar Personal</button>
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