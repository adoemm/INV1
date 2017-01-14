<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%
    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("UpdateDepartamento_Plantel");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList listAux = null;
                int contParam = 0;
%>

<table aling="center">
    <br>                                
    <%
        it = null;
        it = QUID.select_Departamentos().iterator();
        while (it.hasNext()) {
    %>
    <tr>
        <%
            contParam++;
            listAux = null;
            listAux = (LinkedList) it.next();
            String idPlantel = (WebUtil.decode(session, request.getParameter("idPlantel")));
            if (QUID.select_existdepartamentoPorPlantel(idPlantel, listAux.get(0).toString()) == true) {
        %>                                    
        <td> <input type="checkbox" name="<%="option" + contParam%>" value="<%=WebUtil.encode(session, "" + listAux.get(0))%>" checked disabled ></td><td style="text-align: justify;"><%=listAux.get(1)%></td>
            <%
            } else {
            %>
        <td> <input type="checkbox" name="<%="option" + contParam%>" value="<%=WebUtil.encode(session, "" + listAux.get(0))%>"></td><td style="text-align: justify;"><%=listAux.get(1)%></td>                                
            <%
                }
            %>
    </tr>
    <%
        }
    %>
    <%
    %>
</table>
<input type="hidden" name="numparam" value="<%=contParam%>"/>
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