<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
               
                    Iterator it = null;
                    LinkedList listAux = null;
                    LinkedList listAuxCombo = null;

%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Actualizar Modelo</title>
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
                    , data: $('#updateModelo').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
            function popupArchivos() {
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/Insert_ObjetoArchivo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idObjeto=<%=request.getParameter("ID_Modelo")%>&nombreObjeto=<%=WebUtil.encode(session, "MODELO")%>'
                                        , 'Archivos'
                                        , 'width='+ (screen.availWidth - 10).toString()+',height='+ (screen.availHeight - 122).toString()+',toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                                        );
                                if (window.focus) {
                                    newwindow.focus();
                                }
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> > 
                                <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaModelo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Modelos</a> > 
                                Actualizar Modelo
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <form name="updateModelo" method="post"  enctype="application/x-www-form-urlencoded" id="updateModelo">
                        <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                        <input type="hidden" name="ID_Modelo" value="<%=request.getParameter("ID_Modelo")%>"/>  
                        <input type="hidden" name="FormFrom" value="updateModelo"/> 
                        <%
                            it = null;
                            listAux = (LinkedList) QUID.select_ModeloXID(Integer.parseInt(WebUtil.decode(session, request.getParameter("ID_Modelo")))).get(0);
                        %>
                        <fieldset>
                            <legend>Actualizar Modelo</legend>
                            <div>
                                <label for="FK_ID_Marca">*Marca</label>
                                <select name="FK_ID_Marca" >                                            
                                    <option selected value="<%=WebUtil.encode(session, listAux.get(3))%>"><%=listAux.get(2)%></option>
                                    <%
                                        it = null;
                                        it = QUID.select_Marca().iterator();
                                        while (it.hasNext()) {
                                            listAuxCombo = null;
                                            listAuxCombo = (LinkedList) it.next();
                                    %>
                                    <option value="<%=WebUtil.encode(session, listAuxCombo.get(0))%>"><%=listAuxCombo.get(1)%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div>
                                <label for="modelo">*Modelo</label>
                                <input name="modelo" type="text" id="modelo" value="<%=listAux.get(5)%>" size="50" >
                            </div>
                            <div>
                                <label for="descripcion">Descripción</label>          
                                <textarea name="descripcion"  id="descripcion" value="" size="20"><%=listAux.get(4)%></textarea>  
                            </div>
                              <div id="botonEnviarDiv">
                            <input type="button" value="Actualizar" name="Enviar" onclick="enviarInfo();"/>
                            <button onclick="popupArchivos();">Archivos</button>
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