package com.company;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Recreational on 12/3/2017.
 */
public interface IClient {
    Socket getSocket();
    BlockingQueue<String> getMessages();
    void stop();
}
