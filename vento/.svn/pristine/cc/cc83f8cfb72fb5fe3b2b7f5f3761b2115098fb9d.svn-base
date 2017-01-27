<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertValor");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Insertar Valores</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#insertValor').serialize()
                    , success: function(response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divValores').html(response);
                        }
                    }});
            }
            function getValores(idDetalle) {
                $('#wrapper').find('#divValores').html('');
                if (idDetalle !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: false
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getValores4Detalle.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idDetalle=' + idDetalle
                        , success: function(response) {
                            $('#wrapper').find('#divValores').html(response);
                        }});
                }
            }
            function eliminarValor(idDetalle, idValor) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: "<%=PageParameters.getParameter("mainController")%>"
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idDetalle=' + idDetalle + '&idValor=' + idValor + '&FormFrom=eliminarValor'
                    , success: function(response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divValores').html(response);
                        }
                    }});
            }
        </script>

        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body onload="getValores('<%=request.getParameter("idDetalle")%>');">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaDetalle.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Detalles</a>
                                > Insertar Valores
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form id="insertValor" name="insertValor" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertValor"/>  
                        <input type="hidden" name="idDetalle" value="<%=request.getParameter("idDetalle")%>"/>  
                        <fieldset>
                            <legend>Insertar Valores</legend>
                            <%
                                LinkedList datos = QUID.select_Detalle(WebUtil.decode(session, request.getParameter("idDetalle")));
                            %>
                            <div>
                                <label for="nombreDetalle">*Descripción:</label>
                                <input name="nombreDetalle" type="text" id="nombreDetalle" value="<%=datos.get(0)%>" size="40">
                            </div>                          
                        </fieldset>
                        <fieldset>        
                            <legend>Valores</legend>
                            <label for="valor">*Nuevo valor</label>
                            <input type="text" name="valor" size="50">
                            <div id="botonEnviarDiv">
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
                            </div> 
                        </fieldset>
                        <fieldset>
                            <div id="divValores" align="center">
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
