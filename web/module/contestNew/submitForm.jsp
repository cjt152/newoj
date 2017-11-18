<%@ page import="entity.User" %>
<%@ page import="util.Main" %>
<%@ page import="entity.Problem" %>
<%@ page import="entity.Contest" %>
<%@ page import="servise.ContestMain" %>
<%@ page import="entity.OJ.CodeLanguage" %>
<%@ page import="util.Submitter" %>
<%@ page import="util.Pair" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.OJ.OTHOJ" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/25
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<%--
  所需参数：cid   为null表示全局题目
            pid   为null则表单中pid为空
--%>
<%
  String cid=request.getParameter("cid");
  String pid=request.getParameter("pid");
  if(cid==null){
    cid="-1";
  }
  if(pid==null){
    pid="";
  }
%>
<%--<form action="sb.action" method="post" class="form-inline" id="submit">--%>
<center>
<form class="form-inline" id="submitForm">
  <div class="row">
    <div class="form-group">
      <label for="pidinput"> pid</label><input type="text" name ="pid" value="<%=pid%>" class="form-control" id="pidinput"/>
      <label for="laninput"> language</label><select name="language" class="form-control" id="laninput">
      <%
        Contest c = ContestMain.getContest(Integer.parseInt(cid));
        Problem p = c.getProblem(Integer.parseInt(pid));
        OTHOJ oj = Submitter.ojs[p.getOjid()];
        List<Pair<Integer,CodeLanguage>> languageList = oj.getLanguageList(p.getOjspid());
        for (Pair<Integer, CodeLanguage> aLanguageList : languageList) {
          %>
          <option value="<%=aLanguageList.getValue().getId()%>"><%=aLanguageList.getValue().getShow()%>
          </option>
          <%
        }
      %>
    </select>
    </div>
  </div>
  <div class="row">
    <div class="form-group">
      <label for="codeinput"> code</label><br>
      <textarea name="code" class="form-control" cols="100" rows="15" id="codeinput" placeholder="Put your code here"></textarea>
    </div>
  </div>
  <div class="row">
    <input type="button" class="form-control btn-primary" value="Submit" onclick="doSubmit()"/>
  </div>
</form>
</center>
<%--<script type="text/javascript">--%>
  <%--$().ready(function() {--%>
    <%--$("#submitForm").validate({--%>
      <%--rules: {--%>
        <%--code: {--%>
          <%--required: true,--%>
          <%--minlength: 51,--%>
          <%--maxlength: 65535,--%>
<%--//          bsf: true,--%>
          <%--onkeyup: true--%>
        <%--}--%>
      <%--},--%>
      <%--messages: {--%>
        <%--code: {--%>
          <%--required: "Please enter your code",--%>
          <%--minlength: "Code length is improper! Make sure your code length is longer than 50 and not exceed 65536 Bytes",--%>
          <%--maxlength: "Code length is improper! Make sure your code length is longer than 50 and not exceed 65536 Bytes"--%>
        <%--}--%>
      <%--}--%>
    <%--});--%>
  <%--});--%>
<%--</script>--%>