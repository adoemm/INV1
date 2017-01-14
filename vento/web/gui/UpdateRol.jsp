<%-- 
    Document   : editarRol
    Created on : 7/08/2013, 12:29:18 PM
    Author     : Hewlet
--%>

<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("EditarRol");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
                    int contParam = 0;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Editar Rol de Usuario</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <script type="text/javascript" language="javascript" charset="utf-8">
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuSeguridad.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Seguridad</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaRol.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Roles</a> 
                                > Editar Rol
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="rolusuario" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="form">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="editarRol"/>  
                        <input type="hidden" name="id_rol" value="<%=request.getParameter("idrol")%>"/>                          
                        <fieldset>
                            <legend>Editar Rol de Usuario</legend>
                            <div>
                                <label for="rol">*Nombre del Rol:</label>
                                <input name="rol" type="text" id="nombre" value="<%=request.getParameter("nomrol")%>" size="20">
                            </div>
                            <div>
                                <label for="dscrol">*Descripción del Rol:</label>
                                <input name="dscrol" type="text" id="cct" value="<%=request.getParameter("dscrol")%>" size="80">
                            </div>                                                   
                        </fieldset>
                        <fieldset>
                            <legend>Asignación de Permisos</legend>
                            <div>
                                <br>                                
                                <%
                                    it = null;
                                    it = QUID.select_getPermisos().iterator();
                                    while (it.hasNext()) {
                                        contParam++;
                                        listAux = null;
                                        listAux = (LinkedList) it.next();

                                        if (QUID.select_existpermisoPorRol(WebUtil.decode(session, request.getParameter("idrol")), listAux.get(0).toString()) == true) {
                                %>                                    
                                <input type="checkbox" name="<%="option" + contParam%>" value="<%=WebUtil.encode(session, "" + listAux.get(0))%>" checked><%=listAux.get(1)%><br>
                                <%
                                } else {
                                %>
                                <input type="checkbox" name="<%="option" + contParam%>" value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"><%=listAux.get(1)%><br>                                
                                <%
                                        }
                                    }
                                %>
                            </div>                            
                            <div id="botonEnviarDiv" style="">
                                <input type="hidden" name="numparam" value="<%=contParam%>"/>
                                <input type="submit" value="Guardar" name="Enviar" />
                            </div>                                                     
                        </fieldset>
                    </form>
                    <a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&id_rol=<%=request.getParameter("idrol")%>&FormFrom=EliminaRol">
                        <button>Eliminar Rol</button>
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
