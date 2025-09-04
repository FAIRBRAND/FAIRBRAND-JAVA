package ca.coltip.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {
    @JsonProperty("id")
    public int id;
    @JsonProperty("surname")
    public String surname;
    @JsonProperty("firstname")
    public String firstName;
    @JsonProperty("phoneNumber")
    public String phoneNumber;
    @JsonProperty("email")
    public String email;
    @JsonProperty("description")
    public String description;
    @JsonProperty("cvContent")
    public String cvContent;
}
