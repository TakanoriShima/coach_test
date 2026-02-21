<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>404 ページが見つかりません</title>
<style>
body {
	font-family: sans-serif;
	background-color: #f8f9fa;
}

.container {
	max-width: 600px;
	margin: 100px auto;
	text-align: center;
}

h1 {
	color: #dc3545;
}

p {
	margin: 20px 0;
}

a {
	color: #007bff;
	text-decoration: none;
}
</style>
</head>
<body>
	<div class="container">
		<h1>404 ページが見つかりません</h1>

		<p>指定されたページ、またはタスクは存在しません。</p>

		<c:if test="${not empty error}">
			<p style="color: #666;">${error}</p>
		</c:if>

		<p>
			<a href="${pageContext.request.contextPath}/app/task/list">
				タスク一覧に戻る </a>
		</p>
	</div>
</body>
</html>
