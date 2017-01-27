<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Departamento().iterator();
                String onChange = "";
                if (request.getParameter("onChange") != null) {
//                    switch (request.getParameter("onChange")) {
//                        case "":
//                            onChange = "";
//                            break;
//                    }
                }
                if (request.getParameter("cheklist") != null) {
%>
<table width="95%" border="0">
    <tr style="text-align: left;">
        <td>
            <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divDepartamento', this);" style="padding: 0px; margin:0px;cursor: pointer;"/>Seleccionar todo
            <br>
        </td>
    </tr>
    <tr>
        <%
            int i = 3;
            while (it.hasNext()) {
                LinkedList datos = (LinkedList) it.next();
                if (i % 3 == 0) {
        %>
    </tr><tr>
        <%
            }
        %>
        <td>
            <input type="checkbox" name="idDepartamento" value="<%=WebUtil.encode(session, datos.get(0))%>" style="padding: 0px; margin:0px;cursor: pointer;"><%=datos.get(1)%>
        </td>
        <%
                i += 1;
            }
        %>
</table>
<%
} else {
%>
<label for="idDepartamento">*Departamento</label>
<select name="idDepartamento"  onchange="<%=onChange%>" id="idDepartamento">
    <option value=""></option>
    <%        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>
    <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
    <%
        }
    %>
</select>
<%
                }
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