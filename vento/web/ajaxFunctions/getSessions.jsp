<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("SessionManager");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                Iterator it = null;
                LinkedList sessions = (LinkedList) request.getAttribute("sesiones");
%>



<%                   it = sessions.iterator();
    int i = 1;
    while (it.hasNext()) {
        LinkedList data = (LinkedList) it.next();
%>
<tr>
    
    <td><%=i%></td>
    <td><%=data.get(11)%></td>
    <td><%=data.get(0).toString().toUpperCase()%></td>
    <td><%=data.get(1)%></td>
    <td><%=data.get(2)%></td>                    
    <td><%=data.get(3)%></td>                    
    <td><%=data.get(4)%></td>
    <td><%=data.get(5)%></td>
    <td><%=data.get(8)%></td>
    <td><%=data.get(6)%></td>
    <td><%=data.get(9)%></td>
    <td><%=data.get(10)%></td>
    <td id="rOnly" class="rOnly"><a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idSesion=<%=WebUtil.encode(session, data.get(0))%>&FormFrom=terminarSesion"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Terminar" width="22" height="23" alt="Terminar"></a></td>
</tr>
<%
        i += 1;
    }
%>


<%        } else {
                //System.out.println("Usuario No valido para esta pagina");
                out.print("Usted no cuenta con el permiso para accesar a esta pagina");
            }
        } else {
            //System.out.println("No se ha encontrado a imix");
            out.print("petición invalida");
        }
    } catch (Exception ex) {
        out.print("Lo sentimos la petición ha tenido un error :( " + ex.getMessage());
    }
%>