<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ImprimirSoliciud");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Solicitud de Baja</title>
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
                        LinkedList solicitud = QUID.select_Solicitud(WebUtil.decode(session, request.getParameter("idSolicitud")));
                    %>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr>
                            <td rowspan="4">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td rowspan="4" width="40%"><strong>Solicitud de Opinión Técnica para Baja de Bienes Informáticos</strong></td>
                            <td class="celdaSmallLeft">Código</td>
                            <td class="celdaSmallLeft">FO-MP-DAT-01</td>
                            <td rowspan="4">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Revisión</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Fecha</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Página</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" style="text-align: left;">
                        <tr>
                            <td width="15%" class="borde">FECHA</td>
                            <td width="15%" class="borde"><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                            <td width="30%" class="sinBorde"></td>
                            <td width="20%" class="borde">NÚM/SOL/OPINIÓN TEC.</td>
                            <td width="20%" class="borde"><%=solicitud.get(0)%></td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" class="onlyBorde" style="text-align: left;">
                        <tr>
                            <td><div class="celdaGris">SECRETARÍA</div></td>
                            <td><div class="borde">SECRETARÍA DE EDUCACIÓN</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">SUBSECRETARÍA</div></td>
                            <td><div class="borde">SUBSECRETARÍA DE EDUCACIÓN MEDIA SUPERIOR Y SUPERIOR</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DIRECCIÓN GENERAL</div></td>
                            <td><div class="borde">COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DIR/AREA/SUBDIR/DEPTO.</div></td>
                            <td><div class="borde">DIRECCIÓN GENERAL</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">RESONSABLE DE LA SOLICITUD</div></td>
                            <td><div class="borde"><%=solicitud.get(3).toString().toUpperCase()%></div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">CARGO</div></td>
                            <td><div class="borde"><input type="text" size="80" style="border: 0px; text-align: left;font-size: 11px;" value="Escriba aqui el cargo"></div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DOMICILIO</div></td>
                            <td><div class="borde">LIBRAMIENTO JOSE MARIA MORELOS Y PAVÓN 401 SUR LLANO GRANDE METEPEC, ESTADO DE MÉXICO</div></td>
                        </tr>
                        <tr>
                            <td colspan="2"><div class="borde">TÉLEFONO: 01 722 275 80 40 FAX: 01 722 275 80 80 E-MAIL: GEMEDUCT@MAIL.EDOMEX.GOB.MX</div></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: left;">
                        <tr><td colspan="8" class="celdaGris" style="text-align: center;">CARACTERÍSTICAS DE BIENES INFORMÁTICOS</td></tr>
                        <tr class="celdaGris" style="text-align: center;">
                            <td width="12.5%">DESCRIPCIÓN DEL BIEN</td>
                            <td width="12.5%">NÚMERO DE INVENTARIO</td>
                            <td width="12.5%">NÚMERO DE SERIE</td>
                            <td width="12.5%">MARCA</td>
                            <td width="12.5%">MODELO</td>
                            <td width="12.5%">ESTADO ACTUAL DEL BIEN</td>
                            <td width="12.5%">VALOR ESTIMADO</td>
                            <td width="12.5%">MOTIVO DE BAJA</td>
                        </tr>
                        <%
                            it = QUID.select_SolicitudBaja(WebUtil.decode(session, request.getParameter("idSolicitud"))).iterator();
                            int i = 1;
                            while (it.hasNext()) {
                                LinkedList bien = (LinkedList) it.next();
                                if (i % 6 == 0) {
                        %>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: left;">
                        <tr>
                            <td width="17%">OBSERVACIONES</td>
                            <td><%=solicitud.get(5)%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr>
                            <td width="50%">SOLICITANTE<br><br><br></td>
                            <td width="50%">Vo. Bo.<br><br><br></td>
                        </tr>
                        <tr>
                            <td><%=solicitud.get(1).toString().toUpperCase()%><br><input type="text" size="80" style="border: 0px; text-align: center;" value="Escriba aqui el cargo"></td>
                            <td>M.C.P. ABRAHAM MARTINEZ ZAMUDIO<br>DIRECTOR DE ADMINISTRACIÓN Y FINANZAS</td>
                        </tr>
                    </table>
                    <div style="font-size: 10px;"><strong>NOTA: El servidor público que realice la validación del equipo no podrá reemplazar, ni sustraer piezas de los bienes informáticos de la dependencia</strong></div>
                    <table width="92%" border="0" style="text-align: left;">
                        <tr>
                            <td width="50%"><div style="font-size: 9px;">CLAVES PARA EL ESTADO ACTUAL DEL BIEN<br> F:FUNCIONA, R:NO FUNCIONA, NR:NO FUNCIONA REPARABLE</div></td>
                            <td width="50%"><div style="font-size: 9px; text-align: center">CLAVES PARA MOTIVO DE BAJA<br>A:DAÑADO  D:DESUSO  I:IRREPARABLE</div></td>
                        </tr>
                    </table>
                    <p class="break"></p>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr>
                            <td rowspan="4">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/edomex.png"  align="left"  height="105px" width="160px">
                            </td>
                            <td rowspan="4" width="40%"><strong>Solicitud de Opinión Técnica para Baja de Bienes Informáticos</strong></td>
                            <td class="celdaSmallLeft">Código</td>
                            <td class="celdaSmallLeft">FO-MP-DAT-01</td>
                            <td rowspan="4">
                                <img src="<%=PageParameters.getParameter("rsc")%>/img/cecytem2.jpg"  align="center"  height="60px" width="50px">    
                            </td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Revisión</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Fecha</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="celdaSmallLeft">Página</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" style="text-align: left;">
                        <tr>
                            <td width="15%" class="borde">FECHA</td>
                            <td width="15%" class="borde"><%=UTime.calendar2SQLDateFormat(Calendar.getInstance())%></td>
                            <td width="30%" class="sinBorde"></td>
                            <td width="20%" class="borde">NÚM/SOL/OPINIÓN TEC.</td>
                            <td width="20%" class="borde"><%=solicitud.get(0)%></td>
                        </tr>
                    </table>
                    <table width="92%" border="0" align="center" class="onlyBorde" style="text-align: left;">
                        <tr>
                            <td><div class="celdaGris">SECRETARÍA</div></td>
                            <td><div class="borde">SECRETARÍA DE EDUCACIÓN</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">SUBSECRETARÍA</div></td>
                            <td><div class="borde">SUBSECRETARÍA DE EDUCACIÓN MEDIA SUPERIOR Y SUPERIOR</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DIRECCIÓN GENERAL</div></td>
                            <td><div class="borde">COLEGIO DE ESTUDIOS CIENTÍFICOS Y TECNOLÓGICOS DEL ESTADO DE MÉXICO</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DIR/AREA/SUBDIR/DEPTO.</div></td>
                            <td><div class="borde">DIRECCIÓN GENERAL</div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">RESONSABLE DE LA SOLICITUD</div></td>
                            <td><div class="borde"><%=solicitud.get(3).toString().toUpperCase()%></div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">CARGO</div></td>
                            <td><div class="borde"><input type="text" size="80" style="border: 0px; text-align: left;font-size: 11px;" value="Escriba aqui el cargo"></div></td>
                        </tr>
                        <tr>
                            <td><div class="celdaGris">DOMICILIO</div></td>
                            <td><div class="borde">LIBRAMIENTO JOSE MARIA MORELOS Y PAVÓN 401 SUR LLANO GRANDE METEPEC, ESTADO DE MÉXICO</div></td>
                        </tr>
                        <tr>
                            <td colspan="2"><div class="borde">TÉLEFONO: 01 722 275 80 40 FAX: 01 722 275 80 80 E-MAIL: GEMEDUCT@MAIL.EDOMEX.GOB.MX</div></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: left;">
                        <tr><td colspan="8" class="celdaGris" style="text-align: center;">CARACTERÍSTICAS DE BIENES INFORMÁTICOS</td></tr>
                        <tr class="celdaGris" style="text-align: center;">
                            <td width="12.5%">DESCRIPCIÓN DEL BIEN</td>
                            <td width="12.5%">NÚMERO DE INVENTARIO</td>
                            <td width="12.5%">NÚMERO DE SERIE</td>
                            <td width="12.5%">MARCA</td>
                            <td width="12.5%">MODELO</td>
                            <td width="12.5%">ESTADO ACTUAL DEL BIEN</td>
                            <td width="12.5%">VALOR ESTIMADO</td>
                            <td width="12.5%">MOTIVO DE BAJA</td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td><%=bien.get(5)%></td>
                            <td><%=bien.get(13)%></td>
                            <td><%=bien.get(12)%></td>
                            <td><%=bien.get(7)%></td>
                            <td><%=bien.get(9)%></td>
                            <td><%=SystemUtil.getEstatusBien(bien.get(14).toString())%></td>
                            <td>&nbsp;</td>
                            <td><%=SystemUtil.getMotivoBaja(bien.get(16).toString())%></td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: left;">
                        <tr>
                            <td width="17%">OBSERVACIONES</td>
                            <td><%=solicitud.get(5)%></td>
                        </tr>
                    </table>
                    <br>
                    <table width="92%" border="0" align="center" class="borde" rules="all" style="text-align: center;">
                        <tr>
                            <td width="50%">SOLICITANTE<br><br><br></td>
                            <td width="50%">Vo. Bo.<br><br><br></td>
                        </tr>
                        <tr>
                            <td><%=solicitud.get(1).toString().toUpperCase()%><br><input type="text" size="80" style="border: 0px; text-align: center;" value="Escriba aqui el cargo"></td>
                            <td>M.C.P. ABRAHAM MARTINEZ ZAMUDIO<br>DIRECTOR DE ADMINISTRACIÓN Y FINANZAS</td>
                        </tr>
                    </table>
                    <div style="font-size: 10px;"><strong>NOTA: El servidor público que realice la validación del equipo no podrá reemplazar, ni sustraer piezas de los bienes informáticos de la dependencia</strong></div>
                    <table width="92%" border="0" style="text-align: left;">
                        <tr>
                            <td width="50%"><div style="font-size: 9px;">CLAVES PARA EL ESTADO ACTUAL DEL BIEN<br> F:FUNCIONA, R:NO FUNCIONA, NR:NO FUNCIONA REPARABLE</div></td>
                            <td width="50%"><div style="font-size: 9px; text-align: center">CLAVES PARA MOTIVO DE BAJA<br>A:DAÑADO  D:DESUSO  I:IRREPARABLE</div></td>
                        </tr>
                    </table>
                    <p class="break"></p>
                </div>
            </div>
        </div>
    </body>
</html>
<%
} else {
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
