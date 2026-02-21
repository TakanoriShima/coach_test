package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.FavoriteToggleAction;
import action.HelloWorldAction;
import action.HomeAction;
import action.LoginAction;
import action.LogoutAction;
import action.TaskDeleteAction;
import action.TaskEditAction;
import action.TaskListAction;
import action.TaskNewAction;

@WebServlet("/app/*")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Map<String, Action> actionMap;

    @Override
    public void init() throws ServletException {
        try {
            actionMap = new HashMap<>();

            // 既存
            actionMap.put("hello", new HelloWorldAction());
            actionMap.put("task/list", new TaskListAction());
            actionMap.put("task/delete", new TaskDeleteAction());
            actionMap.put("task/edit", new TaskEditAction());

            // 第4回：認証用 Action 登録
            actionMap.put("login", new LoginAction());
            actionMap.put("logout", new LogoutAction());
            actionMap.put("home", new HomeAction());

            actionMap.put("task/new", new TaskNewAction());

            // 追加機能：お気に入り
            actionMap.put("favorite/toggle", new FavoriteToggleAction());

            System.out.println("[FrontController] initialized. actions=" + actionMap.keySet());
        } catch (Exception e) {
            System.err.println("[FrontController] init failed: " + e.getMessage());
            throw new ServletException("Failed to initialize FrontController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字化け対策（POST想定。GETでも害は少ない）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = normalizePathInfo(request.getPathInfo());
        System.out.println("[FrontController] request path=" + path);

        Action action = actionMap.get(path);
        if (action == null) {
            handleNotFound(request, response, path);
            return;
        }

        String result = executeAction(action, request, response);
        handleResult(result, request, response);
    }

    private String normalizePathInfo(String pathInfo) {
        // null / "/" / "" はデフォルト
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            return "hello";
        }

        // 先頭 "/" を除去
        String normalized = pathInfo.substring(1);

        // 末尾 "/" を除去（念のため）
        if (normalized.endsWith("/") && normalized.length() > 1) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        // 空になったらデフォルト
        if (normalized.isEmpty()) {
            return "hello";
        }

        return normalized;
    }

    private void handleNotFound(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        System.err.println("[FrontController] Action not found: " + path);
        System.err.println("[FrontController] Available actions: " + actionMap.keySet());

        // PDFの例に近い形：sendErrorで404
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "ページが見つかりません: " + path);
    }

    private String executeAction(Action action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("[FrontController] executing action=" + action.getClass().getSimpleName());
            return action.execute(request, response);
        } catch (ServletException | IOException e) {
            System.err.println("[FrontController] action error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[FrontController] unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Action実行エラー", e);
        }
    }

    private void handleResult(String result, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ★重要：Action側で sendError/sendRedirect などを呼んでレスポンス確定済みなら何もしない
        if (response.isCommitted()) {
            return;
        }

        if (result == null || result.isEmpty()) {
            throw new ServletException("Actionの戻り値がnull/空です");
        }

        if (result.startsWith("redirect:")) {
            // 例：redirect:/app/hello
            String to = result.substring("redirect:".length());
            response.sendRedirect(request.getContextPath() + to);
            return;
        }

        // forward
        RequestDispatcher dispatcher = request.getRequestDispatcher(result);
        dispatcher.forward(request, response);
    }
}
