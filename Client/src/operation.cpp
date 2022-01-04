#include "operation.h"

#include <boost/algorithm/string.hpp> 
#include <iostream>

using namespace std;

void Operation::shortToBytes(short num, char* bytesArr)
{
	bytesArr[0] = ((num >> 8) & 0xFF);
	bytesArr[1] = (num & 0xFF);
}
/*int Operation::Encode1(char* ptr) {
	short num = 2;
	shortToBytes(num, ptr);
	

	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));

	int i = 2;
	int& pi = i;
	result.erase(result.begin());
	for (string s : result) {

		for (char c : s) {
			ptr[pi] = c;
			pi++;
		}
		ptr[pi] = 0;
		pi++;
	}


	
	
}*/
AdminReg::AdminReg(std::string input) {
	message = input;
	opCode = 1;
}
int AdminReg::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	
	int i = 2;
	int& pi = i;
	result.erase(result.begin());
	
	for (string s : result) {
	
		for (char c : s) {
			ptr[pi] = (int)c;
			pi++;
		}
		ptr[pi] = 0;
		pi++;
	}

	return i;
}

StudentReg::StudentReg(std::string input)
{
	message = input;
	opCode = 2;
}
int StudentReg::Encode(char* ptr) {
	
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));

	int i = 2;
	int& pi = i;
	result.erase(result.begin());

	for (string s : result) {

		for (char c : s) {
			ptr[pi] = c;
			pi++;
		}
		ptr[pi] = 0;
		pi++;
	}

	return i;
}

LoginReq::LoginReq(std::string input) {
	message = input;
	opCode = 3;
}
int LoginReq::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));

	int i = 2;
	
	result.erase(result.begin());

	for (string s : result) {

		for (char c : s) {
			ptr[i] = c;
			i++;
		}
		ptr[i] = 0;
		i++;
	}

	return i;
}

LogoutReq::LogoutReq(std::string input) {
	message = input;
	opCode = 4;
}
int LogoutReq::Encode(char* ptr)
{
	int i = 2;
	shortToBytes(opCode, ptr);
	
	

	return i;
}

CourseReg::CourseReg(std::string input) {
	message = input;
	opCode = 5;
}
int CourseReg::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	int i = 2;
	int& pi = i;

	result.erase(result.begin());

	string courseNumString = message.substr(10, message.length());
	short courseNumShort = std::stoi(courseNumString);
	char courseNum[2];
	shortToBytes(courseNumShort, courseNum);

	for (char s : courseNum) {
		
		
		ptr[pi] = s;
		pi++;
	}


	
	return i;
}
KdamCheck::KdamCheck(std::string input) {
	message = input;
	opCode = 6;
}
int KdamCheck::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	int i = 2;
	int& pi = i;

	result.erase(result.begin());

	string courseNumString = message.substr(10, message.length());
	short courseNumShort = std::stoi(courseNumString);
	char courseNum[2];
	shortToBytes(courseNumShort, courseNum);

	for (char s : courseNum) {


		ptr[pi] = s;
		pi++;
	}

	return i;
}
CourseStat::CourseStat(std::string input) {
	message = input;
	opCode = 7;
}
int CourseStat::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	int i = 2;
	int& pi = i;

	result.erase(result.begin());

	string courseNumString = message.substr(11, message.length());
	short courseNumShort = std::stoi(courseNumString);
	char courseNum[2];
	shortToBytes(courseNumShort, courseNum);

	for (char s : courseNum) {


		ptr[pi] = s;
		pi++;
	}


	
	return i;
}
StudentStat::StudentStat(std::string input) {
	message = input;
	opCode = 8;
}
int StudentStat::Encode(char* ptr)
{
	
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	
	int i = 2;
	int& pi = i;
	result.erase(result.begin());
	for (string s : result) {
		
		for (char c : s) {
			ptr[pi] = c;
			pi++;
		}
		ptr[pi] = 0;
		pi++;
	}


	
	return i;
}
IsRegistered::IsRegistered(std::string input) {
	message = input;
	opCode = 9;
}
int IsRegistered::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	int i = 2;
	int& pi = i;

	result.erase(result.begin());

	string courseNumString = message.substr(13, message.length());
	short courseNumShort = std::stoi(courseNumString);
	char courseNum[2];
	shortToBytes(courseNumShort, courseNum);

	for (char s : courseNum) {


		ptr[pi] = s;
		pi++;
	}


	
	return i;
}
Unregister::Unregister(std::string input) {
	message = input;
	opCode = 10;
}
int Unregister::Encode(char* ptr)
{
	
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	
	
	int i = 2;
	int& pi = i;

	result.erase(result.begin());

	string courseNumString = message.substr(11, message.length());
	short courseNumShort = std::stoi(courseNumString);
	char courseNum[2];
	shortToBytes(courseNumShort, courseNum);

	for (char s : courseNum) {


		ptr[pi] = s;
		pi++;
	}

	return i;
}

MyCourses::MyCourses(std::string input) {
	message = input;
	opCode = 11;
}
int MyCourses::Encode(char* ptr)
{
	shortToBytes(opCode, ptr);
	vector<string> result;
	boost::split(result, message, boost::is_any_of(" "));
	int i = 2;

	return i;
}