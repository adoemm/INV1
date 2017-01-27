<%-- 
    Document   : showHideJquery
    Created on : Apr 30, 2013, 12:56:45 PM
    Author     : JeanPaul
--%>

<%@ include file="/gui/pageComponents/globalSettings.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <jsp:include page='<%=PageParameters.getParameter("globalLibs")%>'/>
        <script>
            function ShowDiv() {
                document.getElementById("myDiv").style.display = "";
            }

            function hideDiv() {
                document.getElementById("myDiv").style.display = 'none';
            }


            $('#myButton').click(function() {
                $('#myDiv').toggle('slow', function() {
                    // Animation complete.
                });
            });
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <div id="myDiv" style="display:none;" class="answer_list" >WELCOME</div>
        <input type="button" name="answer" onclick="ShowDiv()" />
        <input id="myButton" type="button" name="answer" onclick="ShowDiv()" value="show" />
        <input type="button" name="answer" onclick="hideDiv()" value="hide"/>
    </body>
</html>
