<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                LinkedList consumible = QUID.select_Consumible(WebUtil.decode(session, request.getParameter("idConsumible")));

%>
<table width="95%" border="0" style="text-align: left;">
    <tr>
        <td colspan="2"><b>Clave:</b> <%=consumible.get(1)%></td>
    </tr>
    <tr>
        <td><b>Descripción:</b> <%=consumible.get(2)%></td>
        <td><b>No. Parte/Referencia:</b> <%=consumible.get(4)%></td>
    </tr>
    <tr>
        <td><b>Medida:</b> <%=consumible.get(13)%></td>
        <td><b>Precio Actual:</b> <%=consumible.get(8)%></td>
    </tr>
</table>

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