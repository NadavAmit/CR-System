#include <stdlib.h>
#include <thread>
#include "connectionHandler.h"
#include <protocol.h>
#include <serverListener.h>


char* encodeMessage(ConnectionHandler& connectionHandler, Operation* op1,int& msgLen) {


    char msg[1024];
    char* msgPtr = msg;
    msgLen = op1->Encode(msgPtr);


    return msgPtr;
}
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    bool terminateIsTrue = false;
    bool* terminatePtr = &terminateIsTrue;
    

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }


    serverListener serverListener(connectionHandler);
    std::thread listenerThread(&serverListener::Listen,&serverListener);
    listenerThread.detach();


	//Encoder + Sender
    while (1) {
        

        if (terminateIsTrue) {
            
        }
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
        int len=line.length();


        Protocol p;
        Operation* op1 = p.getOPClass(line);
               
        char msg[1024];
        char* msgPtr = msg;
        int msgLen = op1->Encode(msgPtr);
        

      
        
        if (!connectionHandler.sendBytes(msg,msgLen)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        
        
		
        std::cout << "Sent " << msgLen << " bytes to server" << std::endl;


       
        memset(buf, 0, sizeof buf);
        
    }
    return 0;
}

