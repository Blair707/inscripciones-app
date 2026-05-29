package com.eduplatform.controller;

import com.eduplatform.dto.ApiResponseDto;
import com.eduplatform.dto.EnrollmentSummaryDto;
import com.eduplatform.service.EnrollmentService;
import com.eduplatform.service.FileService;
import com.eduplatform.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment Files", description = "Endpoints para gestión de archivos de inscripción en AWS S3")
@CrossOrigin(origins = "*")
public class EnrollmentFileController {

    private final EnrollmentService enrollmentService;
    private final FileService fileService;
    private final S3Service s3Service;

    @GetMapping("/{id}/generate")
    @Operation(summary = "Generar archivo TXT del resumen de inscripción")
    public ResponseEntity<byte[]> generateFile(@PathVariable Long id) throws Exception {
        EnrollmentSummaryDto summary = enrollmentService.getEnrollmentById(id);
        File file = fileService.generateEnrollmentTxtFile(summary);

        byte[] content = java.nio.file.Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PostMapping("/{id}/upload")
    @Operation(summary = "Subir resumen de inscripción a AWS S3")
    public ResponseEntity<ApiResponseDto<String>> uploadToS3(@PathVariable Long id) throws Exception {
        EnrollmentSummaryDto summary = enrollmentService.getEnrollmentById(id);
        File file = fileService.generateEnrollmentTxtFile(summary);
        String s3Key = s3Service.uploadFile(id, file);
        return ResponseEntity.ok(ApiResponseDto.success(s3Key, "Archivo subido exitosamente a S3"));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Descargar resumen de inscripción desde AWS S3")
    public ResponseEntity<byte[]> downloadFromS3(@PathVariable Long id) throws Exception {
        String fileName = "resumen-inscripcion-" + id + ".txt";
        byte[] content = s3Service.downloadFile(id, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Modificar resumen de inscripción en AWS S3")
    public ResponseEntity<ApiResponseDto<String>> updateInS3(@PathVariable Long id) throws Exception {
        EnrollmentSummaryDto summary = enrollmentService.getEnrollmentById(id);
        File file = fileService.generateEnrollmentTxtFile(summary);
        String s3Key = s3Service.updateFile(id, file);
        return ResponseEntity.ok(ApiResponseDto.success(s3Key, "Archivo actualizado exitosamente en S3"));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Eliminar resumen de inscripción de AWS S3")
    public ResponseEntity<ApiResponseDto<String>> deleteFromS3(@PathVariable Long id) {
        String fileName = "resumen-inscripcion-" + id + ".txt";
        s3Service.deleteFile(id, fileName);
        return ResponseEntity.ok(ApiResponseDto.success(fileName, "Archivo eliminado exitosamente de S3"));
    }
}