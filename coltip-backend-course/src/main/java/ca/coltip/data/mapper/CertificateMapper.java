package ca.coltip.data.mapper;

import ca.coltip.data.dto.CertificateDTO;
import ca.coltip.data.dto.CourseDTO;
import ca.coltip.data.dto.UserManagementDTO;
import ca.coltip.data.entities.Certificate;
import ca.coltip.data.entities.Course;
import ca.coltip.data.entities.User;

public class CertificateMapper {

    public static CertificateDTO mapToDTO(Certificate cert) {
        User user = cert.getUser();
        Course course = cert.getCourse();

        UserManagementDTO userDTO = new UserManagementDTO(
                user.getId(),
                user.getSurname(),
                user.getFirstName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getDescription(),
                user.getCvContent()
        );

        CourseDTO courseDTO = CourseMapper.mapToCourseDTO(course);

        return new CertificateDTO(
                cert.getId(),
                cert.getContent(),
                userDTO,
                courseDTO
        );
    }

    public static Certificate mapToEntity(CertificateDTO dto) {
        Certificate cert = new Certificate();
        cert.setId(dto.id());
        cert.setContent(dto.content());
        return cert;
    }
}