package messagesystem.messagesystem;

import common.messaging.Message;
import common.messaging.MsClient;

public interface MessageSystem {

  void addClient(MsClient msClient);

  void removeClient(String clientId);

  boolean newMessage(Message msg);

  void dispose() throws InterruptedException;
}

