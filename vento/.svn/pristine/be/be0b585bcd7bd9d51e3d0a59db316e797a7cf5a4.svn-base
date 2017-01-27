<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_Conumible4ConteoFisico(
                        WebUtil.decode(session, request.getParameter("idPlantel")),
                        WebUtil.decode(session, request.getParameter("idSubcategoria")),
                        WebUtil.decode(session, request.getParameter("idConteoFisico"))).iterator();

%>
<table class="cssLayout" style="width:100%;" >
    <tr>
        <td>NP</td>
        <td>Plantel</td>
        <td>Categoría</td>
        <td>Subcategoría</td>
        <td style="width:7%;">Clave</td>
        <td>Desc.</td>
        <td>Unidad</td>
        <td>Estatus</td>
        <td>Costo P.</td>
        <td>Existencias</td>
        <td>Valorizado</td>
        <td>Conteo Fisico</td>
        <td  style="width:5%;">Variación</td>
    </tr>
    <%        int i = 1;
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>
    <tr>
        <td><%=i%>
            <input type="hidden" name="idConsumible" id="idConsumible" value="<%=WebUtil.encode(session, datos.get(3))%>">
            <input type="hidden" name="conteoLogico_<%=WebUtil.encode(session, datos.get(3))%>" id="conteoLogico_<%=WebUtil.encode(session, datos.get(3))%>" value="<%=datos.get(9)%>">
            <input type="hidden" name="precioHistorico_<%=WebUtil.encode(session, datos.get(3))%>" id="precioHistorico_<%=WebUtil.encode(session, datos.get(3))%>" value="<%=datos.get(8)%>">
        </td>
        <td><%=datos.get(0)%></td>
        <td><%=datos.get(1)%></td>
        <td><%=datos.get(2)%></td>
        <td><%=datos.get(4)%></td>
        <td><%=datos.get(5)%></td>
        <td><%=datos.get(6)%></td>
        <td><%=datos.get(7)%></td>
        <td><%=datos.get(8)%></td>
        <td><%=datos.get(9)%></td>
        <td><%=datos.get(10)%></td>
        <td><input type="text" name="<%=WebUtil.encode(session, datos.get(3))%>" id="<%=WebUtil.encode(session, datos.get(3))%>" style="padding: 0px; margin: 0px;" size="6" value="<%=datos.get(11) != null ? datos.get(11) : ""%>" onChange="calculaVariacion(this,'variacion_<%=WebUtil.encode(session, datos.get(3))%>','<%=datos.get(9)%>');" onBlur="calculaVariacion(this,'variacion_<%=WebUtil.encode(session, datos.get(3))%>','<%=datos.get(9)%>');"></td>
        <td style="width:5%;"><input type="text" name="variacion_<%=WebUtil.encode(session, datos.get(3))%>" id="variacion_<%=WebUtil.encode(session, datos.get(3))%>" value="<%=datos.get(13)%>" style="padding: 0px; margin: 0px;" size="6" disabled></td>
        <%
            if (datos.get(11) != null) {
        %>
    <input type="hidden" name="idConteoFisicoConsumible_<%=WebUtil.encode(session, datos.get(3))%>" id="idConteoFisicoConsumible_<%=WebUtil.encode(session, datos.get(3))%>" value="<%=WebUtil.encode(session, datos.get(15))%>">
    <%
        }
    %>
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