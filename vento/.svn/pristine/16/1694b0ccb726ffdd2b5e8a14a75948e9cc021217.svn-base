<%-- 
    Document   : testCheckBox
    Created on : Apr 4, 2013, 10:21:37 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="http://code.jquery.com/jquery-1.9.1.min.js"
        type="text/javascript"></script>
        <script src="https://github.com/carhartl/jquery-cookie/blob/master/jquery.cookie.js"
        type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                var checkbox = $('#boxlawreg').find(':checkbox'), checkboxCookieName = 'checkbox-state';

                checkbox.each(function() {
                    $(this).attr('checked', $.cookie(checkboxCookieName + '|' + $(this).attr('name')));
                });

                checkbox.click(function() {
                    $.cookie(checkboxCookieName + '|' + $(this).attr('name'), $(this).prop('checked'));
                });
            });
        </script>
    </head>
    <body>
        <div id="boxlawreg">
            <input type="checkbox" name="option1" value="1" />1<br />
            <input type="checkbox" name="option2" value="2" />2<br />
            <input type="checkbox" name="option3" value="3" />3<br />
            <input type="checkbox" name="option4" value="4" />4<br />
        </div>
    </body>
</html>
