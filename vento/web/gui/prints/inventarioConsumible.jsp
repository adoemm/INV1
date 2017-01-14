<%-- 
    Document   : inventario
    Created on : 21/06/2016, 01:57:43 PM
    Author     : emmanuel
--%>
<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteInventario");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Inventario</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
             function imprimeError(form) {
                $.msgBox({
                    title: "Error"
                    , content: "No hay consumibles dados de Alta"
                    , type: "error"
                    , opacity: 0.75
                    , buttons: [{value: "OK"}]
                    , success: function (result) {
                            if(result==="OK")
                            {
                               window.close();
                            }
                        }
                    
                });
              

            }
        </script>
    </head>
    <body style="background-color:#ffffff;">

        <%
            int idPlantel = Integer.parseInt(WebUtil.decode(session, request.getParameter("idPlantel")));
            String plantel = QUID.select_Plantel(idPlantel);
            int numConsumibles= QUID.contador_ConsumiblesxPlantel(idPlantel);
            if(numConsumibles>0)
            {
        %>
        <div align="center" style="width: 25cm; height: 28cm; margin: 0 auto;">
            <div>
                <div align="center">
                    <table width="92%" border="0" align="center">
                        <tr>
                            <td align="left" width="7%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td class="estilo2" width="80%">
                                <p align="center">
                                    <strong>COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</strong>
                                    <br><b><strong>CONTROL DE CONSUMIBLES</strong></b>
                                    <br><b><strong><%=plantel%></strong></b>
                                    <br><b><strong>REGISTRO DE INVENTARIO DE PLANTEL</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>

                </div>
                <hr><br>
                <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                    <tr class="celdaRellenoCenter">
                        <td>Clave</td>
                        <td>Descripcción</td>    
                        <td>Unidad de Medida</td>
                        <td>Existencias</td>
                    </tr>


                    <%
                         Iterator it = QUID.select_Consumible("" + idPlantel, false, "", false, false).iterator();
                        LinkedList listAux = null;
                        int existencias = 0;
                        while (it.hasNext()) {
                            listAux = (LinkedList) it.next();
                            int auxExistencias =(int)Double.parseDouble(listAux.get(9).toString());
                            if (auxExistencias != 0) {
                                existencias = existencias + auxExistencias;


                    %>
                    <tr style="text-align: center;">
                        <td><%=listAux.get(1).toString()%></td>
                        <td><%=listAux.get(2).toString()%></td>
                        <td><%=listAux.get(13).toString()%></td>
                        <td><%=auxExistencias%></td>
                    </tr>





                    <%  } 
                        }
                    %>
                    <tr class="celdaRellenoCenter">
                        <td></td>
                        <td></td>
                        <td>TOTAL DE EXISTENCIAS</td>
                        <td><%=existencias%></td>
                    </tr>
                </table> 
            </div>
        </div> <% } else {
              
        %> 
        <script>
            imprimeError();
        </script>
        
        
          <%  }

        %>
    </body>
    
</html>

<%} else {
    //System.out.println("Usuario No valido para esta pagina");
%>                
<%@ include file="/gui/pageComponents/invalidUser.jsp"%>
<%    }
} else {
    //System.out.println("No se ha encontrado a imix");
%>
<%@ include file="/gui/pageComponents/invalidParameter.jsp"%>
<%        }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<%@ include file="/gui/pageComponents/handleUnExpectedError.jsp"%>
</body>
</html>
<%
        //response.sendRedirect(redirectURL);
    }
%>
