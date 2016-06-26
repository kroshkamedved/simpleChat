package server;

import java.io.IOException;

/**
 * Created by kroshkamedved on 18.05.16.
 */
public class RunServer {
    public static void main(String[] args){
        try {
            Server server = Server.getServer(8080);
                server.run();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
