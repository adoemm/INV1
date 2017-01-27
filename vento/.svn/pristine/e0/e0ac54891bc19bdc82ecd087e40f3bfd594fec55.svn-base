<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaConteoFisico");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Toma de Inventario</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
        <%@ include file="/gui/pageComponents/dataTablesFullFunctionParameters.jsp"%>
    </head>
    <body  onload="populate();">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Menú Consumibles</a> 
                                > Toma de Inventario
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <br>
                    <br>
                    <div id="contenent_info" >
                        <%
                            String idPlantel = request.getParameter("idPlantel") != null ? WebUtil.decode(session, request.getParameter("idPlantel")) : session.getAttribute("FK_ID_Plantel").toString();
                            if (SystemUtil.haveAcess("VerTodo", userAccess)) {
                        %>
                        <form name="setPlantel" method="post" enctype="application/x-www-form-urlencoded" id="setPlantel" action="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaConteoFisico.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                            <fieldset>
                                <legend>Seleccione un Plantel para Consulta</legend>
                                <div>
                                    <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                        <jsp:param name='plantelActual' value='<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>' />
                                        <jsp:param name='onChange' value='submit' />
                                    </jsp:include>
                                </div>
                            </fieldset>
                        </form>
                        <%
                            }
                        %>
                        <form name="usuario" method="post" action="" enctype="application/x-www-form-urlencoded" id="form">
                            <fieldset>
                                <legend>Toma de Inventario</legend>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>Folio</th>
                                            <th>Plantel</th>
                                            <th>Fecha de Registro</th>
                                            <th>Estatus</th>
                                            <th>Fecha Modificación</th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>Folio</th>
                                            <th>Plantel</th>
                                            <th>Fecha de Registro</th>
                                            <th>Estatus</th>
                                            <th>Fecha Modificación</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>Folio</th>
                                            <th>Plantel</th>
                                            <th>Fecha de Registro</th>
                                            <th>Estatus</th>
                                            <th>Fecha Modificación</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>  
                        <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertConteoFisico.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>">
                        <button>Tomar Inventario</button>
                    </a> 
                </div>
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
                <script type="text/javascript" language="javascript" charset="utf-8">
        function populate()
        {
            var t = $('#example').dataTable();
            t.fnClearTable();
            var data = new Array();
            var ids = new Array();
            var cells = new Array();

                    <%
                        int cont = 0;
                        it = QUID.select_ConteoFisico(idPlantel, false).iterator();
                        while (it.hasNext()) {
                            listAux = (LinkedList) it.next();
                    %>
            ids[<%=cont%>] = '<%=WebUtil.encode(session, listAux.get(0))%>';
            cells[0] = '<%=listAux.get(0).toString().replaceAll("'", "&#39;")%>';
            cells[1] = '<%=listAux.get(6).toString().replaceAll("'", "&#39;")%>';
            cells[2] = '<%=listAux.get(3).toString().replaceAll("'", "&#39;")%>';
            cells[3] = '<%=listAux.get(1).toString().replaceAll("'", "&#39;")%>';
            cells[4] = '<%=listAux.get(2).toString().replaceAll("'", "&#39;")%>';
            cells[5] = '<a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertConteoFisicoCosumible.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>&idConteoFisico=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-User-Desktop-64.png" title="Continuar Captura" width="22" height="23" alt="Continuar Captura"></a>';
            cells[6] = '<a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("excelReports")%>/DiferenciasConteoFisico.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>&idConteoFisico=<%=WebUtil.encode(session, listAux.get(0))%>" target="_blank"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-X-Office-Spreadsheet-64.png" title="Informe" width="22" height="23" alt="Informe"></a>';
            cells[7] = '<a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("excelReports")%>/DetalleDiferenciasConteoFisico.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel") != null ? request.getParameter("idPlantel") : WebUtil.encode(session, session.getAttribute("FK_ID_Plantel"))%>&idConteoFisico=<%=WebUtil.encode(session, listAux.get(0))%>" target="_blank"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Logviewer-64.png" title="Informe" width="22" height="23" alt="Informe"></a>';
            data[<%=cont%>] = t.fnAddData(cells, false);
                    <%
                            cont++;
                        }
                    %>

            $("tfoot th").each(function(i) {
                this.innerHTML = fnCreateSelect(t.fnGetColumnData(i));
                $('select', this).change(function() {
                    t.fnFilter($(this).val(), i);
                });
            });
            t.fnDraw();
            document.getElementById('contenent_info').style.display = 'block';
        }
                </script>
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