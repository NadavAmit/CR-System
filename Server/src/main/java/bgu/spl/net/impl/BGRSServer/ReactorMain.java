package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Database;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class ReactorMain {
    public static void main (String[] args) throws IOException {
        Database.getInstance().initialize("./Courses.txt");
        Server server = Server.reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]), MessagingProtocolBGR::new, MessageEncoderDecoderBGR::new);
        server.serve();
    }
}
