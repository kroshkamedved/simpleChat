package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    // Singleton

    private static final Server INSTANCE;


    static {INSTANCE = new Server();}



    private boolean serverStatus;

    private ServerSocket serverSocket;

    private List<InputStream> clientsIS;

    private List<OutputStream> clientsOS;

    private List<BufferedWriter> clientsBW;

    private List<Socket> clients;




    /** return instance of server
     * or throws exception in case if server has already running
    */

    public static Server getServer(int port) throws IOException {

        if (INSTANCE.serverStatus ) throw new ServerIsRunningException();
        try {
            INSTANCE.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        INSTANCE.clients = Collections.synchronizedList(new ArrayList<>());
        INSTANCE.clientsOS = new ArrayList<>();
        INSTANCE.clientsIS = new ArrayList<>();
        INSTANCE.clientsBW = new ArrayList<>();
        INSTANCE.serverStatus = true;
        return INSTANCE;
    }



    /** runs server at first time
    */
    public void run() {

        while (serverStatus) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            runNewThread(client);

            // start sending server messages;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Scanner sc = new Scanner(System.in);
                    while (serverStatus){
                        toAll(sc.nextLine());
                    }
                }
            }).start();

        }
    }

    /** new thread generation
    */
    private void runNewThread(final Socket client) {

       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = client.getInputStream();
                    OutputStream os = client.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

                    clients.add(client);
                    clientsIS.add(is);
                    clientsOS.add(os);
                    clientsBW.add(bufferedWriter);

                    String message = String.format(" DATA:\n" +
                                                        "PORT: %s\n " +
                                                            "ADDRES:%s \n" +
                                                                " CONNECTION TIME:%s\n" +
                                                                            "PEOPLES ONLINE:%d\n",
                                        client.getPort(),
                                        client.getInetAddress(),
                                        new Date(),
                                        clients.size());


                    bufferedWriter.append("You are connected to the server "+message).flush();


                    toClient("Hi, this is SERVER!!!",bufferedWriter);
                    toAll("new client has been connected");

                    String msg;
                    while ((msg = bufferedReader.readLine()) != null){
                        System.out.println(msg);
                    }
                    try {
                        clients.remove(client);
                        clientsIS.remove(is);
                        clientsOS.remove(os);
                        client.close();
                    } catch (IOException ex){
                            //IGNORE//
                    }
                    System.err.println("Client gone.....");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();
    }

    // name is self-explainable

    public void stopServer(){
        INSTANCE.serverStatus = false;
    }



    // sent MSG to all
    private void toAll(String message){
        for (BufferedWriter writer : clientsBW) {
            try {
                writer.append(new Date().toString()+": "+message+"\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // sent MSG to client
    private void toClient(String message,BufferedWriter client){
        try {
                client.append(new Date().toString()+": "+message+"\n").flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




