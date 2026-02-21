<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>Hello World</title>
</head>
<body>
    <h1>FrontController 動作確認</h1>

    <p>${message}</p>
    <p>${currentTime}</p>

    <hr>

    <p>pathInfo: ${pathInfo}</p>
    <p>remoteAddr: ${remoteAddr}</p>
    <p>User-Agent: ${userAgent}</p>

</body>
</html>
