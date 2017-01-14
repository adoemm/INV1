<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaPuntuacion");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Puntuación</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
            function getDetallesPuntuacion(idPuntuacion) {
                if (idPuntuacion !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: true
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDetalles4Puntuacion.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPuntuacion=' + idPuntuacion + '&ID_Plantel=<%=request.getParameter("ID_Plantel")%>'
                        , success: function (response) {
                            $('#wrapper').find('#divDetalles').html(response);
                            openDialogBox('floatBoxDetalles');
                        }});
                }
            }
            function enviarInfo(form) {
                closeDialogBox('floatBoxDetalles');
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: true
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function (response) {
                        $('#wrapper').find('#divResult').html(response);
                        if (!(response.indexOf("title: \"Error\"") >= 0)) {
                            location.reload();
                        }
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPlanteles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Planteles</a> 
                                > Puntuación
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
                                <legend>Puntuación</legend>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Rubro</th>
                                            <th>Puntuación</th>
                                            <th>Observaciones</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int i = 1;
                                            it = QUID.select_Puntuacion(WebUtil.decode(session, request.getParameter("ID_Plantel")), false).iterator();
                                            while (it.hasNext()) {
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <tr id="<%=WebUtil.encode(session, listAux.get(0))%>" class="gradez">       
                                            <td id="rOnly"><%=i%></td>                                        
                                            <td id="rOnly"><%=StringUtil.truncString(listAux.get(5).toString(),70)+".."%></td>
                                            <td id="rOnly"><%=listAux.get(4)%></td>
                                            <td id="rOnly"><%=StringUtil.truncString(listAux.get(3).toString(),50)%></td>
                                            <td id="rOnly" class="rOnly"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Actualizar" width="22" height="23" alt="Actualizar" onclick="getDetallesPuntuacion('<%=WebUtil.encode(session, listAux.get(0))%>')"></td>
                                            <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPuntuacion=<%=WebUtil.encode(session, listAux.get(0))%>&FormFrom=deletePuntuacion&ID_Plantel=<%=request.getParameter("ID_Plantel")%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar" width="22" height="23" alt="Eliminar"></a></td>
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
                                            <th>Rubro</th>
                                            <th>Puntuación</th>
                                            <th>Observaciones</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>NP</th>
                                            <th>Rubro</th>
                                            <th>Puntuación</th>
                                            <th>Observaciones</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <%@ include file="/gui/pageComponents/reinicializarDataTables.jsp"%>
                    <div >  
                        <form action="<%=PageParameters.getParameter("mainController")%>" method="POST">                            
                            <input type="hidden" name="FormFrom" id="FormFrom" value="insertPuntuacion">
                            <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                            <input type="hidden" name="ID_Plantel" value="<%=request.getParameter("ID_Plantel")%>"/>
                            <fieldset>
                                <legend>Nueva Puntuacion</legend>
                                <table>
                                    <tr>
                                        <td style=" text-align: right; vertical-align: top"><label for="rubro">*Rubro</label></td>
                                        <td>
                                            <select name="idRubro" style="width: 100%;">
                                                <option value=""></option>
                                                <%
                                                    it = QUID.select_Rubro().iterator();
                                                    while (it.hasNext()) {
                                                        LinkedList datos = (LinkedList) it.next();
                                                %>
                                                <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=StringUtil.truncString(datos.get(1).toString(),25)+"..."%></option>
                                                <%
                                                    }
                                                %>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style=" text-align: right; vertical-align: top"><label for="puntuacion">*Puntuación (Ej. 8.0, 9.5)</label></td>
                                        <td><input type="text" name="puntuacion"></td>
                                    </tr>
                                    <tr>
                                        <td style=" text-align: right; vertical-align: top"><label for="observaciones">Observaciones</label></td>
                                        <td>
                                            <textarea name="observaciones" cols="35" rows="8"></textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td><input type="submit" value="Guardar" name="Guardar"></td>
                                    </tr>
                                </table>
                            </fieldset>
                        </form>
                    </div>
                    <div id="overlay" class="overlay" ></div>
                    <div id="floatBoxDetalles" class="floatBox">
                        <div class="closeButton2" onclick="closeDialogBox('floatBoxDetalles');"></div>
                        <div id="divDetalles"></div>
                    </div>
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