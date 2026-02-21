<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="util.SessionUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
	<%
	User u = SessionUtil.getLoginUser(request);
	%>
	<h1>Home</h1>
	<p>
		<c:out value="${loginUser.username}" />
		さんがログイン中です
	</p>

	<p>
		<a href="${pageContext.request.contextPath}/app/task/list">タスク一覧</a>
	</p>

	<p>
		<a href="<%=request.getContextPath()%>/app/logout">ログアウト</a>
	</p>
</body>
</html>
