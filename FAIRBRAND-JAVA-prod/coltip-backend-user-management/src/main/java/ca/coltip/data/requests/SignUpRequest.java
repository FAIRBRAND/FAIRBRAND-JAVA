package ca.coltip.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {
    @JsonProperty("firstname")
    public String firstname;
    @JsonProperty("lastname")
    public String lastname;
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
}
