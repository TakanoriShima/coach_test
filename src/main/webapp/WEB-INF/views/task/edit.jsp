<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>タスク編集</title>

<style>
body { font-family: sans-serif; margin: 20px; }
.container { max-width: 720px; margin: 0 auto; }
.box { border: 1px solid #ddd; padding: 16px; border-radius: 8px; }
.row { margin-bottom: 12px; }
label { display: block; font-weight: bold; margin-bottom: 6px; }
input[type="text"], textarea, select { width: 100%; padding: 8px; box-sizing: border-box; }

.error { background: #ffe6e6; border: 1px solid #ffb3b3; padding: 10px; border-radius: 6px; margin-bottom: 12px; }
.field-error { margin-top: 6px; color: #b00020; }

.actions { display: flex; gap: 10px; align-items: center; margin-top: 15px; }
.btn { padding: 8px 14px; border: 1px solid #333; background: #fff; cursor: pointer; border-radius: 6px; }
a { color: #0366d6; text-decoration: none; }
</style>
</head>

<body>
<div class="container">
  <h1>タスク編集</h1>

  <!-- 画面全体エラー（例：DBエラーなど） -->
  <c:if test="${not empty error}">
    <div class="error"><c:out value="${error}"/></div>
  </c:if>

  <div class="box">
    <form action="${pageContext.request.contextPath}/app/task/edit" method="post">
      <input type="hidden" name="id" value="${task.id}" />

      <!-- タイトル -->
      <div class="row">
        <label for="title">タイトル（必須）</label>
        <input type="text" id="title" name="title"
               value="<c:out value='${task.title}'/>"
               required maxlength="200" />
        <c:if test="${not empty errors.title}">
          <div class="field-error">
            <ul>
              <c:forEach var="msg" items="${errors.title}">
                <li><c:out value="${msg}"/></li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <!-- 説明 -->
      <div class="row">
        <label for="description">説明</label>
        <textarea id="description" name="description" rows="5"><c:out value="${task.description}"/></textarea>
        <c:if test="${not empty errors.description}">
          <div class="field-error">
            <ul>
              <c:forEach var="msg" items="${errors.description}">
                <li><c:out value="${msg}"/></li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <!-- ステータス -->
      <div class="row">
        <label for="status">ステータス</label>
        <select id="status" name="status">
          <option value="pending"     <c:if test="${task.status == 'pending'}">selected</c:if>>未着手</option>
          <option value="in_progress" <c:if test="${task.status == 'in_progress'}">selected</c:if>>進行中</option>
          <option value="completed"   <c:if test="${task.status == 'completed'}">selected</c:if>>完了</option>
        </select>
        <c:if test="${not empty errors.status}">
          <div class="field-error">
            <ul>
              <c:forEach var="msg" items="${errors.status}">
                <li><c:out value="${msg}"/></li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <!-- 優先度 -->
      <div class="row">
        <label for="priority">優先度</label>
        <select id="priority" name="priority">
          <option value="low"    <c:if test="${task.priority == 'low'}">selected</c:if>>低</option>
          <option value="medium" <c:if test="${task.priority == 'medium'}">selected</c:if>>中</option>
          <option value="high"   <c:if test="${task.priority == 'high'}">selected</c:if>>高</option>
        </select>
        <c:if test="${not empty errors.priority}">
          <div class="field-error">
            <ul>
              <c:forEach var="msg" items="${errors.priority}">
                <li><c:out value="${msg}"/></li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <div class="actions">
        <button class="btn" type="submit">更新</button>
        <a href="${pageContext.request.contextPath}/app/task/list">キャンセル</a>
      </div>
    </form>
  </div>
</div>
</body>
</html>
