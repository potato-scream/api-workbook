package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserModel {
    String name;
    String job;
    String email;
    String password;
}
