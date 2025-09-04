package ca.coltip.service;

import ca.coltip.data.dto.CertificateDTO;

import java.util.List;

public interface ICertificateService {
    byte[] generateCertificate(int userId, int courseId);

    CertificateDTO updateCertificate(int id, CertificateDTO updatedData);

    void deleteCertificate(int id);

    List<CertificateDTO> getAllUserCertificates(int userId);

    CertificateDTO getCertificateById(int id);

    byte[] getGeneratedCertificateById(int id);
}
