<%@page import="java.util.Calendar"%>
<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateResguardo");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Actualizar Resguardo</title>
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
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divBienes').html(response);
                        }
                    }});
            }
            function getInfoBien(idBien) {
                $('#wrapper').find('#divInfoBien').html('');
                if (idBien !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getInfoBien.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=' + idBien
                        , success: function(response) {
                            $('#wrapper').find('#divInfoBien').html(response);
                        }});
                }
            }
            function getResguados(idPersonaPlantel) {
                $('#wrapper').find('#divBienes').html('');
                if (idPersonaPlantel !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getResguado4Personal.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPersonalPlantel=' + idPersonaPlantel
                        , success: function(response) {
                            $('#wrapper').find('#divBienes').html(response);
                        }});
                }
            }
            function eliminarResguardo(idResguardo,idPersonaPlantel) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: "<%=PageParameters.getParameter("mainController")%>"
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idResguardo=' + idResguardo + '&FormFrom=deleteResguardoPersonal&idPersonalPlantel=' + idPersonaPlantel
                    , success: function(response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divBienes').html(response);
                        }
                    }});
            }
            function buscarBien(keyCode) {
                document.getElementById('cajaDebotones').style.display = 'none';
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    $('#idBien').attr('value', "");
                    searchText = document.getElementById("searchText").value;
                    if (searchText.length === 3) {
                        $('#suggestions').hide();
                        $('#idBien').attr('value', "");
                        document.getElementById('cajaDebotones').style.display = 'none';
                    } else {
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: false
                            , url: '/vento/ajaxFunctions/buscarBien.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText + '&idPlantel=<%=request.getParameter("idPlantel")%>'
                            , success: function(response) {
                                if (response.length > 0) {
                                    $('#suggestions').show();
                                    $('#autoSuggestionsList').html(response);
                                }
                            }
                        });
                    }
                }
            }
            function fillSuggestions(id, thisValue) {
                if (id.length === 0 || thisValue.length === 0) {
                    document.getElementById('cajaDebotones').style.display = 'none';
                } else {
                    $('#searchText').val('');
                    $('#idBien').attr('value', id);
                    getInfoBien(id);
                    document.getElementById('cajaDebotones').style.display = 'block';
                    setTimeout("$('#suggestions').hide();", 200);
                }
            }
        </script>
    </head>
    <body onload="getResguados('<%=request.getParameter("idPersonalPlantel")%>')">
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaResguardo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Resguardo</a> 
                                > Actualizar Resguardo
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateResguardo" method="post" action="" enctype="application/x-www-form-urlencoded" id="updateResguardo">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="idPersonalPlantel" value="<%=request.getParameter("idPersonalPlantel")%>"/>
                        <input type="hidden" name="FormFrom" value="updateResguardo"/>
                        <input type="hidden" id='idBien' name="idBien" value="" />
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Actualizar Resguardo</legend>
                            <div>
                                <p>
                                    <%
                                        LinkedList personal = QUID.select_PersonalXPersonalPlantel(WebUtil.decode(session, request.getParameter("idPersonalPlantel")));
                                    %>
                                    <b>Personal:</b>&nbsp;<%=personal.get(1)%>
                                </p>
                            </div>
                            <div>
                                <label>*Buscar</label>
                                <input type="text" name="searchText" id="searchText" onkeyup='buscarBien(event.keyCode);' onblur='fillSuggestions();' size="75">
                                <div class="suggestionsBox" id="suggestions" style="display: none;">
                                    <div class="suggestionList" id="autoSuggestionsList" >
                                    </div>
                                </div>
                            </div>
                            <div id="cajaDebotones" style="display:none;">
                                <div id="divInfoBien" class="msginfo" style="padding-left: 15%; width: 55%;margin-left: 10%;">
                                </div>
                                <div>
                                    <label for="noTarjetaResguardo">No. Tarjeta de resguardo</label>
                                    <input type="text" name="noTarjetaResguardo">
                                </div>
                                <div>
                                    <label for="fechaAsignacion">*Fecha de asignación</label>
                                    <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaAsignacion"   name="fechaAsignacion" value="<%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>" size="10" MAXLENGTH="10">
                                </div>
                                <div>
                                    <label for="observaciones">Observaciones</label>
                                    <textarea name="observaciones" cols="35" rows="4"></textarea>
                                </div>
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updateResguardo'));"/>
                            </div> 
                        </fieldset>
                        <fieldset>
                            <legend>Bienes Asignados</legend>
                            <div id="divBienes" align="center"></div>
                        </fieldset>
                    </form>
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