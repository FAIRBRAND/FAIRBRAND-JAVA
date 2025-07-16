package ca.coltip.data.dto;

import ca.coltip.data.entities.User;

public class UserManagementDTO {
    private final int id;
    private final String surname;
    private final String firstName;
    private final String phoneNumber;
    private final String email;
    private final String description;
    private final String cvContent;

    public UserManagementDTO(int id, String surname, String firstName, String phoneNumber, String email, String description, String cvContent) {
        this.id = id;
        this.surname = surname;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
        this.cvContent = cvContent;
    }

    public UserManagementDTO(User user) {
        this.id = user.getId();
        this.surname = user.getSurname();
        this.firstName = user.getFirstName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.description = user.getDescription();
        this.cvContent = user.getCvContent();
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setSurname(this.surname);
        user.setFirstName(this.firstName);
        user.setPhoneNumber(this.phoneNumber);
        user.setEmail(this.email);
        user.setDescription(this.description);
        user.setCvContent(this.cvContent);
        return user;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getCvContent() {
        return cvContent;
    }
}
