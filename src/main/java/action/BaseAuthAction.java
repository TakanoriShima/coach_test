package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;
import model.User;
import util.AuthUtil;

public abstract class BaseAuthAction implements Action {

	@Override
	public final String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User loginUser = AuthUtil.getLoginUser(request);
		if (loginUser == null) {
			// 未ログイン：ログインへ（元URL保存）
			return AuthUtil.redirectToLogin(request);
		}

		// 認証OK：サブクラスの処理へ
		return executeAuthenticated(request, response, loginUser);
	}

	protected abstract String executeAuthenticated(HttpServletRequest request, HttpServletResponse response,
			User loginUser) throws ServletException, IOException;
}
