package action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Task;
import model.User;
import repository.TaskRepository;

public class TaskListAction extends BaseAuthAction {

    private final TaskRepository taskRepo = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        int userId = loginUser.getId();

        // --- 第9回：検索・ソートのパラメータ ---
        String keyword = request.getParameter("keyword");
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null; // 空はnullに統一
        }

        String sort = request.getParameter("sort");
        if (sort == null || sort.trim().isEmpty()) {
            sort = "DESC"; // デフォルト：新しい順
        }
        // sortの安全化（ASC以外はDESC）
        sort = "ASC".equalsIgnoreCase(sort) ? "ASC" : "DESC";

        // --- 第10回：ページ番号 ---
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && pageParam.matches("\\d+")) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        if (currentPage < 1) currentPage = 1;

        final int pageSize = 10;

        try {
            // ① 総件数
            int totalRecords = taskRepo.countTasks(userId, keyword);

            // ② 総ページ数（切り上げ）
            int totalPages = (totalRecords + pageSize - 1) / pageSize;
            if (totalPages == 0) totalPages = 1;

            // 範囲外ページは 1 に戻す（指示通り）
            if (currentPage > totalPages) currentPage = 1;

            // ③ OFFSET
            int offset = (currentPage - 1) * pageSize;

            // ④ データ取得（ページング対応）
            List<Task> tasks = taskRepo.searchWithPaging(userId, keyword, sort, pageSize, offset);

            // ⑤ 表示範囲（X件中 Y-Z件目）
            int startRecord = (totalRecords == 0) ? 0 : offset + 1;
            int endRecord = Math.min(offset + pageSize, totalRecords);

            boolean hasPrevious = currentPage > 1;
            boolean hasNext = currentPage < totalPages;

            // --- JSPへ渡す ---
            request.setAttribute("tasks", tasks);
            request.setAttribute("keyword", keyword);
            request.setAttribute("sort", sort);

            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("startRecord", startRecord);
            request.setAttribute("endRecord", endRecord);
            request.setAttribute("hasPrevious", hasPrevious);
            request.setAttribute("hasNext", hasNext);

            return "/WEB-INF/views/task/list.jsp";

        } catch (SQLException e) {
            request.setAttribute("error", "データの取得に失敗しました");
            response.setStatus(500);
            return "/WEB-INF/views/error/500.jsp";
        }
    }
}
