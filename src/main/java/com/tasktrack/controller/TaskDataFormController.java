package com.tasktrack.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tasktrack.config.DbConfig;
import com.tasktrack.model.TaskModel;
import com.tasktrack.service.TaskService;
import com.tasktrack.service.ViewTaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller for handling task data form operations
 * Supports adding, updating, and deleting tasks
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/taskDataForm" })
public class TaskDataFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ViewTaskService viewTaskService;
    private TaskService taskService;
    
    /**
     * Initialize the controller with required services
     */
    @Override
    public void init() throws ServletException {
        viewTaskService = new ViewTaskService();
        taskService = new TaskService();
    }
    
    /**
     * Handle GET requests - display form
     * Loads task data if ID is provided for editing
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String taskIdParam = request.getParameter("id");
            if (taskIdParam != null && !taskIdParam.isEmpty()) {
                try {
                    int taskId = Integer.parseInt(taskIdParam);
                    TaskModel task = viewTaskService.getTaskByIdSafe(taskId);
                    
                    if (task != null) {
                        request.setAttribute("task", task);
                    } else {
                        request.setAttribute("error", "Task not found. ID: " + taskId);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid task ID format.");
                }
            }

            populateReferenceData(request);
        } catch (Exception e) {
            System.err.println("Error loading form data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading form data: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/WEB-INF/pages/taskDataForm.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process form submission
     * Handles add, update, and delete operations
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {     
        String action = request.getParameter("action");
        TaskModel taskModel = extractTaskModelFromRequest(request);
        
        try {

            String validationError = validateTaskData(taskModel);
            if (validationError != null) {
                request.setAttribute("error", validationError);
                request.setAttribute("task", taskModel);
                populateReferenceData(request);
                request.getRequestDispatcher("/WEB-INF/pages/taskDataForm.jsp").forward(request, response);
                return;
            }
            
            boolean success = false;
            if ("update".equals(action)) {
                success = processUpdateTask(request, taskModel);
            } else if ("delete".equals(action)) {
                success = processDeleteTask(request, taskModel);
            } else {
                success = processAddTask(request, taskModel);
            }
                        
            populateReferenceData(request);
            request.getRequestDispatcher("/WEB-INF/pages/taskDataForm.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error processing form: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing form: " + e.getMessage());
            request.setAttribute("task", taskModel);
            
            try {
                populateReferenceData(request);
            } catch (Exception ex) {
                System.err.println("Error loading reference data: " + ex.getMessage());
            }
            
            request.getRequestDispatcher("/WEB-INF/pages/taskDataForm.jsp").forward(request, response);
        }
    }
    
    /**
     * Extract task model data from the request parameters
     */
    private TaskModel extractTaskModelFromRequest(HttpServletRequest request) {
        TaskModel taskModel = new TaskModel();
        
        String taskIdParam = request.getParameter("taskId");
        if (taskIdParam != null && !taskIdParam.isEmpty()) {
            try {
                taskModel.setTaskId(Integer.parseInt(taskIdParam));
            } catch (NumberFormatException e) {
                System.err.println("Invalid task ID format: " + taskIdParam);
            }
        }
        
        taskModel.setTaskTitle(request.getParameter("taskTitle"));
        taskModel.setStatus(request.getParameter("status"));
        taskModel.setAssignedBy(request.getParameter("assignedBy"));
        taskModel.setAssignedTo(request.getParameter("assignedTo"));
        taskModel.setProgress(request.getParameter("progress"));
        
        try {
            String assignedDateStr = request.getParameter("assignedDate");
            if (assignedDateStr != null && !assignedDateStr.isEmpty()) {
                taskModel.setAssignedDate(LocalDate.parse(assignedDateStr));
            } else {
                taskModel.setAssignedDate(LocalDate.now());
            }
            
            String dueDateStr = request.getParameter("dueDate");
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                taskModel.setDueDate(LocalDate.parse(dueDateStr));
            } else {
                taskModel.setDueDate(LocalDate.now());
            }
        } catch (Exception e) {
            System.err.println("Date parsing error: " + e.getMessage());
            taskModel.setAssignedDate(LocalDate.now());
            taskModel.setDueDate(LocalDate.now());
        }
        

        try {
            String difficultyStr = request.getParameter("difficultyRank");
            if (difficultyStr != null && !difficultyStr.isEmpty()) {
                taskModel.setDifficultyRank(Integer.parseInt(difficultyStr));
            } else {
                taskModel.setDifficultyRank(1);
            }
            
            String priorityStr = request.getParameter("priorityRank");
            if (priorityStr != null && !priorityStr.isEmpty()) {
                taskModel.setPriorityRank(Integer.parseInt(priorityStr));
            } else {
                taskModel.setPriorityRank(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("Number parsing error: " + e.getMessage());
            taskModel.setDifficultyRank(1);
            taskModel.setPriorityRank(1);
        }
        
        return taskModel;
    }
    
    /**
     * Validate task data before processing
     * @return null if valid, error message if invalid
     */
    private String validateTaskData(TaskModel taskModel) {
        if (taskModel.getTaskTitle() == null || taskModel.getTaskTitle().trim().isEmpty()) {
            return "Task title is required";
        }
        
        try (Connection conn = DbConfig.getDbConnection()) {
            if (conn == null) {
                return "Could not connect to database for validation";
            }
            
            if (!checkDifficultyExists(conn, taskModel.getDifficultyRank())) {
                return "The selected Difficulty Rank doesn't exist in the system.";
            }
            
            if (!checkPriorityExists(conn, taskModel.getPriorityRank())) {
                return "The selected Priority Rank doesn't exist in the system.";
            }
        } catch (Exception e) {
            System.err.println("Validation error: " + e.getMessage());
            return "Error validating task data: " + e.getMessage();
        }
        
        return null; 
    }
    
    /**
     * Process update task operation
     */
    private boolean processUpdateTask(HttpServletRequest request, TaskModel taskModel) {
        if (taskModel.getTaskId() <= 0) {
            request.setAttribute("error", "Invalid task ID for update operation");
            return false;
        }
        
        boolean success = taskService.updateTask(taskModel);
        if (success) {
            request.setAttribute("success", "Task updated successfully!");
            request.setAttribute("task", taskModel); 
        } else {
            request.setAttribute("error", "Failed to update task. Please try again.");
            request.setAttribute("task", taskModel); 
        }
        
        return success;
    }
    
    /**
     * Process delete task operation
     */
    private boolean processDeleteTask(HttpServletRequest request, TaskModel taskModel) {
        if (taskModel.getTaskId() <= 0) {
            request.setAttribute("error", "Invalid task ID for delete operation");
            return false;
        }
        
        boolean success = taskService.deleteTask(taskModel.getTaskId());
        if (success) {
            request.setAttribute("success", "Task deleted successfully!");
            request.removeAttribute("task"); 
        } else {
            request.setAttribute("error", "Failed to delete task. Please try again.");
            TaskModel freshTask = viewTaskService.getTaskByIdSafe(taskModel.getTaskId());
            if (freshTask != null) {
                request.setAttribute("task", freshTask);
            } else {
                request.setAttribute("task", taskModel);
            }
        }
        
        return success;
    }
    
    /**
     * Process add task operation
     */
    private boolean processAddTask(HttpServletRequest request, TaskModel taskModel) {
        boolean success = taskService.addTask(taskModel);
        if (success) {
            request.setAttribute("success", "Task added successfully!");
            request.removeAttribute("task");
        } else {
            request.setAttribute("error", "Failed to add task. Please try again.");
            request.setAttribute("task", taskModel);
        }
        
        return success;
    }
    
    /**
     * Check if a difficulty rank exists in the database
     */
    private boolean checkDifficultyExists(Connection conn, int difficultyRank) {
        try {
            String sql = "SELECT 1 FROM difficulty WHERE Difficulty_rank = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, difficultyRank);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            System.err.println("Error checking difficulty: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a priority rank exists in the database
     */
    private boolean checkPriorityExists(Connection conn, int priorityRank) {
        try {
            String sql = "SELECT 1 FROM priority WHERE Priority_rank = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, priorityRank);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            System.err.println("Error checking priority: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load reference data for dropdowns
     */
    private void populateReferenceData(HttpServletRequest request) {
        try {
            Connection conn = DbConfig.getDbConnection();
            if (conn != null) {
                List<Integer> difficultyRanks = new ArrayList<>();
                String diffSql = "SELECT Difficulty_rank FROM difficulty ORDER BY Difficulty_rank";
                PreparedStatement diffStmt = conn.prepareStatement(diffSql);
                ResultSet diffRs = diffStmt.executeQuery();
                while (diffRs.next()) {
                    difficultyRanks.add(diffRs.getInt("Difficulty_rank"));
                }
                request.setAttribute("difficultyRanks", difficultyRanks);
                
                List<Integer> priorityRanks = new ArrayList<>();
                String priSql = "SELECT Priority_rank FROM priority ORDER BY Priority_rank";
                PreparedStatement priStmt = conn.prepareStatement(priSql);
                ResultSet priRs = priStmt.executeQuery();
                while (priRs.next()) {
                    priorityRanks.add(priRs.getInt("Priority_rank"));
                }
                request.setAttribute("priorityRanks", priorityRanks);
                
                conn.close();
            } else {
                System.err.println("Could not establish database connection for reference data");
            }
        } catch (Exception e) {
            System.err.println("Error loading reference data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}