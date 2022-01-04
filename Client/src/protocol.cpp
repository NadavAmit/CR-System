#include "protocol.h"
#include <string>
#include <iostream>

using namespace std;


Protocol::Protocol() {
	nameToCommandHashMap = {
   {"ADMINREG",1},
   {"STUDENTREG",2},
   {"LOGIN",3},
   {"LOGOUT",4},
   {"COURSEREG",5},
   {"KDAMCHECK",6},
   {"COURSESTAT",7},
   {"STUDENTSTAT",8},
   {"ISREGISTERED",9},
   {"UNREGISTER",10},
   {"MYCOURSES",11},
   {"ACK",12},
   {"ERR",13},
	};
};


int Protocol::getOPCode(std::string input)
{
	int opCode;

	
	std::string command = input.substr(0, input.find(' '));
	auto it = nameToCommandHashMap.find(command);
	if (it == nameToCommandHashMap.end()) {
		std::cout << "ERROR : Command not found";
		opCode = 0;
	}
	else
	{
		
		opCode = it->second;
	}


	return opCode;

}

Operation* Protocol::getOPClass(std::string input) {
	int opCode = getOPCode(input);
	Operation* operation = nullptr;

	if (opCode == 1) {
		operation = new AdminReg(input);
	}
	if (opCode == 2) {
		operation = new StudentReg(input);
	}
	if (opCode == 3) {
		operation = new LoginReq(input);
	}
	if (opCode == 4) {
		operation = new LogoutReq(input);
	}
	if (opCode == 5) {
		operation = new CourseReg(input);
	}
	if (opCode == 6) {
		operation = new KdamCheck(input);
	}
	if (opCode == 7) {
		operation = new CourseStat(input);
	}
	if (opCode == 8) {
		operation = new StudentStat(input);
	}
	if (opCode == 9) {
		operation = new IsRegistered(input);
	}
	if (opCode == 10) {
		operation = new Unregister(input);
	}
	if (opCode == 11) {
		operation = new MyCourses(input);
	}
	return operation;
}
