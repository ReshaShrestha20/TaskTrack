package com.tasktrack.controller;

import java.io.IOException;
import java.util.List;

import com.tasktrack.model.TaskModel;
import com.tasktrack.service.ViewTaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller for displaying and searching tasks
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/viewTask" })
public class ViewTaskController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ViewTaskService viewTaskService;

    @Override
    public void init() throws ServletException {
        this.viewTaskService = new ViewTaskService();
    }

    /**
     * Handle GET requests - display task list with optional search filter
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String searchQuery = request.getParameter("search");
            List<TaskModel> tasks;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                tasks = viewTaskService.searchTasksByTitleSafe(searchQuery);
                request.setAttribute("searchQuery", searchQuery);
            } else {
                tasks = viewTaskService.getAllTasksSafe();
            }

            request.setAttribute("tasks", tasks);
        } catch (Exception e) {
            System.err.println("Error retrieving tasks: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve task data: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/pages/viewTask.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process search form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchInput = request.getParameter("searchInput");
        try {
            List<TaskModel> tasks;
            
            if (searchInput != null && !searchInput.trim().isEmpty()) {
                tasks = viewTaskService.searchTasksByTitleSafe(searchInput);
                request.setAttribute("searchQuery", searchInput);
            } else {
                tasks = viewTaskService.getAllTasksSafe();
            }
            
            request.setAttribute("tasks", tasks);            
        } catch (Exception e) {
            System.err.println("Error retrieving tasks: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve task data: " + e.getMessage());
        }
        
        request.getRequestDispatcher("WEB-INF/pages/viewTask.jsp").forward(request, response);
    }
}