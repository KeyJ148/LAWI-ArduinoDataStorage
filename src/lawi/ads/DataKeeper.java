package lawi.ads;

import lawi.ads.arduino.Arduino;
import lawi.ads.arduino.Package;

public class DataKeeper implements Runnable{

    private Arduino arduino;
    private boolean work = true;

    public DataKeeper(Arduino arduino){
        this.arduino = arduino;
        new Thread(this).start();
    }

    @Override
    public void run(){
        try {
            arduino.openConnection();
            while (true){
                Package pack = arduino.nextPackage();
                System.out.println(pack); //TODO: Лог и запись в БД
            }
        } catch (Exception e){
            work = false;

            e.printStackTrace(); //TODO: Лог и вывод на консоль/сокет
        }
    }

    public boolean isWork(){
        return work;
    }
}
