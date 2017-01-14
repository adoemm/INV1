<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReporteMovimientoSalida");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Registro de Salida</title>
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
                        LinkedList movimiento = QUID.select_MovimientoSalida(WebUtil.decode(session, request.getParameter("idMovimiento")));
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
                                    <br><b><strong>REGISTRO DE SALIDA</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <br><br>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all" style="font-size: 10px;">
                        <tr  class="celdaRellenoCenter">
                            <td>Folio</td>
                            <td>Número de Turno</td>
                            <td>Fecha de Salida</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td><%=movimiento.get(2)%></td>
                            <td><%=movimiento.get(25)%></td>
                            <td><%=movimiento.get(3)%></td>                            
                        </tr>
                        <tr>
                            <td class="celdaRellenoCenter">Surtimiento a</td>
                            <td class="celdaRellenoCenter" colspan="2">Area/Depto.</td>
                        </tr>
                        <tr style="text-align: center;">
                            <td><%=movimiento.get(22)%></td>
                            <td colspan="2"><%=movimiento.get(18)%></td>
                        </tr>
                        <tr>
                            <td colspan="3" class="celdaRellenoCenter">Motivo</td>
                        </tr>
                        <tr>
                            <td colspan="3">&nbsp;<%=movimiento.get(11)%></td>
                        </tr>
                    </table>
                    <%
                        Iterator it = QUID.select_MovimientoConsumible(WebUtil.decode(session, request.getParameter("idMovimiento"))).iterator();
                    %>
                    <div  class="firma" style="width:92%;text-align: right;">
                        Fecha de Consulta: <%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%>
                    </div>
                    <table width="92%" border="0" align="center" class="onlyBorde" rules="all"  style="font-size: 10px;">
                        <tr  class="celdaRellenoCenter">
                            <td style="width: 5%;">NP</td>
                            <td style="width: 20%;">Clave</td>
                            <td style="width: 25%;">Descripción</td>
                            <td style="width: 20%;">Medida</td>
                            <td style="width: 10%;">Cantidad</td>
                            <td style="width: 10%;">Precio Unitario</td>
                            <td style="width: 10%;">Costo Total</td>
                        </tr>
                        <%
                            int i = 1;
                            Double Total = 0.0;
                            while (it.hasNext()) {
                                LinkedList datos = (LinkedList) it.next();
                                Double subtotal = Double.parseDouble(datos.get(23).toString()) * Double.parseDouble(datos.get(24).toString());
                        %>
                        <tr>
                            <td style="text-align: center;"><%=i%></td>
                            <td style="width: 20%;"><%=datos.get(4)%></td>
                            <td style="width: 25%;"><%=datos.get(5)%></td>
                            <td style="width: 20%;"><%=datos.get(22)%></td>
                            <td style="width: 10%;text-align: right;"><%=datos.get(23)%></td>
                            <td style="width: 10%;text-align: right;">$<%=datos.get(24)%></td>
                            <td style="width: 10%;text-align: right;">$<%=StringUtil.formatDouble2Decimals(subtotal)%></td>
                        </tr>
                        <%
                                i += 1;
                                Total += subtotal;
                            }
                        %> 
                        <tr style="background-color: #CCCCCC; font-weight: bold;text-align: right;">
                            <td colspan="5"></td>
                            <td>TOTAL</td>
                            <td>$<%=StringUtil.formatDouble2Decimals(Total)%></td>
                        </tr>
                        <tr>
                            <td colspan="7" class="celdaRellenoCenter">Observaciones</td>
                        </tr>
                        <tr>
                            <td colspan="7">&nbsp;<%=movimiento.get(7)%></td>
                        </tr>
                    </table>
                        
                    <br><br>
                    <table border="0" width="92%" style="text-align: center;" class="firma">
                        <tr>
                            <td><input type="text" size="35" style="border:0px;border-bottom:solid  1px #000000;"></td>
                            <td>&nbsp;</td>
                            <td><span style="text-decoration: underline;"><%=movimiento.get(21).toString().equalsIgnoreCase("OTRO USUARIO")?"<input type=\"text\" size=\"35\" style=\"border:0px;border-bottom:solid  1px #000000;\">":movimiento.get(19)+" "+movimiento.get(20)+" "+movimiento.get(21)%></span></td>
                        </tr>
                        <tr>
                            <td>ENTREGÓ/ELABORÓ</td>
                            <td>&nbsp;</td>
                            <td>RECIBIÓ</td>
                        </tr>
                         <tr>
                            <td colspan="3"><br></td>
                        </tr>
                        <tr>
                            <td><input type="text" size="35" style="border:0px;border-bottom:solid  1px #000000;"></td>
                            <td>&nbsp;</td>
                            <td><input type="text" size="35" style="border:0px;border-bottom:solid  1px #000000;"></td>
                        </tr>
                        <tr>
                            <td><%=nombrePlantel%></td>
                            <td>&nbsp;</td>
                            <!--<td>DEPARTAMENTO DE RECURSOS MATERIALES Y SERVICIOS GENERALES</td>-->
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
