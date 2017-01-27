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

            function selam2() {
                $('#commentForm').submit(function(e) {
                    if ($(this).valid()) {
                        var f = this;
                        window.open("", $(this).attr("target"), "width=500,height=500");
                        setTimeout(function() {  // delay resetting the form until after submit
                            hideForm();
                            f.reset();
                        }, 0);
                        return true;
                    }
                    else {
                        e.preventDefault();  // only prevent default if the form is not valid
                    }
                    return false;
                });
            }

            function submitForm() {
                $.ajax(
                        {
                            type: 'POST',
                            url: '/copalli/copalli',
                            data: $('#commentForm').serialize(),
                            async: false,
                            success: function(response) {
                                //$('#ContactForm').find('.form_result').html(response);
                                alert("Alert despues subit");
                            }
                        });
                return false;
            }

            $(document).ready(function() {
                $('#NewTab').click(function() {
                    $(this).target = "_blank";
                    //window.open($(this).prop('https://www.google.com/ncr'));
                    window.open('https://www.google.com/ncr');
                    return false;
                });
            });
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
            <button type="button" value="Submit" id="myButton" onclick="selam();">selam</button>
            <button type="button" value="Submit" id="myButton" onclick="selam2();">selam2</button>
            <button type="button" value="Submit" id="myButton" onclick="submitForm();">submitForm</button>
            <button type="button" value="Submit" id="NewTab">new tab</button>
            
        </form>
    </body>
</html>
