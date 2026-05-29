package com.eduplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(Long enrollmentId, File file) throws IOException {
        String key = enrollmentId + "/" + file.getName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("text/plain")
                .build();

        s3Client.putObject(request, RequestBody.fromFile(file));
        log.info("Archivo subido a S3: {}", key);
        return key;
    }

    public byte[] downloadFile(Long enrollmentId, String fileName) throws IOException {
        String key = enrollmentId + "/" + fileName;

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        byte[] content = response.readAllBytes();
        log.info("Archivo descargado desde S3: {}", key);
        return content;
    }

    public String updateFile(Long enrollmentId, File file) throws IOException {
        log.info("Actualizando archivo en S3 para inscripcion: {}", enrollmentId);
        return uploadFile(enrollmentId, file);
    }

    public void deleteFile(Long enrollmentId, String fileName) {
        String key = enrollmentId + "/" + fileName;

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(request);
        log.info("Archivo eliminado de S3: {}", key);
    }
}