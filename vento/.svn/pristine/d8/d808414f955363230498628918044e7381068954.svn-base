<%@page import="jspread.core.util.SystemUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ReportBarCode");
                Calendar cal = Calendar.getInstance();
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Código de Barras</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body style="background-color:#ffffff; margin: 0px;padding: 0px;">
        <div align="center" style="width: 21.5cm; height: 27.5cm; margin-left: 1mm;">
                <div style="height:  10mm;float:left; width: 100%"></div>
                <%

                    if (request.getParameter("idBien") != null) {
                        LinkedList bien = QUID.select_Bien(WebUtil.decode(session, request.getParameter("idBien")));
                %>
                <div class="etiqueta">
                    <div class="titulo">
                        <%="PTL. " + bien.get(1).toString().toUpperCase()%>   
                        <%="DTO. "+bien.get(22)%>
                    </div>
                    <img src="<%=PageParameters.getParameter("jspBarcode")%>?stringToBarcode=<%=bien.get(12)%>" alt="Código de Barras" class="barcode">
                    <div class="titulo">
                        <%="INV. "+bien.get(15).toString().toUpperCase()%><br>
                        <%=bien.get(7) + " " + bien.get(9)%>   
                    </div>
                </div>
                <%

                } else if (request.getParameter("idSubcategoria") != null) {
                    String [] encodeSubCategorias=request.getParameterValues("idSubcategoria");
                    String [] subCategorias=new String[encodeSubCategorias.length];
                    for (int i = 0; i < encodeSubCategorias.length; i++) {  
                            subCategorias[i]=WebUtil.decode(session,encodeSubCategorias[i]);
                        }
                    
                    
                    Iterator t = QUID.select_Bien4Depto4Subcategoria(
                            WebUtil.decode(session, request.getParameter("idPlantel")),
                            false, "Baja", false, true,
                            subCategorias,
                            WebUtil.decode(session, request.getParameter("idDepartamento"))).iterator();
                    int i = 1;
                    while (t.hasNext()) {
                        LinkedList bien = (LinkedList) t.next();
                        if (i % 31 == 0) {
                %>
                <p class="break"></p>
                <div style="height:  13mm;float:left; width: 100%"></div>
                <%
                    }
                %>
                <div class="etiqueta">
                    <div class="titulo">
                        <%="PTL. " + bien.get(1).toString().toUpperCase()%>   
                        <%="DTO. "+bien.get(21)%>
                    </div>
                    <img src="<%=PageParameters.getParameter("jspBarcode")%>?stringToBarcode=<%=bien.get(12)%>" alt="Código de Barras" class="barcode">
                    <div class="titulo">
                        <%="INV. "+bien.get(15).toString().toUpperCase()%><br>
                        <%=bien.get(7) + " " + bien.get(9)%>   
                    </div>
                </div>
                <%
                            i += 1;
                        }
                    }

                %>
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
