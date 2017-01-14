<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Modelo4Marca(WebUtil.decode(session, request.getParameter("idMarca"))).iterator();
                if (request.getParameter("inCheck") != null) {
                    if (request.getParameter("getIDs") != null) {
                        while (it.hasNext()) {
                            LinkedList datos = (LinkedList) it.next();
%>
<input type="checkbox" name="idModelo" value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%>
<br>
<%
    }
} else {
    while (it.hasNext()) {
        LinkedList datos = (LinkedList) it.next();
%>
<input type="checkbox" name="idModelo" value="<%=datos.get(1)%>"><%=datos.get(1)%>
<br>
<%
        }
    }
} else {

%>
<div class="divControlHelp">
    <div id="divAyudaModelo" class="div4HelpButton" >
        <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="24px" height="24px" onclick="showButton('divInfoModelo');
                hideButton('divAyudaModelo');" title="Abrir ayuda">
    </div>
    <label for="idModelo">*Modelo</label>
    <select name="idModelo" onchange="getDescripcionModelo(this.value)">
        <option value=""></option>
        <%
        if(request.getParameter("conTokyo")!=null){
        %>
        <option value="<%=WebUtil.encode(session, "Todos")%>">Todos los Modelos</option>
        <%
        }
        %>
        <%
            while (it.hasNext()) {
                LinkedList datos = (LinkedList) it.next();
        %>
        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
        <%
            }
        %>
    </select>
    <div id="divInfoModelo" class="msginfo div4HelpInfo"  onmouseover="showButton('botonCerrarInfoModelo');" onmouseout="hideButton('botonCerrarInfoModelo');">
        <div class="deleteBar div4HelpInfoCloseButton" >
            <img  class="div4HelpCloseButton" align="right" id="botonCerrarInfoModelo"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="hideButton('divInfoModelo');
                    showButton('divAyudaModelo');" title="Cerrar ayuda">
        </div>
        <p id="divDescModelo"></p>
    </div>
</div>
<%        }
            } else {
                //System.out.println("Usuario No valido para esta pagina");
                out.print("Usted no cuenta con el permiso para accesar a esta pagina");
            }
        } else {
            //System.out.println("No se ha encontrado a imix");
            out.print("Peticion invalida");
        }
    } catch (Exception ex) {
        out.print("Lo sentimos la petición ha tenido un error :(");
    }
%>