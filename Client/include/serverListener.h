#pragma once
#include <string>
#include <connectionHandler.h>

class serverListener
{private:
	ConnectionHandler& _connectionHandler;
	std::string answer;
public:
	serverListener(ConnectionHandler& connectionHandler);
	void Listen();
	
};

