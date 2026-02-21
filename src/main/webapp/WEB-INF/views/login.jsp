<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Login</title></head>
<body>
<h1>Login</h1>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
  <p style="color:red;"><%= error %></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/app/login">
  ユーザー名:
  <input type="text" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>">
  <br>
  パスワード:
  <input type="password" name="password">
  <br>
  <button type="submit">ログイン</button>
</form>
</body>
</html>
