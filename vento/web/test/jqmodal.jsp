<%-- 
    Document   : jqmodal
    Created on : Mar 20, 2013, 5:51:56 PM
    Author     : JeanPaul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>jQuery UI Dialog - Modal confirmation</title>
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
        <link rel="stylesheet" href="/resources/demos/style.css" />
        <script>
            $(function() {
                $("#dialog-confirm").dialog({
                    resizable: false,
                    height: 140,
                    modal: true,
                    buttons: {
                        "Delete all items": function() {
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <div id="dialog-confirm" title="Empty the recycle bin?">
            <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>These items will be permanently deleted and cannot be recovered. Are you sure?</p>
        </div>
        <p>Sed vel diam id libero <a href="http://example.com">rutrum convallis</a>. Donec aliquet leo vel magna. Phasellus rhoncus faucibus ante. Etiam bibendum, enim faucibus aliquet rhoncus, arcu felis ultricies neque, sit amet auctor elit eros a lectus.</p>
    </body>
</html>