<%-- 
    Document   : attachFieldOnSumbit
    Created on : Apr 5, 2013, 8:43:22 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" language="javascript" src="http://code.jquery.com/jquery-latest.js"></script>

        <script type="text/javascript" language="javascript">
            $('#commentForm').submit(function() { //listen for submit event
                alert("selam");

                $.each(params, function(i, param) {
                    $('<input />').attr('type', 'hidden')
                            .attr('name', param.name)
                            .attr('value', param.value)
                            .appendTo('#commentForm');
                });
                return true;
            });

            function selam() {
                alert("selam");
                //var form = $container.find('.commentForm');
                //var form = document.getElementById("commentForm");
                //var form = 
                $('#commentForm').append(
                        $(document.createElement('input'))
                        .attr('type', 'hidden')
                        .attr('name', 'somename')
                        .attr('value', 'lolcat')
                        //.attr('type', 'somecalculatedvalue')
                        );

                $('#commentForm').append(
                        $(document.createElement('input'))
                        .attr('type', 'hidden')
                        .attr('name', 'perra')
                        .attr('value', 'perra')
                        //.attr('type', 'somecalculatedvalue')
                        );
                $('#commentForm').submit();
            }
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form id="commentForm" method="POST" action="/copalli/copalli">
            <input type="text" name="name" title="Your name"/>
            <textarea  cols="40" rows="10" name="comment" title="Enter a comment">
            </textarea>
            <input type="submit" value="Post"/>
            <input type="reset" value="Reset"/>
            <button type="button" value="Submit" id="myButton" onclick="selam();">Enviar</button>
        </form>
    </body>
</html>
