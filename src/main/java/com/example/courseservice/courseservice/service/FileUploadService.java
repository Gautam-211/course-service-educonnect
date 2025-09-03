package com.example.courseservice.courseservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.courseservice.courseservice.exception.CloudinaryUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folderName) {
        try {
            // Upload the file to Cloudinary
            // "resource_type": "auto" lets Cloudinary automatically determine the file type
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", folderName, // Specify the folder name
                    "public_id", UUID.randomUUID().toString() // Use a random public_id for the file
            ));

            // Return the secure URL of the uploaded file
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new CloudinaryUploadException("Could not upload file to Cloudinary : " + e);
        }
    }
}