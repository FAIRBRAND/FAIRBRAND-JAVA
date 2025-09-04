package ca.coltip.service.impl;

import ca.coltip.data.dto.CertificateDTO;
import ca.coltip.data.entities.Certificate;
import ca.coltip.data.entities.Course;
import ca.coltip.data.entities.CourseProgress;
import ca.coltip.data.entities.User;
import ca.coltip.data.mapper.CertificateMapper;
import ca.coltip.exceptions.ResourceNotFoundException;
import ca.coltip.data.repository.CertificateRepository;
import ca.coltip.data.repository.CourseProgressRepository;
import ca.coltip.data.repository.CourseRepository;
import ca.coltip.data.repository.UserRepository;
import ca.coltip.service.ICertificateService;
import ca.coltip.utils.CertificateGenerator;
import ca.coltip.utils.Constants;
import ca.coltip.utils.RecordStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService implements ICertificateService {
    private final CourseProgressRepository courseProgressRepository;
    private final CertificateGenerator certificateGenerator;
    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CertificateService(CourseProgressRepository courseProgressRepository, CertificateGenerator certificateGenerator, CertificateRepository certificateRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.courseProgressRepository = courseProgressRepository;
        this.certificateGenerator = certificateGenerator;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public byte[] generateCertificate(int userId, int courseId) {
        CourseProgress progress = courseProgressRepository.findProgressByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course progress", "userId", userId, "courseId", courseId));

        if (progress.getCompletionLevel() < Constants.COURSE_COMPLETED) {
            throw new RuntimeException("User has not completed course");
        }

        String courseContent = "Certificate for " + progress.getUser().getName()
                + " completing " + progress.getCourse().getCourseTitle();

        Certificate cert = certificateRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseGet(() -> {
                    Certificate newCert = new Certificate();
                    newCert.setContent(courseContent);
                    newCert.setUser(progress.getUser());
                    newCert.setCourse(progress.getCourse());
                    return certificateRepository.save(newCert);
                });

        return certificateGenerator.createCertificatePdf(progress.getUser().getName(), progress.getCourse().getCourseTitle(), cert.getCreatedAt());
    }

    @Override
    public CertificateDTO updateCertificate(int id, CertificateDTO updatedData) {
        Certificate cert = certificateRepository.findById(id)
                .filter(c -> c.getRecordStatus() == 1)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));

        cert.setContent(updatedData.content());

        if (updatedData.user() != null) {
            User user = userRepository.findById(updatedData.user().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", updatedData.user().getId()));
            cert.setUser(user);
        }

        if (updatedData.course() != null && updatedData.user() != null) {
            Course course = courseRepository.findById(updatedData.user().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", updatedData.user().getId()));
            cert.setCourse(course);
        }

        Certificate saved = certificateRepository.save(cert);
        return CertificateMapper.mapToDTO(saved);
    }

    @Override
    public void deleteCertificate(int id) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
        cert.setRecordStatus(RecordStatus.DELETED.getCode());
        certificateRepository.save(cert);
    }

    @Override
    public List<CertificateDTO> getAllUserCertificates(int userId) {
        List<Certificate> certs = certificateRepository.findAllByUserIdAndRecordStatus(userId, RecordStatus.AVAILABLE.getCode());
        return certs.stream()
                .map(CertificateMapper::mapToDTO)
                .toList();
    }

    @Override
    public CertificateDTO getCertificateById(int id) {
        Certificate cert = certificateRepository.findByIdAndRecordStatus(id, RecordStatus.AVAILABLE.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
        return CertificateMapper.mapToDTO(cert);
    }

    @Override
    public byte[] getGeneratedCertificateById(int id) {
        Certificate cert = certificateRepository.findByIdAndRecordStatus(id, RecordStatus.AVAILABLE.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));

        return certificateGenerator.createCertificatePdf(
                cert.getUser().getName(),
                cert.getCourse().getCourseTitle(),
                cert.getCreatedAt()
        );
    }
}
