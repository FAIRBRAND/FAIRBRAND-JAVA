package ca.coltip.controller;

import ca.coltip.data.dto.CourseDTO;
import ca.coltip.response.ApiResponse;
import ca.coltip.service.ICourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDTO>>> fetchAll() {
        List<CourseDTO> courseDTOS = courseService.fetchAll();
        ApiResponse<List<CourseDTO>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Courses fetched successfully", courseDTOS);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO newCourse = courseService.createCourse(courseDTO);
        ApiResponse<CourseDTO> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), "Course created successfully", newCourse);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);
        CourseDTO updatedCourse = courseService.updateCourse(courseDTO);
        ApiResponse<CourseDTO> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Course updated successfully", updatedCourse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Course with id "+id+" has been successfully deleted", null);
        return ResponseEntity.ok(response);
    }
}
