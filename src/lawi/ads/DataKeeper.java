package lawi.ads;

import lawi.ads.arduino.Arduino;
import lawi.ads.arduino.Package;

public class DataKeeper implements Runnable{

    private Arduino arduino;
    private boolean work = false, stop = false;

    public DataKeeper(Arduino arduino){
        this.arduino = arduino;

        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.setName("DataKeeper");
        thread.start();
    }

    public void stop(){
        stop = true;
        arduino.closeConnection();
    }

    @Override
    public void run(){
        try {
            arduino.openConnection();
            work = true;
            while (!stop){
                Package pack = arduino.nextPackage();
                System.out.println(pack); //TODO: Лог и запись в БД
            }
        } catch (Exception e){
            e.printStackTrace(); //TODO: Лог и вывод на консоль/сокет
        } finally {
            try {
                arduino.closeConnection();
                work = false;
            } catch (Exception e){
                e.printStackTrace(); //TODO: Лог и вывод на консоль/сокет
            }
        }
    }

    public boolean isWork(){
        return work;
    }
}
