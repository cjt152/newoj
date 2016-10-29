<%@ page import="util.HTML.HTML" %>
<%@ page import="entity.Permission" %>
<%@ page import="util.Main" %>
<%@ page import="entity.User" %>
<%--
  Created by IntelliJ IDEA.
  User: Syiml
  Date: 2015/6/14 0014
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pa=request.getParameter("page");
    User user = Main.loginUser();
    if(user==null){
        response.sendRedirect("Login.jsp");
        return;
    }
    Permission per=user.getPermission();
    if(pa!=null&&(pa.equals("AddTag")||pa.equals("PermissionAdmin")||pa.equals("ChallengeAdmin"))){
        Main.saveURL();
    }
%>
<html>
<head>
    <title>管理 - <%=Main.config.OJName%></title>
</head>
<body>
<div class="container-fluid">
    <jsp:include page="module/head.jsp"/>
      <div style='width:14%;float:left' class="adminNAV">
          <%=HTML.adminNAV(per,pa)%>
      </div>
      <div style='width:84%;float:right'>
          <%=HTML.adminMAIN(per, pa)%>
      </div>
</div>
<jsp:include page="module/foot.jsp"/>
</body>
</html>

