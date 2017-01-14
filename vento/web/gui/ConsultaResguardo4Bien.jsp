<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("verInfoResguardo");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Información de Resguardo</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%=PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>

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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Bienes</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Editar Bien</a> 
                                > Información de Resguardo
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="infoResguardo" method="post"  action="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("prints")%>/infoResguardo4Bien.jsp" enctype="application/x-www-form-urlencoded" id="infoResguardo">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="infoResguardo"/>
                        <input type="hidden" id='idBien' name="idBien" value="<%=request.getParameter("idBien")%>"/>
                        <fieldset>
                            <legend>Información de Resguardo</legend>
                            <%
                                Iterator resguardos = QUID.select_Resguardo4Bien(WebUtil.decode(session, request.getParameter("idBien")), "Activo").iterator();
                                boolean withInfo = false;
                                while (resguardos.hasNext()) {
                                    withInfo = true;
                                    LinkedList datosResguardo = (LinkedList) resguardos.next();
                            %>

                            <div>
                                <label>Plantel</label>
                                <input type="text" size="50" value="<%=datosResguardo.get(0).toString().toUpperCase()%>">
                            </div>
                            <div>
                                <label>Personal Encargado</label>
                                <input type="text" size="50" value="<%=datosResguardo.get(2).toString().toUpperCase()%>">
                            </div>
                            <div>
                                <label>Cargo</label>
                                <input type="text" size="50" value="<%=datosResguardo.get(4).toString().toUpperCase()%>">
                            </div>
                            <div>
                                <label>No. de Tarjeta de Resguardo</label>
                                <input type="text" size="50" value="<%=datosResguardo.get(10).toString().toUpperCase()%>">
                            </div>
                            <div>
                                <label>Fecha de Asignación</label>
                                <input type="text" size="50" value="<%=datosResguardo.get(7).toString().toUpperCase()%>">
                            </div>

                            <%
                                }
                            %>
                            <div id="botonEnviarDiv" style="display: none;" >
                                <input type="submit" value="Imprimir" name="Imprimir"/>
                            </div> 
                            <%
                                if (withInfo) {
                            %>
                            <script type="text/javascript" language="javascript" charset="utf-8">
                                document.getElementById('botonEnviarDiv').style.display = 'block';
                            </script>
                            <%
                                }
                            %>
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