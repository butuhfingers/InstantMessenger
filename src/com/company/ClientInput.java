package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Recreational on 12/3/2017.
 */
public class ClientInput implements Runnable {
    ///////////////////
    //Variables
    ///////////////////
    private IClient client;
    private Reader reader;
    private String currentMessage = "";
    private String guiLog = "";

    ///////////////////
    //Getters and Setters
    ///////////////////
    public String getCurrentMessage(){
        return currentMessage;
    }


    ///////////////////
    //Constructors
    ///////////////////
    public ClientInput(IClient client){
        this.client = client;
    }

    ///////////////////
    //Methods
    ///////////////////
    public void run(){
        //Startup our streams
        try {
            reader = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            Debug.Log("Started Input Steam",this);
        }catch(IOException exception){
            Debug.Log("IOException created: " + exception.getMessage(), this);
        }

        //Startup the thread
        while(true){
            receiveData();
            try {
                Thread.sleep(1);
            }catch(Exception exception){
                //Do nothing
            }
        }
    }

    //We need to check if we need to receive data
    private void receiveData(){
        try{
            if(!this.reader.ready()) {
                return;
            }
        }catch(IOException exception){
            client.stop();
            Debug.Log("Ready() IOException: " + exception.getMessage(), this);
            return;
        }


        try {
            int characterNumber = reader.read();
            char character;

            if(characterNumber == -1) {
                return;
            }

            character = (char)characterNumber;
            currentMessage += character;

            if(currentMessage.contains("\n")) {
                if (client.getClass() == ServerClient.class) {
                    ServerClient thisClient = (ServerClient) client;
                    thisClient.broadcastMessage(currentMessage);
                } else if (client.getClass() == Client.class) {
                    guiLog += currentMessage;
                    Client thisClient = (Client) client;
                    thisClient.updateClientChatLog(guiLog);
                }

                currentMessage = "";
            }

            Debug.Log("Receiving message: " + character, this);
        } catch (IOException exception) {
            Debug.Log("Reader IOException: " + exception.getMessage(), this);
        }
    }

    public void stop(){
        try {
            reader.close();
            reader = null;
        }catch(IOException exception){
            Debug.Log("Closing Reader IOException: " + exception.getMessage(), this);
        }
    }
}
