<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertBaja");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Agregar Bien</title>
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
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText+'&idBien=<%=request.getParameter("idBien")%>'
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
                    $('#searchText').val(thisValue);
                    $('#idBien').attr('value', id);
                    document.getElementById('cajaDebotones').style.display = 'block';
                    setTimeout("$('#suggestions').hide();", 200);
                }
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitud.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Solicitudes</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateSolicitud.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSolicitud=<%=request.getParameter("idSolicitud")%>">Editar Solicitud</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSolicitudBaja.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSolicitud=<%=request.getParameter("idSolicitud")%>">Bienes para Baja</a> 
                                > Asignación de Bienes 
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertSolicitudBaja" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertSolicitudBaja">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertSolicitudBaja"/>
                        <input type="hidden" name="idSolicitud" value="<%=request.getParameter("idSolicitud")%>"/>
                        <input type="hidden" id='idBien' name="idBien" value=""/>
                        <input type="hidden" id='idPlantel' name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Agregar Bien</legend>
                            <div>
                                <label>*Número de inventario</label>
                                <input type="text" name="searchText" id="searchText" onkeyup='buscarBien(event.keyCode);' onblur='fillSuggestions();' size="75">
                                <div class="suggestionsBox" id="suggestions" style="display: none;">
                                    <div class="suggestionList" id="autoSuggestionsList" >
                                    </div>
                                </div>
                            </div>
                            <div id="cajaDebotones" style="display:none;">
                                <div>
                                    <label for="motivoBaja">*Motivo de baja</label>
                                    <select name="motivoBaja" id="motivoBaja">
                                        <option value=""></option>
                                        <option value="<%=WebUtil.encode(session, "Dañado")%>">Dañado</option>
                                        <option value="<%=WebUtil.encode(session, "Desuso")%>">Desuso</option>
                                        <option value="<%=WebUtil.encode(session, "Irreparable")%>">Irreparable</option>
                                    </select>
                                </div>
                                <div>
                                    <label for="observaciones">Observaciones</label>
                                    <textarea name="observaciones" id="observaciones" cols="25" rows="5"></textarea>
                                </div>
                                <div id="botonEnviarDiv" >
                                    <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertSolicitudBaja'));"/>
                                </div>  
                            </div>   
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