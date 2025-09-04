package ca.coltip.service;

import ca.coltip.data.dto.CourseTypeDTO;

import java.util.List;

public interface ICourseTypeService {
    List<CourseTypeDTO> fetchAll();
    CourseTypeDTO createCourseType(CourseTypeDTO courseTypeDTO);
    CourseTypeDTO updateCourseType(CourseTypeDTO courseTypeDTO);
    void deleteCourseType(int id);
}
