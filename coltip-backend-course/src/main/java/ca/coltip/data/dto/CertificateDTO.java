package ca.coltip.data.dto;

public record CertificateDTO(
    int id,
    String content,
    UserDto user,
    CourseDTO course
) {
}