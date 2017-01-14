<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Grupo4DeptoPlantel(WebUtil.decode(session, request.getParameter("idDeptoPlantel"))).iterator();
                String onChange = "";

%>
<div>
    <label for="idGrupo">Agrupación</label>
    <select name="idGrupo" onchange="<%=onChange%>">
        <option value=""></option>
        <%
            while (it.hasNext()) {
                LinkedList datos = (LinkedList) it.next();
        %>
        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
        <%
            }
        %>
    </select>
</div>
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