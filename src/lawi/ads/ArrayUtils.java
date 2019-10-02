package lawi.ads;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayUtils {

    public static String[] collectionStringToArray(Collection<String> collection){
        String[] result = new String[collection.size()];
        return collection.toArray(result);
    }

    public static byte[] collectionByteToArray(Collection<Byte> collection){
        byte[] result = new byte[collection.size()];

        int i = 0;
        for (Byte b : collection) result[i++] = b;

        return result;
    }

    public static List<Byte> byteArrayToList(byte[] bytes){
        List<Byte> list = new ArrayList<>(bytes.length);
        for(int i=0; i<bytes.length; i++) list.add(bytes[i]);
        return list;
    }

    public static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int byteArrayToInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static float byteArrayToFloat(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
}
