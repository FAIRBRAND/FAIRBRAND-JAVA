package ca.coltip.data.mapper;

import ca.coltip.data.dto.CourseTypeDTO;
import ca.coltip.data.entities.CourseType;

public class CourseTypeMapper {
    public static CourseTypeDTO matoCourseTypeDTO(CourseType courseType) {
        CourseTypeDTO courseTypeDTO = new CourseTypeDTO();
        courseTypeDTO.setId(courseType.getId());
        courseTypeDTO.setName(courseType.getCourseTypeName());
        return courseTypeDTO;
    }

    public static CourseType mapToCourseType(CourseTypeDTO courseTypeDTO) {
        CourseType courseType = new CourseType();
        courseType.setId(courseTypeDTO.getId());
        courseType.setCourseTypeName(courseTypeDTO.getName());
        return courseType;
    }
}
