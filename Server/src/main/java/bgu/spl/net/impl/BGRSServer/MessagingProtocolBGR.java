package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Database;
import bgu.spl.net.api.MessagingProtocol;

public class MessagingProtocolBGR implements MessagingProtocol<String> {
    //FIELDS
    private boolean shouldTerminate = false;
    private boolean registered = false;
    private boolean loggedIn = false;
    private String loggedINName = "";
    private int adminOrStudent = -1;       //DEFAULT VALUE --- ADMIN = 0    STUDENT = 1
    private String operation = "";
    private String legal = "";

    @Override
    public String process(String msg) {
        String action = "";
        int index = msg.indexOf(' ');
        if(index != -1){
            operation = msg.substring(0,index);
            if(operation.equals("ADMINREG") || operation.equals("STUDENTREG")){
                legal = checkRegistrationLegality(msg);
                if(legal.equals("ACK")){
                    registered = true;
                }
            }
            else if(operation.equals("LOGIN")){
                legal = checkLoginInLegality(msg);
                if(legal.equals("ACK")){
                    loggedIn = true;
                    String[] tmp = interruptOperationUsernameAndPassword(msg);
                    loggedINName = tmp[1];
                }
            }
            else if(operation.equals("COURSEREG")){
                legal = checkCourseRegistration(msg);
            }
            else if(operation.equals("KDAMCHECK")){
                legal = checkKdam(msg);
            }
            else if(operation.equals("COURSESTAT")){
                legal = checkCourseStat(msg);
            }
            else if(operation.equals("STUDENTSTAT")){
                legal = checkStudentStat(msg);
            }
            else if(operation.equals("ISREGISTERED")){
                legal = checkIsRegistered(msg);
            }
            else if(operation.equals("UNREGISTER")){
                legal = checkUnregister(msg);
            }
        }
        else{
            if(msg.equals("LOGOUT")){
                legal = checkLogoutLegality();
                if(legal.equals("ACK")){
                    loggedIn = false;
                    shouldTerminate = true;
                }
            }
            else if(msg.equals("MYCOURSES")){
                legal = checkMyCourses(msg);
            }
        }
        action = parseAReturnMessage(legal);
        return action;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private String checkRegistrationLegality(String msg){         //"ADMINREG ADEL 123W"
        String[] details = interruptOperationUsernameAndPassword(msg);
        operation = details[0];
        if(operation.equals("ADMINREG")){
            if(registered || Database.getInstance().checkNameExist(operation,details[1])){
                return "Error";
            }
            Database.getInstance().addAdmin(details[1],details[2]);
            adminOrStudent = 0;
            return "ACK";
        }
        else{
            if(registered || Database.getInstance().checkNameExist(operation,details[1])){
                return "Error";
            }
            Database.getInstance().addStudent(details[1],details[2]);
            adminOrStudent = 1;
            return "ACK";
        }
    }
    //notRegistered == alreadyLoggedIn == usename doesnot match password
    private String checkLoginInLegality(String msg){                         //"LOGIN ADEL 123W"
        String[] details = interruptOperationUsernameAndPassword(msg);
        operation = details[0];
        if(!registered || loggedIn){
            return "Error";
        }
        //CHECK IF PASSWORD IS CORRECT
        if(!Database.getInstance().checkUserNamePassword(adminOrStudent, details[1], details[2])){
            return "Error";
        }
        return "ACK";
    }

    //notRegistered == notLoggedin
    private String checkLogoutLegality(){
        if(!registered || !loggedIn){
            return "Error";
        }
        return "ACK";
    }

    //admin tries to register to  course == not register == not logges in == there is no enough seats == he does not have al kdam courses
    private  String checkCourseRegistration(String msg){        //COURSEREG 912
        if(!registered || !loggedIn || adminOrStudent == 0){
            return "Error";
        }
        //kdam
        String[] details = interruptOperationAndCourseNumber(msg);
        if(!Database.getInstance().checkKdamCourses(loggedINName,details[1])){
            return "Error";
        }
        //there is no enough seats
        if(!Database.getInstance().checkCapacityAndRegister(loggedINName, details[1])){
            return "Error";
        }
        return "ACK";
    }

    private String checkKdam(String msg){
        if(!registered || !loggedIn){
            return "Error";
        }
        String[] details = interruptOperationAndCourseNumber(msg);
        String result = Database.getInstance().getKdamCourses(details[1]);
        return "ACK" + " " + result;
    }

    private String checkCourseStat(String msg){
        if(adminOrStudent == 1 || !registered || !loggedIn){
            return "Error";
        }
        String[] details = interruptOperationAndCourseNumber(msg);
        String result = Database.getInstance().getCourseStat(details[1]);
        return "ACK"+ " " + result;
    }

    private String checkStudentStat(String msg){    //"STUDENTSTAT Adel"
        if(!registered || !loggedIn){
            return "Error";
        }
        String result = "Student: " + loggedINName + "\n" + "Courses: ";
        int index = msg.indexOf(' ');
        index = index + 1;
        msg = msg.substring(index);
        String courses = Database.getInstance().getMyCourses(msg);
        return result + courses;
    }

    private String checkIsRegistered(String msg){
        if(!registered || !loggedIn){
            return "Error";
        }
        String[] details = interruptOperationAndCourseNumber(msg);
        if(Database.getInstance().isStudentRegistered(loggedINName, details[1])){
            return "ACK";
        }
        return "ACK";
    }

    private String checkUnregister(String msg){
        if(!registered || !loggedIn){
            return "Error";
        }
        String[] details = interruptOperationAndCourseNumber(msg);
        if(Database.getInstance().unregisterStudent(loggedINName,details[1])){
            return "ACK";
        }
        return "Error";
    }

    private String checkMyCourses(String msg){
        if(!registered || !loggedIn || adminOrStudent == 0){
            return "Error";
        }
        String result = Database.getInstance().getMyCourses(loggedINName);
        return "ACK " + result;
    }

    private String parseAReturnMessage(String legality){
        String result = "";
        if(legality.charAt(0) == 'A' && legality.charAt(1) == 'C' && legality.charAt(2) == 'K'){
            if(legality.length() > 3){
                legality = legality.substring(4);
                result = "ACK " + operation + " " + legality;
            }
            else {
                result = "ACK " + operation;
            }
            return result;
        }
        else{
            result = "ERROR ";
            result = result + operation;
        }
        return result;
    }

    private String[] interruptOperationUsernameAndPassword(String msg){    //RETURNS THE OPERATION USERNAME AND PASSWORD
        String[] result = new String[3];
        int index = msg.indexOf(' ');
        String operation = msg.substring(0,index);
        result[0] = operation;
        msg = msg.substring(index + 1);
        index = msg.indexOf(' ');
        String userName = msg.substring(0, index);
        result[1] = userName;
        String password = msg.substring(index + 1);
        result[2] = password;
        return result;
    }

    private String[] interruptOperationAndCourseNumber(String msg){         //RETURNS THE OPERATION AND COURSE NUMBER
        String[] result = new String[2];
        int index = msg.indexOf(' ');
        String tmp = msg.substring(0, index);
        result[0] = tmp;
        index = index + 1;
        msg = msg.substring(index);
        result[1] = msg;
        return result;
    }
}

//adel
