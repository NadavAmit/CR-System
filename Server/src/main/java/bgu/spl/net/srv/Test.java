package bgu.spl.net.srv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Test {
    public static byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static void main(String []args) throws IOException {
        /*short num = 1;
        byte[] b = shortToBytes(num);
        System.out.println(b[0]); //0
        System.out.println(b[1]); //1
        byte[] b1 = {65,68,69,76,0,87,65,84,84,65,68};
        byte[] b2 = {65,68,69,76,48,87,65,84,84,65,68};
        byte[] b3 = {65,68,69,76,87,65,84,84,65,68};
        byte[] b4 = {0,1,65,68,69,76,87,65,84,84,65,68};
        byte[] b5 = {65,68,69,76,32,87,65,84,84,65,68};
        byte[] b6 = {0,48};
        String result1 = new String(b1, 0, b1.length, StandardCharsets.UTF_8);
        System.out.println("FIRST ATTEMPT:"+ result1);  //ADELWATTAD
        String result2 = new String(b2, 0, b2.length, StandardCharsets.UTF_8);
        System.out.println("SECOND ATTEMPT:"+ result2); //ADEL0WATTAD
        String result3 = new String(b3, 0, b3.length, StandardCharsets.UTF_8);
        System.out.println("SECOND ATTEMPT:"+ result3); //ADELWATTAD
        String result4 = new String(b4, 0, b4.length, StandardCharsets.UTF_8);
        System.out.println("SECOND ATTEMPT:"+ result4); //$ADELWATTAD
        String result5 = new String(b5, 0, b5.length, StandardCharsets.UTF_8);
        System.out.println("SECOND ATTEMPT:"+ result5); //ADEL WATTAD
        System.out.println(b6[0] == 0); //true
        System.out.println(b6[1] == 0); //false
        System.out.println(b6[1] == 48); //true
        String name = "AdelWattad";
        int index = name.indexOf("d");
        System.out.println(index);
        name = name.substring(index,index + 2);
        System.out.println(name);
        String tmp = "Adel";
        byte[] b = tmp.getBytes(StandardCharsets.UTF_8);
        System.out.println(b[0]);  //65*/
        /*short s = 129;
        byte[] b = shortToBytes(s);
        System.out.println("length : "+b.length);
        System.out.println(b[0]);
        System.out.println(b[1]);*/
        //Database.getInstance().initialize();
        //byte[] bytes = {41,42,43};
        //String result1 = new String(bytes, 0, bytes.length, StandardCharsets.);
        //System.out.println(result1);
        byte b1 = 100;
        String str = Byte.toString(b1);
        System.out.println(str);
        byte[] b2 = str.getBytes(StandardCharsets.UTF_8);
        byte b3 = b2[0];
        System.out.println("b2 length is "+b2.length);
        System.out.println("b2 is : "+b2[0]);
        System.out.println("b3 is : "+b3);
    }
}
