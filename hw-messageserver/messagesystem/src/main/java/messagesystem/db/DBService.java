package messagesystem.db;

import common.dto.CreateUserDTO;

public interface DBService {
    String requestUsers();
    String createUser(CreateUserDTO createUserDTO);
}
