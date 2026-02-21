<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>400 Bad Request</title>
</head>
<body>
	<h1>400 リクエストが不正です</h1>
	<p>${error}</p>
	<p>
		<a href="${pageContext.request.contextPath}/app/task/list">タスク一覧へ戻る</a>
	</p>
</body>
</html>
