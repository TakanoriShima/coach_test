package action;

import controller.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return "/WEB-INF/views/home.jsp";
	}
}
