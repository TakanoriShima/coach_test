package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import repository.TaskRepository;

public class TaskDeleteAction extends BaseAuthAction {

	private final TaskRepository taskRepo = new TaskRepository();

	@Override
	protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
			throws ServletException, IOException {

		String idStr = request.getParameter("id");
		if (idStr == null || idStr.isBlank()) {
			request.setAttribute("error", "IDが指定されていません");
			response.setStatus(400);
			return "/WEB-INF/views/error/400.jsp";
		}

		long taskId;
		try {
			taskId = Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "IDは数値で指定してください");
			response.setStatus(400);
			return "/WEB-INF/views/error/400.jsp";
		}

		System.out.println("[TaskDelete] loginUserId=" + loginUser.getId() + " taskId=" + taskId);

		// 他人のタスクなら403（認可）
		if (!taskRepo.isOwner(taskId, loginUser.getId())) {
			request.setAttribute("error", "この操作を実行する権限がありません");
			response.setStatus(403);
			return "/WEB-INF/views/error/403.jsp";
		}

		int rows = taskRepo.deleteByIdAndUserId(taskId, loginUser.getId());

		if (rows == 0) {
			request.setAttribute("error", "タスクが存在しないか、既に削除されています");
			response.setStatus(404);
			return "/WEB-INF/views/error/404.jsp";
		}

		return "redirect:/app/task/list";

	}
}
