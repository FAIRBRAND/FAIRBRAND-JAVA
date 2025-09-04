package ca.coltip.service.impl;

import ca.coltip.data.dto.UserManagementDTO;
import ca.coltip.data.entities.User;
import ca.coltip.exceptions.UserException;
import ca.coltip.data.repository.UserRepository;
import ca.coltip.service.UserManagementService;
import ca.coltip.utils.RecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;

    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserManagementDTO> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAllByRecordStatus(RecordStatus.AVAILABLE.getCode(), PageRequest.of(page, size));
        return users.map(UserManagementDTO::new);
    }

    @Override
    public UserManagementDTO findUserById(int id) {
        User user = userRepository.findByIdAndRecordStatus(id, RecordStatus.AVAILABLE.getCode()).orElseThrow(() -> new UserException(
                "User with id " + id + " is not found", HttpStatus.NOT_FOUND
        ));
        return new UserManagementDTO(user);
    }

    @Override
    public UserManagementDTO save(UserManagementDTO user) {
        User newUser = user.toEntity();
        newUser.setDateCreated(LocalDateTime.now());
        User savedUser = userRepository.save(newUser);
        return new UserManagementDTO(savedUser);
    }

    @Override
    public UserManagementDTO update(UserManagementDTO user) {
        User userToUpdate = userRepository.findById(user.getId()).orElseThrow(() -> new UserException(
                "User with id " + user.getId() + " is not found", HttpStatus.NOT_FOUND
        ));

        userToUpdate.setDateModified(LocalDateTime.now());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setSurname(user.getSurname());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setCvContent(user.getCvContent());
        userToUpdate.setDescription(user.getDescription());

        User updatedUser = userRepository.save(userToUpdate);
        return new UserManagementDTO(updatedUser);
    }

    @Override
    public void deleteUserById(int id) {
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new UserException(
                "User with id " + id + " is not found", HttpStatus.NOT_FOUND
        ));
        userToDelete.setRecordStatus(RecordStatus.DELETED.getCode());
        userToDelete.setDateModified(LocalDateTime.now());
        userRepository.save(userToDelete);
    }
}
