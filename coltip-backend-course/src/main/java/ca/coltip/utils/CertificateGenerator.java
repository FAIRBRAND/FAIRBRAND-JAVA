package ca.coltip.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.*;

@Component
public class CertificateGenerator {
    public byte[] createCertificatePdf(String userName, String courseTitle, LocalDateTime dateTime) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Logo placeholder (top-left)
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(HELVETICA_BOLD), 18);
                contentStream.newLineAtOffset(100, 540);  // Near top-left
                contentStream.showText("Fair-brand");
                contentStream.endText();

                // Title - Certificate of Completion (center top)
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(HELVETICA_BOLD), 32);
                contentStream.newLineAtOffset(100, 400);
                contentStream.showText("Certificate of Completion");
                contentStream.endText();

                // Course Title
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(HELVETICA_BOLD), 24);
                contentStream.newLineAtOffset(100, 350);
                contentStream.showText(courseTitle);
                contentStream.endText();

                // Delivered to
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(HELVETICA_BOLD), 18);
                contentStream.newLineAtOffset(100, 300);
                contentStream.showText("Delivered to " + userName);
                contentStream.endText();

                // Date
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(HELVETICA_BOLD), 14);
                contentStream.newLineAtOffset(100, 100);
                contentStream.showText("Date: " + DateTimeUtils.formatDateTime(dateTime));
                contentStream.endText();
            } catch (IOException e) {
                throw new RuntimeException("Error generating PDF", e);
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
