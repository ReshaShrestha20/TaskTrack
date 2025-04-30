package com.tasktrack.util;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectionUtil {
	public static final String rootURL = "WEB-INF/pages";
	public static final String registerURL = rootURL + "register.jsp";
	public static final String loginURL = rootURL+"/login.jsp";
	public static final String homeURL = rootURL+"/home.jsp";
    /**
     * Sets an attribute in the request with a message
     * 
     * @param req     The HTTP request
     * @param msgType The type of message (error, success, etc.)
     * @param msg     The message content
     */
    public void setMsgAttribute(HttpServletRequest req, String msgType, String msg) {
        req.setAttribute(msgType, msg);
    }

    /**
     * Forwards the request to a specific page
     * 
     * @param req  The HTTP request
     * @param resp The HTTP response
     * @param page The destination page
     * @throws ServletException If the target resource throws this exception
     * @throws IOException      If the target resource throws this exception
     */
    public void redirectToPage(HttpServletRequest req, HttpServletResponse resp, String page)
            throws ServletException, IOException {
        req.getRequestDispatcher(page).forward(req, resp);
    }

    /**
     * Sets a message attribute and forwards to a specific page
     * 
     * @param req     The HTTP request
     * @param resp    The HTTP response
     * @param msgType The type of message (error, success, etc.)
     * @param msg     The message content
     * @param page    The destination page
     * @throws ServletException If the target resource throws this exception
     * @throws IOException      If the target resource throws this exception
     */
    public void setMsgAndRedirect(HttpServletRequest req, HttpServletResponse resp, String msgType, String msg,
            String page) throws ServletException, IOException {
        setMsgAttribute(req, msgType, msg);
        redirectToPage(req, resp, page);
    }
}