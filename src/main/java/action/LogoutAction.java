package action;

import controller.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.SessionUtil;

public class LogoutAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        // セッション破棄（存在しなくてもOKなように実装しておく）
        SessionUtil.logout(request);

        // ログアウト後はログイン画面へ
        return "redirect:/app/login";
    }
}
