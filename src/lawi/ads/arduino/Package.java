package lawi.ads.arduino;

import lawi.ads.ArrayUtils;

import java.util.Arrays;

public class Package {

    public float t;
    public float h;
    public float p;
    public byte bmeStatus;

    public Package(byte[] data){
        t = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 0, 4));
        h = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 4, 8));
        p = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 8, 12));
        bmeStatus = data[12];
    }
}
