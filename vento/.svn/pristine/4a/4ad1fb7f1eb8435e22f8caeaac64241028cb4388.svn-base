<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaModelo");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Catálogo de Modelos</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
            function popupArchivos(idObjeto) {
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/Insert_ObjetoArchivo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idObjeto=' + idObjeto + '&nombreObjeto=<%=WebUtil.encode(session, "MODELO")%>'
                        , 'Archivos'
                        , 'width=' + (screen.availWidth - 10).toString() + ',height=' + (screen.availHeight - 122).toString() + ',toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                        );
                if (window.focus) {
                    newwindow.focus();
                }
            }

            function calcPatronSerialNumber(ID_Modelo) {
                if (ID_Modelo === '') {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainController")%>"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&FormFrom=calcPatronSerialNumber'
                        , success: function (response) {
                            $('#wrapper').find('#divResult').html(response);
                        }});
                } else {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainController")%>"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idModelo=' + ID_Modelo + '&FormFrom=calcPatronSerialNumber'
                        , success: function (response) {
                            $('#wrapper').find('#divResult').html(response);
                        }});
                }
            }
            function forzarCalcPatronSerialNumber(ID_Modelo) {

                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: "<%=PageParameters.getParameter("mainController")%>"
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idModelo=' + ID_Modelo +'&FormFrom=calcPatronSerialNumber&ID_Usuario=<%=WebUtil.encode(session, session.getAttribute("userID"))%>'
                                , success: function (response) {
                                    $('#wrapper').find('#divResult').html(response);
                                }});

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
                                > Catálogo de Modelos</td>
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
                                <legend>Catálogo de Modelos</legend>
                                <p align="right"><a href="javascript:location.reload()"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-View-Refresh-64.png"/></a></p>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>Descripcion</th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int i = 1;
                                            it = QUID.select_Modelo().iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td id="rOnly"><%=i%></td> 
                                            <td id="rOnly"><%=listAux.get(2)%></td>
                                            <td id="rOnly"><%=listAux.get(5)%></td> 
                                            <td id="rOnly"><%=listAux.get(4)%></td>
                                            <td id="rOnly" class="rOnly"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/attachment.png" onclick="popupArchivos('<%=WebUtil.encode(session, listAux.get(0))%>');" title="Ver Archivos" width="22" height="23" alt="Ver Archivos"></td>
                                            <td id="rOnly" class="rOnly"><a href="#"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Calculator-64.png" title="Calcular patron" width="22" height="23" alt="Calcular patron" onclick="calcPatronSerialNumber('<%=WebUtil.encode(session, listAux.get(0))%>')"></a></td>
                                            <td id="rOnly" class="rOnly"><a target="_blank" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModeloPatron.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Modelo=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Document-Page-Setup-64.png" title="Ver Patrones" width="22" height="23" alt="Ver Patrones"></a></td>
                                            <td id="rOnly" class="rOnly"><a href="#"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Software-Update-Available-64.png" title="Forzar Calculo" width="22" height="23" alt="Forzar Calculo" onclick="forzarCalcPatronSerialNumber('<%=WebUtil.encode(session, listAux.get(0))%>')"></a></td>                                        
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Modelo=<%=WebUtil.encode(session, listAux.get(0))%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Editar Modelo" width="22" height="23" alt="Editar Modelo"></a></td>                                        
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&ID_Modelo=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=deleteModelo"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar nombre del modelo" width="22" height="23" alt="Eliminar nombre del modelo"></a></td>
                                        </tr>
                                        <%
                                                i += 1;
                                            }
                                        %>  
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>NP</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>Descripcion</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>Descripcion</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <div id="divResult"> 
                    </div> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">
                        <button>Nuevo Modelo</button>
                    </a> 
                    <a href="#" onclick="calcPatronSerialNumber('')">
                        <button>Calcular los Patrones de Todos los Modelos</button>
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
