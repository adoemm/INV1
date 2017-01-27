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
                LinkedList listAux = null;
                boolean verTodo = SystemUtil.haveAcess("VerTodo", userAccess);
                String FK_ID_Plantel = session.getAttribute("FK_ID_Plantel").toString();
                if (request.getParameter("idPlantel") != null && request.getParameter("page") == null) {
                    verTodo = false;
                    FK_ID_Plantel = WebUtil.decode(session, request.getParameter("idPlantel"));
                } else if (request.getParameter("idPlantel") != null && request.getParameter("page") != null) {
                    FK_ID_Plantel = QUID.select_PlantelAlmacen4Campo("FK_ID_PlantelSurte",
                            WebUtil.decode(session, request.getParameter("idPlantel")));
                    verTodo = false;
                }

                LinkedList result = QUID.select_searchConsumible(FK_ID_Plantel, request.getParameter("busqueda"), verTodo);
                if (request.getParameter("page") == null) {
                    if (result.size() > 1) {
                        it = result.iterator();
                        while (it.hasNext()) {
                            listAux = (LinkedList) it.next();
                            out.println("<li onclick=\"fillSuggestions('" + WebUtil.encode(session, listAux.get(1)) + "','" + listAux.get(2) + "');\">" + listAux.get(2) + "</li>");
                        }
                    } else if (!result.isEmpty()) {
                        LinkedList aux = (LinkedList) result.get(0);
%>
<script type="text/javascript">
    fillSuggestions('<%=WebUtil.encode(session, aux.get(1))%>', '<%=aux.get(2)%>');
</script>
<%

    }
} else {
    it = result.iterator();
    int i=0;
    while (it.hasNext()) {
        listAux = (LinkedList) it.next();
            out.println("<li onclick=\"crearteInput('" + WebUtil.encode(session, listAux.get(1)) + "','idConsumible','" + listAux.get(2) + "');\">" + listAux.get(2) + "</li>");
            i+=1;
        }
    }
%>

<%        } else {
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