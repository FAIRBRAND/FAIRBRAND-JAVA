package ca.coltip.data.mapper;

import ca.coltip.data.dto.CertificateDTO;
import ca.coltip.data.dto.CourseDTO;
import ca.coltip.data.dto.UserDto;
import ca.coltip.data.entities.Certificate;
import ca.coltip.data.entities.Course;
import ca.coltip.data.entity.User;

public class CertificateMapper {

    public static CertificateDTO mapToDTO(Certificate cert) {
        User user = cert.getUser();
        Course course = cert.getCourse();

        UserDto userDTO = new UserDto(user);

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