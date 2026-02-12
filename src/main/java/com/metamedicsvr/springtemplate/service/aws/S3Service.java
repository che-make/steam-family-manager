package com.metamedicsvr.springtemplate.service.aws;

import com.metamedicsvr.springtemplate.error.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // Method to upload a file to S3 and return the public URL
    public String uploadFile(MultipartFile file, String folderPath, String customFileName) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss-yyyyMMdd");
        String fileName = customFileName + "-" + LocalDateTime.now().format(formatter) + "." + extension;

        if (!folderPath.endsWith("/")) {
            folderPath += "/";
        }

        // Create the complete key for the file (folderPath + fileName)
        String key = folderPath + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(file.getContentType())
                .key(key)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileProcessingException("Error processing file " + file.getOriginalFilename());
        }

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    // Method to delete a file from S3 given its public URL
    public void deleteFileByUrl(String fileUrl) {
        String objectKey = extractKeyFromUrl(fileUrl);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    // Method to extract the key from the URL of the file
    private String extractKeyFromUrl(String fileUrl) {
        String baseUrl = "https://" + bucketName + ".s3.amazonaws.com/";
        return fileUrl.replace(baseUrl, "");  // Delete the base URL to get the key
    }
}