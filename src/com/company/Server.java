package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Recreational on 12/3/2017.
 */
public class Server implements Runnable{
    //////////////////
    //Variables
    //////////////////
    private boolean isRunning;
    private int port;
    private ServerSocket serverSocket;
    private Collection<ServerClient> serverClients;

    //////////////////
    //Getters and Setters
    //////////////////

    //////////////////
    //Constructor
    //////////////////
    public Server(int port){
        this.port = port;
        this.isRunning = false;
        serverClients = new LinkedList<ServerClient>();
        try {
            serverSocket = new ServerSocket(port);
            Debug.Log("ServerSocket constructed", this);
        }catch(IOException exception){
            Debug.Log("ServerSocket IOException created: ", this);
        }

        Debug.Log("Server is setup", Debug.Level.everyone, this);
    }


    //////////////////
    //Methods
    //////////////////
    //What to do when we start up the server on a new thread
    public void run(){
        while(true){
            acceptConnections();
        }
    }

    //Accept the incoming clients
    private void acceptConnections(){
        //We need to start accepting clients
        Debug.Log("Aceepting connections...", Debug.Level.everyone, this);

        try{
            Socket socket = this.serverSocket.accept();

            createServerClient(socket);
        }catch(IOException excpetion){
            Debug.Log("Socket Acceptance IOException created: ", this);
        }
    }

    //We need to add the connection to the client list
    private void createServerClient(Socket socket){
        ServerClient client = new ServerClient(this, socket);
        client.start();
        serverClients.add(client);
        Debug.Log("ServerClient added", this);
    }

    //We need to broadcast the message to the rest of the clients
    public void broadcastMessage(ServerClient originClient, String messageToSend){
        //Go through all of our clients and send the message
        for(ServerClient client : serverClients){
            client.sendMessage(messageToSend);
        }
    }

    //We need to remove a client
    public void removeClient(ServerClient client){
        client.stop();
        serverClients.remove(client);
    }
}
