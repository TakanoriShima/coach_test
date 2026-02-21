package util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

public class AuthUtil {

	private AuthUtil() {
	}

	// SessionUtil と同じキーを使う前提
	private static final String LOGIN_USER_KEY = "loginUser";
	private static final String RETURN_URL_KEY = "returnUrl";

	/** セッションからログインユーザー取得 */
	public static User getLoginUser(HttpServletRequest request) {
		if (request == null)
			return null;
		HttpSession session = request.getSession(false);
		if (session == null)
			return null;

		Object obj = session.getAttribute(LOGIN_USER_KEY);
		if (obj instanceof User)
			return (User) obj;

		// 型が壊れてたら安全側に倒す
		if (obj != null)
			session.removeAttribute(LOGIN_USER_KEY);
		return null;
	}

	/** ログイン済みか */
	public static boolean isLoggedIn(HttpServletRequest request) {
		return getLoginUser(request) != null;
	}

	/** 所有者チェック（DBを見ずに、ownerUserIdとログインユーザーIDを比較する版） */
	public static boolean isOwner(HttpServletRequest request, long ownerUserId) {
		User u = getLoginUser(request);
		return u != null && u.getId() == ownerUserId;
	}

	/** 統合チェック：ログイン済み かつ owner */
	public static boolean canAccess(HttpServletRequest request, long ownerUserId) {
		return isOwner(request, ownerUserId);
	}

	/**
	 * 未ログイン時：元URLをセッションに保存してログインへ FrontControllerは "redirect:/app/..."
	 * 形式を扱ってるのでそれに合わせる
	 */
	public static String redirectToLogin(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();
		String returnUrl = requestURI + (queryString != null ? "?" + queryString : "");

		HttpSession session = request.getSession(true);
		session.setAttribute(RETURN_URL_KEY, returnUrl);

		return "redirect:/app/login";
	}
}
