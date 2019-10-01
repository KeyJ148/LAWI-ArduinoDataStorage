package lawi.ads.arduino;

import lawi.ads.ArrayUtils;

import java.util.Arrays;

public class Package {

    public static final int LENGTH = 13;

    public float t;
    public float h;
    public float p;
    public byte bmeStatus;

    public Package(byte[] data){
        if (data.length < 13) throw new IllegalArgumentException("Length data must be equal or greater than " + LENGTH + " (Package.LENGTH)");

        t = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 0, 4));
        h = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 4, 8));
        p = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 8, 12));
        bmeStatus = data[12];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Temperature: "); sb.append(t); sb.append(" *C\n");
        sb.append("Humidity: "); sb.append(h); sb.append(" %\n");
        sb.append("Pressure: "); sb.append(p); sb.append(" mm Hg\n");
        sb.append("BME status: "); sb.append(bmeStatus); sb.append("\n");

        return sb.toString();
    }
}
