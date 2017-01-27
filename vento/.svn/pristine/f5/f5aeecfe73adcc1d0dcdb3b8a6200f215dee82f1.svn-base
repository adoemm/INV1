<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<table class="cssLayout" style="width:90%;" >
    <tr>
        <td width="10%">NP</td>
        <td width="20%">Categoría</td>
        <td>Rubro</td>
        <td style="text-align: center; width:10%; ">&nbsp;</td>
    </tr>
    <%        Iterator rubros = QUID.select_Rubro4Bien(WebUtil.decode(session, request.getParameter("idBien")),
                WebUtil.decode(session, request.getParameter("idCheckList"))).iterator();
        int i = 1;
        while (rubros.hasNext()) {
            LinkedList datos = (LinkedList) rubros.next();
    %>
    <tr>
        <td><%=i%></td>
        <td style="text-align: left;"><%=datos.get(3)%></td>
        <td><%=datos.get(0)%></td>
        <td>
            <input style="margin: 0px;" type="checkbox" name="idRubro" value="<%=WebUtil.encode(session, datos.get(2))%>" <%=datos.get(1) != null ? "checked" : ""%>/>
        </td>
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