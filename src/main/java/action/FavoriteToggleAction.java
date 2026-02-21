package action;

import java.io.IOException;
import java.sql.SQLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import repository.TaskRepository;

public class FavoriteToggleAction extends BaseAuthAction {

    private final TaskRepository taskRepo = new TaskRepository();

    @Override
    protected String executeAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            User loginUser)
            throws ServletException, IOException {

        String taskIdStr = request.getParameter("taskId");

        // パラメータチェック
        if (taskIdStr == null || taskIdStr.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "taskId が指定されていません");
            return null;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(taskIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "taskId が不正です");
            return null;
        }

        // ★正の整数のみ許可
        if (taskId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "taskId は正の整数で指定してください");
            return null;
        }

        try {
            boolean ok = taskRepo.toggleFavorite(taskId, loginUser.getId());

            // 更新0件＝存在しない or 他人のタスク → 403
            if (!ok) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "操作できません");
                return null;
            }

            // ★一覧の条件（keyword/sort/page）を保ったまま戻す
            String keyword = request.getParameter("keyword");
            String sort = request.getParameter("sort");
            String page = request.getParameter("page");

            StringBuilder qs = new StringBuilder();
            if (keyword != null) {
                qs.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
            }
            if (sort != null) {
                qs.append("&sort=").append(URLEncoder.encode(sort, StandardCharsets.UTF_8));
            }
            if (page != null) {
                qs.append("&page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8));
            }

            String queryString = qs.length() > 0 ? "?" + qs.substring(1) : "";
            return "redirect:/app/task/list" + queryString;

        } catch (SQLException e) {
            e.printStackTrace(); // 後で消してOK
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DBエラー");
            return null;
        }
    }
}
