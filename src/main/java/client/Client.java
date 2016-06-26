package client;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // IO sockets & server
    private final Socket server;
    private final OutputStream os;
    private final InputStream is;
    String serverHost;
    int serverPort;

    //  Client initializing
public Client(String host, int port) throws UnaccesibleRoutException {
        try {
            this.server = new Socket(host, port);
            is = server.getInputStream();
            os = server.getOutputStream();
            serverHost = host;
            serverPort =port;
        } catch (IOException e) {
            throw new UnaccesibleRoutException("Wrong host or port");
        }
    }

    public void connect() {
        new Thread(new ChatReadable()).start();
        new Thread(new ChatWritable()).start();
    }

    private class ChatReadable implements Runnable {
        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
           while (true) {
                try {
                    String msg = br.readLine();
                    if (msg == null) throw new RuntimeException("Connection with server lost");
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class ChatWritable implements Runnable {
        @Override
        public void run() {
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(os));
            Scanner sc = new Scanner(System.in);
            String msg;
            while ((msg = sc.nextLine())!=null){
                try {
                    bf.append(msg);
                    bf.newLine();
                    bf.flush();
                } catch (IOException e) {
                    throw new RuntimeException("can't send a message");
                }
            }
        }
    }

}
