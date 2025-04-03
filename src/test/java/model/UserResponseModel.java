package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserResponseModel {
    String job;
    String error;
    String updatedAt;
    String token;
    Integer id;
}
