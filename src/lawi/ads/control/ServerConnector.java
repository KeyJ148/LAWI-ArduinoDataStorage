package lawi.ads.control;

import lawi.ads.ArduinoDataStorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerConnector implements Runnable{

    private int port;
    private boolean work = true;

    public ServerConnector(int port){
        this.port = port;

        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.setName("ServerConnector");
        thread.start();
    }

    public void stop(){
        work = false;
    }

    @Override
    public void run(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (work){
                try {
                    Socket socket = serverSocket.accept();
                    ArduinoDataStorage.controllers.add(new ControlStream(socket.getInputStream(), socket.getOutputStream()));
                } catch (IOException e){
                    e.printStackTrace(); //TODO: Лог
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Лог и выход
        }
    }
}
