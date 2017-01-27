<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@ include file="/gui/pageComponents/globalSettings.jsp"%>
<%
    try {
        if (fine) {
%>
<!DOCTYPE html>
<html lang="<%=PageParameters.getParameter("Content-Language")%>">
    <head>
        <title></title>
        <%
            String url = PageParameters.getParameter("globalLibs");
            //System.out.println("URL: " + url);
        %>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script src="<%=PageParameters.getParameter("jsRcs")%>/jshashtable-2.1.js" type="text/javascript"></script>

        <script type="text/javascript" charset="utf-8">
            $(document).ready(function() {
                var indiFiltering = $('#example').dataTable({
                    "bJQueryUI": true,
                    "bAutoWidth": false,
                    //"bScrollInfinite": false,
                    //"sScrollY": "800px",
                    //"sScrollX": "100%",
                    //"sScrollXInner": "365%",
                    "bScrollCollapse": true,
                    "bPaginate": true,
                    "sPaginationType": "full_numbers",
                    "bProcessing": true,
                    "bFilter": true,
                    "bSort": true,
                    "oLanguage": {
                        "sProcessing": "Procesando...",
                        "sLengthMenu": "Mostrar _MENU_ registros",
                        "sZeroRecords": "No se encontraron resultados",
                        "sEmptyTable": "Ningún dato disponible en esta tabla",
                        "sInfo": "Registros del _START_ al _END_ de un total de _TOTAL_ registros",
                        "sInfoEmpty": "Registros del 0 al 0 de un total de 0 registros",
                        "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
                        "sInfoPostFix": "",
                        "sSearch": "Buscar:",
                        "sUrl": "",
                        "sInfoThousands": ",",
                        "sLoadingRecords": "Cargando...",
                        "oPaginate": {
                            "sFirst": "Primero",
                            "sLast": "Último",
                            "sNext": "Siguiente",
                            "sPrevious": "Anterior"
                        },
                        "oAria": {
                            "sSortAscending": ": Activar para ordenar la columna de manera ascendente",
                            "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                        }
                    },
                    /*
                     "fnDrawCallback": function(oSettings) {
                     // Need to redo the counters if filtered or sorted 
                     if (oSettings.bSorted || oSettings.bFiltered) {
                     for (var i = 0, iLen = oSettings.aiDisplay.length; i < iLen; i++) {
                     var nTr = oSettings.aoData[ oSettings.aiDisplay[i] ].nTr;
                     
                     // Update the index column and do so without redrawing the table
                     this.fnUpdate(i + 1, nTr, 0, false, false);
                     }
                     }
                     },
                     */
                    "fnInitComplete": function() {
                        this.fnAdjustColumnSizing();
                    }
                });
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                var oTable;
                $('#form').submit(function() {
                    var sData = oTable.$('input').serialize();
                    alert("The following data would have been submitted to the server: \n\n" + sData);
                    return false;
                });
                oTable = $('#example').dataTable();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
                /* Add a select menu for each TH element in the table footer */
                $("tfoot th").each(function(i) {
                    this.innerHTML = fnCreateSelect(indiFiltering.fnGetColumnData(i));
                    $('select', this).change(function() {
                        indiFiltering.fnFilter($(this).val(), i);
                    });
                });
                //$(window).bind('resize', function () {
                //    oTable.fnAdjustColumnSizing();
                //} );

                $('#target').click(function() {
                    oTable.fnAdjustColumnSizing();
                });
            });
            (function($) {
                /*
                 * Function: fnGetColumnData
                 * Purpose:  Return an array of table values from a particular column.
                 * Returns:  array string: 1d data array 
                 * Inputs:   object:oSettings - dataTable settings object. This is always the last argument past to the function
                 *           int:iColumn - the id of the column to extract the data from
                 *           bool:bUnique - optional - if set to false duplicated values are not filtered out
                 *           bool:bFiltered - optional - if set to false all the table data is used (not only the filtered)
                 *           bool:bIgnoreEmpty - optional - if set to false empty values are not filtered from the result array
                 * Author:   Benedikt Forchhammer <b.forchhammer /AT\ mind2.de>
                 */
                $.fn.dataTableExt.oApi.fnGetColumnData = function(oSettings, iColumn, bUnique, bFiltered, bIgnoreEmpty) {
                    // check that we have a column id
                    if (typeof iColumn == "undefined")
                        return new Array();
                    // by default we only want unique data
                    if (typeof bUnique == "undefined")
                        bUnique = true;
                    // by default we do want to only look at filtered data
                    if (typeof bFiltered == "undefined")
                        bFiltered = true;
                    // by default we do not want to include empty values
                    if (typeof bIgnoreEmpty == "undefined")
                        bIgnoreEmpty = true;
                    // list of rows which we're going to loop through
                    var aiRows;
                    // use only filtered rows
                    if (bFiltered == true)
                        aiRows = oSettings.aiDisplay;
                    // use all rows
                    else
                        aiRows = oSettings.aiDisplayMaster; // all row numbers

                    // set up data array	
                    var asResultData = new Array();
                    for (var i = 0, c = aiRows.length; i < c; i++) {
                        iRow = aiRows[i];
                        var aData = this.fnGetData(iRow);
                        var sValue = aData[iColumn];
                        // ignore empty values?
                        if (bIgnoreEmpty == true && sValue.length == 0)
                            continue;
                        // ignore unique values?
                        else if (bUnique == true && jQuery.inArray(sValue, asResultData) > -1)
                            continue;
                        // else push the value onto the result data array
                        else
                            asResultData.push(sValue);
                    }

                    return asResultData;
                }
            }(jQuery));
            function fnCreateSelect(aData)
            {
                var r = '<select width="100" style="width: 100px"><option value=""></option>', i, iLen = aData.length;
                for (i = 0; i < iLen; i++)
                {
                    r += '<option value="' + aData[i] + '">' + aData[i] + '</option>';
                }
                return r + '</select>';
            }

            function fnShowHide(iCol, button)
            {
                var contAux = 0;
                var newName;
                var buttonNameAux = document.getElementById(button.id).value;
                //alert(""+buttonNameAux);
                var buttonName = buttonNameAux.toString().split(" ");
                //alert(""+buttonName[0]);
                if (buttonName[0] == "Ver" || buttonName[0] == "ver") {
                    contAux++;
                    newName = "Ocultar";
                    while (contAux < buttonName.length) {
                        newName = newName + " " + buttonName[contAux];
                        contAux++;
                    }
                    document.getElementById(button.id).value = newName;
                } else {
                    contAux++;
                    newName = "Ver";
                    while (contAux < buttonName.length) {
                        newName = newName + " " + buttonName[contAux];
                        contAux++;
                    }
                    document.getElementById(button.id).value = newName;
                }

                /* Get the DataTables object again - this is not a recreation, just a get of the object */
                var hideShowCol = $('#example').dataTable();
                var bVis = hideShowCol.fnSettings().aoColumns[iCol].bVisible;
                hideShowCol.fnSetColumnVis(iCol, bVis ? false : true);
            }
        </script>

        <script>
            function initGlobalVars() {
                //map = new Map();  //creates an "in-memory" map
                //var map = new Map("storageId");  //cre
                //alert(map['map_name_1']);
                map = new Hashtable();
            }

            function add(obj, value) {
                //alert(obj);
                //alert(value);
                //alert(obj.checked);
                //var color = {}; // unique object instance
                //var shape = {}; // unique object instance
                //map.put(color, "blue");
                //map.put(shape, "round");
                //console.log("Item is", map.get(color), "and", map.get(shape));

                if (obj.checked === true) {
                    map.put(value, value);
                    //console.log(map.get(value));
                } else {
                    map.remove(value);
                }
                //alert(map.size());
                //alert(map.keys());
            }

            function selam() {
                var cont = 0;
                var keys = map.keys();
                while (cont < map.size()) {
                    //alert("key: " + keys[cont]);
                    $('#FormBox').append(
                            $(document.createElement('input'))
                            //.attr('type', 'hidden')
                            .attr('id', 'selected' + keys[cont])
                            .attr('name', 'selected' + keys[cont])
                            .attr('value', keys[cont])
                            .attr('class', 'search')
                            //.attr('type', 'somecalculatedvalue')
                            );
                    cont++;
                }
                $('#FormBox').submit();
                /*
                 while (cont < map.size()) {
                 var element = "'#selected" + keys[cont] + "'";
                 alert(element);
                 $(element).remove();
                 cont++;
                 }
                 */
            }

            $(document).ready(function() {
                $('#consultar').click(function() {
                    $(this).target = "_blank";
                    var params = "";
                    var keys = map.keys();
                    var cont = 0;
                    url = "/copalli/copalli";
                    url = url + "?";
                    url = url + "formFrom=sintomas";
                    url = url + "&" + "formFrom=sintomas";
                    while (cont < map.size()) {
                        //alert("key: " + keys[cont]);
                        url = url + "&" + "selected" + keys[cont] + "=" + keys[cont] + "";
                        cont++;
                    }
                    window.open(url);
                    return false;
                });
            });

            function borrarID10() {
                var elementId = "selected10";

                //var element = "'#selected10'";
                //$('#selected10').remove();
                //
                //$element = $('#selected10');
                //$($element).remove();
                $("input[id=" + elementId + "]").remove();
            }
        </script>
        <script>
            $(document).ready(function() {
                $('#FormBox').submit(function() {
                    alert('hii');
                    var cont = 0;
                    var keys = map.keys();
                    while (cont < map.size()) {
                        var elementId = 'selected' + keys[cont];
                        alert(elementId);
                        $("input[id=" + elementId + "]").remove();
                        cont++;
                    }

                    //var d = document.getElementById('selected10');
                    //d.removeChild();
                });
            })
        </script>

        <!--
        <style>
            label { position: absolute; text-align:right; width:230px; }
            input, textarea, select { margin-left: 240px; }
            label.check, label.radio { position:relative; text-align:left; }
        </style> 
        -->
    </head>
    <body>
        <div id="wrapper">
            <div id="divBody">
                <jsp:include page='<%=("" + PageParameters.getParameter("logo"))%>' />
                <div id="barMenu">
                    <jsp:include page='<%=(PageParameters.getParameter("barMenu"))%>' />
                </div>
                <!--<div class="form-container">-->
                <div>
                    <form name="new" id="FormBox" method="post" enctype="application/x-www-form-urlencoded" target="_blank" action="<%=PageParameters.getParameter("mainController")%>">
                    </form>
                </div>
                &nbsp;
                &nbsp;
            </div>
            <div id="divFoot">
                <jsp:include page='<%=(PageParameters.getParameter("footer"))%>' />
            </div>
        </div>
    </body>
</html>
<%
    }
} catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
%>
<jsp:forward page='<%=PageParameters.getParameter("ajaxFunctions") + "/msgNRedirectFull.jsp"%>'>
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