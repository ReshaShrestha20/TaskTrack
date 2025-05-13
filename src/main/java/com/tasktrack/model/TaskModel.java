package com.tasktrack.model;

import java.time.LocalDate;

public class TaskModel {
    private int taskId; 
    private String taskTitle;
    private String status;
    private LocalDate assignedDate;
    private LocalDate dueDate;
    private String progress;
    private String assignedBy;
    private String assignedTo;
    private int difficultyRank;
    private int priorityRank;

    // Constructors and getters/setters
    public TaskModel() {
     
    }

    // Getters and setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getDifficultyRank() {
        return difficultyRank;
    }

    public void setDifficultyRank(int difficultyRank) {
        this.difficultyRank = difficultyRank;
    }

    public int getPriorityRank() {
        return priorityRank;
    }

    public void setPriorityRank(int priorityRank) {
        this.priorityRank = priorityRank;
    }
    
    public String getDifficultyText() {
        if (difficultyRank <= 3) return "Low";
        if (difficultyRank <= 7) return "Medium";
        return "High";
    }
    
    public String getPriorityText() {
        if (priorityRank <= 3) return "High";
        if (priorityRank <= 7) return "Normal";
        if (priorityRank <= 10) return "Low";
        return "Urgent";
    }
    
    public String getStatusClass() {
        if (status == null) return "";
        
        String statusLower = status.toLowerCase();
        if (statusLower.contains("complete")) return "complete";
        if (statusLower.contains("progress")) return "in-progress";
        if (statusLower.contains("missed") || statusLower.contains("created")) return "missed";
        if (statusLower.contains("pending")) return "pending";
        
        return statusLower;
    }
    
    public String getStandardizedStatus() {
        if (status == null) return "";
        
        String statusLower = status.toLowerCase();
        if (statusLower.contains("complete")) return "Complete";
        if (statusLower.contains("progress")) return "In-progress";
        if (statusLower.contains("missed") || statusLower.contains("created")) return "Missed";
        
        return status; 
    }
}