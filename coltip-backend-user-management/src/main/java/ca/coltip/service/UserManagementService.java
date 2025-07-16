package ca.coltip.service;

import ca.coltip.data.dto.UserManagementDTO;
import org.springframework.data.domain.Page;

public interface UserManagementService {
    Page<UserManagementDTO> getAllUsers(int page, int size);
    UserManagementDTO findUserById(int id);
    UserManagementDTO save(UserManagementDTO user);
    UserManagementDTO update(UserManagementDTO user);
    void  deleteUserById(int id);
}
