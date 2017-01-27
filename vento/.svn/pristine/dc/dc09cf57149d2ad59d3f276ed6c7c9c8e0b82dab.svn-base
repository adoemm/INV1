<%@page import="java.io.IOException"%>
<%@page import="java.net.InetAddress"%>
<%@page import="jspread.core.util.SystemUtil"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%    try {
        if (fine) {
            if (request.getParameter(WebUtil.encode(session, "imix")) != null) {
                LinkedList<String> access4ThisPage = new LinkedList();
                access4ThisPage.add("subirArchivo");

                LinkedList<String> userAccess = (LinkedList<String>) session.getAttribute("userAccess");
                if (UserUtil.isAValidUser(access4ThisPage, userAccess)) {

%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title>Adjuntar Archivo</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>        
        <jsp:include page='<%=PageParameters.getParameter("styleFormCorrections")%>'/>   
        <jsp:include page='<%= PageParameters.getParameter("ajaxFunctions") + "/tablaCapturaCSS.jsp"%>'/>
        <script type="text/javascript">
            var xhr = new XMLHttpRequest();
            var conecction = null;
            var flag = 1;
            function showButton(idBoton) {
                var boton = document.getElementById(idBoton);
                boton.style.display = "block";

            }
            function hideButton(idBoton) {
                var boton = document.getElementById(idBoton);
                boton.style.display = "none";

            }
            function testConection() {
                var result2 = 0;
                if (flag === 1) {
                    result2 = Ping('<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/PingPage.jsp');
                    if (result2 === 0) {
                        cancelarUpload();
                        alert("Se perdio la conexión con el servidor.");
                    }
                }
                return result2;
            }

            function Ping(recurso) {
                result = 0;
                $.ajax({
                    type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , url: recurso
                    , data: ''
                    , async: false
                    , timeout: 5000
                    , success: function(data) {
                        result = 1;
                    }
                    , error: function() {
                        window.clearInterval(conecction);
                        conecction = null;
                        flag = 0;
                        result = 0;
                    }
                });
                return result;
            }


            function eliminarArchivo(idTableArchivo, mainID,idArchivo,target) {//mainID es el ID del bien o del software
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: true
                    , url: "<%=PageParameters.getParameter("mainController")%>"
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&mainID=' + mainID + '&FormFrom='+target+'&idTableArchivo=' + idTableArchivo+'&idArchivo='+idArchivo
                    , success: function(response) {
                        if (response.indexOf("title: \"Error\"") >= 0) {
                            $('#wrapper').find('#divResult').html(response);
                        } else {
                            $('#wrapper').find('#divDocumentos').html(response);
                        }
                    }});
            }
            function getDocumentos(mainID) {
                $('#wrapper').find('#divDocumentos').html('');
                if (mainID !== null) {
                    $.ajax({type: 'POST'
                        , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                        , cache: false
                        , async: true
                        , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getArchivos.jsp"
                        , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&FormFrom=<%=request.getParameter("FormFrom")%>&mainID=' + mainID
                        , success: function(response) {
                            $('#wrapper').find('#divDocumentos').html(response);
                        }});
                }
            }
            function msg(msg, tipo) {
                $.msgBox({
                    title: "Información",
                    content: msg,
                    type: tipo,
                    buttons: [{value: "Aceptar"}],
                    opacity: 0.75,
                    success: function(result) {
                    }
                });
            }

            function restartForm() {
                var pBar = document.getElementById("progressBar");
                pBar.value = 0;
                document.getElementById('progressNumber').innerHTML = '0%';
                //document.getElementById('divUpload').innerHTML = '<input  type="file" name="fileToUpload" id="fileToUpload" onchange="fileSelected();" size="75"/>';
                //document.getElementById('progressNumber').innerHTML = '0%';
                //document.getElementById('fileName').innerHTML = '';
                //document.getElementById('fileSize').innerHTML = '';
                //document.getElementById('fileType').innerHTML = '';
                //document.getElementById('status').innerHTML = "Seleccione un archivo.";
            }
            function fileSelected() {
                var file = document.getElementById('fileToUpload').files[0];
                if (file) {
                    var fileSize = 0;
                    if (file.size > 1024 * 1024)
                        fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                    else
                        fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
                    document.getElementById('fileName').innerHTML = 'Nombre del archivo: ' + file.name;
                    document.getElementById('fileSize').innerHTML = 'Tamaño del archivo: ' + fileSize;
                    document.getElementById('fileType').innerHTML = 'Tipo de archivo: ' + file.type;
                }
            }
//funcion para subir multiples archivos y formularios con diversos campos
//la funcion encripta los textarea y los input tipo text por cuestiones de acentos y ñ's
            function uploadMultipleFiles(form) {
                var fd = new FormData();
                for (i = 0; i < form.elements.length; i++)
                {
                    if (form.elements[i].type === "file") {
                        fd.append("fileToUpload", form.elements[i].files[0]);
                    } else if (form.elements[i].type === "text"
                            || form.elements[i].type === "textarea") {
                        var valor = "";
                        $.ajax({type: 'POST'
                            , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                            , cache: false
                            , async: true
                            , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getEncodeParam.jsp"
                            , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&param=' + document.getElementById('descripcion').value
                            , success: function(response) {
                                valor = response.toString();
                            }
                        });
                        fd.append(form.elements[i].name, valor);
                    } else {
                        fd.append(form.elements[i].name, form.elements[i].value);
                    }
                }
                xhr.upload.addEventListener("progress", uploadProgress, false);
                xhr.addEventListener("load", uploadComplete, false);
                xhr.upload.addEventListener("error", uploadFailed, false);
                xhr.upload.addEventListener("abort", uploadCanceled, false);
                document.getElementById('status').innerHTML = "Enviando";
                xhr.open("POST", "<%=PageParameters.getParameter("mainController")%>", true);
            <%
                if (Integer.parseInt(PageParameters.getParameter("timeOutToUploadFile").toString()) > 0) {
            %>
                xhr.timeout = <%=PageParameters.getParameter("timeOutToUploadFile")%>;
            <%
                }
            %>
                xhr.ontimeout = function() {
                    alert("Tiempo de espera agotado.");
                    restartForm();
                };
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4) {
                        if (xhr.status === 404) {
                            alert("Recurso no encontrado.");
                            restartForm();
                        }
                    }
                };
                if (navigator.onLine) {
                    var result3 = null;
                    flag = 1;
                    result3 = testConection();
                    if (result3 === 1) {
                        startTestConecction();
                        xhr.send(fd);
                    }
                } else {
                    alert("Sin conexión. Por favor verifique su conexión de red.");
                }
            }
//la funcion encrpta los textarea y los input tipo tex por cuestiones de acentos y ñ's
            function uploadFile() {
                var fd = new FormData();
                fd.append("fileToUpload", document.getElementById('fileToUpload').files[0]);
                fd.append("FormFrom", "<%=request.getParameter("FormFrom")%>");
                fd.append("mainID", document.getElementById('mainID').value);
                fd.append("idTipoArchivo", document.getElementById('idTipoArchivo').value);
                var desc = "";
                $.ajax({type: 'POST'
                    , contentType: 'application/x-www-form-urlencoded;charset=utf-8'
                    , cache: false
                    , async: true
                    , url: "<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("ajaxFunctions")%>/getEncodeParam.jsp"
                    , data: '<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>&param=' + document.getElementById('descripcion').value
                    , success: function(response) {
                        $('#wrapper').find('#divDesc').html(response.toString());
                        desc = response.toString();

                    }});
                fd.append("descripcion", desc);
                //var xhr = new XMLHttpRequest();
                xhr.upload.addEventListener("progress", uploadProgress, false);
                xhr.addEventListener("load", uploadComplete, false);
                xhr.upload.addEventListener("error", uploadFailed, false);
                xhr.upload.addEventListener("abort", uploadCanceled, false);
                document.getElementById('status').innerHTML = "Enviando";
                xhr.open("POST", "<%=PageParameters.getParameter("mainController")%>", true);
            <%
                if (Integer.parseInt(PageParameters.getParameter("timeOutToUploadFile").toString()) > 0) {
            %>
                xhr.timeout = <%=PageParameters.getParameter("timeOutToUploadFile")%>;
            <%
                }
            %>
                xhr.ontimeout = function() {
                    alert("Tiempo de espera agotado.");
                    restartForm();
                };
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === 4) {
                        if (xhr.status === 404) {
                            alert("Recurso no encontrado.");
                            restartForm();
                        }
                    }
                };
                if (navigator.onLine) {
                    var result3 = null;
                    flag = 1;
                    result3 = testConection();
                    if (result3 === 1) {
                        startTestConecction();
                        xhr.send(fd);
                    }
                } else {
                    alert("Sin conexión. Por favor verifique su conexión de red.");
                }
            }

            function uploadProgress(evt) {
                if (evt.lengthComputable) {
                    var percentComplete = Math.round(evt.loaded * 100 / evt.total);
                    var pBar = document.getElementById("progressBar");
                    pBar.value = percentComplete;
                    document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '% Procesando... ';
                }
                else {
                    document.getElementById('progressNumber').innerHTML = ' no es posible calcular. ';
                }
            }

            function uploadComplete(evt) {
                var response = evt.target.responseText;
                document.getElementById('status').innerHTML = "" + response;
                getDocumentos('<%=request.getParameter("mainID")%>');
                $('#wrapper').find('#divResult').html(response);
                restartForm();
                if (response.indexOf("title: \"Error\"") < 0) {
                    document.getElementById('descripcion').value = '';
                    document.getElementById('idTipoArchivo').selectedIndex = "0";
                }
            }

            function uploadFailed(evt) {
                if (xhr.readyState === 4) {
                    if (xhr.status === 0) {
                        alert("Error de red.");
                    }
                } else {
                    alert("Ocurrio un error al subir el archivo.");
                }
                restartForm();
            }

            function uploadCanceled(evt) {
                alert("La operación ha sido cancelada.");
                restartForm();
            }
            function cancelarUpload() {
                xhr.abort();
            }

            window.history.forward();
            function noBack() {
                window.history.forward();
            }
            function startTestConecction() {
                conecction = setInterval(testConection, 5000);
            }

        </script>
    </head>
    <body onload="getDocumentos('<%=request.getParameter("mainID")%>');
          ">
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
                                <a class="NVL" href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, "" + UTime.getTimeMilis())%>"> Menu Principal</a> 
                                <%
                                    if (request.getParameter("FormFrom").equalsIgnoreCase("file4Software")) {
                                %>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaSoftware.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Software</a> 
                                <%
                                } else if (request.getParameter("FormFrom").equalsIgnoreCase("file4Bien")) {
                                %>
                                > <a class="NVL" href="<%=PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui")%>/ConsultaBien.jsp?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, UTime.getTimeMilis())%>">Catálogo de Bienes</a> 
                                <%
                                    }
                                %>
                                > Adjuntar Archivo
                            </td>
                            <td width="36" align="right" valign="top">
                                <script language="JavaScript" src="<%=PageParameters.getParameter("jsRcs")%>/funcionDate.js" type="text/javascript"></script>
                            </td>
                        </tr>                        
                    </table>
                    <div>
                        <form>
                            <fieldset>
                                <legend>Documentación Registrada</legend>  
                                <div style="position: relative;">
                                    <div id="divAyuda" style=" text-align: right;display: none; margin: 0px;padding: 0px;position: absolute; left: 97%;" >
                                        <img  id="botonAyuda"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/help-browser.png"%>" width="36px" height="36px" onclick="document.getElementById('divInfo').style.display = 'block';
            document.getElementById('divAyuda').style.display = 'none'" title="Abrir ayuda">
                                    </div>
                                    <div id="divInfo" class="msginfo" style="display: block;width: 100%;position: relative;" onmouseover="showButton('botonCerrar');" onmouseout="hideButton('botonCerrar');">
                                        <div class="deleteBar" style="margin:0px;padding: 0px;text-align: right;position: absolute; left: 98%;" >
                                            <img  style="margin: 3px;padding: 0px;" align="right" id="botonCerrar"  src="<%=PageParameters.getParameter("imgRsc") + "/icons/delete.png"%>" width="16px" height="16px" onclick="document.getElementById('divInfo').style.display = 'none';
                                                    document.getElementById('divAyuda').style.display = 'block'" title="Cerrar ayuda">
                                        </div>
                                        <p>
                                            &nbsp;Adjunte aquí los archivos requeridos para el bien/artículo.
                                            <br>&nbsp;<span style="font-size: 11px;">*Los arhivos permitidos por el sistema son de tipo:&nbsp;<%=SystemUtil.getPermitedExtensions().toString()%>.</span>
                                        </p>
                                    </div>
                                </div>
                                <div>

                                </div>

                                <div id="divDocumentos"  align="center">
                                    
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div>
                        <form name="filetoUpload" id="filetoUpload" enctype="multipart/form-data" method="post" action="">
                            <input type="hidden" name="mainID" id="mainID" value="<%=request.getParameter("mainID")%>">
                            <input type="hidden" name="FormFrom" id="FormFrom" value="<%=request.getParameter("FormFrom")%>">
                            <fieldset>
                                <legend>Adjuntar Archivo</legend>  
                                <div>
                                    <label for="idTipoArchivo">*Tipo de Documento</label>
                                    <select name="idTipoArchivo" id="idTipoArchivo">
                                        <option value=""></option>
                                        <%
                                            Iterator t = QUID.select_Tipo_Archivo().iterator();
                                            while (t.hasNext()) {
                                                LinkedList datos = datos = (LinkedList) t.next();
                                        %>
                                        <option value="<%=WebUtil.encode(session, datos.get(0))%>"><%=datos.get(1)%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                                <div>
                                    <label for="descripcion">Descripción</label>
                                    <textarea name="descripcion" id="descripcion" cols="40" rows="5"></textarea>
                                </div>
                                <div>
                                    <table border="0" align="left" width="95%">
                                        <tr align="left">
                                            <td><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Document-Open-64.png" title="Abrir Archivo" width="48" height="48" alt="Abrir Archivo"></td>
                                            <td>
                                                <label  for="fileToUpload">Seleccione un archivo:</label>
                                                <div id="divUpload">
                                                    <input  type="file" name="fileToUpload" id="fileToUpload" onchange="fileSelected();" size="75"/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                            <td>
                                                <input type="button" onclick="
                                                        uploadFile();
                                                        return false;" value="Guardar" name="enviar" id="enviar"/>
                                                <a href="<%=PageParameters.getParameter("mainMenu")%>?<%=WebUtil.encode(session, "imix")%>=<%=WebUtil.encode(session, "" + UTime.getTimeMilis())%>">
                                                    <input align="left"type="button" value="Cerrar" name="subirDespues" id="subirDespues"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </table> 
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div>
                        <form name="" method="post" action="" id="">
                            <div>                     
                                <fieldset>
                                    <legend>Status</legend>  
                                    <div>
                                        <table border="0">
                                            <tr> 
                                                <td><img src="<%=PageParameters.getParameter("imgRsc")%>/icons/Gnome-Dialog-Information-64.png" title="Información" width="48" height="48" alt="Información">
                                                </td>
                                                <td>
                                                    <div id="status">
                                                        Seleccione un archivo.
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <p></p>
                                    <p></p>
                                    <div id="fileName"></div>
                                    <div id="fileSize"></div>
                                    <div id="fileType"></div>
                                    <p></p> 
                                    <progress id="progressBar" class="progressBarThin" max="100" value="0" weight ="100%" width="100%"></progress>
                                    <div id="progressNumber"></div>
                                    <div style=" text-align: right;">
                                        <input type="button" id="cancelar" name="cancelar" value="Cancelar" onclick="cancelarUpload();">
                                    </div>
                                </fieldset> 
                            </div>
                        </form>
                    </div>
                    <div id="divResult"> 
                    </div> 
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
<jsp:include page='<%=PageParameters.getParameter("msgUtil") + "/msgNRedirectFull.jsp"%>' flush = 'true'>
    <jsp:param name='title' value='Error' />
    <jsp:param name='msg' value='Intento de acceso Ilegal - Usted no cuenta con el permiso para accesar a esta pagina' />
    <jsp:param name='type' value='error' />
    <jsp:param name='url' value='<%=PageParameters.getParameter("mainMenu")%>' />
</jsp:include>
<%
    }
} else {
    //System.out.println("No se ha encontrado a imix");
%>
<jsp:include page='<%=PageParameters.getParameter("msgUtil") + "/msgNRedirectFull.jsp"%>' flush = 'true'>
    <jsp:param name='title' value='Error' />
    <jsp:param name='msg' value='Peticion invalida' />
    <jsp:param name='type' value='error' />
    <jsp:param name='url' value='<%=PageParameters.getParameter("mainMenu")%>' />
</jsp:include>
<%
        }
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<jsp:forward page='<%=PageParameters.getParameter("msgUtil") + "/msgNRedirectFull.jsp"%>'>
    <jsp:param name='type' value='error'/>
    <jsp:param name='title' value='Error Grave'/>
    <jsp:param name='msg' value='Lo sentimos la pagina ha tenido un error grave'/>
    <jsp:param name='url' value='<%=PageParameters.getParameter("errorURL")%>'/>
</jsp:forward>
</body>
</html>
<%
        //response.sendRedirect(redirectURL);
    }
%>