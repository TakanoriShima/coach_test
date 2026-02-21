<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>タスク一覧</title>

<style>
.flash {
	padding: 10px;
	margin: 10px 0;
	border: 1px solid #cfe3cf;
	background: #eef8ee;
	color: #2e6b2e;
}

/* ===== お気に入り（☆/★） ===== */
.favorite-btn {
	text-decoration: none;
	font-size: 18px;
	cursor: pointer;
	margin-right: 6px;
}

.favorite-active {
	color: #ffc107; /* 黄色 */
}

.favorite-inactive {
	color: #6c757d; /* グレー */
}

/* ===== ページング見た目（お好み） ===== */
.pagination a, .pagination span {
	margin: 0 4px;
}

.current-page {
	font-weight: bold;
	text-decoration: underline;
}

.filter-section {
	margin: 10px 0;
}

.filter-btn {
	padding: 6px 12px;
	margin-right: 5px;
	border: 1px solid #ccc;
	background-color: #f8f9fa;
	cursor: pointer;
}

.filter-btn.active {
	background-color: #007bff;
	color: white;
}

h1{
  font-size: 22px;
  margin: 3px 0 3px;
  line-height: 1.15;
}

</style>
</head>

<body>

	<h1>タスク一覧</h1>

	<p>
		<a href="${pageContext.request.contextPath}/app/task/new"
			style="display: inline-block; padding: 4px 9px; background: #e07655; color: white; text-decoration: none; border-radius: 4px;">
			＋ 新規作成 </a>
	</p>

	<!-- <p>検索結果：${empty tasks ? 0 : fn:length(tasks)}件</p>-->

	<form action="${pageContext.request.contextPath}/app/task/list"
		method="get">
		<input type="text" name="keyword" value="${fn:escapeXml(keyword)}"
			placeholder="タスク名で検索"> <input type="hidden" name="sort"
			value="${fn:escapeXml(sort)}">
		<button type="submit">検索</button>
		<input type="hidden" name="page" value="1">
	</form>

	<c:if test="${not empty keyword}">
		<p>
			検索キーワード：「
			<c:out value="${keyword}" />
			」 <a href="${pageContext.request.contextPath}/app/task/list">クリア</a>
		</p>
	</c:if>

	<c:url var="sortDescUrl" value="/app/task/list">
		<c:param name="keyword" value="${keyword}" />
		<c:param name="sort" value="DESC" />
		<c:param name="page" value="1" />
	</c:url>

	<c:url var="sortAscUrl" value="/app/task/list">
		<c:param name="keyword" value="${keyword}" />
		<c:param name="sort" value="ASC" />
		<c:param name="page" value="1" />
	</c:url>

	<!-- フィルターボタン -->
	<div class="filter-section">
		<button type="button" id="showAllBtn" class="filter-btn active">
			すべて表示</button>
		<button type="button" id="showFavBtn" class="filter-btn">
			お気に入りのみ</button>
	</div>

	<div>
		並べ替え： <a href="${sortDescUrl}"
			style="${sort == 'DESC' ? 'font-weight:bold; text-decoration:underline;' : ''}">
			新しい順 </a> | <a href="${sortAscUrl}"
			style="${sort == 'ASC' ? 'font-weight:bold; text-decoration:underline;' : ''}">
			古い順 </a>
	</div>

	<!-- ページング情報（件数と範囲）-->
	<c:if test="${totalRecords > 0}">
		<p>全${totalRecords}件中 ${startRecord}-${endRecord}件目を表示
			（${currentPage}/${totalPages}ページ）</p>
	</c:if>

	<!-- ページリンク（前へ・数字・次へ）-->
	<c:if test="${totalPages > 1}">
		<div class="pagination">

			<!-- 前へ -->
			<c:if test="${currentPage > 1}">
				<c:url var="prevUrl" value="/app/task/list">
					<c:param name="keyword" value="${keyword}" />
					<c:param name="sort" value="${sort}" />
					<c:param name="page" value="${currentPage - 1}" />
				</c:url>
				<a href="${prevUrl}">＜前へ</a>
			</c:if>

			<!-- ページ番号 -->
			<c:forEach begin="1" end="${totalPages}" var="pageNum">
				<c:choose>
					<c:when test="${pageNum == currentPage}">
						<span class="current-page">${pageNum}</span>
					</c:when>
					<c:otherwise>
						<c:url var="pageUrl" value="/app/task/list">
							<c:param name="keyword" value="${keyword}" />
							<c:param name="sort" value="${sort}" />
							<c:param name="page" value="${pageNum}" />
						</c:url>
						<a href="${pageUrl}">${pageNum}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<!-- 次へ -->
			<c:if test="${currentPage < totalPages}">
				<c:url var="nextUrl" value="/app/task/list">
					<c:param name="keyword" value="${keyword}" />
					<c:param name="sort" value="${sort}" />
					<c:param name="page" value="${currentPage + 1}" />
				</c:url>
				<a href="${nextUrl}">次へ＞</a>
			</c:if>

		</div>
	</c:if>

	<c:if test="${not empty sessionScope.flash}">
		<div class="flash">${sessionScope.flash}</div>
		<c:remove var="flash" scope="session" />
	</c:if>

	<c:choose>
		<c:when test="${not empty tasks}">
			<ul>
				<c:forEach var="task" items="${tasks}">
					<li class="task-row ${task.favorite ? 'favorite-task' : ''}">
						<!-- ===== お気に入り切り替え（c:url安全版）===== --> <c:url var="toggleUrl"
							value="/app/favorite/toggle">
							<c:param name="taskId" value="${task.id}" />
							<c:param name="keyword" value="${keyword}" />
							<c:param name="sort" value="${sort}" />
							<c:param name="page" value="${currentPage}" />
						</c:url> <a href="${toggleUrl}"
						class="favorite-btn ${task.favorite ? 'favorite-active' : 'favorite-inactive'}">
							${task.favorite ? '★' : '☆'} </a> #${task.id} <c:out
							value="${task.title}" />（ <c:choose>
							<c:when test="${task.status == 'pending'}">未着手</c:when>
							<c:when test="${task.status == 'in_progress'}">進行中</c:when>
							<c:when test="${task.status == 'completed'}">完了</c:when>
							<c:otherwise>${task.status}</c:otherwise>
						</c:choose> / <c:choose>
							<c:when test="${task.priority == 'low'}">低</c:when>
							<c:when test="${task.priority == 'medium'}">中</c:when>
							<c:when test="${task.priority == 'high'}">高</c:when>
							<c:otherwise>${task.priority}</c:otherwise>
						</c:choose> ） <!-- 編集 --> <a
						href="${pageContext.request.contextPath}/app/task/edit?id=${task.id}">編集</a>

						<!-- 削除 --> <a
						href="${pageContext.request.contextPath}/app/task/delete?id=${task.id}"
						onclick="return confirm('本当に削除しますか？\n\n削除したデータは復元できません。');">
							削除 </a>
					</li>
				</c:forEach>
			</ul>
		</c:when>
		<c:otherwise>
			<p>タスクがありません</p>
		</c:otherwise>
	</c:choose>

	<p>
		<a href="${pageContext.request.contextPath}/app/home">Home</a> | <a
			href="${pageContext.request.contextPath}/app/logout">ログアウト</a>
	</p>

	<script>
document.addEventListener("DOMContentLoaded", function () {

    const showAllBtn = document.getElementById("showAllBtn");
    const showFavBtn = document.getElementById("showFavBtn");
    const rows = document.querySelectorAll(".task-row");

    showAllBtn.addEventListener("click", function () {
        rows.forEach(row => row.style.display = "");
        setActive(showAllBtn);
    });

    showFavBtn.addEventListener("click", function () {
        rows.forEach(row => {
            if (row.classList.contains("favorite-task")) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
        setActive(showFavBtn);
    });

    function setActive(activeBtn) {
        showAllBtn.classList.remove("active");
        showFavBtn.classList.remove("active");
        activeBtn.classList.add("active");
    }
});
</script>


</body>
</html>
