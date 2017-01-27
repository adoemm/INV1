<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                if (request.getParameter("idCategoria") != null) {
                    String[] subcategoras = request.getParameterValues("idCategoria");
%>
<table width="95%" border="0">
    <tr style="text-align: left;">
        <td>
            <input type="checkbox"  name="selectAll" onclick="seleccionarTodo('divSubcategorias', this);" style="margin: 0px;"/>Seleccionar todo
                                <br>
        </td>
    </tr>
    <tr>
        <%
            int j = 4;
            for (int i = 0; i < subcategoras.length; i++) {
                Iterator it = null;
                it = QUID.select_SubCategoria4Categoria(WebUtil.decode(session, subcategoras[i])).iterator();
                while (it.hasNext()) {
                    LinkedList subcategoria = (LinkedList) it.next();
                    if (j % 4 == 0) {
        %>
    </tr>
    <tr>  
        <%
            }
        %>
        <td>
            <input type="checkbox" name="idSubcategoria" style="padding: 0px; margin:0px;cursor: pointer;" value="<%=WebUtil.encode(session, subcategoria.get(0))%>"><%=subcategoria.get(1)%>
        </td>
        <%
                    j += 1;
                }
            }
        %>
    </tr>
</table>
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