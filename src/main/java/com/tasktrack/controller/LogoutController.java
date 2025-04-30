package com.tasktrack.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.tasktrack.util.CookiesUtil;
import com.tasktrack.util.SessionUtil;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post called");
		CookiesUtil.deleteCookie(response, "role");
		SessionUtil.invalidateSession(request);
		response.sendRedirect(request.getContextPath() + "/login");
	}

}