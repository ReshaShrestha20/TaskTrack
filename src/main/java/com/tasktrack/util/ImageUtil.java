package com.tasktrack.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

/**
 * Utility class for handling image file uploads.
 */
public class ImageUtil {
    
    /**
     * Extracts the file name from the given Part object.
     */
    public String getImageNameFromPart(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String imageName = null;
        
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        
        if (imageName == null || imageName.isEmpty()) {
            imageName = "default-avatar.png";
        }
        
        System.out.println("Extracted image name: " + imageName);
        return imageName;
    }

    /**
     * Uploads the image file to the server.
     */
    public boolean uploadImage(Part part, String saveFolder, ServletContext context) {
        String webappPath = context.getRealPath("/");
        String savePath = webappPath + "resources" + File.separator + "images" + 
                         File.separator + saveFolder;
        
        File fileSaveDir = new File(savePath);
        
        System.out.println("Image save path: " + savePath);
        System.out.println("Directory exists: " + fileSaveDir.exists());
        
        if (!fileSaveDir.exists()) {
            boolean dirCreated = fileSaveDir.mkdirs();
            System.out.println("Directory created: " + dirCreated);
            if (!dirCreated) {
                System.err.println("Failed to create directory: " + savePath);
                return false;
            }
        }
        
        System.out.println("Directory is writable: " + fileSaveDir.canWrite());
        
        try {
            String imageName = getImageNameFromPart(part);
            String filePath = savePath + File.separator + imageName;
            System.out.println("Full file path: " + filePath);
            
            part.write(filePath);
            System.out.println("Image file written successfully");
            
            // Verify the file was created
            File uploadedFile = new File(filePath);
            System.out.println("File exists after upload: " + uploadedFile.exists());
            System.out.println("File size: " + uploadedFile.length() + " bytes");
            
            return uploadedFile.exists() && uploadedFile.length() > 0;
        } catch (IOException e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates if the image file has a valid extension.
     */
    public boolean isValidImageExtension(Part part) {
        String fileName = getImageNameFromPart(part);
        if (fileName.equals("default-avatar.png")) {
            return true;
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        boolean isValid = extension.equals("jpg") || extension.equals("jpeg") || 
                         extension.equals("png") || extension.equals("gif");
                         
        System.out.println("Image extension: " + extension + ", is valid: " + isValid);
        return isValid;
    }
}