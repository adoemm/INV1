<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                LinkedList bien = QUID.select_Bien(WebUtil.decode(session, request.getParameter("idBien")));
                
%>
<input type="hidden" name="idPlantel" id="idPlantel" value="<%=WebUtil.encode(session,bien.get(0))%>">
<table width="95%" border="0" style="text-align: left;">
    <tr>
        <td colspan="2"><b>Plantel:</b> <%=bien.get(1)%></td>
    </tr>
    <tr>
        <td><b>Marca:</b> <%=bien.get(7)%></td>
        <td><b>Modelo:</b> <%=bien.get(9)%></td>
    </tr>
    <tr>
        <td><b>No. serie:</b> <%=bien.get(12)%></td>
        <td><b>No. inventario:</b> <%=bien.get(15)%></td>
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