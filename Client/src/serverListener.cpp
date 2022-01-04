#include <serverListener.h>


serverListener::serverListener(ConnectionHandler& connectionHandler): _connectionHandler(connectionHandler){
}

void serverListener::Listen()
{   
    while(1){
    
    if (!_connectionHandler.getLine(answer)) {
        std::cout << "Disconnected. Exiting...\n" << std::endl;
        
        break;
    }
   
    
    
    std::cout << answer << " " << std::endl << std::endl;
    answer.clear();
    if (answer == "ACK 4") {
        std::cout << "Exiting...\n" << std::endl;
        break;
    }
    }
}
