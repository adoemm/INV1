<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Grupos(WebUtil.decode(session, request.getParameter("idPlantel")), false).iterator();
                boolean updatePDG = request.getParameter("updatePDG") != null ? true : false;
%>

<table class="cssLayout" style="<%=updatePDG?"width:95%;":"width:50%;"%>">

    <tr>
        <%
            if (updatePDG) {
        %>
        <td></td>
        <%
            }
        %>
        <td width="10%">NP</td>
        <td width="20%">Departamento</td>
        <td>Agrupación</td>
    </tr>
    <tr style="text-align: left;">
        <td colspan="4" style="text-align: left;">
            <%
                if (updatePDG) {
            %>
            <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divGrupos', this);" style="padding: 0px;margin: 0px;"/>Seleccionar todo
            <%
                }
            %>    
        </td>
    </tr>
    <%
        int i = 1;
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>
    <tr>
        <%
            if (updatePDG) {
        %>
        <td width="5%"><input type="checkbox" name="idPDG" value="<%=WebUtil.encode(session, datos.get(6))%>" style="padding: 0px;margin: 0px;"></td>
            <%
                }
            %>
        <td><%=i%> </td>
        <td> <%= datos.get(3)%></td>
        <td> <%= datos.get(5)%></td>
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