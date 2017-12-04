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
public class GUIMessenger implements ActionListener {
    ///////////////////
    //Variables
    ///////////////////
    private JFrame startingFrame;
    private Server server;

    ///////////////////
    //Constructors
    ///////////////////
    public GUIMessenger(){
        startingFrame = new JFrame();
        startingFrame.setTitle("Start Menu");
        startingFrame.add(this.startMenu());

        startingFrame.pack();
        startingFrame.setVisible(true);
    }

    ///////////////////
    //Methods
    ///////////////////
    private JPanel startMenu(){
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(1, 3);
        panel.setLayout(layout);

        //Create our buttons
        JButton serverButton = new JButton("Create Server"); serverButton.addActionListener(this);
        JButton clientButton = new JButton("Create Client"); clientButton.addActionListener(this);
        JButton exitButton = new JButton("Exit Application");   exitButton.addActionListener(this);

        //Add the buttons to the panel
        panel.add(serverButton);
        panel.add(clientButton);
        panel.add(exitButton);

        return panel;
    }

    //Our action listener
    public void actionPerformed(ActionEvent event){
        String command = event.getActionCommand();

        //Check if server was created
        if(command.equals("Create Server")){
            if(server == null){
                this.server = new Server(8888);
                Thread serverThread = new Thread(server);
                serverThread.start();
            }
        }

        //Check if a client was created
        if(command.equals("Create Client")){
            new GUIClient();
        }

        //Check if we want to exit the application
        if(command.equals("Exit Application")){
            System.exit(0);
        }
    }
}
