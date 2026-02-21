package action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Action;

public class HelloWorldAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String message;

		if (name != null && !name.isEmpty()) {
			message = "こんにちは、" + name + " さん！";
		} else {
			message = "Hello World! FrontController 動作確認";
		}

		request.setAttribute("message", message);
		request.setAttribute("currentTime", new Date());
		request.setAttribute("pathInfo", request.getPathInfo());
		request.setAttribute("userAgent", request.getHeader("User-Agent"));
		request.setAttribute("remoteAddr", request.getRemoteAddr());

		return "/WEB-INF/views/hello.jsp";
	}
}
