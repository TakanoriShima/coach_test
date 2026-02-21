package action;

import controller.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import repository.UserRepository;
import util.SessionUtil;

public class LoginAction implements Action {

	private UserRepository userRepository = new UserRepository();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String method = request.getMethod();

		if ("GET".equals(method)) {
			return showLoginForm(request, response);
		} else if ("POST".equals(method)) {
			return processLogin(request, response);
		} else {
			response.setStatus(405);
			return null;
		}
	}

	private String showLoginForm(HttpServletRequest request, HttpServletResponse response) {
		if (SessionUtil.isLoggedIn(request)) {
			return "redirect:/app/home";
		}
		return "/WEB-INF/views/login.jsp";
	}

	private String processLogin(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// 入力チェック（資料の「nullチェック＋入力値保持」）
		if (username == null || username.trim().isEmpty()) {
			request.setAttribute("error", "ユーザー名を入力してください");
			request.setAttribute("username", "");
			return "/WEB-INF/views/login.jsp";
		}
		if (password == null || password.isEmpty()) {
			request.setAttribute("error", "パスワードを入力してください");
			request.setAttribute("username", username);
			return "/WEB-INF/views/login.jsp";
		}

		User user = userRepository.findByUsername(username);

		if (user != null && password.equals(user.getPassword())) {
			SessionUtil.login(request, user);
			return "redirect:/app/home";
		} else {
			request.setAttribute("error", "ユーザー名またはパスワードが正しくありません");
			request.setAttribute("username", username); // 入力保持 
			return "/WEB-INF/views/login.jsp";
		}
	}
}
