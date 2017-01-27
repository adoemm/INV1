<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                LinkedList modelo = QUID.select_Modelo(WebUtil.decode(session, request.getParameter("idModelo")));
%>
<script type="text/javascript" language="javascript" charset="utf-8">
    function seleccionarTodo(contenedor, valor) {
        var checks = document.getElementById(contenedor);
        for (i = 0; i < checks.getElementsByTagName('input').length; i++) {
            if (checks.getElementsByTagName('input')[i].type === "checkbox") {
                checks.getElementsByTagName('input')[i].checked = valor.checked;
            }
        }
    }
</script>
<%
    if (WebUtil.decode(session, request.getParameter("accion")).equals("1")) {


%>
<form name="insertPlantelAlmacen" method="post" action="" enctype="application/x-www-form-urlencoded" id="insertPlantelAlmacen">
    <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
    <input type="hidden" name="FormFrom" value="insertPlantelAlmacen"/>
    <input type="hidden" name="idPlantelSurte" value="<%=request.getParameter("idPlantel")%>"/>
    <fieldset>
        <legend>Definir Abastecedor</legend>
        <div class="msginfo" align="center">
            Seleccione los planteles que serán abstecidos por <b><%=QUID.select_PlantelXCampo("nombre", WebUtil.decode(session, request.getParameter("idPlantel")))%></b>.
        </div>
        <div>
            <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divPlanteles', this);" />Seleccionar todo
        </div>
        <div id="divPlanteles">
            <%
                Iterator t = QUID.select_NombrePlantelID(session.getAttribute("FK_ID_Plantel").toString(), true).iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <input type="checkbox" name="idPlantelSolicita" value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%><br>
            <%
                }
            %>
        </div>
    </fieldset>
    <div id="botonEnviarDiv" >
        <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('insertPlantelAlmacen'));"/>
    </div> 
</form>
<%
} else if (WebUtil.decode(session, request.getParameter("accion")).equals("2")) {
%>
<form name="updatePlantelAlmacen" method="post" action="" enctype="application/x-www-form-urlencoded" id="updatePlantelAlmacen">
    <input type="hidden" name="<%=WebUtil.encode(session, "imix")%>" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
    <input type="hidden" name="FormFrom" value="updatePlantelAlmacen"/>
    <input type="hidden" name="idPlantelSurte" value="<%=request.getParameter("idPlantel")%>"/>
    <fieldset>
        <legend>Definir Abastecedor</legend>
        <div class="msginfo" align="center">
            Desmarque los planteles que ya no serán abastecidos por <b><%=QUID.select_PlantelXCampo("nombre", WebUtil.decode(session, request.getParameter("idPlantel")))%></b>.
        </div>
        <div>
            <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divPlanteles', this);" />Seleccionar todo
        </div>
        <div id="divPlanteles">
            <%
                Iterator t = QUID.select_PlantelAlmacen(WebUtil.decode(session, request.getParameter("idPlantel")), "1").iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
            %>
            <input type="checkbox" name="idPlantelSolicita" checked value="<%=WebUtil.encode(session, datos.get(0))%>" ><%=datos.get(2)%><br>
            <%
                }
            %>
        </div>
    </fieldset>
    <div id="botonEnviarDiv" >
        <input type="button" value="Guardar" name="Enviar" onclick="enviarInfo(document.getElementById('updatePlantelAlmacen'));"/>
    </div> 
</form>
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