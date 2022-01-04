package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int roundCounter = 0;
    private int amountOfZero = -1;  //DEFAULT VALUE
    private int zeroCounter = 0;
    private byte[] opcodebytes = new byte[2];
    private boolean needBytes = false;   //FOR COMMANDS THAT HAVE BYTES BUT NOT HAVE ZERO BITS

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison

        if(nextByte == 0){
            zeroCounter = zeroCounter + 1;
            if(zeroCounter != amountOfZero){
                nextByte = 32;   //space
            }
        }
        if(needBytes){
            zeroCounter = zeroCounter + 1;
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

    @Override
    public byte[] encode(String message) {
        byte[]  b = new byte[1 << 10];
        int bLength = 0;
        short opcode;
        short messageOpcode;
        if(message.charAt(0) == 'A'){     //ACK MESSAGE  -> opcode = 12
            opcode = 12;
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)((opcode >> 8) & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)(opcode & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 65;     //A
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 67;    //C
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 75;   //K
            int index = message.indexOf('K');   //index=2     //"ACK ADMINREG"
            index = index + 2;                //index = 4
            message = message.substring(index);      //"ADMINREG"
            if(message.equals("ADMINREG")){
                messageOpcode = 1;
            }
            else if(message.equals("STUDENTREG")){
                messageOpcode = 2;
            }
            else if(message.equals("LOGIN")){
                messageOpcode = 3;
            }
            else if(message.equals("LOGOUT")){
                messageOpcode = 4;
            }
            else if(message.equals("COURSEREG")){
                messageOpcode = 5;
            }
            else if(message.equals("KDAMCHECK")){
                messageOpcode = 6;
            }
            else if(message.equals("COURSESTAT")){
                messageOpcode = 7;
            }
            else if(message.equals("STUDENTSTAT")){
                messageOpcode = 8;
            }
            else if(message.equals("ISREGISTERED")){
                messageOpcode = 9;
            }
            else if(message.equals("UNREGISTER")){
                messageOpcode = 10;
            }
            else{                              //MYCOURSES
                messageOpcode = 11;
            }
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)((messageOpcode >> 8) & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)(messageOpcode & 0xFF);
        }
        else{
            opcode = 13;
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)((opcode >> 8) & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)(opcode & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 69;  //E
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 82;  //R
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 82;  //R
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 79;  //O
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = 82;  //R
            int index = message.indexOf(' ');       //index = 5   //MESSAGE = "ERROR ADMINREG ALREADYREGISTERED"
            index = index + 1;      //index = 6
            message = message.substring(index);    //"ADMINREG ALREADYREGISTERED
            index = message.indexOf(' ');      //index = 8
            String newMessage = message.substring(0,index);      //"ADMINREG"
            String letters = message.substring((index + 1));     //"ALREADYREGISTERED"
            byte[] bLetters = letters.getBytes(StandardCharsets.UTF_8);
            if(newMessage.equals("ADMINREG")){
                messageOpcode = 1;
            }
            else if(newMessage.equals("STUDENTREG")){
                messageOpcode = 2;
            }
            else if(newMessage.equals("LOGIN")){
                messageOpcode = 3;
            }
            else if(newMessage.equals("LOGOUT")){
                messageOpcode = 4;
            }
            else if(newMessage.equals("COURSEREG")){
                messageOpcode = 5;
            }
            else if(newMessage.equals("KDAMCHECK")){
                messageOpcode = 6;
            }
            else if(newMessage.equals("COURSESTAT")){
                messageOpcode = 7;
            }
            else if(newMessage.equals("STUDENTSTAT")){
                messageOpcode = 8;
            }
            else if(newMessage.equals("ISREGISTERED")){
                messageOpcode = 9;
            }
            else if(newMessage.equals("UNREGISTER")){
                messageOpcode = 10;
            }
            else{                              //MYCOURSES
                messageOpcode = 11;
            }
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)((messageOpcode >> 8) & 0xFF);
            if(bLength >= b.length){
                b = Arrays.copyOf(b, bLength * 2);
            }
            b[bLength++] = (byte)(messageOpcode & 0xFF);
            for(int i = 0; i < bLetters.length; i++){
                if(bLength >= b.length){
                    b = Arrays.copyOf(b, bLength * 2);
                }
                b[bLength++] = bLetters[i];
            }
        }
        return b;
        //return (message + "\n").getBytes(); //uses utf8 by default
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
        return result;
    }

    private int getAmountOfZero(){
        if(opcodebytes[0] == 0 && opcodebytes[1] == 1){       //ADMINREG SITUATION
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 2){  //STUDENTREG SITUATION
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 3){  //LOGIN
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 4) {  //LOGOUT
            return 0;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 5) {   //COURSEREG
            needBytes = true;
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 6) {   //KDAMCHECK
            needBytes = true;
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 7) {   //COURSESTAT
            needBytes = true;
            return 2;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 8) {   //STUDENTSTAT
            return 1;
        }
        else if(opcodebytes[0] == 0 && opcodebytes[1] == 9) {   //ISREGISTERED
            needBytes = true;
            return 2;
        }
        else if(opcodebytes[0] == 1 && opcodebytes[1] == 0) {   //UNREGISTER
            needBytes = true;
            return 2;
        }
        else {  //(opcodebytes[0] == 1 && (opcodebytes[1] == 1)    MYCOURSES
            return 0;
        }
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
            message = "LOGOUT " + message;
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
            message = "MYCOURSES " + message;
        }
        return message;
    }
}
