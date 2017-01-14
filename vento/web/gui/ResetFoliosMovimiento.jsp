<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ResetFolios");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Restablecer Folios</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form) {
                $.msgBox({
                    title: "Confirmar"
                    , content: "Esta seguro que desea restablecer los folios?"
                    , type: "confirm"
                    , buttons: [{value: "SI"}, {value: "NO"}]
                    , opacity: 0.75
                    , success: function (result) {
                        if (result === "SI") {
                            $.ajax({type: 'POST'
                                , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                , cache: false
                                , async: false
                                , url: '<%=PageParameters.getParameter("mainController")%>'
                                , data: $(form).serialize()
                                , success: function (response) {
                                    $('#wrapper').find('#divResult').html(response);
                                }});
                        }
                    }
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuConsumibles.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Menú Consumibles</a> 
                                > Restablecer Folios
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="resetFoliosMovimiento" method="post"  enctype="application/x-www-form-urlencoded" id="resetFoliosMovimiento">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="resetFoliosMovimiento"/> 

                        <fieldset>
                            <legend>Restablecer Folios</legend>
                            <div>
                                <label for="cuentaTotalEntrada">*Ultimo Folio para Movimientos de Entrada</label>
                                <input name="cuentaTotalEntrada" type="text" id="cuentaTotalEntrada" value="<%=QUID.select_siguienteFolioXTipoMovimiento("ENTRADA") - 1%>" >
                            </div>
                            <br><br>
                            <div>
                                <label for="cuentaTotalSalida">*Ultimo Folio para Movimientos de Salida</label>
                                <input name="cuentaTotalSalida" type="text" id="cuentaTotalSalida" value="<%=QUID.select_siguienteFolioXTipoMovimiento("SALIDA") - 1%>" >
                            </div>
                            <div id="botonEnviarDiv">
                                <input type="button" value="Actualizar" name="Enviar" onclick="enviarInfo(document.getElementById('resetFoliosMovimiento'));"/>
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