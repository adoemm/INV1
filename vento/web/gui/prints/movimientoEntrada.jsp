<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteMovimientoEntrada");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Registro de Entrada</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff;">
        <div align="center" style="width: 25cm; height: 28cm; margin: 0 auto;">
            <div>
                <div align="center">
                     <%
                        LinkedList movimiento = QUID.select_Movimiento4ID(
                                WebUtil.decode(session, request.getParameter("idMovimiento")),
                                "", false, false);
                        String nombrePlantel=QUID.select_PlantelXCampo("nombre",movimiento.get(1).toString()).toUpperCase();
                    %>
                    <table width="92%" border="0" align="center">
                        <tr>
                            <td align="left" width="7%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td class="estilo2" width="80%">
                                <p align="center">
                                    <strong>COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</strong>
                                    <br><b><strong>CONTROL DE CONSUMIBLES</strong></b>
                                    <br><b><strong><%=nombrePlantel%></strong></b>
                                    <br><b><strong>REGISTRO DE ENTRADA</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                   
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr  class="celdaRellenoCenter">
                            <td>Folio</td>
                            <td>Referencia</td>    
                            <td>Fecha de Registro</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td><%=movimiento.get(2)%></td>
                            <td><%=movimiento.get(6)%></td>
                            <td><%=movimiento.get(3)%></td>                            
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Proveedor</td>
                            <td class="celdaRellenoCenter">No. Factura</td>
                            <td class="celdaRellenoCenter">Fecha de Factura</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td><%=movimiento.get(14)%></td>
                            <td><%=movimiento.get(5)%></td>
                            <td><%=movimiento.get(18)%></td>
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Tipo Compra</td>
                            <td colspan="2"><%=movimiento.get(16)%></td>
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Surtimiento a</td>
                            <td colspan="2"><%=QUID.select_PlantelXCampo("nombre", movimiento.get(1).toString()).toUpperCase()%></td>
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Motivo</td>
                            <td colspan="2"><%=movimiento.get(12)%></td>
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Observaciones</td>
                            <td colspan="2"><%=movimiento.get(8)%></td>
                        </tr>
                    </table>
                    <br><br>
                    <%
                        Iterator it = QUID.select_MovimientoConsumible(WebUtil.decode(session, request.getParameter("idMovimiento"))).iterator();
                    %>
                    <div  class="firma" style="width:92%;text-align: right;">
                        Fecha de Consulta: <%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>
                    </div>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all">
                        <tr  class="celdaRellenoCenter">
                        <td style="width: 5%;">NP</td>
                        <td style="width: 20%;">Clave</td>
                        <td style="width: 25%;">Descripción</td>
                        <td style="width: 20%;">Medida</td>
                        <td style="width: 10%;">Cantidad</td>
                        <td style="width: 10%;">Precio U.</td>
                        <td style="width: 10%;">Importe</td> 
                        </tr>
                        <%        int i = 1;
                            Double Total = 0.0;
                            String strIVA=movimiento.get(9).toString();
                            while (it.hasNext()) {
                                LinkedList datos = (LinkedList) it.next();
                                Double subtotal = Double.parseDouble(datos.get(23).toString()) * Double.parseDouble(datos.get(24).toString());
                        %>
                        <tr>
                            <td><%=i%></td>
                            <td style="width: 20%;"><%=datos.get(4)%></td>
                            <td style="width: 25%;"><%=datos.get(5)%></td>
                            <td style="width: 20%;"><%=datos.get(22)%></td>
                            <td style="width: 10%;text-align: right;"><%=datos.get(23)%></td>
                            <td style="width: 10%;text-align: right;"><%=datos.get(24)%></td>
                            <td style="width: 10%;text-align: right;"><%=StringUtil.formatDouble2Decimals(subtotal)%></td>
                        </tr>
                        <%
                                i += 1;
                                Total += subtotal;
                            }
                        %> 
                        <tr>
                            <td colspan="6" style="text-align: right"><b>Subtotal</b></td>
                            <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(Total)%></td>
                        </tr>
                        <%
                            if (!strIVA.equals("") && StringUtil.isValidDouble(strIVA)) {
                        %>
                        <tr>
                            <td colspan="6" style="text-align: right"><b>IVA</b></td>
                            <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(strIVA)%></td>
                        </tr>
                        <tr>
                            <td colspan="6" style="text-align: right"><b>Total</b></td>
                            <td style="text-align: right"><%=StringUtil.formatDouble2Decimals(Total + Double.parseDouble(strIVA))%></td>
                        </tr>
                        <%
                            }
                        %>

                    </table>
                    <br><br>
                    <table border="0" width="92%" style="text-align: center;" class="firma">
                        <tr>
                            <td>_______________________________________</td>
                            <td>&nbsp;</td>
                            <td>_______________________________________</td>
                        </tr>
                        <tr>
                             <td>ENTREGÓ/PROVEEDOR</td>
                            <td>&nbsp;</td>
                            <td>RECIBIÓ</td>
                        </tr>
                        <tr>
                            <td><br><br></td>
                            <td></td>
                        </tr>
                         <tr>
                            <td>_______________________________________</td>
                            <td>&nbsp;</td>
                            <td>_______________________________________</td>
                        </tr>
                        <tr>
                            <td><%=nombrePlantel%></td>
                            <td>&nbsp;</td>
                            <td>DEPARTAMENTO DE RECURSOS MATERIALES</td>
                        </tr>
                    </table>
                    <p class="break"></p>
                </div>
            </div>
        </div>
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
