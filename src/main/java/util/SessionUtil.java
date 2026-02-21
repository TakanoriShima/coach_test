package util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

public class SessionUtil {

	private static final String LOGIN_USER_KEY = "loginUser";
	private static final String LOGIN_TIME_KEY = "loginTime";

	public static User getLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;
		return (User) session.getAttribute(LOGIN_USER_KEY);
	}

	public static boolean isLoggedIn(HttpServletRequest request) {
		return getLoginUser(request) != null;
	}

	public static void login(HttpServletRequest request, User user) {
		// セッション固定化攻撃対策：古いセッション破棄→新規作成
		request.getSession().invalidate();
		HttpSession session = request.getSession(true);
		session.setAttribute(LOGIN_USER_KEY, user);
		session.setAttribute(LOGIN_TIME_KEY, new Date());
	}

	public static void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
	}
}
