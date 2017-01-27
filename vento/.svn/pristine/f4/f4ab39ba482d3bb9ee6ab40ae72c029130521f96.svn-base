<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Rubro4CheckList(WebUtil.decode(session, request.getParameter("idCheckList"))).iterator();

%>
<table class="cssLayout" style="width:50%;" >
    <tr>
        <td style="width: 10%;">NP</td>
        <td>Categoría</td>
        <td>Rubro</td>
        <td></td>
    </tr>
    <%        int i = 1;
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>
    <tr>
        <td><%=i%></td>
        <td style="text-align: left;"><%=datos.get(3)%></td>
        <td><%=datos.get(2)%></td>
        <td><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar" width="22" height="23" alt="Eliminar" onclick="eliminarRubro('<%=request.getParameter("idCheckList")%>','<%=WebUtil.encode(session,datos.get(4))%>')"></td>
    </tr>
    <%
            i += 1;
        }
    %> 
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