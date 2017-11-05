<%@ page import="entity.User" %>
<%@ page import="entity.Problem" %>
<%@ page import="entity.OJ.OTHOJ" %>
<%@ page import="util.Submitter" %>
<%@ page import="entity.OJ.CodeLanguage" %>
<%@ page import="util.Pair" %>
<%@ page import="java.util.List" %>
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
  if(cid==null){
    cid="-1";
  }
  if(pid==null){
    pid="";
  }
%>
<form action="sb.action" method="post" class="form-inline" id="submit">
  <div class="row">
    <h2> Submit</h2>
    <div class="form-group hidden" >
      <label for="cidinput">cid</label><input type="text" name ="cid" value=<%=cid%> class="form-control" id="cidinput" readonly/>
      <%--<label for="userinput">user</label><input type="text" name ="user" value=<%=Main.loginUser().getUsername()%> class="form-control" id="userinput"/>--%>
      <!--user must read-->
    </div>
  </div>
  <div class="row">
    <div class="form-group">
      <label for="pidinput"> pid</label><input type="text" name ="pid" value="<%=pid%>" class="form-control" id="pidinput"/>
      <label for="laninput"> language</label><select name="language" class="form-control" id="laninput">
      <%
        Problem p = Main.problems.getProblem(Integer.parseInt(pid));
        OTHOJ oj = Submitter.ojs[p.getOjid()];
        List<Pair<Integer,CodeLanguage>> languageList = oj.getLanguageList(p.getOjspid());
        for (Pair<Integer, CodeLanguage> aLanguageList : languageList) {
      %>
      <option value="<%=aLanguageList.getValue().getId()%>"><%=aLanguageList.getValue().getShow()%>
      </option>
      <%
        }
        %>
        <%--if(p.getOjid() == 9 ) {--%>
                                        <%--%><option value="0" selected>G++</option><%--%>
        <%--}else if(p.getOjid() == 7) {--%>
                                        <%--%><option value="0" selected>G++</option>--%>
                                          <%--&lt;%&ndash;<option value="2">Java</option>&ndash;%&gt;--%>
                                          <%--<option value="3">Python3</option><%--%>

        <%--}else{--%>
      <%--%>--%>
                                        <%--<option value="0" selected>G++</option>--%>
                                        <%--<option value="1">GCC</option>--%>
                                        <%--<option value="2">Java</option>--%>
      <%--<%}%>--%>
                                        </select>
    </div>
  </div>
  <div class="row">
    <div class="form-group">
      <label for="codeinput"> code</label><br>
      <textarea name="code" class="form-control" cols="80" rows="15" id="codeinput" placeholder="Put your code here"></textarea>
    </div>
  </div>
  <div class="row">
    <input type="submit" class="form-control" value="Submit"/>
  </div>
</form>

<script type="text/javascript">
  $().ready(function() {
    $("#submit").validate({
      rules: {
        code: {
          required: true,
          minlength: 10,
          maxlength: 100000,
          bsf: true
        }
      },
      messages: {
        code: {
          required: "请输入提交代码",
          minlength: "代码长度必须在10到100000之间",
          maxlength: "代码长度必须在10到100000之间"
        }
      }
    });
  });
</script>