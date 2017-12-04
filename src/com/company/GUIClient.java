package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Recreational on 12/3/2017.
 */
public class GUIClient implements ActionListener{
    ///////////////////
    //Variables
    ///////////////////
    private JFrame frame;
    private Client client;
    private JTextField nameField;
    private JTextField messageField;
    private JTextArea chatLog;
    private JScrollPane scrollPane;

    ///////////////////
    //Constructor
    ///////////////////
    public GUIClient(){
        //We need to create the client
        try {
            this.client = new Client(new Socket("localhost", 8888), this);
            this.client.start();
            Debug.Log("Client created", this);
        }catch(IOException exception){
            Debug.Log("IOExcept created: " + exception.getMessage(), this);
        }

        //We need to create the gui for the client
        frame = new JFrame();
        frame.setTitle("GUI Client");

        frame.add(this.clientGui());

        frame.pack();
        frame.setVisible(true);
    }

    ///////////////////
    //Methods
    ///////////////////
    private JPanel clientGui(){
        //Set all of our client stuff
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        //Setup our fields and buttons
        nameField = new JTextField("[Your name]");
        chatLog = new JTextArea("Chat log shows here:",10, 30);
        chatLog.setLineWrap(true);
        chatLog.setEditable(false);
        scrollPane = new JScrollPane(chatLog);
        scrollPane.setAutoscrolls(true);
        messageField = new JTextField("[Your Message]");
        JButton sendButton = new JButton("Send Message"); sendButton.addActionListener(this);
        JButton exitButton = new JButton("Close Client"); exitButton.addActionListener(this);

        //Add the buttons to the frame
        panel.add(nameField);
        panel.add(scrollPane);
        panel.add(messageField);
        panel.add(sendButton);
        panel.add(exitButton);

        return panel;
    }

    //Update the message log
    public void updateChatLog(String text){
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
        chatLog.setText(text);
    }

    //Our action listener
    public void actionPerformed(ActionEvent event){
        String command = event.getActionCommand();

        //Check if we want to exit the application
        if(command.equals("Send Message")){
            client.sendMessage(this.nameField.getText() + ": " + this.messageField.getText());
        }

        if(command.equals("Close Client")){
            client.stop();
            frame.setVisible(false);
            frame = null;
        }
    }
}
