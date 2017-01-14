<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaMarca");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Marcas</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
        <%@ include file="/gui/pageComponents/dataTablesFullFunctionParameters.jsp"%>
        <script type="text/javascript" charset="utf-8">
            $(document).ready(habilitarEditarColumna);
            function habilitarEditarColumna() {
                oTable = $('#example').dataTable();
                oTable.$("td[id!='rOnly']").editable('<%=PageParameters.getParameter("mainController")%>', {
                    indicator: "Guardando..."
                            //, submitdata: {}
                    , tooltip: "Click para editar..."
                    , type: "text"
                    , "callback": function(sValue, y) {
                        var aPos = oTable.fnGetPosition(this);
                        oTable.fnUpdate(sValue, aPos[0], aPos[1]);
                    }
                    , "submitdata": function(value, settings) {
                        $("#res").val("ID_Marca_" + this.parentNode.getAttribute('id'));
                        return {
                            _method: "post"
                            , "row_id": this.parentNode.getAttribute('id')
                            , "column": oTable.fnGetPosition(this)[1]
                            , "FormFrom": 'actualizarMarca'
                            , "imix": '<%=WebUtil.encode(session, UTime.getTimeMilis())%>'
                        };
                    }
                    , "height": "14px"
                    , "width": "100%"
                });
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a> 
                                > Catálogo de Marcas</td>
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
                                <legend>Catálogo de Marcas</legend>
                                <p align="right"><a href="javascript:location.reload()"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-View-Refresh-64.png"/></a></p>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Marca</th>
                                            <th>Modelos</th>
<!--                                            <th></th>-->
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int i = 0;
                                            it = QUID.select_Marca().iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                                i++;
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td id="rOnly"><%=i%></td>                                        
                                            <td><%=listAux.get(1)%></td>
<!--                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModelo4Marca.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMarca=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Modelos" width="22" height="23" alt="Modelos"></a></td>-->
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Marca=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=deleteMarca"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar nombre de marca" width="22" height="23" alt="Eliminar nombre de marca"></a></td>
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
                                            <th>Marca</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Marca</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <div id="divResult"> 
                    </div> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertMarca.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Nueva Marca</button>
                    </a> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Todos los Modelos</button>
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