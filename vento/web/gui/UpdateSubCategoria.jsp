<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("UpdateSubCategoria");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
                    LinkedList listAuxCombo = null;

%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Actualizar Subcategoría</title>
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
                    , data: $('#updateSubCategoria').serialize()
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/MenuCatalogos.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogos del Sistema</a>  
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSubCategoria.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Subcategorías</a>
                                > Actualizar Subcategoría
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateSubCategoria" method="post"  enctype="application/x-www-form-urlencoded" id="updateSubCategoria">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="ID_SubCategoria" value="<%=request.getParameter("ID_SubCategoria")%>"/>  
                        <input type="hidden" name="FormFrom" value="updateSubCategoria"/> 
                        <%
                            it = null;
                            listAux = (LinkedList) QUID.select_SubCategoriaXID(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_SubCategoria")))).get(0);
                        %>
                        <fieldset>
                            <legend>Actualizar Subcategoría</legend>
                            <div>
                                <label for="FK_ID_Categoria">*Categoria</label>
                                <select name="FK_ID_Categoria" >                                            
                                    <option selected value="<%=WebUtil.encode(session, listAux.get(2))%>"><%=listAux.get(3)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_getRowsSubCategoria().iterator();
                                        while (it.hasNext()) {
                                            listAuxCombo = null;
                                            listAuxCombo = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, listAuxCombo.get(1))%>"><%=listAuxCombo.get(0)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="nombreSubCategoria">*Subcategoría</label>
                                <input name="nombreSubCategoria" type="text" id="nombreSubCategoria" value="<%=listAux.get(1)%>" size="50" >
                            </div>
                            <div id="botonEnviarDiv">
                                <input type="button" value="Actualizar" name="Enviar" onclick="enviarInfo();"/>
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

%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%   }
} else {
    System.out.println("No se ha encontrado a imix");
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
    }
%>