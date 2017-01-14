<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("updateUsuario");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Usuario</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <div class="errors"> 
                    <em>Los campos con  <strong>*</strong> son necesarios.</em>
                    </p>
                </div>
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuSeguridad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Seguridad</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaUsuarios.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" > Usuarios</a> > Editar Usuario
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="usuario" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="form">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="editarUsuario"/>
                        <input type="hidden" name="idUsuario" value="<%=request.getParameter("idUsuario")%>"/>
                        <input type="hidden" name="idRolUsuario" value="<%=request.getParameter("idRolUsuario")%>"/>
                        <fieldset>
                            <legend>Editar Usuario</legend>
                            <div>
                                <label for="plantel">*Plantel</label>          
                                <select name="plantel" id="plantel">
                                    <%
                                        LinkedList usuario = QUID.select_Usuario(WebUtil.decode(session, request.getParameter("idUsuario")));
                                        Iterator t = QUID.select_IDNombrePlantel().iterator();
                                        while (t.hasNext()) {
                                            LinkedList aux = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, aux.get(0))%>"
                                            <% if (usuario.get(5).equals(aux.get(0))) {%> 
                                            selected
                                            <% }%>
                                            ><%=aux.get(1)%></op|tion>
                                        <%
                                            }
                                        %>
                                    <option value="<%=WebUtil.encode(session, "1")%>">Dirección General</option>                                    
                                </select>
                            </div>
                            <div>
                                <label for="nombreCompleto">*Nombre completo</label>          
                                <input name="nombreCompleto" type="text" id="nombreCompleto" value="<%=usuario.get(3)%>" size="50" >
                            </div>
                            <div>
                                <label for="usuario">*Usuario</label>          
                                <input name="usuario" type="text" id="usuario" value="<%=usuario.get(1)%>" size="20" >
                            </div>
                            <div>
                                <label for="password">*Password</label>          
                                <input name="password" type="password" id="password" value="" size="20" >
                            </div>
                            <div>
                                <label for="password2">Confirme su password</label>          
                                <input name="password2" type="password" id="password2" value="" size="20" >
                            </div>
                            <div>
                                <label for="correo">*Correo electrónico</label>          
                                <input name="correo" type="text" id="correo" value="<%=usuario.get(2)%>" size="20" >
                            </div>
                            <div>
                                <label for="estatus">*Estatus</label> 
                                <select name="estatus" id="estatus">
                                    <option value="<%=WebUtil.encode(session,"1")%>" <%
                                        if (usuario.get(4).toString().equals("1")) {
                                            %>
                                            selected
                                            <%}

                                            %>>Activo</option>
                                    <option value="<%=WebUtil.encode(session,"0")%>"
                                            <%      if (usuario.get(4).toString().equals("0")) {
                                            %>
                                            selected
                                            <%}

                                            %>>Inactivo</option>
                                </select>
                            </div>
                            <div>
                                <input type="hidden" name="rolActual" value="<%=WebUtil.encode(session, usuario.get(6))%>"/>
                                <label for="rol">*Rol</label>          
                                <select name="rol" id="rol">
                                    <% t = QUID.select_Roles().iterator();
                                        while (t.hasNext()) {
                                            LinkedList aux = (LinkedList) t.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, aux.get(0))%>"
                                            <% if (usuario.get(6).equals(aux.get(0))) {%> 
                                            selected
                                            <% }%>
                                            ><%=aux.get(1)%> </option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="submit" value="Guardar" name="Enviar" />
                            </div>      
                        </fieldset>
                    </form>
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