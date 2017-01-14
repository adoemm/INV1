<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<%
    try {
        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("InsertSoftware");
            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");

            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                //JOptionPane.showMessageDialog(null, "Request: "+request.getParameter("idPlantel"));
                //JOptionPane.showMessageDialog(null, WebUtil.decode(session, request.getParameter("idPlantel")));
                Iterator it = null;
                LinkedList listAux = null;
%>

<div>
    <label for="emailSoporteTecnico">*E-mail de Soporte</label>          
    <input name="emailSoporteTecnico" type="text" id="emailSoporteTecnico" value="" size="50" >
</div>
<div>
    <label for="telefonoSoporte">*Tel. de Soporte</label>          
    <input name="telefonoSoporte" type="text" id="telefonoSoporte" value="" size="50" >
</div>
<%        } else {
                //System.out.println("Usuario No valido para esta pagina");
                out.print("Usted no cuenta con el permiso para accesar a esta pagina");
            }
        } else {
            //System.out.println("No se ha encontrado a imix");
            out.print("petición invalida");
        }
    } catch (Exception ex) {
        out.print("Lo sentimos la petición ha tenido un error :(");
    }
%>