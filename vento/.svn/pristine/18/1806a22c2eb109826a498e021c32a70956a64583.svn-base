<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("InsertBaja");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Registrar Baja</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo(form) {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $(form).serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Bienes</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=request.getParameter("idBien")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Editar Bien</a> 
                                > Registrar Baja
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="insertBaja" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertBaja">
                        <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="FormFrom" value="insertBaja"/>
                        <input type="hidden" name="idBien" value="<%=request.getParameter("idBien")%>"/>
                        <input type="hidden" name="idPlantel" value="<%=request.getParameter("idPlantel")%>"/>
                        <fieldset>
                            <legend>Registrar Baja</legend>
                            <div>
                                <label for="fechaBaja">*Fecha de baja (aaaa-mm-dd)</label>
                                <input type="text" class="w8em format-y-m-d divider-dash highlight-days-67" id="fechaBaja"   name="fechaBaja" value="" size="10" MAXLENGTH="10">
                            </div>
                            <div>
                                <label for="noOficio">*Número de oficio</label>
                                <input type="text" name="noOficio" id="noOficio" size="30">
                            </div>
                            <div>
                                <label for="motivoBaja">*Motivo de baja</label>
                                <select name="motivoBaja" id="motivoBaja">
                                    <option value=""></option>
                                    <option value="<%=WebUtil.encode(session, "Dañado")%>">Dañado</option>
                                    <option value="<%=WebUtil.encode(session, "Desuso")%>">Desuso</option>
                                    <option value="<%=WebUtil.encode(session, "Irreparable")%>">Irreparable</option>
                                </select>
                            </div>
                            <div>
                                <label for="observaciones">Observaciones</label>
                                <textarea name="observaciones" id="observaciones" cols="25" rows="5"></textarea>
                            </div>
                            <div id="botonEnviarDiv" >
                                <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertBaja'));"/>
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