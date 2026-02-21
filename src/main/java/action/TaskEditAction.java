package action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Task;
import model.User;
import repository.TaskRepository;
import util.ValidationUtil;

public class TaskEditAction extends BaseAuthAction {

    private final TaskRepository taskRepo = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            return showEditForm(request, response, loginUser);
        } else if ("POST".equalsIgnoreCase(method)) {
            return processUpdate(request, response, loginUser);
        }

        response.setStatus(405);
        return "redirect:/app/task/list";
    }

    private String showEditForm(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        Integer taskId = parseTaskId(request.getParameter("id"));
        if (taskId == null) {
            return "redirect:/app/task/list";
        }

        Task task = taskRepo.findById(taskId);
        if (task == null) {
            request.setAttribute("error", "タスクが見つかりません");
            response.setStatus(404);
            return "/WEB-INF/views/error/404.jsp";
        }

        if (!taskRepo.isOwner(taskId, loginUser.getId())) {
            request.setAttribute("error", "この操作を実行する権限がありません");
            response.setStatus(403);
            return "/WEB-INF/views/error/403.jsp";
        }

        request.setAttribute("task", task);
        return "/WEB-INF/views/task/edit.jsp";
    }

    private String processUpdate(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
    	
        Integer taskId = parseTaskId(request.getParameter("id"));
        if (taskId == null) {
            return "redirect:/app/task/list";
        }

        // 先に認可
        if (!taskRepo.isOwner(taskId, loginUser.getId())) {
            request.setAttribute("error", "この操作を実行する権限がありません");
            response.setStatus(403);
            return "/WEB-INF/views/error/403.jsp";
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");
       
        // 詳細バリデーション（Map形式）
        Map<String, List<String>> fieldErrors = validateTaskInputEnhanced(title, description, status, priority);

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("errors", fieldErrors);

            // 既存データを基にしつつ、入力値で上書き（入力保持）
            Task task = taskRepo.findById(taskId);
            if (task == null) {
                request.setAttribute("error", "タスクが見つかりません");
                response.setStatus(404);
                return "/WEB-INF/views/error/404.jsp";
            }

            task.setTitle(title);
            task.setDescription(description);

            if (status == null || status.isBlank()) status = "pending";
            if (priority == null || priority.isBlank()) priority = "medium";
            task.setStatus(status);
            task.setPriority(priority);

            request.setAttribute("task", task);
            return "/WEB-INF/views/task/edit.jsp";
        }

        // 正常：更新
        Task task = new Task();
        task.setId(taskId);
        task.setUserId(loginUser.getId());
        task.setTitle(title != null ? title.trim() : null);
        task.setDescription((description == null || description.isBlank()) ? null : description.trim());

        if (status == null || status.isBlank()) status = "pending";
        if (priority == null || priority.isBlank()) priority = "medium";
        task.setStatus(status);
        task.setPriority(priority);

        boolean ok;
        try {
            ok = taskRepo.update(task); // update() throws SQLException の前提
        } catch (SQLException e) {
            request.getSession().setAttribute("flash", "DBエラーが発生しました");
            e.printStackTrace();
            return "redirect:/app/task/list";
        }

        request.getSession().setAttribute("flash", ok ? "タスクを更新しました" : "更新できませんでした");
        return "redirect:/app/task/list";
    }

    private Integer parseTaskId(String idStr) {
        if (idStr == null || idStr.isBlank()) return null;
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, List<String>> validateTaskInputEnhanced(String title, String description, String status, String priority) {
        Map<String, List<String>> fieldErrors = new HashMap<>();

        List<String> titleErrors = ValidationUtil.validateTitle(title);
        if (!titleErrors.isEmpty()) fieldErrors.put("title", titleErrors);

        List<String> descErrors = ValidationUtil.validateDescription(description);
        if (!descErrors.isEmpty()) fieldErrors.put("description", descErrors);

        List<String> statusErrors = ValidationUtil.validateStatus(status);
        if (!statusErrors.isEmpty()) fieldErrors.put("status", statusErrors);

        List<String> priorityErrors = ValidationUtil.validatePriority(priority);
        if (!priorityErrors.isEmpty()) fieldErrors.put("priority", priorityErrors);

        return fieldErrors;
    }
}
