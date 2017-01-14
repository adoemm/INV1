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
        <script type="text/javascript" src="jquery-ui-1.7.2.custom.min.js"></script>
        <script type="text/javascript" src="jquery.chromatable.js"></script>

        <script language="javascript" type="text/javascript">

            $(document).ready(function() {
                $('#tsearch').keyup(function() {
                    searchTable($(this).val());
                });
            });
            function searchTable(inputVal) {
                var table = $('#superTable');
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


            $(document).ready(function() {
                $("#superTable").chromatable({
                    width: "900px",
                    height: "400px",
                    scrolling: "yes"
                });
            });
        </script>
    </HEAD>
    <BODY>
        <div>
            <p>
                <label for="tsearch"> <strong>Search Box</strong>
                </label> <input type="text" id="tsearch" />
            </p>
            <table width="40%" id="superTable" border="1">
                <tbody>
                    <tr bgcolor="#C0C0C0">
                        <th width="7%" align="left">#</th>
                        <th width="11%" align="left">Name</th>
                        <th width="11%" align="left">Company</th>
                        <th width="11%" align="left">Designation</th>
                        <th width="11%" align="left">Selection</th>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">1</td>
                        <td width="11%" style="font-size: 11pt;">Sandeep</td>
                        <td width="11%" style="font-size: 11pt;">Infosys</td>
                        <td width="11%" style="font-size: 11pt;">Developer</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">2</td>
                        <td width="11%" style="font-size: 11pt;">Pramod</td>
                        <td width="11%" style="font-size: 11pt;">TCS</td>
                        <td width="11%" style="font-size: 11pt;">Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">3</td>
                        <td width="11%" style="font-size: 11pt;">Narendra</td>
                        <td width="11%" style="font-size: 11pt;">Accenture</td>
                        <td width="11%" style="font-size: 11pt;">Technical Lead</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">4</td>
                        <td width="11%" style="font-size: 11pt;">Tarun</td>
                        <td width="11%" style="font-size: 11pt;">Metacube</td>
                        <td width="11%" style="font-size: 11pt;">Business Analyst</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">5</td>
                        <td width="11%" style="font-size: 11pt;">Manish</td>
                        <td width="11%" style="font-size: 11pt;">Wipro</td>
                        <td width="11%" style="font-size: 11pt;">QA Engineeer</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">6</td>
                        <td width="11%" style="font-size: 11pt;">Ravi</td>
                        <td width="11%" style="font-size: 11pt;">Satyam</td>
                        <td width="11%" style="font-size: 11pt;">Sales Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">7</td>
                        <td width="11%" style="font-size: 11pt;">Bhawani</td>
                        <td width="11%" style="font-size: 11pt;">HCL</td>
                        <td width="11%" style="font-size: 11pt;">HR Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">8</td>
                        <td width="11%" style="font-size: 11pt;">Yashpal</td>
                        <td width="11%" style="font-size: 11pt;">Nestle</td>
                        <td width="11%" style="font-size: 11pt;">Vice President</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">9</td>
                        <td width="11%" style="font-size: 11pt;">Akhilesh</td>
                        <td width="11%" style="font-size: 11pt;">Hero Honda</td>
                        <td width="11%" style="font-size: 11pt;">Delivery Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr><tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                    <tr>
                        <td width="7%" style="font-size: 11pt;">10</td>
                        <td width="11%" style="font-size: 11pt;">Anand</td>
                        <td width="11%" style="font-size: 11pt;">Tech Mahindra</td>
                        <td width="11%" style="font-size: 11pt;">IT Manager</td>
                        <td width="11%" style="font-size: 11pt;"><input type="checkbox" name="check1" value="lolcat"></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <a href="http://localhost:8084/copalli/gui/exampleSelect.jsp">exampleSelected</a>
    </BODY>
</HTML>