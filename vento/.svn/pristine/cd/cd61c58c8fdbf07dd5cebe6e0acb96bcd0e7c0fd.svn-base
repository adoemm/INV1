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
        <title>Registro de Salida Por fecha</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function imprimeError(form) {
                $.msgBox({
                    title: "Error"
                    , content: "No hay reportes entre estas fechas"
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
    <body style="background-color:#ffffff;" >
        <%
            String fechaInicio, fechaFin;
            fechaInicio = request.getParameter("fechaInicio");
            fechaFin = request.getParameter("fechaFin");
            int idPlantel =0;
            Iterator movimientosPlanteles = null;
            String auxPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));
            if (!auxPlantel.equals("Todos")) {
                idPlantel = Integer.parseInt(auxPlantel);
                String nomPlantel = QUID.select_Plantel(idPlantel);
                int nRegistros = QUID.select_MovimientoxPlantelxFechas(2, fechaInicio, fechaFin, idPlantel);
                if (nRegistros > 0) {
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
                                    <br><b><strong><%=nomPlantel%></strong></b>
                                    <br><b><strong>REGISTRO DE SALIDA POR FECHAS</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <hr>
                    <table width="92%" border="0" align="center">
                        <tr >

                            <td align="center"><p><strong>Fecha inicial de Elaboración: <%=fechaInicio%></strong></p></td>
                            <td align="center"><p><strong>Fecha final de Elaboración: <%=fechaFin%></strong></p></td>
                        </tr>
                    </table>
                    <hr>
                </div>
                   
                <%
                    LinkedList movimientos = QUID.select_MovimientoxPlantelxFechasxTipo(2, fechaInicio, fechaFin, idPlantel);

                    for (int i = movimientos.size()-1; -1<i; i--) {
                        String mov = movimientos.get(i).toString();
                        LinkedList movimiento = QUID.select_MovimientoSalida(mov);
                        
                %>
                <br>
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
                    Iterator it = QUID.select_MovimientoConsumible(mov).iterator();
                %>
                <p></p>
                <table width="92%" border="0" align="center" class="onlyBorde" rules="all"  style="font-size: 10px;">
                        <tr  class="celdaRellenoCenter">
                            <td style="width: 5%;">N°</td>
                            <td style="width: 8%;">Clave</td>
                            <td style="width: 25%;">Descripción</td>
                            <td style="width: 20%;">Medida</td>
                            <td style="width: 10%;">Cantidad</td>
                            <td style="width: 10%;">Precio Unitario</td>
                            <td style="width: 10%;">Costo Total</td>
                        </tr>
                        <%
                            int in = 1;
                            Double Total = 0.0;
                            while (it.hasNext()) {
                                LinkedList datos = (LinkedList) it.next();
                                Double subtotal = Double.parseDouble(datos.get(23).toString()) * Double.parseDouble(datos.get(24).toString());
                        %>
                        <tr style="text-align: center">
                            <td style="text-align: center;"><%=in%></td>
                            <td style="width: 8%;"><%=datos.get(4)%></td>
                            <td style="width: 25%;"><%=datos.get(5)%></td>
                            <td style="width: 20%;"><%=datos.get(22)%></td>
                            <td style="width: 10%;text-align: right;"><%=datos.get(23)%></td>
                            <td style="width: 10%;text-align: right;">$<%=datos.get(24)%></td>
                            <td style="width: 10%;text-align: right;">$<%=StringUtil.formatDouble2Decimals(subtotal)%></td>
                        </tr>
                        <%
                                in += 1;
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
                       <br/><br/>

                <% } %>
            </div>
        </div>
        <% } else {
              
        %> 
        <script>
            imprimeError();
        </script>
        
        
          <%  }
            }else {

            String enunciado = "REPORTE GENERAL DE TODOS LOS PLANTELES";
            movimientosPlanteles = QUID.select_PlantelxMovimientoxfechas(2, fechaInicio, fechaFin).iterator();
             if(movimientosPlanteles.hasNext()){
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
                                    <br><b><strong><%=enunciado%></strong></b>
                                    <br><b><strong>REGISTRO DE SALIDA POR FECHAS</strong></b>
                                </p></td>
                            <td align="right" width="13%">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                    </table>
                    <hr>
                    <table width="92%" border="0" align="center">
                        <tr >

                            <td align="center"><p><strong>Fecha inicial de Elaboración: <%=fechaInicio%></strong></p></td>
                            <td align="center"><p><strong>Fecha final de Elaboración: <%=fechaFin%></strong></p></td>
                        </tr>
                    </table>
                    <hr>
                </div>
                   <%
                    while (movimientosPlanteles.hasNext()) {
                        LinkedList rowPlantel = (LinkedList) movimientosPlanteles.next();
                        int numPlantel = Integer.parseInt(rowPlantel.get(0).toString());
                        String nomPlantel = rowPlantel.get(1).toString();
                %>
                <div align="center">
                    <h1>Reportes del Plantel: <%=nomPlantel%></h1>
                </div>
                <%
                    int nRegistros = QUID.select_MovimientoxPlantelxFechas(2, fechaInicio, fechaFin, numPlantel);
                    if (nRegistros > 0) {
                        LinkedList movimientos = QUID.select_MovimientoxPlantelxFechasxTipo(2, fechaInicio, fechaFin, numPlantel);

                    for (int i = movimientos.size()-1; -1<i; i--) {
                        String mov = movimientos.get(i).toString();
                        LinkedList movimiento = QUID.select_MovimientoSalida(mov);
                        
                %>
                <br>
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
                    Iterator it = QUID.select_MovimientoConsumible(mov).iterator();
                %>
                <p></p>
                <table width="92%" border="0" align="center" class="onlyBorde" rules="all"  style="font-size: 10px;">
                        <tr  class="celdaRellenoCenter">
                            <td style="width: 5%;">N°</td>
                            <td style="width: 8%;">Clave</td>
                            <td style="width: 25%;">Descripción</td>
                            <td style="width: 20%;">Medida</td>
                            <td style="width: 10%;">Cantidad</td>
                            <td style="width: 10%;">Precio Unitario</td>
                            <td style="width: 10%;">Costo Total</td>
                        </tr>
                        <%
                            int in = 1;
                            Double Total = 0.0;
                            while (it.hasNext()) {
                                LinkedList datos = (LinkedList) it.next();
                                Double subtotal = Double.parseDouble(datos.get(23).toString()) * Double.parseDouble(datos.get(24).toString());
                        %>
                        <tr style="text-align: center">
                            <td style="text-align: center;"><%=in%></td>
                            <td style="width: 8%;"><%=datos.get(4)%></td>
                            <td style="width: 25%;"><%=datos.get(5)%></td>
                            <td style="width: 20%;"><%=datos.get(22)%></td>
                            <td style="width: 10%;text-align: right;"><%=datos.get(23)%></td>
                            <td style="width: 10%;text-align: right;">$<%=datos.get(24)%></td>
                            <td style="width: 10%;text-align: right;">$<%=StringUtil.formatDouble2Decimals(subtotal)%></td>
                        </tr>
                        <%
                                in += 1;
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
                       <br/><br/>

                <% } %>
          
        <% } else {
              
        %> 
        <script>
            imprimeError();
        </script>
        
        
          <%  }
          
                        }

}else
{   
%> 
                <script>
                    imprimeError();
                </script>
                <%
}


                    }
  %> 

                  
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
