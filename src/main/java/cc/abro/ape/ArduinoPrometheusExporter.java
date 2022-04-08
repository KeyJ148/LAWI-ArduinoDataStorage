package cc.abro.ape;

import cc.abro.ape.arduino.Arduino;
import cc.abro.ape.arduino.Package;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoPrometheusExporter {

    public static final int baudRate = 9600;
    public static final int P = (int) 1e9+7;
    public static final int blockSeparator = 0xAA9999AA;

    public static void main(String[] args) throws IOException {
        printCOMPortList();

        String port = getComPort(args);
        if (port != null && !port.isEmpty()) {
            System.out.println("Connect to port: " + port);
            DataKeeper dataKeeper = new DataKeeper(new Arduino(
                    port,
                    ArduinoPrometheusExporter.baudRate,
                    ArduinoPrometheusExporter.P,
                    Package.LENGTH,
                    ArduinoPrometheusExporter.blockSeparator));
            dataKeeper.addListener(System.out::println);
            PrometheusExporter exporter = new PrometheusExporter();
            dataKeeper.addListener(exporter::export);
        } else {
            System.out.println("Use parameter or env \"ARDUINO_COM_PORT\" to set port");
        }
    }

    private static void printCOMPortList(){
        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.print("COM Ports list: ");
        for(int i=0; i<ports.length; i++){
            System.out.print(ports[i].getSystemPortName());
            if (i != ports.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private static String getComPort(String[] args) {
        if (args.length > 0) {
            return args[0];
        } else {
            return System.getenv("ARDUINO_COM_PORT");
        }
    }
}
