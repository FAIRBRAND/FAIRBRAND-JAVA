package ca.coltip.controller;

import ca.coltip.data.dto.UserManagementDTO;
import ca.coltip.data.requests.UserRequest;
import ca.coltip.data.response.ApiResponse;
import ca.coltip.data.responses.UserResponse;
import ca.coltip.service.UserManagementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PreAuthorize("hasRole('CAN_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Page<UserManagementDTO> userManagementDTOPage = userManagementService.getAllUsers(page, size);
        UserResponse userResponse = new UserResponse(
                userManagementDTOPage.getContent(),
                userManagementDTOPage.getNumberOfElements(),
                userManagementDTOPage.getTotalPages(),
                userManagementDTOPage.getNumber(),
                userManagementDTOPage.getSize()
        );
        ApiResponse<UserResponse> response = new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", userResponse);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CAN_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserManagementDTO>> findUserById(@PathVariable int id) {
        UserManagementDTO userManagementDTO = userManagementService.findUserById(id);
        ApiResponse<UserManagementDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", userManagementDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CAN_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserManagementDTO>> createUser(@RequestBody UserRequest userRequest) {
        UserManagementDTO userManagementDTO = new UserManagementDTO(
                userRequest.id,
                userRequest.surname,
                userRequest.firstName,
                userRequest.phoneNumber,
                userRequest.email,
                userRequest.description,
                userRequest.cvContent
        );

        UserManagementDTO savedUser = userManagementService.save(userManagementDTO);
        ApiResponse<UserManagementDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "User created successfully", savedUser);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CAN_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserManagementDTO>> updateUser(@PathVariable int id, @RequestBody UserRequest userRequest) {
        UserManagementDTO userManagementDTO = new UserManagementDTO(
                id,
                userRequest.surname,
                userRequest.firstName,
                userRequest.phoneNumber,
                userRequest.email,
                userRequest.description,
                userRequest.cvContent
        );

        UserManagementDTO savedUser = userManagementService.update(userManagementDTO);
        ApiResponse<UserManagementDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully", savedUser);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CAN_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable int id) {
        userManagementService.deleteUserById(id);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "User with id "+id+" has been successfully deleted", null);
        return ResponseEntity.ok(response);
    }
}
