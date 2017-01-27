
<%@page import="jspread.core.util.UserUtil"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="jspread.core.util.WebUtil"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="jspread.core.util.PageParameters"%>
<%@page import="systemSettings.SystemSettings"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedList"%>
<%@page import="jspread.core.db.QUID"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>

<%
    try {
        SystemSettings.ignition();

        request.setCharacterEncoding(PageParameters.getParameter("charset").toString());
        response.setCharacterEncoding(PageParameters.getParameter("charset").toString());

        LinkedList<String> access4ThisPage = new LinkedList();
        access4ThisPage.add("SIAPSystem");

        Iterator it = null;
        LinkedList listAux = null;
        Iterator itAux = null;
        StringBuilder data = new StringBuilder();
        String userSystem = "";

        userSystem = request.getParameter("userSystem");

        LinkedList<String> user = new LinkedList();
        user.add(userSystem);



        if (UserUtil.isAValidUser(access4ThisPage, user)) {
            QUID quid = new QUID();

            //System.out.println("here my voice:"+request.getParameter("parameter"));
            //JOptionPane.showMessageDialog(null, request.getParameter("parameter"));
            //LinkedList listAux = quid.select_searchEscuelaProcedencia(request.getParameter("busqueda"));
//            it = quid.select_searchEscuelaProcedencia(request.getParameter("nomEscuela")).iterator();
//
//            while (it.hasNext()) {
//                itAux = ((LinkedList) it.next()).iterator();
//                data.append("</tr->");
//                while (itAux.hasNext()) {
//                    data.append("</td->");
//                    data.append(itAux.next().toString());
//                }
//            }
            //av45k1pfb024xa3bl359vsb4esortvks74sksr5oy4s5serondry84jsrryuhsr5ys49y5seri5shrdliheuirdygliurguiy5ru
            //out.print(WebUtil.encode("fieldSecure123456789asdfg", data));

            //out.print(listAux);
            //Base64.encodeBase64("cadena".getBytes());        
            //out.print(new String(Base64.encodeBase64(listAux.toString().getBytes()), "UTF-8"));
        }
        out.print(WebUtil.encode(PageParameters.getParameter(userSystem), data));
    } catch (Exception ex) {
        out.print("error");
    }
%> 