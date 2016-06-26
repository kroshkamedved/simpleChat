package client;


public class RunClient {

    public static void main(String[] args) throws UnaccesibleRoutException {
        Client client = new Client("192.168.0.102",8080);
        client.connect();

    }
}
