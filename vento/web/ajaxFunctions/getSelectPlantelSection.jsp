<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="jspread.core.util.WebUtil"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>

<jsp:useBean id="QUIDAux" scope="page" class="jspread.core.db.QUID"/>

<%
    LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
    String onChange = "";
    if (request.getParameter("onChange") != null) {
        switch (request.getParameter("onChange")) {
            case "getDepartamento":
                onChange = "getDepartamento(this.value);";
                break;
            case "getDepartamentoAndGrupo":
                onChange = "getDepartamento(this.value);getGrupo(this.value);";
                break;
            case "submit":
                onChange = "this.form.submit()";
                break;
        }
    }
%>

<div>
    <label for="idPlantel">*Plantel</label>
    <select name="idPlantel" id="idPlantel" onChange="<%=onChange%>">
       
        <%
            Iterator itI = null;
            LinkedList plantelList = null;
            if (SystemUtil.haveAcess("VerTodo", userAccess)) {
                plantelList = QUIDAux.select_IDNombrePlantel();
                if (request.getParameter("conTokyo") != null) {
                    LinkedList todosPLantels = new LinkedList();
                    todosPLantels.add("Todos");
                    todosPLantels.add("Todos");
                    plantelList.addFirst(todosPLantels);
                }
            } else {
                plantelList = QUIDAux.select_NombrePlantel(session.getAttribute("FK_ID_Plantel").toString());
            }
            itI = plantelList.iterator();
            while (itI.hasNext()) {
                LinkedList listAuxI = null;
                listAuxI = (LinkedList) itI.next();
                if (WebUtil.decode(session, request.getParameter("plantelActual")).equalsIgnoreCase(listAuxI.get(0).toString())) {
        %>
        <optgroup label="Plantel actual">
            <option value="<%=WebUtil.encode(session, listAuxI.get(0))%>" selected><%=listAuxI.get(1)%></option>
        </optgroup>
        <%
        } else {
        %>
        <option value="<%=WebUtil.encode(session, listAuxI.get(0))%>"><%=listAuxI.get(1)%></option>
        <%
            }
        %>

        <%
            }
        %>
    </select>
</div>