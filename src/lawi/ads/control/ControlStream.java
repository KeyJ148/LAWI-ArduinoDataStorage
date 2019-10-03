package lawi.ads.control;

import lawi.ads.ArduinoDataStorage;

import java.io.*;
import java.util.Scanner;

public class ControlStream implements Runnable{

    private Scanner in;
    private PrintWriter out;

    private int id;
    private static int nextId = 0;

    public ControlStream(InputStream inputStream, OutputStream outputStream){
        in = new Scanner(inputStream);
        out = new PrintWriter(outputStream);
        id = nextId++;

        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.setName("ControlStream-" + (id));
        thread.start();
    }

    @Override
    public void run(){
        try {
            while (in.ioException() == null && !out.checkError()) {
                CommandHandler.handle(in.nextLine(), out);
            }
        } catch (Exception e){
            e.printStackTrace(); //TODO: Лог
        } finally {
            in.close();
            out.close();
            //TODO: Лог  in.ioException() и out.checkError()

            ArduinoDataStorage.controllers.remove(this);
        }
    }
}
