<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@page import="jspread.core.util.PageParameters"%>

<jsp:include page='<%= PageParameters.getParameter("msgUtil") + "/msgNRedirect.jsp"%>' flush = 'true'>
    <jsp:param name='title' value='Error' />
    <jsp:param name='msg' value='Esta actividad no está en el calendario escolar.' />
    <jsp:param name='type' value='error' />
    <jsp:param name='url' value='<%=PageParameters.getParameter("mainMenu")%>' />
</jsp:include>