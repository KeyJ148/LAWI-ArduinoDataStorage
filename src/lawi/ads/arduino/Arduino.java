package lawi.ads.arduino;

import java.io.IOException;

public class Arduino {

    private ArduinoConnector arduinoConnector;
    private Encoder encoder;
    private Decoder decoder;

    public Arduino(String portDescription, int baudRate, int P, int lenData, int blockSeparator){
        arduinoConnector = new ArduinoConnector(portDescription, baudRate);
        encoder = new Encoder();
        decoder = new Decoder(arduinoConnector, P, lenData, blockSeparator);
    }

    public boolean openConnection(){
        boolean result = arduinoConnector.openConnection();
        if (result) arduinoConnector.serialReadClear();
        return result;
    }

    public void closeConnection(){
        arduinoConnector.closeConnection();
    }

    public Package nextPackage() throws IOException {
        return new Package(decoder.nextPackage());
    }

    /* Геттеры для статистики последовательного порта */

    public long getCountReceiveBytes() {
        return decoder.getCountReceiveBytes();
    }

    public long getCountReceivePackage() {
        return decoder.getCountReceivePackage();
    }

    public long getCountUnsuccessfulValidate() {
        return decoder.getCountUnsuccessfulValidate();
    }

    public long getCountUnsuccessfulBlocks() {
        return decoder.getCountUnsuccessfulBlocks();
    }

    public long getCountIOException() {
        return decoder.getCountIOException();
    }

    public String getStatistic(){
        StringBuilder sb = new StringBuilder();
        sb.append("Count receive bytes: ");
        sb.append(getCountReceiveBytes());
        sb.append("\nCount receive package: ");
        sb.append(getCountReceivePackage());
        sb.append("\nCount unsuccessful validate: ");
        sb.append(getCountUnsuccessfulValidate());
        sb.append("\nCount unsuccessful blocks: ");
        sb.append(getCountUnsuccessfulBlocks());
        sb.append("\nCount IOException: ");
        sb.append(getCountIOException());

        return sb.toString();
    }

}
