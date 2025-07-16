package ca.coltip.data.mapper;

import ca.coltip.data.dto.CourseDTO;
import ca.coltip.data.dto.LanguageDTO;
import ca.coltip.data.entities.Course;

public class CourseMapper {
    public static CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getCourseTitle());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setPhoto(course.getPhoto());
        courseDTO.setCourseType(CourseTypeMapper.matoCourseTypeDTO(course.getCourseType()));
        courseDTO.setLanguage(new LanguageDTO(course.getLanguage()));
        return courseDTO;
    }

    public static Course mapToCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setCourseTitle(courseDTO.getTitle());
        course.setPrice(courseDTO.getPrice());
        course.setPhoto(courseDTO.getPhoto());
        course.setCourseType(CourseTypeMapper.mapToCourseType(courseDTO.getCourseType()));
        course.setLanguage(courseDTO.getLanguage().toEntity());
        return course;
    }
}
