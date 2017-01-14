
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<label for="idMedida"  onclick="getMedida();" title="Recargar" class="labelAction">*Medida</label>
<select name="idMedida" id="idMedida">
    <option value=""></option>
    <%
        Iterator it = null;
        it = QUID.select_Medida().iterator();

        while (it.hasNext()) {
            LinkedList aux = (LinkedList) it.next();
    %>
    <option value="<%=WebUtil.encode(session, aux.get(0))%>"><%=aux.get(1)%></option>
    <%
        }
    %>
</select>
<%        } else {
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