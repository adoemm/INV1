<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateDepartamento_Plantel");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
                    session = request.getSession(true);
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Asignar Nuevo Departamento</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>  
        <script type="text/javascript" language="javascript" charset="utf-8">
            function getDepartamento() {
                var plantel = document.getElementById("idPlantel").value;
                if (plantel !== "") {
                    var response = $.ajax({
                        type: 'get',
                        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                        cache: false,
                        url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getDepartamentosXPlantel.jsp",
                        data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=' + plantel,
                        async: false,
                        success: function(data) {
                        }
                    }).responseText;
                    document.getElementById('departamentoDiv').innerHTML = response;
                } else {
                }
            }
        </script>
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaDepartamento_Plantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de departamentos</a> >Asignar departamento
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateDepatamento_Plantel" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="form">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="updateDepatamento_Plantel"/>  

                        <fieldset>
                            <legend>Asignar Nuevo Departamento</legend>
                            <div>
                                    <label for="idPlantel">*Plantel:</label>
                                    <select name="idPlantel" id="idPlantel" onkeyup="getDepartamento();" onChange="getDepartamento();">
                                        <option value=""></option>
                                        <%
                                            it = null;
                                            if (SystemUtil.haveAcess("VerTodo", userAccess)) {
                                                it = QUID.select_IDNombrePlantel().iterator();
                                            } else {
                                                it = QUID.select_NombrePlantel(session.getAttribute("FK_ID_Plantel").toString()).iterator();
                                            }
                                            while (it.hasNext()) {
                                                listAux = null;
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"><%=listAux.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                        </fieldset>
                        <fieldset>
                            <legend>Asignación de Departamentos</legend>
                            <div id="departamentoDiv">   
                            </div>                           
                            <div id="botonEnviarDiv" style="">
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
