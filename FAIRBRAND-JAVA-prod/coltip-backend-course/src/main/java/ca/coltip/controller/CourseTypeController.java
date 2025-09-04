package ca.coltip.controller;

import ca.coltip.data.dto.CourseTypeDTO;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.service.ICourseTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courseTypes")
public class CourseTypeController {
    private final ICourseTypeService courseTypeService;

    public CourseTypeController(ICourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseTypeDTO>>> fetchAllCourseType() {
        List<CourseTypeDTO> courseTypeDTOs = courseTypeService.fetchAll();
        ApiResponse<List<CourseTypeDTO>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Course type fetched successfully", courseTypeDTOs);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseTypeDTO>> createCourseType(@RequestBody CourseTypeDTO courseTypeDTO) {
        CourseTypeDTO createdCourseType = courseTypeService.createCourseType(courseTypeDTO);
        ApiResponse<CourseTypeDTO> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), "Course type created successfully", createdCourseType);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseTypeDTO>> updateCourseType(@PathVariable int id, @RequestBody CourseTypeDTO courseTypeDTO) {
        courseTypeDTO.setId(id);
        CourseTypeDTO updatedCourseType = courseTypeService.updateCourseType(courseTypeDTO);
        ApiResponse<CourseTypeDTO> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Course type updated successfully", updatedCourseType);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourseType(@PathVariable int id) {
        courseTypeService.deleteCourseType(id);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Course type with id "+id+" has been successfully deleted", null);
        return ResponseEntity.ok(response);
    }
}
