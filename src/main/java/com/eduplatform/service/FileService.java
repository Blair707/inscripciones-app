package com.eduplatform.service;

import com.eduplatform.dto.EnrollmentSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class FileService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public File generateEnrollmentTxtFile(EnrollmentSummaryDto summary) throws IOException {
        String fileName = "resumen-inscripcion-" + summary.getEnrollmentId() + ".txt";
        File file = new File(System.getProperty("java.io.tmpdir"), fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("================================================");
            writer.newLine();
            writer.write("       Resumen de Inscripcion - EduPlatform     ");
            writer.newLine();
            writer.write("================================================");
            writer.newLine();
            writer.write("N° Inscripcion : " + summary.getEnrollmentId());
            writer.newLine();
            writer.write("Fecha          : " + (summary.getEnrollmentDate() != null
                    ? summary.getEnrollmentDate().format(FORMATTER) : "N/A"));
            writer.newLine();
            writer.write("Estado         : " + summary.getStatus());
            writer.newLine();
            writer.newLine();
            writer.write("------------------------------------------------");
            writer.newLine();
            writer.write("Datos del Estudiante");
            writer.newLine();
            writer.write("------------------------------------------------");
            writer.newLine();
            writer.write("ID             : " + summary.getStudentId());
            writer.newLine();
            writer.write("Nombre         : " + summary.getStudentName());
            writer.newLine();
            writer.write("Email          : " + summary.getStudentEmail());
            writer.newLine();
            writer.newLine();
            writer.write("------------------------------------------------");
            writer.newLine();
            writer.write("Cursos Inscritos");
            writer.newLine();
            writer.write("------------------------------------------------");
            writer.newLine();

            for (EnrollmentSummaryDto.EnrollmentCourseItemDto course : summary.getCourses()) {
                writer.write("- " + course.getCourseName());
                writer.newLine();
                writer.write("  Instructor  : " + course.getInstructor());
                writer.newLine();
                writer.write("  Duracion    : " + course.getDurationHours() + " horas");
                writer.newLine();
                writer.write("  Categoria   : " + course.getCategory());
                writer.newLine();
                writer.write("  Costo       : $" + course.getCost());
                writer.newLine();
                writer.newLine();
            }

            writer.write("------------------------------------------------");
            writer.newLine();
            writer.write("Resumen de Pago");
            writer.newLine();
            writer.write("------------------------------------------------");
            writer.newLine();
            writer.write("Subtotal       : $" + summary.getSubtotal());
            writer.newLine();
            writer.write("Descuento      : " + summary.getDiscountPercentage() + "% (-$" + summary.getDiscountAmount() + ")");
            writer.newLine();
            writer.write("TOTAL          : $" + summary.getTotalAmount());
            writer.newLine();
            writer.write("Metodo de pago : " + (summary.getPaymentMethod() != null ? summary.getPaymentMethod() : "N/A"));
            writer.newLine();
            writer.write("================================================");
            writer.newLine();
        }

        log.info("Archivo TXT generado: {}", file.getAbsolutePath());
        return file;
    }
}