package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Supplier;

public class MessageEncoderDecoderBGR implements MessageEncoderDecoder<String> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int roundCounter = 0;
    private int amountOfZero = -1;  //DEFAULT VALUE
    private int zeroCounter = 0;
    private byte[] opcodebytes = new byte[2];
    private boolean needBytes = false;   //FOR COMMANDS THAT HAVE BYTES BUT NOT HAVE ZERO BITS
    private String courseNumber = "";

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if(nextByte == 0 && !needBytes){
            zeroCounter = zeroCounter + 1;
            if(zeroCounter != amountOfZero && roundCounter >=2){
                nextByte = 32;   //space
            }
        }
        if(needBytes){
            zeroCounter = zeroCounter + 1;
        }
        if(roundCounter == 3 && isRelevant()){
            courseNumber = Byte.toString(nextByte);
        }
        if(roundCounter >= 2){
            pushByte(nextByte);
            roundCounter = roundCounter + 1;
        }
        if(roundCounter < 2){
            opcodebytes[roundCounter] = nextByte;
            roundCounter = roundCounter + 1;
            if(roundCounter == 2){
                amountOfZero = getAmountOfZero();
            }
        }
        if(amountOfZero == zeroCounter){
            return popString();   //WE GET A COMPLETED LINE
        }
        else {
            return null; //not a complete line yet */
        }

        /* if (nextByte == '\n') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet */
    }

    private int getAmountOfZero(){
        if(opcodebytes[0] == 0 && opcodebytes[1] == 1){       //ADMINREG SITUATION
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 2){  //STUDENTREG SITUATION
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 3){  //LOGIN
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 4) {  //LOGOUT
            return 1;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 5) {   //COURSEREG
            needBytes = true;
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 6) {   //KDAMCHECK
            needBytes = true;
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 7) {   //COURSESTAT
            needBytes = true;
            return 3;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 8) {   //STUDENTSTAT
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 9) {   //ISREGISTERED
            needBytes = true;
            return 3;
        }
        else if(opcodebytes[0] == 1 && opcodebytes[1] == 0) {   //UNREGISTER
            needBytes = true;
            return 3;
        }
        else {  //(opcodebytes[0] == 1 && (opcodebytes[1] == 1)    MYCOURSES
            return 0;
        }
    }

    private boolean isRelevant(){
        if(opcodebytes[0] == 0){
            if(opcodebytes[1] == 5 || opcodebytes[1] == 6 || opcodebytes[1] == 7 || opcodebytes[1] == 9){
                return true;
            }
        }
        else{
            if(opcodebytes[1] == 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public byte[] encode(String message) {
        byte[] operationBytes;
        short kind;
        byte[] last = new byte[1];
        int index = message.indexOf(' ');
        String operation;
        String kindOfMessage = message.substring(0,index);      //ACK OR ERROR
        message = message.substring(index + 1);
        if(kindOfMessage.equals("ACK")){
            kind = 12;
            byte[] returnedMessage= shortToBytes(kind);
            index = message.indexOf(' ');
            if(index != -1){
                operation = message.substring(0,index);
                operationBytes = getOperationBytes(operation);
                returnedMessage = merge(returnedMessage,operationBytes);
                message = message.substring(index + 1);
                byte[] ackMessage = message.getBytes(StandardCharsets.UTF_8);
                returnedMessage = merge(returnedMessage,ackMessage);
            }
            else{
                operationBytes = getOperationBytes(message);
                returnedMessage = merge(returnedMessage,operationBytes);
            }
            returnedMessage = merge(returnedMessage,last);
            return returnedMessage;
        }
        else{
            kind = 13;
            byte[] returnedMessage= shortToBytes(kind);
            operationBytes = getOperationBytes(message);
            returnedMessage = merge(returnedMessage,operationBytes);
            return returnedMessage;
        }
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private byte[] getOperationBytes(String operation){
        short tmp;
        byte[] result;
        if(operation.equals("ADMINREG")){
            tmp = 1;
        }
        else if(operation.equals("STUDENTREG")){
            tmp = 2;
        }
        else if(operation.equals("LOGIN")){
            tmp = 3;
        }
        else if(operation.equals("LOGOUT")){
            tmp = 4;
        }
        else if(operation.equals("COURSEREG")){
            tmp = 5;
        }
        else if(operation.equals("KDAMCHECK")){
            tmp = 6;
        }
        else if(operation.equals("COURSESTAT")){
            tmp = 7;
        }
        else if(operation.equals("STUDENTSTAT")){
            tmp = 8;
        }
        else if(operation.equals("ISREGISTERED")){
            tmp = 9;
        }
        else if(operation.equals("UNREGISTER")){
            tmp = 10;
        }
        else{             //MYCOURSES
            tmp = 11;
        }
        result = shortToBytes(tmp);
        return result;
    }

    private byte[] merge(byte[] b1, byte[] b2){
        int newLength = b1.length + b2.length;
        byte[] result = new byte[newLength];
        System.arraycopy(b1,0,result,0,b1.length);
        System.arraycopy(b2,0,result,b1.length,b2.length);
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;    //adds the nextByte before increment len by 1
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = getMessage();
        len = 0;
        roundCounter = 0;
        amountOfZero = -1;  //DEFAULT VALUE
        zeroCounter = 0;
        needBytes = false;   //FOR COMMANDS THAT HAVE BYTES BUT NOT HAVE ZERO BITS
        return result;
    }

    private String getMessage(){
        String message = new String(bytes, 0, len, StandardCharsets.UTF_8);
        if(opcodebytes[0] == 0 && opcodebytes[1] == 1){
            message = "ADMINREG " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 2){
            message = "STUDENTREG " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 3){
            message = "LOGIN " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 4){
            message = "LOGOUT" + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 5){
            message = "COURSEREG " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 6){
            message = "KDAMCHECK " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 7){
            message = "COURSESTAT " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 8){
            message = "STUDENTSTAT " + message;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 9){
            message = "ISREGISTERED " + message;
        }
        else if(opcodebytes[0] == 1 && opcodebytes[1] == 0){
            message = "UNREGISTER " + message;
        }
        else{                                          //(opcodebytes[0] == 1 && (opcodebytes[1] == 1)
            message = "MYCOURSES" + message;
        }
        if(!courseNumber.equals("")){
            message = message.substring(0,message.length() - 1);
            message = message + courseNumber;
        }
        System.out.println("the result of decoding is:   " + message);
        return message;
    }
}
