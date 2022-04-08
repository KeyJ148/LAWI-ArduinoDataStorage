package cc.abro.ape.arduino;

import cc.abro.ape.ArrayUtils;

import java.util.Arrays;

public class Package {

    public static final int LENGTH = 18;

    public float t;
    public float h;
    public float p;
    public int co2;
    public byte bmeStatus;
    public byte mhzStatus;

    public Package(byte[] data){
        if (data.length < LENGTH) throw new IllegalArgumentException("Length data must be equal or greater than " + LENGTH + " (Package.LENGTH)");

        t = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 0, 4));
        h = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 4, 8));
        p = ArrayUtils.byteArrayToFloat(Arrays.copyOfRange(data, 8, 12));
        co2 = ArrayUtils.byteArrayToInt(Arrays.copyOfRange(data, 12, 16));
        bmeStatus = data[16];
        mhzStatus = data[17];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Temperature: "); sb.append(t); sb.append(" *C\n");
        sb.append("Humidity: "); sb.append(h); sb.append(" %\n");
        sb.append("Pressure: "); sb.append(p); sb.append(" mm Hg\n");
        sb.append("CO2: "); sb.append(co2); sb.append(" ppm\n");
        sb.append("BME status: "); sb.append(bmeStatus); sb.append("\n");
        sb.append("MHZ status: "); sb.append(mhzStatus); sb.append("\n");

        return sb.toString();
    }
}
