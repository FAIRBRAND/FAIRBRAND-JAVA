package ca.coltip.data.responses;

import ca.coltip.data.dto.UserManagementDTO;
import ca.coltip.data.response.PaginatedResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserResponse extends PaginatedResponse {
    @JsonProperty("users")
    private List<UserManagementDTO> userManagementDTOS;

    public UserResponse(List<UserManagementDTO> userManagementDTOS, int totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.userManagementDTOS = userManagementDTOS;
    }
}
