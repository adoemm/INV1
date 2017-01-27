<%-- 
    Document   : buscarCCTPlantel
    Created on : Nov 15, 2016, 10:52:39 AM
    Author     : emmanuel
--%>

<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("LoggedUser");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList result = null;
                LinkedList listAux = null;
                result = QUID.select_CCTyNombrePlantel(request.getParameter("searchText"));

                if (result.size() > 0) {
                    it = result.iterator();
                    while (it.hasNext()) {
                        listAux = (LinkedList) it.next();
                        out.println("<li onclick=\"fillSuggestions('" + listAux.get(0)+" &#160;---&#160; " + listAux.get(1) +"', '"+WebUtil.encode(session,listAux.get(2))+"');\">" + listAux.get(0) + " &#160;---&#160; " + listAux.get(1) + "</li>");
                        
                    }
                }

            } else {
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