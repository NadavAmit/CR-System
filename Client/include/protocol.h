#pragma once
#include <unordered_map>
#include "operation.h"
class Protocol
{

public:

	int getOPCode(std::string input);
	Operation* getOPClass(std::string input);
	Protocol();
private:
	std::unordered_map<std::string, int> nameToCommandHashMap;
};
