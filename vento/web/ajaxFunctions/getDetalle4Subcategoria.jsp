<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Detalle4Subcategoria(WebUtil.decode(session, request.getParameter("idSubcategoria"))).iterator();
                if (request.getParameter("inCheck") != null) {
%>
<div class="msginfo">
    *Seleccione las especificaciones que desea que aparezcan en el reporte.
</div>
<div>
    <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divDesgloseDetalles', this);" />Seleccionar todo
</div>
<div id="divDesgloseDetalles">
    <%
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>
    <div>
        <input type="checkbox" name="idDetalle" value="<%=datos.get(1)%>"><%=datos.get(1)%>
    </div>
    <%

        }
    %>
</div>
<%
} else {

%>
<div class="msginfo">Seleccione las especificaciones del bien/artículo, si no desea guardar alguna déjela en blanco.</div>
<table class="cssLayout" style="width:80%;">
    <tr>
        <td width="10%">NP</td>
        <td width="40%">Detalle</td>
        <td>Valor</td>
    </tr>
    <%        int i = 1;
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
            Iterator dt = QUID.select_Valor4Detalle(datos.get(0).toString()).iterator();
    %>
    <tr>
        <td><%=i%></td>
        <td><%=datos.get(1)%></td>
        <td><select name="<%=WebUtil.encode(session, datos.get(5))%>"  class="selectP" style="width: 100px;">
                <option value=""></option>
                <%
                    while (dt.hasNext()) {
                        LinkedList aux = (LinkedList) dt.next();
                %>
                <option value="<%=WebUtil.encode(session, aux.get(0))%>"><%=aux.get(1)%></option>
                <%

                    }
                %>
            </select></td></tr>
            <%
                    i += 1;
                }
            %>
</table>
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