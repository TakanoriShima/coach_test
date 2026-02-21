package action;

import java.io.IOException;
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

public class TaskNewAction extends BaseAuthAction {

    private final TaskRepository taskRepo = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            return showNewForm(request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            return processNewTask(request, loginUser);
        }

        response.setStatus(405);
        return "redirect:/app/task/list";
    }

    /** 新規作成フォーム表示 */
    private String showNewForm(HttpServletRequest request) {
        // 初期値（初回表示用）
        if (request.getAttribute("title") == null) request.setAttribute("title", "");
        if (request.getAttribute("description") == null) request.setAttribute("description", "");
        if (request.getAttribute("status") == null) request.setAttribute("status", "pending");
        if (request.getAttribute("priority") == null) request.setAttribute("priority", "medium");

        return "/WEB-INF/views/task/new.jsp";
    }

    /** 新規作成処理（POST） */
    private String processNewTask(HttpServletRequest request, User loginUser) {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");

        // 未指定ならデフォルト（validateStatus/validatePriorityの前に整える）
        if (status == null || status.isBlank()) status = "pending";
        if (priority == null || priority.isBlank()) priority = "medium";

        // 詳細バリデーション（フィールド別）
        Map<String, List<String>> fieldErrors = validateTaskInputEnhanced(title, description, status, priority);

        // 入力値保持（エラーでも成功でも、JSPが拾える）
        request.setAttribute("title", title);
        request.setAttribute("description", description);
        request.setAttribute("status", status);
        request.setAttribute("priority", priority);

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("errors", fieldErrors);
            return "/WEB-INF/views/task/new.jsp";
        }

        // 保存
        Task task = new Task();
        task.setUserId(loginUser.getId());
        task.setTitle(title == null ? null : title.trim());
        task.setDescription((description == null || description.isBlank()) ? null : description.trim());
        task.setStatus(status);
        task.setPriority(priority);

        boolean ok = taskRepo.save(task); // saveがthrowsしない設計ならtry/catch不要

        if (ok) {
            request.getSession().setAttribute("flash", "タスクを作成しました");
            return "redirect:/app/task/list";
        } else {
            request.setAttribute("error", "保存に失敗しました");
            return "/WEB-INF/views/task/new.jsp";
        }
    }

    private Map<String, List<String>> validateTaskInputEnhanced(
            String title, String description, String status, String priority) {

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
