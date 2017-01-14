<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertMovimiento");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Nuevo Movimiento</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   

        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#nuevoMovimiento').serialize()
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
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaTraspaso.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Movimientos</a>
                                > Nuevo Movimiento
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form id="nuevoMovimiento" name="nuevoMovimiento" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="nuevoMovimiento"/>                        
                        <fieldset>
                            <legend>Nuevo Movimiento</legend>
                            <fieldset>
                                <legend>Datos Generales</legend>
                                
                                <div>
                                    <label for="motivoTranspaso">*Tipo de Movimiento</label>
                                      <select name="motivoTranspaso">                                            
                                    <option value=""></option>
                                    <option value=<%=WebUtil.encode(session, "Reasignación")%>>Reasignación</option>
                                    <option value=<%=WebUtil.encode(session, "Prestamo")%>>Prestamo</option>
                                </select>
                                </div>                          
                            <div>
                                <label for="FK_ID_Personal">*Responsable</label>          
                                 <select name="FK_ID_Personal" >
                                        <option value=""></option>
                                        <%
                                            it = null;
                                                it = QUID.select_NombreEncargadoPlantel(session.getAttribute("FK_ID_Plantel").toString()).iterator();
                                            while (it.hasNext()) {
                                                listAux = null;
                                                listAux = (LinkedList) it.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"><%=listAux.get(1)%><%=listAux.get(2)%><%=listAux.get(3)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                            </div>
                                 <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" rows="4" cols="50" size="25"></textarea> 
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
