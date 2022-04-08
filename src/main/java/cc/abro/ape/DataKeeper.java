package cc.abro.ape;

import cc.abro.ape.arduino.Arduino;
import cc.abro.ape.arduino.Package;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DataKeeper implements Runnable{

    private final Arduino arduino;
    private final Set<Consumer<Package>> listeners = new HashSet<>();

    private boolean work = false, stop = false;

    public DataKeeper(Arduino arduino){
        this.arduino = arduino;

        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.setName("DataKeeper");
        thread.start();
    }

    @Override
    public void run(){
        try {
            arduino.openConnection();

            work = true;
            while (!stop){
                Package pack = arduino.nextPackage();
                for (Consumer<Package> listener : listeners) {
                    listener.accept(pack);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                arduino.closeConnection();
                work = false;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addListener(Consumer<Package> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<Package> listener) {
        listeners.remove(listener);
    }

    public void stop(){
        stop = true;
        arduino.closeConnection();
    }

    public boolean isWork(){
        return work;
    }
}