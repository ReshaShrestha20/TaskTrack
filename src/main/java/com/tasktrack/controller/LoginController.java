package com.tasktrack.controller;

import java.io.IOException;

import com.tasktrack.model.UserModel;
import com.tasktrack.service.LoginService;
import com.tasktrack.util.CookiesUtil;
import com.tasktrack.util.RedirectionUtil;
import com.tasktrack.util.SessionUtil;
import com.tasktrack.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/login","/"})
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String rootURL = "WEB-INF/pages";
    private final String loginURL = rootURL+"/login.jsp";
    private final String homeURL = rootURL+"/home.jsp";
    
    private ValidationUtil validationUtil;
    private RedirectionUtil redirectionUtil;
    private LoginService loginService;

    public LoginController() {
        this.validationUtil = new ValidationUtil();
        this.redirectionUtil = new RedirectionUtil();
        this.loginService = new LoginService();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("username") != null) {
            String role = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("role".equals(c.getName())) {
                        role = c.getValue();
                        break;
                    }
                }
            }
            if ("admin".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/adminDashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }
        req.getRequestDispatcher(loginURL).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (!validationUtil.isNullOrEmpty(username) && !validationUtil.isNullOrEmpty(password)) {

            UserModel userModel = new UserModel(username, password);
            Boolean loginStatus = loginService.loginUser(userModel);

            if (loginStatus != null && loginStatus) {
                SessionUtil.setAttribute(req, "username", username);
                String userRole = loginService.getUserRole(username);
                if (userRole != null && userRole.equalsIgnoreCase("Admin")) {
                    CookiesUtil.addCookie(resp, "role", "admin", 30 * 24 * 60 * 60); // 30 days
                    resp.sendRedirect(req.getContextPath() + "/adminDashboard");
                } else {
                    CookiesUtil.addCookie(resp, "role", "user", 30 * 24 * 60 * 60); // 30 days
                    resp.sendRedirect(req.getContextPath() + "/home");
                }
            } else {
                handleLoginFailure(req, resp, loginStatus);
            }
        } else {
            redirectionUtil.setMsgAndRedirect(req, resp, "error", "Please fill all the fields!",
                    RedirectionUtil.loginURL);
        }
    }

    /**
     * Handles login failures by setting attributes and forwarding to the login
     * page.
     *
     * @param req HttpServletRequest object
     * @param resp HttpServletResponse object
     * @param loginStatus Boolean indicating the login status
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
            throws ServletException, IOException {
        String errorMessage;
        if (loginStatus == null) {
            errorMessage = "Our server is under maintenance. Please try again later!";
        } else {
            errorMessage = "User credential mismatch. Please try again!";
        }
        req.setAttribute("error", errorMessage);
        req.getRequestDispatcher(RedirectionUtil.loginURL).forward(req, resp);
    }
}