<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("RelacionPersonalPlantel");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Vincular Personal</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   

        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                //alert("enviando info");
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#vincularPersonal').serialize()
                    , success: function (response) {
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Personal</a> > Vincular Empleado
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <fieldset style="margin: 10px 0; padding: 10px; border: #DDD 1px solid;">                       
                        <br>                      
                        <legend style="font-weight: bold; color: #0C1A22;">Vincular Empleado</legend>
                        <div >
                            <form name="vincularPersonal" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="vincularPersonal">                    
                                <input type="hidden" id='idPersonal' name="idPersonal" value="<%=request.getParameter("idPersonal")%>"/>
                                <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                                <input type="hidden" name="FormFrom" value="vincularPersonal"/>
                                <div>
                                    <label for="idPlantel">*Plantel:</label>
                                    <select name="idPlantel" >
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
                                <fieldset>
                                    <legend>Relación con el Plantel</legend>
                                    <div>
                                        <label for="cargo">*Cargo:</label>
                                        <select id="cargo" name="cargo">
                                            <option value=""></option>
                                            <option value="Auxiliar de Coordinador de Plantel">Auxiliar de  Coordinador de Plantel</option>
                                            <option value="Coordinador de Plantel">Coordinador de Plantel</option>
                                            <option value="Director de Plantel">Director de Plantel</option>
                                            <option value="Jefe de Sala de Cómputo">Jefe de Sala de Cómputo</option>
                                            <option value="Subdirector de Plantel">Subdirector de Plantel</option>
                                            <option value="Otro">Otro</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label for="situacionActual">*Situación Actual</label>
                                        <select id="situacionActual" name="situacionActual">
                                            <option value=""></option>
                                            <option value="Activo">Activo</option>
                                            <option value="Inactivo">Inactivo</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label for="fechaCargo">Fecha Asignación (aaaa-mm-dd)</label>
                                        <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaCargo"   name="fechaCargo" size="10" MAXLENGTH="10">
                                    </div>
                                </fieldset>
                                <br>
                                <div>
                                    <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo();"/>
                                </div>
                            </form>
                        </div>
                    </fieldset>
                </div>
                <div id="divResult"> 
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