package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Database;
import bgu.spl.net.srv.Server;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TPCMain {
    public static void main(String[] args) throws IOException {
        boolean result = Database.getInstance().initialize("Courses.txt");
        if(!result){
            throw new FileNotFoundException();
        }
        Server server= Server.threadPerClient(Integer.parseInt(args[0]), MessagingProtocolBGR::new, MessageEncoderDecoderBGR::new);
        server.serve();
    }
}




/*
1.encode         v
2.error messages  v
3.ack messages   v
4.decoder in cpp
5.update tpc   v
 */