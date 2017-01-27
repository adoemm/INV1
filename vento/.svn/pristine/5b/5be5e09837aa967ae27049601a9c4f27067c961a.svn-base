<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertProveedor");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Proveedor</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   

        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#nuevoProveedor').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaProveedor.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Proveedores</a>
                                > Nuevo Proveedor
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form id="nuevoProveedor" name="nuevoProveedor" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="nuevoProveedor"/>                        
                        <fieldset>
                            <legend>Nuevo Proveedor</legend>
                            <fieldset>
                                <legend>Datos Generales</legend>
                                <div>
                                    <label for="nombre">*Nombre:</label>
                                    <input name="nombre" type="text" id="nombre" value="" size="40">
                                </div>                          
                                <div>
                                    <label for="clave">Clave del proveedor:</label>
                                    <input name="clave" type="text" id="clave" value="" size="50">
                                </div>
                                <div>
                                    <label for="telefono">Télefono:</label>
                                    <input name="telefono" type="text" id="telefono" value="" size="10">
                                </div>
                                <div>
                                    <label for="correo">Correo Electrónico:</label>
                                    <input name="correo" type="text" id="correo" value="" size="40">
                                </div>
                            </fieldset>
                            <fieldset>
                                <legend>Dirección</legend>
                                <div>
                                    <label for="calle">*Calle:</label>
                                    <textarea name="calle" rows="4" cols="50" size="25"></textarea> 
                                </div>
                                <div>
                                    <label for="noExt">*No. Exterior:</label>
                                    <input name="noExt" type="text" id="noExt" value="" size="25">
                                </div>
                                <div>
                                    <label for="colonia">Colonia:</label>
                                    <input name="colonia" type="text" id="colonia" value="" size="25">
                                </div>
                                <div>
                                    <label for="cp">Codigo Postal:</label>
                                    <input name="cp" type="text" id="cp" value="" size="5">
                                </div>
                                <div>
                                    <label for="localidad">Localidad:</label>
                                    <input name="localidad" type="text" id="localidad" value="" size="25">
                                </div>
                            </fieldset>
                            <div id="botonEnviarDiv">
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
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
