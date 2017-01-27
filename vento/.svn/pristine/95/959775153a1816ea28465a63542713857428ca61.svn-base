<%@page pageEncoding="utf-8" language="java"%>
<%@page import="jspread.core.util.security.EDP"%>
<%@page import="jspread.core.db.QUID"%>
<%@page import="jspread.core.util.PageParameters"%>
<%@page import="systemSettings.SystemSettings"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Iterator"%>
<%@page import="jspread.core.util.WebUtil"%>
<%@page import="jspread.core.util.StringUtil"%>
<%@page import="jspread.core.util.UTime"%>
<%@page import="jspread.core.util.UserUtil"%>


<jsp:useBean id="QUID" scope="page" class="jspread.core.db.QUID"/>

<%
    try {
        session = request.getSession(true);
        QUID.setRequest(request);

        if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
            LinkedList<String> access4ThisPage = new LinkedList();
            access4ThisPage.add("ReporteConteoBien");

            LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
            if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%            String date_num = "Conteo4Departamento.xls";
            response.setContentType("application/vnd.ms-excel; encoding=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + date_num + "\"");
        %>
    </head>
    <body>
        <%
            try{
            Iterator planteles=QUID.select_Planteles("", true).iterator();
            while(planteles.hasNext()){
                LinkedList ptl=(LinkedList)planteles.next();
                
            
//            String idPlantel = WebUtil.decode(session, request.getParameter("idPlantel"));            
              String idPlantel=ptl.get(0).toString();
              System.out.println("plantel:"+idPlantel);
            String[] departamentos = request.getParameterValues("idDepartamento");
            String[] subCategorias = request.getParameterValues("idSubcategoria");
            LinkedList idSubCategorias = new LinkedList();
            for (int i = 0; i < subCategorias.length; i++) {
                idSubCategorias.add(WebUtil.decode(session, subCategorias[i]));
            }
            LinkedList modelos4Consulta = new LinkedList();
            Iterator t = QUID.select_Modelo().iterator();

            while (t.hasNext()) {
                LinkedList datos = (LinkedList) t.next();
                boolean haveBienes = false;
                for (int i = 0; i < departamentos.length && !haveBienes; i++) {

                    if (!QUID.select_BienXID_ModeloXSubCategoriaXDepartamento(
                            datos.get(0).toString(),
                            idSubCategorias,
                            idPlantel,
                            WebUtil.decode(session, departamentos[i]),
                            "Baja",
                            false).isEmpty()) {
                        modelos4Consulta.add(datos);
                        haveBienes = true;
                    }
                }

            }
            int[] totales = new int[modelos4Consulta.size()];
            LinkedList datosPlantel = QUID.select_PlantelXID(Integer.parseInt(idPlantel));
        %>
        <table border="0" width="100%">
            <tr>
                <td>PLANTEL: <%=datosPlantel.get(1).toString().toUpperCase()%></td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr>
                <td>MARCA/MODELO</td>
                <%
                    t = modelos4Consulta.iterator();
                    while (t.hasNext()) {
                        LinkedList datos = (LinkedList) t.next();
                %>
                <td width="20%"><%=datos.get(2) + "/" + datos.get(5)%></td>
                <%
                    }
                %>
                <td>TOTAL</td>
            </tr>

            <%
                for (int i = 0; i < departamentos.length; i++) {
                    String idDepartamento = WebUtil.decode(session, departamentos[i]);
            %>
            <tr>
                <td><%=QUID.select_DepartamentoPlantel(idDepartamento).get(3)%></td>

                <%
                    t = modelos4Consulta.iterator();
                    int total4Depto = 0;
                    int currentModelo = 0;
                    while (t.hasNext()) {
                        LinkedList datos = (LinkedList) t.next();
                        int modelosXDepto = 0;
                        modelosXDepto = QUID.select_BienXID_ModeloXSubCategoriaXDepartamento(
                                datos.get(0).toString(),
                                idSubCategorias,
                                idPlantel,
                                idDepartamento,
                                "Baja",
                                false).size();
                        total4Depto += modelosXDepto;
                        totales[currentModelo] += modelosXDepto;
                        currentModelo += 1;

                %>
                <td <%=modelosXDepto != 0 ? "style=\"background-color:#CCCCCC;\"" : ""%>><%=modelosXDepto%></td>
                <%
                    }
                %>
                <td><%=total4Depto%></td>
            </tr>
            <%
                }
            %>
            <tr style="background-color:#CCCCCC; ">
                <td>TOTALES</td>
                <%
                    int sumaTotal = 0;
                    for (int i = 0; i < totales.length; i++) {
                        sumaTotal += totales[i];
                %>
                <td><%=totales[i]%></td>
                <%
                    }
                %>
                <td><%=sumaTotal%></td>
            </tr>
        </table>
            <%
            }
            }catch(Exception e){
                System.out.println("error: "+e.getMessage());
            }
            %>
    </body> 
</html>
<%            } else {
                out.print("Usted NO tiene permios para realizar esta acción");
            }
        } else {
            out.print("Petición invalida");
        }
    } catch (Exception ex) {
        out.print("Ocurrio un error");
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
%>