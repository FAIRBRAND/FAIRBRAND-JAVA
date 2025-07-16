package ca.coltip.data.dto;

public record CertificateDTO(
        int id,
        String content,
        UserManagementDTO user,
        CourseDTO course
) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public UserManagementDTO user() {
        return user;
    }

    @Override
    public CourseDTO course() {
        return course;
    }
}