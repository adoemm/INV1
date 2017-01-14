<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("CentroOpcionesPersonal");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {
                    Iterator it = null;
                    LinkedList listAux = null;
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Vincular Personal</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <script type="text/javascript" language="javascript" charset="utf-8">
            function buscar(keyCode) {
                document.getElementById('cajaDebotones').style.display = 'none';
                if (keyCode === 32 || keyCode === 13 || keyCode === 8) {
                    $('#idPersonal').attr('value', "");
                    searchText = document.getElementById("buscar").value;
                    if (searchText.length === 3) {
                        $('#suggestions').hide();
                        $('#idPersonal').attr('value', "");
                        document.getElementById('cajaDebotones').style.display = 'none';
                    } else {
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: false
                            , url: '/vento/ajaxFunctions/serachPersonal.jsp'
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + searchText
                            , success: function(response) {
                                if (response.length > 0) {
                                    $('#suggestions').show();
                                    $('#autoSuggestionsList').html(response);
                                }
                            }
                        });
                    }
                }
            }

            function fillSuggestions(id, thisValue) {
                if (id.length === 0 || thisValue.length === 0) {
                    document.getElementById('cajaDebotones').style.display = 'none';
                } else {
                    $('#buscar').val(thisValue);
                    $('#idPersonal').attr('value', id);
                    document.getElementById('cajaDebotones').style.display = 'block';

                    setTimeout("$('#suggestions').hide();", 200);
                }
            }

            function createPopup(url, name) {
                newwindow = window.open(
                        url
                        , name
                        , 'width=560,height=560,toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=300'
                        );
                if (window.focus) {
                    newwindow.focus();
                }
            }
            function popupInformacionProfesor() {
                var idPersonal = document.getElementById("idPersonal").value;
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/UpdatePersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPersonal=' + idPersonal
                        , 'Información Personal'
                        , 'width=1000,height=800,toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                        );
                if (window.focus) {
                    newwindow.focus();
                }
            }
            function popupVincularPlantel() {
                var idPersonal = document.getElementById("idPersonal").value;
                newwindow = window.open(
                        '<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/RelacionPersonalPlantel.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&idPersonal=' + idPersonal
                        , 'Vincular Personal'
                        , 'width=1000,height=800,toolbar=0,menubar=0,resizable=1,scrollbars=1,status=1,location=0,directories=0,top=50'
                        );
                if (window.focus) {
                    newwindow.focus();
                }
            }

        </script>
        <script type="text/javascript" language="javascript" charset="utf-8">
            function enviarInfo() {
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: false
                    , url: '<%=PageParameters.getParameter("mainController")%>'
                    , data: $('#vincularPersonal').serialize()
                    , success: function(response) {
                        $('#wrapper').find('#divResult').html(response);
                    }});
            }
        </script>
        <script type="text/javascript" language="javascript" charset="utf-8">
            window.history.forward();
            function noBack() {
                window.history.forward();
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <div class="errors">
                    <p>
                        <em>Los campos con  <strong>*</strong> son necesarios.</em>
                    </p>
                </div>
                <div class="form-container" width="100%">                    
                    <p></p>
                    <table width="100%" height="25px" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="64%" height="25" align="left" valign="top">
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>"> Menú Principal</a> 
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaPersonal.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>" >Catálogo de Personal</a> > Centro de Opciones del Personal
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <fieldset style="margin: 10px 0; padding: 10px; border: #DDD 1px solid;">                       
                        <br>                      
                        <legend style="font-weight: bold; color: #0C1A22;">Buscar Empleado</legend>

                        <label for="buscar">*Buscar Empleado:</label>
                        <input type='text' size="85" value="" name='buscar' id='buscar' onkeyup='buscar(event.keyCode);' onblur='fillSuggestions();' />                        
                        <div class="suggestionsBox" id="suggestions" style="display: none;">
                            <div class="suggestionList" id="autoSuggestionsList">
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>
                        <div id="cajaDebotones" style="display:none;">
                            <input type="hidden" id="idPersonal" name="idPersonal" value=""/>
                            <input type="hidden" name="imix" value="<%=WebUtil.encode(session, UTime.getTimeMilis())%>"/>
                            <input type="hidden" name="FormFrom" value="vincularPersonal"/>
                            <button type="button" onclick="popupInformacionProfesor();">Información Personal</button>
                            <button type="button" onclick="popupVincularPlantel();">Relacionar con el Plantel</button>
                            <form name="vincularPersonal" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded" id="vincularPersonal">                    
                            </form>
                        </div>
                    </fieldset>
                </div>
                <div id="divResult"> 
                </div> 
                <div id="divFoot">
                    <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
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