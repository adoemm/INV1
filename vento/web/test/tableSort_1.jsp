<%-- 
    Document   : testTableSearch
    Created on : Apr 5, 2013, 12:46:14 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
    <HEAD>
        <TITLE>Jquery Search Text In HTML Table</TITLE>
        <script type="text/javascript" language="javascript" src="http://code.jquery.com/jquery-latest.js"></script>
        <script type="text/javascript" src="jquery.tablesorter.js"></script> 

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#tsearch').keyup(function() {
                    searchTable($(this).val());
                });
            });
            function searchTable(inputVal) {
                var table = $('#table');
                table.find('tr').each(function(index, row) {
                    var allCells = $(row).find('td');
                    if (allCells.length > 0) {
                        var found = false;
                        allCells.each(function(index, td) {
                            var regExp = new RegExp(inputVal, 'i');
                            if (regExp.test($(td).text())) {
                                found = true;
                                return false;
                            }
                        });
                        if (found == true)
                            $(row).show();
                        else
                            $(row).hide();
                    }
                });
            }
            $(document).ready(function()
            {
                $("#table").tablesorter();
            }
            );
            $(document).ready(function()
            {
                $("#table").tablesorter({sortList: [[0, 0], [1, 0]]});
            }
            );
        </script>
    </HEAD>
    <BODY>
        <table id="tablesorter-demo" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
            <thead>
                <tr>
                    <th class="header">First Name</th>
                    <th class="header">Last Name</th>
                    <th class="header">Age</th>
                    <th class="header">Total</th>
                    <th class="header">Discount</th>
                    <th class="header">Difference</th>
                    <th class="header headerSortDown">Date</th>
                </tr>
            </thead>
            <tbody>
                <tr class="odd">
                    <td>Bruce</td>
                    <td>Almighty</td>
                    <td>45</td>
                    <td>$153.19</td>
                    <td>44.7%</td>
                    <td>+77</td>
                    <td>Jan 18, 2001 9:12 AM</td>
                </tr><tr class="even">
                    <td>John</td>
                    <td>Hood</td>
                    <td>33</td>
                    <td>$19.99</td>
                    <td>25%</td>
                    <td>+12</td>
                    <td>Dec 10, 2002 5:14 AM</td>
                </tr><tr class="odd">
                    <td>Clark</td>
                    <td>Kent</td>
                    <td>18</td>
                    <td>$15.89</td>
                    <td>44%</td>
                    <td>-26</td>
                    <td>Jan 12, 2003 11:14 AM</td>
                </tr><tr class="even">
                    <td>Peter</td>
                    <td>Parker</td>
                    <td>28</td>
                    <td>$9.99</td>
                    <td>20.9%</td>
                    <td>+12.1</td>
                    <td>Jul 6, 2006 8:14 AM</td>
                </tr><tr class="odd">
                    <td>Bruce</td>
                    <td>Evans</td>
                    <td>22</td>
                    <td>$13.19</td>
                    <td>11%</td>
                    <td>-100.9</td>
                    <td>Jan 18, 2007 9:12 AM</td>
                </tr><tr class="even">
                    <td>Bruce</td>
                    <td>Evans</td>
                    <td>22</td>
                    <td>$13.19</td>
                    <td>11%</td>
                    <td>0</td>
                    <td>Jan 18, 2007 9:12 AM</td>
                </tr></tbody>
        </table>
        <a href="http://localhost:8084/copalli/gui/exampleSelect.jsp">exampleSelected</a>
    </BODY>
</HTML>