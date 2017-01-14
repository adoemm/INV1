
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = QUID.select_MovimientoConsumible(WebUtil.decode(session, request.getParameter("idMovimiento"))).iterator();
                String strIVA = QUID.select_Movimiento4Campo("iva", WebUtil.decode(session, request.getParameter("idMovimiento")));
%>

<table  class="cssLayout" style="width:60%;" >
    <tr>
        <td style="width: 5%;">NP</td>
        <td style="width: 20%;">Clave</td>
        <td style="width: 25%;">Descripción</td>
        <td style="width: 20%;">Medida</td>
        <td style="width: 10%;">Cantidad</td>
        <td style="width: 10%;">Precio U.</td>
        <td style="width: 10%;">Importe</td>
        <%
            if (request.getParameter("onlyRead") == null) {

        %>
        <td style="width: 10%;"></td>
        <%            }
        %>
    </tr>
    <%        int i = 1;
        Double Total = 0.0;
        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
            Double subtotal = Double.parseDouble(datos.get(23).toString()) * Double.parseDouble(datos.get(24).toString());
    %>
    <tr>
        <td><%=i%></td>
        <td style="width: 20%;"><%=datos.get(4)%></td>
        <td style="width: 25%;"><%=datos.get(5)%></td>
        <td style="width: 20%;"><%=datos.get(22)%></td>
        <td style="width: 10%;text-align: right;"><%=datos.get(23)%></td>
        <td style="width: 10%;text-align: right;"><%=datos.get(24)%></td>
        <td style="width: 10%;text-align: right;"><%=StringUtil.formatDouble2Decimals(subtotal)%></td>
        <%
            if (request.getParameter("onlyRead") == null) {
        %>
        <td style="text-align: center;">
            <img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar" width="22" height="23" alt="Eliminar" onclick="eliminarConsumible('<%=WebUtil.encode(session, datos.get(25))%>', '<%=request.getParameter("idMovimiento")%>')">
        </td>
        <%            }
        %>
    </tr>
    <%
            i += 1;
            Total += subtotal;
        }
    %> 
    <tr>
        <td colspan="6" style="text-align: right"><b>Subtotal</b></td>
        <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(Total)%></td>
    </tr>
    <%
        if (!strIVA.equals("") && StringUtil.isValidDouble(strIVA)) {
    %>
    <tr>
        <td colspan="6" style="text-align: right"><b>IVA</b></td>
        <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(strIVA)%></td>
    </tr>
    <tr>
        <td colspan="6" style="text-align: right"><b>Total</b></td>
        <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(Total + Double.parseDouble(strIVA))%></td>
    </tr>
    <%
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