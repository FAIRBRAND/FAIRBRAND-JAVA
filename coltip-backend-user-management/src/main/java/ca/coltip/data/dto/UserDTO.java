package ca.coltip.data.dto;

import ca.coltip.data.entities.User;

public class UserDTO {
    private int id;
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setEmail(this.email);
        return user;
    }

    public String getEmail() {
        return email;
    }
}