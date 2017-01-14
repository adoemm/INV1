<script type="text/javascript" language="javascript" charset="utf-8">
    var t = $('#example').dataTable({
        "bJQueryUI": true,
        "bAutoWidth": false,
        "aaSorting": [], //evita el ordermaniento de la primera columna
        "bDestroy": true, //permite reinicializar la tabla
        "bRetrieve": true, //permite reinicializar la tabla
        //"bScrollInfinite": false,
        //"sScrollY": "800px",
        //"sScrollX": "100%",
        //"sScrollXInner": "365%",
        "bScrollCollapse": true,
        "bPaginate": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bFilter": true,
        //"bServerSide": true,
        //"bSort": true,
        "bDeferRender": true,
        "oLanguage": {
            "sProcessing": "Procesando...",
            "sLengthMenu": "Mostrar _MENU_ registros",
            "sZeroRecords": "No se encontraron resultados",
            "sEmptyTable": "Ningún dato disponible en esta tabla",
            "sInfo": "Del _START_ al _END_ de _TOTAL_ registros",
            "sInfoEmpty": "Registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix": "",
            "sSearch": "Buscar:",
            "sUrl": "",
            "sInfoThousands": ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst": "<<",
                "sLast": ">>",
                "sNext": ">",
                "sPrevious": "<"
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
    t.fnDraw();
    $("#loadingScreen").Loadingdotdotdot("Stop");
    $("#loadingScreen").html("");
    document.getElementById('loadingScreen').style.display = 'none';
    document.getElementById('contenent_info').style.display = 'block';
</script>