<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaMovimientoSalida");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Salida</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">

            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }

            function getConsumibleMovimiento(idMovimiento) {
                $('#wrapper').find('#divConsumibleMovimiento').html('');
                if (idMovimiento !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getConsumible4Movimiento.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idMovimiento=' + idMovimiento + '&onlyRead=<%=WebUtil.encode(session, "1")%>'
                        , success: function(response) {
                            $('#wrapper').find('#divConsumibleMovimiento').html(response);
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
            function getPersonal(idPlantel) {
                if (idPlantel !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getPersonal4Plantel.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=' + idPlantel
                        , success: function(response) {
                            $('#wrapper').find('#divPersonal').html(response);
                        }});
                }
            }

        </script>
    </head>
    <body onload="getConsumibleMovimiento('<%=request.getParameter("idMovimiento")%>')">
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <div class="errors">
                    <p>
                        <em>Los campos con  <strong>*</strong> son necesarios.</em>
                    </p>
                </div>
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Menú Consumibles</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaMovimientoSalida.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Movimientos de Salida</a> 
                                > Editar Salida
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateMovimientoSalida" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateMovimientoSalida">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateMovimientoSalida"/>
                        <input type="hidden" name="idMovimiento" value="<%=request.getParameter("idMovimiento")%>"/>
                        <fieldset>
                            <%
                                LinkedList salida = QUID.select_MovimientoSalida(WebUtil.decode(session, request.getParameter("idMovimiento")));
                            %>
                            <legend>Editar Salida</legend>
                            <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/getSelectPlantelSection.jsp"%>' flush = 'true'>
                                <jsp:param name='plantelActual' value='<%=WebUtil.encode(session, salida.get(1))%>' />
                            </jsp:include>
                            <div>
                                <label for="folio">*Folio</label>
                                <input type="text" name="txtFolio" value="<%=salida.get(2)%>" disabled>
                                <input type="hidden" name="folio" value="<%=WebUtil.encode(session, salida.get(2))%>">
                                <input type="hidden" name="idTipoMovimiento" value="<%=WebUtil.encode(session, salida.get(9))%>">
                                <input type="hidden" name="idTraslado" value="<%=WebUtil.encode(session, salida.get(24))%>">
                            </div>
                            <div>
                                <label for="noTurno">*Número de Turno</label>
                                <input type="text" name="noTurno" value="<%=salida.get(25)%>">
                            </div>
                            <div>
                                <label for="fechaMovimiento">*Fecha</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaMovimiento"   name="fechaMovimiento" value="<%=salida.get(3)%>" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for"peDestino">*Plantel Destino</label>
                                <select name="peDestino" onchange="getDepartamento(this.value);
            getPersonal(this.value);">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, salida.get(23))%>" selected><%=salida.get(22)%></option>
                                    <%
                                        Iterator t = QUID.select_IDNombrePlantel().iterator();
                                        while (t.hasNext()) {
                                            LinkedList plantel = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, plantel.get(0))%>"><%=plantel.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="divDepartamento">
                                <label for="idDepartamento">*Departamento</label>
                                <select name="idDepartamento"  id="idDepartamento">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, salida.get(14))%>" selected><%=salida.get(18)%></option>
                                    <%
                                        it = QUID.select_Departamento_PlantelXID(salida.get(23).toString()).iterator();
                                        while (it.hasNext()) {
                                            LinkedList datos = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(3)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="divPersonal">
                                <label for="idPersonalPlantel">*Recibe</label>
                                <select name="idPersonalPlantel"  id="idPersonalPlantel">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, salida.get(15))%>" selected><%=salida.get(21)+" "+salida.get(20)+" "+salida.get(19)%></option>
                                    <%        while (it.hasNext()) {
                                            LinkedList datos = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, datos.get(14))%>"><%=datos.get(1) + " " + datos.get(2) + " " + datos.get(3)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="motivoMovimiento">Motivo</label>
                                <textarea  name="motivoMovimiento" cols="30" rows="5"><%=salida.get(11)%></textarea>
                            </div>
                            <div>
                                <label for="estatus">*Estatus</label>
                                <select name="estatus">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, salida.get(6))%>" selected><%=salida.get(6)%></option>
                                    <option value="<%=WebUtil.encode(session, "Completo")%>">Completo</option>
                                    <option value="<%=WebUtil.encode(session, "Incompleto")%>"">Incompleto</option>
                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea  name="observaciones" cols="30" rows="5" id="observaciones"><%=salida.get(7)%></textarea>
                            </div>
                        </fieldset>
                        <div id="botonEnviarDiv" >
                            <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateMovimientoSalida'));">
                        </div> 
                    </form>
                    <br><br>
                    <div id="divConsumibleMovimiento" align="center"></div>
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