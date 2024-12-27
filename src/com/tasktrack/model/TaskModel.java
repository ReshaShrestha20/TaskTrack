/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tasktrack.model;

/**
 *
 * @author SUPRIYA
 */
public class TaskModel {
    private int taskId;
    private String taskTitle;
    private String assignedBy;
    private String assignedTo;
    private String assignedDate;
    private String dueDate;
    private String progress;
    private String status;

    public TaskModel() {
    }

    public TaskModel(int taskId,String taskTitle, String assignedBy, String assignedTo, String assignedDate, String dueDate, String progress, String status) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.progress = progress;
        this.status = status;
    }
    
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
    
    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}