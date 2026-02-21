<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>タスク新規作成</title>

<style>
body { font-family: sans-serif; margin: 20px; }
.container { max-width: 720px; margin: 0 auto; }
.box { border: 1px solid #ddd; padding: 16px; border-radius: 8px; }
.row { margin-bottom: 14px; }
label { display: block; font-weight: bold; margin-bottom: 6px; }
input[type="text"], textarea, select { width: 100%; padding: 8px; box-sizing: border-box; }

.field-errors { margin: 6px 0 0; padding-left: 18px; color: #b00020; }
.field-errors li { margin: 2px 0; }

.error-box {
  background: #ffe6e6;
  border: 1px solid #ffb3b3;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 12px;
  color: #b00020;
}

.has-error input, .has-error textarea, .has-error select {
  border: 2px solid #ff6b6b;
  background: #fff5f5;
}

.actions { display: flex; gap: 10px; align-items: center; margin-top: 12px; }
.btn { padding: 8px 14px; border: 1px solid #333; background: #fff; cursor: pointer; border-radius: 6px; }
a { color: #0366d6; text-decoration: none; }
small { color: #666; }
</style>
</head>

<body>
<div class="container">
  <h1>タスク新規作成</h1>

  <!-- 例外系の単発エラー（DBエラーなど） -->
  <c:if test="${not empty error}">
    <div class="error-box">
      <c:out value="${error}" />
    </div>
  </c:if>

  <div class="box">
    <form method="post" action="${pageContext.request.contextPath}/app/task/new">

      <!-- タイトル -->
      <div class="row ${not empty errors.title ? 'has-error' : ''}">
        <label for="title">タイトル（必須）</label>
        <input id="title" type="text" name="title"
               value="${fn:escapeXml(title)}"
               required maxlength="200">

        <c:if test="${not empty errors.title}">
          <ul class="field-errors">
            <c:forEach var="msg" items="${errors.title}">
              <li><c:out value="${msg}" /></li>
            </c:forEach>
          </ul>
        </c:if>
        <small>2文字以上 / 200文字以内</small>
      </div>

      <!-- 説明 -->
      <div class="row ${not empty errors.description ? 'has-error' : ''}">
        <label for="description">説明</label>
        <textarea id="description" name="description" rows="5">${fn:escapeXml(description)}</textarea>

        <c:if test="${not empty errors.description}">
          <ul class="field-errors">
            <c:forEach var="msg" items="${errors.description}">
              <li><c:out value="${msg}" /></li>
            </c:forEach>
          </ul>
        </c:if>
        <small>1000文字以内</small>
      </div>

      <!-- ステータス -->
      <div class="row ${not empty errors.status ? 'has-error' : ''}">
        <label for="status">ステータス</label>
        <select id="status" name="status">
          <option value="pending"     ${status == 'pending' ? 'selected' : ''}>未着手</option>
          <option value="in_progress" ${status == 'in_progress' ? 'selected' : ''}>進行中</option>
          <option value="completed"   ${status == 'completed' ? 'selected' : ''}>完了</option>
        </select>

        <c:if test="${not empty errors.status}">
          <ul class="field-errors">
            <c:forEach var="msg" items="${errors.status}">
              <li><c:out value="${msg}" /></li>
            </c:forEach>
          </ul>
        </c:if>
      </div>

      <!-- 優先度 -->
      <div class="row ${not empty errors.priority ? 'has-error' : ''}">
        <label for="priority">優先度</label>
        <select id="priority" name="priority">
          <option value="low"    ${priority == 'low' ? 'selected' : ''}>低</option>
          <option value="medium" ${priority == 'medium' ? 'selected' : ''}>中</option>
          <option value="high"   ${priority == 'high' ? 'selected' : ''}>高</option>
        </select>

        <c:if test="${not empty errors.priority}">
          <ul class="field-errors">
            <c:forEach var="msg" items="${errors.priority}">
              <li><c:out value="${msg}" /></li>
            </c:forEach>
          </ul>
        </c:if>
      </div>

      <div class="actions">
        <button class="btn" type="submit">作成</button>
        <a href="${pageContext.request.contextPath}/app/task/list">キャンセル</a>
      </div>

    </form>
  </div>
</div>
</body>
</html>
