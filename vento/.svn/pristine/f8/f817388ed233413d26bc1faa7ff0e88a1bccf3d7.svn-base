<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <style type="text/css">
            body {
                font-family: Helvetica;
                font-size: 13px;
                color: #000;
            }
            h3 {
                margin: 0px;
                padding: 0px;
            }
            .suggestionsBox {
                position: relative;
                left: 260px;
                margin: 0px 0px 0px 0px;
                width: 375px;
                background-color: #FFF;
                -moz-border-radius: 7px;
                -webkit-border-radius: 7px;
                border: 2px solid #000;
                color: #000;
                font-size: 10px;
            }
            .suggestionList {
                margin: 0px;
                padding: 0px;
            }
            .suggestionList li {
                margin: 0px 0px 3px 0px;
                padding: 3px;
                /*cursor: pointer;*/
            }
            .suggestionList li:hover {
                background-color: #BCDC83;
            }
        </style>
        <script type="text/javascript">
            function buscar() {
                //inputString = document.getElementById('botonEnviarDiv').style.display = 'none';
                inputString = document.getElementById("inputString").value;
                if (inputString.length === 0) {
                    $('#suggestions').hide();
                } else {
                    $.ajax({type: 'POST'
                                , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                , cache: false
                                , async: false
                                , url: '/deo/ajaxFunctions/serachAlumno.jsp'
                                , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&busqueda=' + inputString
                                , success: function(response) {
                            if (response.length > 0) {
                                $('#suggestions').show();
                                $('#autoSuggestionsList').html(response);
                            }
                        }
                    });
                }
            }

            function buscarAlumno(keyCode) {
                if (keyCode === 32 || keyCode === 13) {
                    searchText = document.getElementById("buscarAlumno").value;
                    if (searchText.length === 0) {
                        $('#suggestions').hide();
                    } else {
                        $.ajax({type: 'POST'
                                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                                    , cache: false
                                    , async: false
                                    , url: '/deo/ajaxFunctions/serachAlumno.jsp'
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
                $('#buscarAlumno').val(thisValue);
                $('#idAlumno').attr('value', id);
                //$('#idAlumno').val(id);
                setTimeout("$('#suggestions').hide();", 200);
            }
        </script>
    </head>
    <body>

        <div> <h3><font color="red">Indian States</states></font></h3> <br /> Enter India State Name to see autocomplete
            <input type='text' size="85" value="" name='buscarAlumno' id='buscarAlumno' onkeyup='buscarAlumno(event.keyCode);' onblur='fillSuggestions();' />
        </div>
        <div class="suggestionsBox" id="suggestions" style="display: none;">
            <div class="suggestionList" id="autoSuggestionsList">
            </div>
        </div>
        <form id="nuevoAlumno" name="nuevoAlumno" method="post" action="<%=PageParameters.getParameter("mainController")%>" enctype="application/x-www-form-urlencoded">
            <input type="hidden" id='idAlumno' name="idAlumno" value=""/>
            <input type="submit" value="Enviar">
        </form>
    </body>
</html>
