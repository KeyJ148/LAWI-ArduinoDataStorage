package cc.abro.ape;

import cc.abro.ape.arduino.Package;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class PrometheusExporter {

    private final Gauge temperature = Gauge.build()
            .name("temperature")
            .help("temperature")
            .register();
    private final Gauge humidity = Gauge.build()
            .name("humidity")
            .help("humidity")
            .register();
    private final Gauge pressure = Gauge.build()
            .name("pressure")
            .help("pressure")
            .register();
    private final Gauge co2 = Gauge.build()
            .name("co2")
            .help("co2")
            .register();
    private final Gauge statuses = Gauge.build()
            .name("statuses")
            .help("statuses")
            .labelNames("device")
            .register();

    public PrometheusExporter() throws IOException {
        new HTTPServer.Builder()
                .withPort(1234)
                .withDaemonThreads(true)
                .build();
    }

    public void export(Package pack) {
        temperature.set(pack.t);
        humidity.set(pack.h);
        pressure.set(pack.p);
        co2.set(pack.co2);
        statuses.labels("BME").set(pack.bmeStatus);
        statuses.labels("MHZ").set(pack.mhzStatus);
    }
}
