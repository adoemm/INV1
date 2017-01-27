<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<form action="<%=PageParameters.getParameter("mainController")%>" id="updatePuntuacion" name="updatePuntuacion">
    <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
    <input type="hidden" name="FormFrom" value="updatePuntuacion"/>
    <input type="hidden" name="idPuntuacion" value="<%=request.getParameter("idPuntuacion")%>"/>
    <input type="hidden" name="ID_Plantel" value="<%=request.getParameter("ID_Plantel")%>"/>
    <%
        LinkedList puntuacion = QUID.select_Puntuacion(WebUtil.decode(session, request.getParameter("idPuntuacion")));
    %>
    <fieldset style="border-radius: 10px; width: 93%; margin-top:75px">
        <legend style="text-align: left;">Detalles</legend>
        <div style="text-align: left;">
            <label for="rubro">*Rubro</label>
            <select name="idRubro" style="width: 100%;">
                <option value="<%=WebUtil.encode(session, puntuacion.get(5))%>"><%=puntuacion.get(4)%></option>
                <%
                    Iterator it = QUID.select_Rubro().iterator();
                    while (it.hasNext()) {
                        LinkedList datos = (LinkedList) it.next();
                %>
                <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=StringUtil.truncString(datos.get(1).toString(),25)+"..."%></option>
                <%
                    }
                %>
            </select>
        </div>
        <div style="text-align: left;">
            <label for="puntuacion">*Puntuación (Ej. 8.0, 9.5)</label>
            <input type="text"  name="puntuacion" value="<%=puntuacion.get(3)%>" >
        </div>
        <div style="text-align: left;">
            <label for="observaciones">*Observaciones</label>
            <textarea name="observaciones" cols="35"  style="resize:none; width: 100%;height: 75px;"><%=puntuacion.get(2)%></textarea>
        </div>
    </fieldset>
    <div style=" text-align: right; margin-right: 0px;">
        <input align="left" type="button" value="Guardar" onclick="enviarInfo(document.getElementById('updatePuntuacion'))"/>
        <input align="left" type="button" value="Cerrar" onclick="closeDialogBox('floatBoxDetalles');"/>
    </div>
</form>
<%
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