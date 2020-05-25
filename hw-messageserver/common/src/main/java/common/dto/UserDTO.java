package common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
    private long id;

    private String name;

    private String login;

    private String password;
}
