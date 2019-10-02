package lawi.ads.arduino;

import lawi.ads.ArrayUtils;

import java.io.IOException;
import java.util.*;

public class Decoder {

    private static final int SERIAL_READ_DELAY = 100; //Проверять входной буфер через каждые N миллисекунд
    private static final int SERIAL_READ_TIMEOUT = 30*1000; //Максимальное время ожидания пакета данных в миллисекундах

    private final ArduinoConnector arduinoConnector;
    private final int P; //Константа для расчёта значения хеш-функции (должна быть больше 255)
    private final int lenData; //Длина блока данных
    private final byte[] blockSeparator; //Байты между блоками данных
    private final int lenMessage; //Длина блока сообщения (данные + проверяющие механизмы)

    private volatile long countReceiveBytes = 0;
    private volatile long countReceivePackage = 0;
    private volatile long countUnsuccessfulValidate = 0;
    private volatile long countUnsuccessfulBlocks = 0;
    private volatile long countIOException = 0;

    public Decoder(ArduinoConnector arduinoConnector, int P, int lenData, int blockSeparator) {
        if (arduinoConnector == null) throw new IllegalArgumentException("ArduinoConnector cannot be null");
        if (P <= 255) throw new IllegalArgumentException("P must be greater than 255");
        if (lenData <= 0) throw new IllegalArgumentException("Length data must be greater than 0");

        this.arduinoConnector = arduinoConnector;
        this.P = P;
        this.lenData = lenData;
        this.blockSeparator = ArrayUtils.intToByteArray(blockSeparator);

        lenMessage = 4+lenData+4; //символ начала блока (int) + длина блока данных + хеш-функция (int)
    }

    public byte[] nextPackage() throws IOException {
        //Считываем в буффер длину одного пакета, если данные некоректны,
        //продолжаем считывать пока не попадем на валидный пакет
        List<Byte> buffer = new ArrayList<>();
        buffer.addAll(ArrayUtils.byteArrayToList(readBytes(lenMessage)));

        int offset = 0;
        while (!validateBuffer(ArrayUtils.collectionByteToArray(buffer), offset)){
            buffer.addAll(ArrayUtils.byteArrayToList(readBytes(1)));
            offset++;
        }

        countReceivePackage++;
        if (offset > 0) countUnsuccessfulBlocks++;
        return ArrayUtils.collectionByteToArray(buffer.subList(offset+4, offset+4+lenData));
    }

    private byte[] readBytes(int count) throws IOException{
        try {
            byte[] result = arduinoConnector.serialReadBytesWaitTimeout(count, SERIAL_READ_DELAY, SERIAL_READ_TIMEOUT);
            countReceiveBytes += result.length;

            return result;
        } catch (IOException e){
            countIOException++;
            throw e;
        }
    }

    private boolean validateBuffer(byte[] buffer){
        return validateBuffer(buffer, 0);
    }

    private boolean validateBuffer(byte[] buffer, int offset){
        System.out.print("Validate buffer: {");
        for(int i=offset; i<offset+lenMessage-1; i++) System.out.print(buffer[i] + ", ");
        System.out.print(buffer[offset+lenMessage-1] + "} ");

        if (buffer.length < offset + lenMessage) return false;

        byte[] separator = Arrays.copyOfRange(buffer, offset, offset + 4); //Блок разделителя
        byte[] data = Arrays.copyOfRange(buffer, offset + 4, offset + 4 + lenData); //Блок данных
        byte[] hashInput = Arrays.copyOfRange(buffer, offset + 4 + lenData, offset + 4 + lenData + 4); //Блок хеш-функции

        if (!Arrays.equals(separator, blockSeparator) || hash(data) != ArrayUtils.byteArrayToInt(hashInput)){
            countUnsuccessfulValidate++;
            System.out.println("fail");
            return false;
        }

        System.out.println("success");
        return true;
    }

    //Хеш вычисялется как сумма: b[i]*pow(P, i)
    //Чтобы иметь переносимость между платформами не используется double, а pow вычисляется умножением каждый шаг
    //Т.к. в Java byte хранит данные от -128 до +128, а в C++ в аналогичной функции хеширования используется диапазон от 0 до 255,
    //то необходимо сделать +256 для отрицательных чисел
    private int hash(byte[] b){
        int hash = 0;
        int pow = 1;
        for (int i = 0; i < b.length; i++) {
            int b_i = (b[i] >= 0)? b[i] : (((int) b[i])+256);
            hash = hash + b_i*pow;
            pow = pow * P;
        }

        return hash;
    }

    /* Геттеры для статистики последовательного порта */

    public long getCountReceiveBytes() {
        return countReceiveBytes;
    }

    public long getCountReceivePackage() {
        return countReceivePackage;
    }

    public long getCountUnsuccessfulValidate() {
        return countUnsuccessfulValidate;
    }

    public long getCountUnsuccessfulBlocks() {
        return countUnsuccessfulBlocks;
    }

    public long getCountIOException() {
        return countIOException;
    }
}
