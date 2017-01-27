<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                it = QUID.select_SubCategoria4Categoria(WebUtil.decode(session, request.getParameter("idCategoria"))).iterator();
                String onChange = "";
                if (request.getParameter("onChange") != null) {
                    switch (request.getParameter("onChange")) {
                        case "getDetalle4Subcategoria":
                            onChange = "getDetalle4Subcategoria(this.value);";
                            break;
                        case "getConsumible":
                            onChange = "getConsumible();";
                            break;
                    }
                }
                if (request.getParameter("inCheck") != null) {
%>
<div>
    <%
        if (request.getParameter("getIDs") != null) {


    %>
    <div>
        <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divDesgloseSubcategorias', this);" />Seleccionar todo
    </div>
    <div id="divDesgloseSubcategorias">
    <%        while (it.hasNext()) {
            LinkedList datos = (LinkedList) it.next();
    %>


    <input type="checkbox" name="idSubcategoria" value="<%=WebUtil.encode(session, datos.get(0))%>"/><%=datos.get(1)%>
    <br>
    <%

        }
    %>
    </div>
    <%
    } else {
    %>
    <div class="msginfo">
        *Seleccione las subcategorías que desea contabilizar.
    </div>
    <div>
        <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divDesgloseSubcategorias', this);" />Seleccionar todo
    </div>
    <div id="divDesgloseSubcategorias">
        <%
            while (it.hasNext()) {
                LinkedList datos = (LinkedList) it.next();
        %>
        <input type="checkbox" name="idSubcategoria" value="<%=datos.get(1)%>"/><%=datos.get(1)%>
        <br>
        <%
            }
        %>
    </div>
    <%
        }
    %>
</div>
<%
} else {
%>
<div>
    <label for="idSubcategoria">*Subcategoria</label>
    <select name="idSubcategoria" onchange="<%=onChange%>" id="idSubcategoria">
        <option value=""></option>
        <%
            if (request.getParameter("all") != null) {
        %>
        <option value="<%=WebUtil.encode(session, "TODOS")%>">TODOS</option>
        <%
            }
            while (it.hasNext()) {
                LinkedList datos = (LinkedList) it.next();
        %>
        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
        <%
            }
        %>
    </select>
</div>
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