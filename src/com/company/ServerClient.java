package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Recreational on 12/3/2017.
 */
public class ServerClient implements IClient {
    ///////////////////
    //Variables
    ///////////////////
    private Server server;
    private Socket socket;
    private ClientInput input;
    private Thread inputThread;
    private ClientOutput output;
    private Thread outputThread;
    private BlockingQueue<String> messageQueue;

    ///////////////////
    //Getters and Setters
    ///////////////////
    public Socket getSocket(){
        return this.socket;
    }
    public BlockingQueue<String> getMessages(){
        return this.messageQueue;
    }


    ///////////////////
    //Constructors
    ///////////////////
    public ServerClient(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
        this.messageQueue = new LinkedBlockingQueue<String>();
    }

    ///////////////////
    //Methods
    ///////////////////
    public void start(){
        //We need to start our input and output on threads
        input = new ClientInput(this);
        output = new ClientOutput(this);
        inputThread = new Thread(input);
        inputThread.start();
        outputThread = new Thread(output);
        outputThread.start();
    }

    public void sendMessage(String message){
        //Test message
        try {
            this.messageQueue.put(message);
        }catch(InterruptedException exception){
            Debug.Log("messageQueue Interrupted Exception: " + exception.getMessage(), this);
        }
    }

    public void broadcastMessage(String message){
        this.server.broadcastMessage(this, message);
    }

    public void stop(){
        try {
            inputThread.stop();
            inputThread = null;
            outputThread.stop();
            outputThread = null;
            input.stop();
            input = null;
            output.stop();
            output = null;
            socket.close();
            socket = null;
            server.removeClient(this);
        } catch (IOException exception) {
            Debug.Log("Closing Socket IOException: " + exception.getMessage(), this);
        }
    }
}
