package com.ra.base_spring_boot.utils;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;

@Service
public class UploadService {
    @Autowired
    private Drive drive;
    public String uploadFileToDrive(MultipartFile file) throws IOException {
        java.io.File tempFile = java.io.File.createTempFile("temp",null);
        file.transferTo(tempFile);
        String folderId = "11f8uG-CvAUssefujtcKrYJ-kQVriODcN";
        File fileMetaData = new File();
        fileMetaData.setName(tempFile.getName());
        fileMetaData.setParents(Collections.singletonList(folderId));
        FileContent mediaFile = new FileContent(file.getContentType(), tempFile);
        File uploadedFile = drive.files().create(fileMetaData,mediaFile).setFields("id, webViewLink, webContentLink").execute();
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");
        drive.permissions().create(uploadedFile.getId(), permission).execute();
        tempFile.delete();
        return  uploadedFile.getWebViewLink();
    }
}
