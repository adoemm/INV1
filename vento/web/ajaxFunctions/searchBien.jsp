<%@page import="jspread.core.util.WebUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList listAux = null;
%>

<%
//    it = QUID.select_searchAlumnoNamesNControl(session.getAttribute("FK_ID_Plantel").toString(), request.getParameter("busqueda")).iterator();
//    while (it.hasNext()) {
//        listAux = (LinkedList) it.next();
//        //out.println("<li onclick=\"fill('" + listAux.get(2) + "');\">" + listAux.get(2) + "</li>");
//        out.println("<li onclick=\"fillSuggestions('" + WebUtil.encode(session, listAux.get(1)) + "','" + listAux.get(2) + "');\">" + listAux.get(2) + "</li>");
//    }
%>

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
