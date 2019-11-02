package lawi.ads;

import lawi.ads.arduino.Arduino;
import com.fazecast.jSerialComm.SerialPort;
import lawi.ads.arduino.Package;
import lawi.ads.control.ControlStream;
import lawi.ads.control.ServerConnector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArduinoDataStorage {

    //TODO: заменить на загрузку из конфига, для controllers сделать функцию рассылки сообщения всем
    public static final int baudRate = 9600;
    public static final int P = (int) 1e9+7;
    public static final int blockSeparator = 0xAA9999AA;

    public static DataKeeper dataKeeper;
    public static List<ControlStream> controllers = Collections.synchronizedList(new ArrayList<>());

    private ServerConnector serverConnector;

    public ArduinoDataStorage(int port, boolean console, String COMPortPrefix){
        if (console) controllers.add(new ControlStream(System.in, System.out));
        if (port != -1) serverConnector = new ServerConnector(port);
    }

    public static void main(String[] args) {
        int port = -1;
        boolean console = false;
        String COMPortPrefix = "";

        for(int i=0;i < args.length; i++){
            if (args[i].equals("-c")){
                console = true;
            } else if (args[i].equals("-s")){
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                    System.err.println("After key -s (socket), you must specify the port number");
                    //TODO: Лог
                    return;
                }
            } else if (args[i].equals("-p")){
                try {
                    COMPortPrefix = args[++i];
                } catch (ArrayIndexOutOfBoundsException e){
                    System.err.println("After key -p (prefix), you must specify the port name prefix");
                    //TODO: Лог
                    return;
                }
            }
        }

        if (port == -1 && !console){
            System.err.println("Key -s (socket) or -c (console) must be specified");
            //TODO: Лог
            return;
        }

        new ArduinoDataStorage(port, console, COMPortPrefix);
    }
}
