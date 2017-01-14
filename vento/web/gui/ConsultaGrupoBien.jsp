<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("ConsultaGrupoBien");
                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Bienes de la Agrupación</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
        <%@ include file="/gui/pageComponents/dataTablesFullFunctionParameters.jsp"%>
    </head>
    <body  onload="populate();">
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <div class="form-container" width="100%">
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaGrupo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>">Catálogo de Agrupaciones</a>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateGrupo.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">Editar Agrupación</a>
                                > Bienes de la Agrupación
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <br>
                    <br>
                    <div id="contenent_info" >
                        <form name="usuario" method="post" action="" enctype="application/x-www-form-urlencoded" id="form">
                            <fieldset>
                                <legend>Bienes de la Agrupación</legend>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="example">
                                    <thead>
                                        <tr>
                                            <th>Categoria</th>
                                            <th>Subcategoria</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>No. Serie</th>
                                            <th>No. Inventario</th>
                                            <th>Estatus</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" class="display" id="exampleFilters">
                                    <tfoot>
                                        <tr>
                                            <th>Categoria</th>
                                            <th>Subcategoria</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>No. Serie</th>
                                            <th>No. Inventario</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </tfoot>
                                    <thead>
                                        <tr>
                                            <th>Categoria</th>
                                            <th>Subcategoria</th>
                                            <th>Marca</th>
                                            <th>Modelo</th>
                                            <th>No. Serie</th>
                                            <th>No. Inventario</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </thead>
                                </table>
                            </fieldset>
                        </form>
                    </div>  
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPlantel=<%=request.getParameter("idPlantel")%>&idDepartamento=<%=request.getParameter("idDepartamento")%>&idPdg=<%=request.getParameter("idPdg")%>" target="_blank">
                        <button>Nuevo Bien</button>
                    </a> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertGrupoBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Agregar Bien Existente</button>
                    </a> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertPBTFull.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Agregar Proveedor a Todos</button>
                    </a> 
                    <a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/InsertGarantiaFull.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupo=<%=request.getParameter("idGrupo")%>&idPdg=<%=request.getParameter("idPdg")%>&idPlantel=<%=request.getParameter("idPlantel")%>">
                        <button>Agregar Garantía a Todos</button>
                    </a> 
                </div>
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
                </div> 
                <script type="text/javascript" language="javascript" charset="utf-8">
        function populate()
        {
            var t = $('#example').dataTable();
            t.fnClearTable();
            var data = new Array();
            var ids = new Array();
            var cells = new Array();

                    <%
                        int cont = 0;
                        it = QUID.select_Bien4Grupo(WebUtil.decode(session, request.getParameter("idGrupo")),
                                "Baja",false,true).iterator();
                        while (it.hasNext()) {
                            listAux = (LinkedList) it.next();
                    %>
            ids[<%=cont%>] = '<%=WebUtil.encode(session, listAux.get(0))%>';
            cells[0] = '<%=listAux.get(9).toString().replaceAll("'", "&#39;")%>';
            cells[1] = '<%=listAux.get(10).toString().replaceAll("'", "&#39;")%>';
            cells[2] = '<%=listAux.get(7).toString().replaceAll("'", "&#39;")%>';
            cells[3] = '<%=listAux.get(8).toString().replaceAll("'", "&#39;")%>';
            cells[4] = '<%=listAux.get(2).toString().replaceAll("'", "&#39;")%>';
            cells[5] = '<%=listAux.get(5).toString().replaceAll("'", "&#39;")%>';
            cells[6] = '<%=listAux.get(6).toString().replaceAll("'", "&#39;")%>';
            cells[7] = '<a href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdateBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idBien=<%=WebUtil.encode(session, listAux.get(0))%>" target="_blank"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Accessories-Text-Editor-64.png" title="Actualizar" width="22" height="23" alt="Actualizar"></a>';
            cells[8] = '<a href="<%=PageParameters.getParameter("mainController")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idGrupoBien=<%=WebUtil.encode(session, listAux.get(13))%>&idGrupo=<%=request.getParameter("idGrupo")%>&FormFrom=deleteGrupoBien&idPlantel=<%=request.getParameter("idPlantel")%>"><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Process-Stop-64.png" title="Eliminar del Grupo" width="22" height="23" alt="Eliminar del Grupo"></a>';
            data[<%=cont%>] = t.fnAddData(cells, false);
                    <%
                            cont++;
                        }
                    %>

            $("tfoot th").each(function(i) {
                this.innerHTML = fnCreateSelect(t.fnGetColumnData(i));
                $('select', this).change(function() {
                    t.fnFilter($(this).val(), i);
                });
            });
            t.fnDraw();
            document.getElementById('contenent_info').style.display = 'block';
        }
                </script>
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