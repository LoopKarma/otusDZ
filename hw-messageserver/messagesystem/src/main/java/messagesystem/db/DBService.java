package messagesystem.db;

import messagesystem.dto.CreateUserDTO;

public interface DBService {
    String requestUsers();
    String createUser(CreateUserDTO createUserDTO);
}
