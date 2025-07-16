package ca.coltip.controller;

import ca.coltip.data.dto.CertificateDTO;
import ca.coltip.response.ApiResponse;
import ca.coltip.service.impl.CertificateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{userId}/{courseId}")
    public ResponseEntity<byte[]> generateCertificate(@PathVariable int userId,
                                                      @PathVariable int courseId) {
        byte[] pdf = certificateService.generateCertificate(userId, courseId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .body(pdf);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificateDTO>> updateCertificate(@PathVariable int id,
                                                                         @RequestBody CertificateDTO dto) {
        CertificateDTO updated = certificateService.updateCertificate(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Certificate updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCertificate(@PathVariable int id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Certificate deleted successfully", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CertificateDTO>>> getAllUserCertificates(@PathVariable int userId) {
        List<CertificateDTO> certificates = certificateService.getAllUserCertificates(userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Certificates fetched successfully", certificates));
    }

    @GetMapping("/generated/{id}")
    public ResponseEntity<byte[]> getGeneratedCertificateById(@PathVariable int id) {
        byte[] pdf = certificateService.getGeneratedCertificateById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .body(pdf);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponse<CertificateDTO>> getCertificateById(@PathVariable int id) {
        CertificateDTO dto = certificateService.getCertificateById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Certificate fetched successfully", dto));
    }
}