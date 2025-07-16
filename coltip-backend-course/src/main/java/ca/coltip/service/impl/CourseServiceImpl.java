package ca.coltip.service.impl;

import ca.coltip.data.dto.CourseDTO;
import ca.coltip.data.entities.Course;
import ca.coltip.data.entities.CourseType;
import ca.coltip.data.mapper.CourseMapper;
import ca.coltip.data.mapper.CourseTypeMapper;
import ca.coltip.exceptions.ResourceNotFoundException;
import ca.coltip.repository.CourseRepository;
import ca.coltip.repository.CourseTypeRepository;
import ca.coltip.service.ICourseService;
import ca.coltip.utils.RecordStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseTypeRepository courseTypeRepository) {
        this.courseRepository = courseRepository;
        this.courseTypeRepository = courseTypeRepository;
    }

    @Override
    public List<CourseDTO> fetchAll() {
        List<Course> courses = courseRepository.findAllByRecordStatus(RecordStatus.AVAILABLE.getCode());
        return courses.stream().map(CourseMapper::mapToCourseDTO).toList();
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        CourseType courseType = courseTypeRepository.findByIdAndRecordStatus(courseDTO.getCourseType().getId(), RecordStatus.AVAILABLE.getCode()).orElseThrow(
                () -> new ResourceNotFoundException("Course type", "id", courseDTO.getCourseType().getId())
        );
        Course newCourse = CourseMapper.mapToCourse(courseDTO);
        newCourse.setCourseType(courseType);
        Course created = courseRepository.save(newCourse);
        return CourseMapper.mapToCourseDTO(created);
    }


    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course courseToUpdate = courseRepository.findById(courseDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Course", "id", courseDTO.getId())
        );
        courseToUpdate.setCourseType(
                CourseTypeMapper.mapToCourseType(courseDTO.getCourseType())
        );
        courseToUpdate.setCourseTitle(courseDTO.getTitle());
        courseToUpdate.setPhoto(courseDTO.getPhoto());
        courseToUpdate.setPrice(courseDTO.getPrice());
        courseToUpdate.setDescription(courseDTO.getDescription());

        Course updatedCourse = courseRepository.save(courseToUpdate);
        return CourseMapper.mapToCourseDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(int id) {
        Course courseToUpdate = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Course", "id", id
        ));
        courseToUpdate.setRecordStatus(RecordStatus.DELETED.getCode());
        courseRepository.save(courseToUpdate);
    }
}
