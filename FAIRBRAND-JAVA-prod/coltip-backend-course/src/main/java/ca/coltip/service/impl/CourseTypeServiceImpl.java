package ca.coltip.service.impl;

import ca.coltip.data.dto.CourseTypeDTO;
import ca.coltip.data.entities.CourseType;
import ca.coltip.data.mapper.CourseTypeMapper;
import ca.coltip.exceptions.ResourceNotFoundException;
import ca.coltip.data.repository.CourseTypeRepository;
import ca.coltip.service.ICourseTypeService;
import ca.coltip.utils.RecordStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTypeServiceImpl implements ICourseTypeService {
    private final CourseTypeRepository courseTypeRepository;

    public CourseTypeServiceImpl(CourseTypeRepository courseTypeRepository) {
        this.courseTypeRepository = courseTypeRepository;
    }

    @Override
    public List<CourseTypeDTO> fetchAll() {
        List<CourseType> courseTypeList = courseTypeRepository.findAllByRecordStatus(RecordStatus.AVAILABLE.getCode());
        return courseTypeList.stream()
                .map(CourseTypeMapper::matoCourseTypeDTO)
                .toList();
    }

    @Override
    public CourseTypeDTO createCourseType(CourseTypeDTO courseTypeDTO) {
        CourseType courseType = CourseTypeMapper.mapToCourseType(courseTypeDTO);
        CourseType newCourseType = courseTypeRepository.save(courseType);
        return CourseTypeMapper.matoCourseTypeDTO(newCourseType);
    }

    @Override
    public CourseTypeDTO updateCourseType(CourseTypeDTO courseTypeDTO) {
        CourseType courseTypeToUpdate = courseTypeRepository.findById(courseTypeDTO.getId()).orElseThrow(() -> new ResourceNotFoundException(
                "Course type", "id", courseTypeDTO.getId()
        ));
        courseTypeToUpdate.setCourseTypeName(courseTypeDTO.getName());
        CourseType updatedCourseType = courseTypeRepository.save(courseTypeToUpdate);
        return CourseTypeMapper.matoCourseTypeDTO(updatedCourseType);
    }

    @Override
    public void deleteCourseType(int id) {
        CourseType courseTypeToUpdate = courseTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Course type", "id", id
        ));
        courseTypeToUpdate.setRecordStatus(RecordStatus.DELETED.getCode());
        courseTypeRepository.save(courseTypeToUpdate);
    }
}
