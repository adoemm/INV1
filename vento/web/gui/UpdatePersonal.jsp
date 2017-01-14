<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaPersonal");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Actualizar Empleado</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>          

        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#actualizarPersonal').serialize()
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Personal</a> > Actualizar Empleado
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form id="actualizarPersonal" name="actualizarPersonal" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="actualizarPersonal"/>                       
                        <input type="hidden" name="idPersonal" value="<%=request.getParameter("idPersonal")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>                        
                        <fieldset>
                            <legend>Actualizar Empleado</legend>
                            <fieldset>
                                <legend>Datos Generales</legend>
                                <%
                                    LinkedList datos = QUID.select_InfoPersonal(WebUtil.decode(session, request.getParameter("idPersonal")));
                                    if (datos != null) {
                                %>
                                <div>
                                    <label for="nombre">*Nombre:</label>
                                    <input name="nombre" type="text" id="nombre" value="<%=datos.get(0)%>" size="50">
                                </div>
                                <div>
                                    <label for="aPaterno">*Apellido Paterno:</label>
                                    <input name="aPaterno" type="text" id="aPaterno" value="<%=datos.get(6)%>" size="40">
                                </div>
                                <div>
                                    <label for="aMaterno">*Apellido Materno:</label>
                                    <input name="aMaterno" type="text" id="aMaterno" value="<%=datos.get(7)%>" size="40">
                                </div>
                                <div>
                                    <label for="preparacionProfesional">*Preparación Profesional:</label>
                                    <input name="preparacionProfesional" type="text" id="preparacionProfesional" value="<%=datos.get(4)%>" size="50">
                                </div>    
                                 <div>
                                    <label for="siglasTitulo">Siglas del Titulo:</label>
                                    <input name="siglasTitulo" type="text" id="siglasTitulo" value="<%=datos.get(5)%>" size="10">
                                </div>
                                <div>
                                    <label for="telefono">*Télefono(10 dígitos):</label>
                                    <input name="telefono" type="text" id="telefono" value="<%=datos.get(1)%>" size="10">
                                </div>
                                <div>
                                    <label for="correo">*Correo Electrónico:</label>
                                    <input name="correo" type="text" id="correo" value="<%=datos.get(2)%>" size="50">
                                </div>
                            </fieldset>
                           
                            <div id="botonEnviarDiv">
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
                            </div> 
                        </fieldset>
                        <%
                            }
                        %>
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
