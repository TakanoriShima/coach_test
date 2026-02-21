<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>403 Forbidden</title>
</head>
<body>
	<h1>403 アクセス権限エラー</h1>
	<p>${error}</p>
	<p>
		<a href="${pageContext.request.contextPath}/app/task/list">タスク一覧へ戻る</a>
	</p>
</body>
</html>
