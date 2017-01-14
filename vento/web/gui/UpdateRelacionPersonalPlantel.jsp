<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateRelacionPersonalPlantel");

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
                    , data: $('#updateVincularPersonal').serialize()
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
                        <%
                            String cargo = "";
                            String situacionActual = "";
                            String nombrePersonal = "";
                            LinkedList datos = QUID.select_InfoPersonalPlantel(WebUtil.decode(session, request.getParameter("ID_PersonalPlantel")));
                            if (!datos.isEmpty()) {
                                cargo = datos.get(1).toString();
                                situacionActual = datos.get(0).toString();
                                nombrePersonal = datos.get(2).toString() + " " + datos.get(3).toString() + " " + datos.get(4).toString();
                            }
                        %>
                        <div >
                            <form name="updateVincularPersonal" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="updateVincularPersonal">                    
                                <input type="hidden" name="idPlantel" id="idPlantel" value="<%=request.getParameter("FK_ID_Plantel")%>"/>
                                <input type="hidden" name="ID_PersonalPlantel" id="ID_PersonalPlantel" value="<%=request.getParameter("ID_PersonalPlantel")%>"/>
                                <input type="hidden" id='idPersonal' name="idPersonal" value="<%=request.getParameter("idPersonal")%>"/>
                                <input type="hidden" id='cargo' name="cargo" value="<%=cargo%>"/>
                                <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                                <input type="hidden" name="FormFrom" value="updateVincularPersonal"/>
                                <fieldset>
                                    <legend>Relación con el Plantel</legend>
                                    <div>
                                        <b>Plantel <%=request.getParameter("nombrePlantel")%>
                                            <br><%=nombrePersonal%></b>
                                    </div>
                                    <div>
                                        <label for="selectcargo">*Cargo:</label>
                                        <select id="selectcargo" name="selectcargo"  disabled>
                                            <option value="<%=cargo%>"><%=cargo%></option>
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
                                            <option value="<%=situacionActual%>"><%=situacionActual%></option>
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