package com.company;

import java.io.*;

/**
 * Created by Recreational on 12/3/2017.
 */
public class ClientOutput implements Runnable {
    ///////////////////
    //Variables
    ///////////////////
    private IClient client;
    private DataOutputStream writer;

    ///////////////////
    //Constructors
    ///////////////////
    public ClientOutput(IClient client){
        this.client = client;
    }

    ///////////////////
    //Methods
    ///////////////////
    //To startup the thread
    public void run(){
        //Startup our streams
        try {
            writer = new DataOutputStream(client.getSocket().getOutputStream());
            Debug.Log("Started Output Steam",this);
        }catch(IOException exception){
            Debug.Log("IOException created: " + exception.getMessage(), this);
        }

        //Startup the thread
        while(true){
            sendData();
            try {
                Thread.sleep(1);
            }catch(Exception exception){
                //Do nothing
            }
        }
    }

    //We need to check if we need to receive data
    private void sendData(){
        //Check if we have any message to send
        if(client.getMessages().size() > 0){
            try {
                String message = client.getMessages().take();

                writer.writeBytes(message + "\n");

                Debug.Log("Sending message: " + message, this.client);
            }catch(IOException exception) {
                Debug.Log("Writer IOException: " + exception.getMessage(), this);
            }catch (InterruptedException exception){
                Debug.Log("Take() Interrupted Exception: " + exception.getMessage(), this);
            }
        }
    }

    public void stop(){
        try {
            writer.close();
            writer = null;
        }catch(IOException exception){
            Debug.Log("Closing Writer IOException: " + exception.getMessage(), this);
        }
    }
}
