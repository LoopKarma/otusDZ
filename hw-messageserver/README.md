# Important: This module should be opened as separate project

This example contains 3 separate applications:
- **MsServer** - Socket server
- **Frontend** - Frontend websocket Spring app
- **Storage** - Spring data app

To run example:
1. run MsServer, it should start on http://localhost:8080
2. run Storage, it will start on http://localhost:8090
3. run Frontend, it will randomly pick port, check console to find it later
4. All set

### Implementation details

#### All users request
First request to **Frontend** application will request all users, 
request will be passed to **MsServer** via Socket connection. 
It will find suitable handler(`messagesystem.db.handlers.GetUsersRequestHandler`).
Handler will request data from **Storage** pack it in a **MsServer**'s response message and push it to a message queue.
**MsServer** finds suitable handler for the response message (this time it's `messagesystem.frontend.handler.FrontendHandler`).
FrontendHandler will return result message to **Frontend** using stored socket's OutputStream.
**Frontend** will pass message's content to a proper websocket broker and frontend JavaScript renders data.

#### Create user request
Request to **Frontend** to creating new user again  
will be passed to **MsServer** via Socket connection. 
It will find suitable handler(`messagesystem.db.handlers.CreateUserRequestHandler`).
Handler will do a request against **Storage** then pack response in a **MsServer**'s message and return result back to **MsServer**.
**MsServer** processing the response message (this time it's `messagesystem.frontend.handler.FrontendHandler`).
FrontendHandler will return result message to **Frontend** using stored socket's OutputStream.
**Frontend** will pass message's content to a proper websocket broker and frontend JavaScript renders data.



