package com.company;

import jdk.nashorn.internal.ir.Block;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Recreational on 12/3/2017.
 */
public class Client implements IClient {
    ///////////////////
    //Variables
    ///////////////////
    private Socket socket;
    private ClientInput input;
    private Thread inputThread;
    private ClientOutput output;
    private Thread outputThread;
    private BlockingQueue<String> messageQueue;
    private GUIClient guiClient;

    ///////////////////
    //Getters and Setters
    ///////////////////
    public Socket getSocket(){
        return this.socket;
    }

    public BlockingQueue<String> getMessages(){
        return this.messageQueue;
    }

    public String getCurrentMessage(){
        return this.input.getCurrentMessage();
    }

    ///////////////////
    //Constructors
    ///////////////////
    public Client(Socket socket, GUIClient guiClient){
        this.socket = socket;
        this.messageQueue = new LinkedBlockingQueue<String>();
        this.guiClient = guiClient;
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
        return;
    }

    public void sendMessage(String message){
        //Test message
        try {
            this.messageQueue.put(message);
        }catch(InterruptedException exception){
            Debug.Log("messageQueue Interrupted Exception: " + exception.getMessage(), this);
        }
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
        } catch (IOException exception) {
            Debug.Log("Closing Socket IOException: " + exception.getMessage(), this);
        }
    }

    //Update our gui client
    public void updateClientChatLog(String text){
        this.guiClient.updateChatLog(text);
    }
}
