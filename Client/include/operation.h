#pragma once
#include <string>
#include <vector>
class Operation
{public:
	std::string message;
	short opCode;
	
	void shortToBytes(short num, char* bytesArr);
	virtual int Encode(char* ptr)=0;
};
class AdminReg :public Operation {
public:
	int Encode(char* ptr);
	AdminReg(std::string input);
};
class StudentReg :public Operation {
public:
	
	StudentReg(std::string input);
	int Encode(char* ptr);
};
class LoginReq :public Operation {
public:
	int Encode(char* ptr);
	LoginReq(std::string input);
};

class LogoutReq :public Operation {
public:
	int Encode(char* ptr);
	LogoutReq(std::string input);
};
class CourseReg :public Operation {
public:
	int Encode(char* ptr);
	CourseReg(std::string input);
};
class KdamCheck :public Operation {
public:
	int Encode(char* ptr);
	KdamCheck(std::string input);
};
class CourseStat :public Operation {
public:
	int Encode(char* ptr);
	CourseStat(std::string input);
};
class StudentStat :public Operation {
public:
	int Encode(char* ptr);
	StudentStat(std::string input);
};
class IsRegistered :public Operation {
public:
	int Encode(char* ptr);
	IsRegistered(std::string input);
};
class Unregister :public Operation {
public:
	int Encode(char* ptr);
	Unregister(std::string input);
};
class MyCourses :public Operation {
public:
	int Encode(char* ptr);
	MyCourses(std::string input);
};
