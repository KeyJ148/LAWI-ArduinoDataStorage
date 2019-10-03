package lawi.ads.control;

import com.fazecast.jSerialComm.SerialPort;
import lawi.ads.ArduinoDataStorage;
import lawi.ads.DataKeeper;
import lawi.ads.arduino.Arduino;
import lawi.ads.arduino.Package;

import java.io.PrintWriter;
import java.util.Arrays;

public class CommandHandler {

    public static class FormatMessageException extends Exception{

        public FormatMessageException(int needLen){
            this("Length command must be " + needLen);
        }

        public FormatMessageException(String message){
            super(message);
        }
    }

    private static final String ERROR_FORMAT_MESSAGE = "Error: wrong format message";

    public static void handle(String commandLine, PrintWriter out){
        try {
            String[] commandArray = commandLine.split(" ");
            int type = Integer.parseInt(commandArray[0]);
            String[] commandData = (commandArray.length < 2)? null : Arrays.copyOfRange(commandArray, 1, commandArray.length);

            switchType(type, commandData, out);
        } catch (NumberFormatException e){
            out.println(ERROR_FORMAT_MESSAGE);
            //TODO: Лог

            return;
        }
    }

    private static void switchType(int type, String[] data, PrintWriter out){
        try {
            switch (type) {
                case 1: getCOMPortList(data, out);break;
                case 2: getStateDataKeeper(data, out);break;
                case 3: createDataKeeper(data, out);break;
                case 4: stopDataKeeper(data, out);break;
                default: out.println(ERROR_FORMAT_MESSAGE); break;
            }
        } catch (FormatMessageException e){
            out.println(ERROR_FORMAT_MESSAGE);
        }
    }

    private static void getCOMPortList(String[] command, PrintWriter out){
        SerialPort[] ports = SerialPort.getCommPorts();

        out.print("COM Port List: ");
        for(int i=0; i<ports.length; i++){
            out.print(ports[i].getSystemPortName());
            if (i != ports.length - 1) out.print(", ");
            else out.println();
        }
    }

    private static void getStateDataKeeper(String[] command, PrintWriter out){
        if (ArduinoDataStorage.dataKeeper == null){
            out.println("DaraKeeper was not created");
            return;
        }

        out.println("Work: " + ArduinoDataStorage.dataKeeper.isWork());
    }

    private static void createDataKeeper(String[] command, PrintWriter out) throws FormatMessageException{
        if (command.length < 1) throw new FormatMessageException(1);
        if (ArduinoDataStorage.dataKeeper == null){
            out.println("DaraKeeper was not created");
            return;
        }
        if (ArduinoDataStorage.dataKeeper.isWork()){
            out.println("DataKeeper work now");
            return;
        }

        ArduinoDataStorage.dataKeeper = new DataKeeper(
                new Arduino(command[1], ArduinoDataStorage.baudRate, ArduinoDataStorage.P, Package.LENGTH, ArduinoDataStorage.blockSeparator));
    }

    private static void stopDataKeeper(String[] command, PrintWriter out){
        if (ArduinoDataStorage.dataKeeper == null){
            out.println("DaraKeeper was not created");
            return;
        }

        ArduinoDataStorage.dataKeeper.stop();
    }
}
