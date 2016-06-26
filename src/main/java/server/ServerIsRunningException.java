package server;


public class ServerIsRunningException extends RuntimeException {
     ServerIsRunningException(){
        super("The server is running");
    }

}
